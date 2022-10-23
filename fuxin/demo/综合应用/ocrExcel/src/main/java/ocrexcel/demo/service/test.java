package ocrexcel.demo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import ocrexcel.demo.Test.cn.isuyu.demo.keyword.port.utils.CustomerRenderListener;
import ocrexcel.demo.Test.vos.WordVO;
import ocrexcel.demo.fuxin.enums.MergeEnum;
import ocrexcel.demo.fuxin.moddle.CellInfo;
import ocrexcel.demo.fuxin.moddle.StrInfo;
import ocrexcel.demo.fuxin.utils.PdfStrAxisUtil;
import org.apache.commons.collections.CollectionUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.foxit.sdk.addon.conversion.Convert;
import com.foxit.sdk.common.Library;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

@Slf4j
public class test extends PdfStrAxisUtil {


    //单纯的</TH>  没有意义  需要去掉

    //table  面线点的 切割标签名
    //一级目录
    private static final String[] PRIMARY_LABEL = {"Table"};
    //二级标签
    private static final String[] SECONDARY_LABEL = {"TR"};
    //三级标签
    private static final String[] TERTIARY_LABEL = {"TD", "TH"};

    //判断时候是同一列的  误差值  下限以及上限 误差依次为：①前对齐误差  ② 后对齐误差  ③  居中误差
    //计算公式： （1）(xStart_1 - xStart_2)/fontSize    (2)(xEnd_1 - xEnd_2)/fontSize   (3)(xMiddle_1 - xMiddle_2)/fontSize
    private static final Float[][] COL_ERROR = {{0.0F, 1.5F}, {0.0F, 2.2F}, {0.0F, 3.0F}};

    //判断是否是 某两列的 合并列的差值阈值  高于这个阈值  就进入到了两列合并的判断
    //合并列仅仅适用于  中对称 计算公式 ：(xMiddle_1 - xMiddle_2)/fontSize
    private static final Float IN_MERGE_COL = 0.9F;


    //用于表示正无穷的 值
    private static final Float MAX_NUM = 100000F;

    //判断是否是同一行的 误差值
    //计算公式：|yAxis1 - yAxis2|/min(yHeight1,yHeight2)
    private static final Float[] ROW_ERROR = {0.0F, 0.5F};//0.9


    private static final Float EMPTY_ROW_OFFSET = 1f;

    //屏蔽词汇
    private static final String[] IGNORE_WORD = {"[Table", "数据来源：wind、中信建投", "资料来源："};


    //table 中有这个标签  就 忽略这个table
    private static final String[] IGNORE_ELEMENT = {"ImageData"};

    //福昕注册的过期年份
    private static final Integer EXPIRATION_TIME = 2022;

    //福昕回退日期
    //  第一次版本回退日期
    //private static final String FALLBACK_DATE = "2022-07-01";
    private static final String FALLBACK_DATE = "2022-10-21";

    //福昕注册  失败后的尝试 次数
    private static final Integer FUXIN_INIT_LOOP = 35;

    //单列多行的 误差
    private static final Integer MERGE_COL_ERROR = 2;


    //这个是 到2022年07月05日过期  获取本地时间
    //我们可以 程序运行前  将本地时间  回退
    //也可以直接买永久的


    //private static String source_file = "D:/PDF/pdf/";
    private static String source_file = "../ocrFiles/pdfs/";
    private static String excel_path = "../ocrFiles/excels/";
    private static String output_path = "../ocrFiles/xmls/";
    private static String bat_path = "../ocrFiles/bats/";

    String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";


    /*
    private static String key = "8f0YFcONvRkN+ldwlpAFW0NF+Q/jOhOBvj1efQAKLLKux+nGeHqbsdctYiw0TuZREd2ungiuZk2tsbLXEusM2l+cVxG18jYetvFudmoPiOHiDqfKr2MseAswJLWvtMr+i9zTHDYhDuZd9ESe2YJ6rQxX1i9OK/ahiUGyOcwQKMl4p9zkJ0PYiW6nJlf6YQ2uufXpn4jvJ8crJIPZ0GG561aEKjw5GmrEeaEgpnEsMXzDm3lY8gak9qCq4iHP7Rt/gLYgN81sXm+LiDjkBUJ9458JXwOJMR/CIVqLCceOOK/xVH9tKhWM38L+LZHDPcDKUPl5Q3z/bGam9xWQUjIGFcEzN/aMAsVXohDoX7yFSvGMTx7KXklqx/sJinOfQB2w3NZuQWBhC8FyaqxDbnN8/61jEc7waH/6JBjg2sovJUtAmIJYcTBJXBiaAFhpJ3sODTnVCLrkO90ZS2ujrdoLgoyQb/LizDjjuQL/pGWnp+KGc+OL5QrkU6CIEszPNeUfWzAFLHvIS0KJOkU+FhDh3woANn4KdNxih4fQnOhDSIYuFsf8bLdd2cmRf+c6Kh1Hlz4k5TprGgYHgeR7/fKVFzQU8ThnUgEgmIE69+2l5c89RYwnl8IcCNM9Zkqfs7Q/swaQ8qzSn/lzUH1s9lgLosIp+B9wiAHptqnLdel7nHq7k4CZbPZbGyk2vngYhmterOmM+7ErS3wTlQfr3Xq4INvYgenUbOmazGTHOQ6KMdWiX9a7GyuCVEDtLwiDtvwogj6Hz0L8FBu/wJ5YVG9O8R/4AvJ5vI0rHtPXQm2JijV1MqlUuSX5EXF3TTflhDGrxjyl0WTZePvcFIJgP26SwZhzAfAaLDz9qejSZakPSqFuUY0q201DB8tw4fyITj8hVIruR7IGvq+bnEGSuwI/zKSPNrtpEZPkvhUyitQlebK2AIF6Xc6vlXHnusb4D2N8YGjCTom1BSaNZGcVOmOD9cj6GPw4/uDaKffDfvFYChfN4U1wELZAsvkbY8Gl9oVFqRosOWkfm3kCg7q7+LwtUdgYFSVTqnFgO4zyNNC9Cje/aNrEL775/KVuZcWZTpEggxYuldML8qw8z6sHxO84/W4u/t9ZNBMGfp+g3RSEVGmE8iDRE2ehhIqo26injEF+d4f62BF5bjJfrzUZI6vbMkwUrj/0y5HF095aLx9nSGTYqcrP/4V53wgwBRnKCu76sHXGD+F+6FLzhN4PvMDaY+V7lYJj9J6fR0qbuGh9nokSbCWUMQMEjAyRCGhzQ28DP6ewi46MSVRzbZvdTolWnN/qsDs9Q593SE8YdswZxicGwsCZkCspArE=";
    private static String sn = "Izt0owtF5aH9zVuoR62u2FasDHg1UPvSg/JURm8gJMUB5Tb1eDgiUg==";*/


    private void init() {
        String os = System.getProperty("os.name").toLowerCase();
        String lib = "fsdk_java_";
        if (os.startsWith("win")) {
            lib += "win";
        } else if (os.startsWith("mac")) {
            lib += "mac";
        } else {
            lib +="linux";
        }
        if (System.getProperty("sun.arch.data.model").equals("64")) {
            lib += "64";
        } else {
            lib += "32";
        }
        System.loadLibrary(lib);
    }

    //时间  YYYY-MM-DD
    public void main(String foreDate, String fileName) throws Exception {

        //时间戳形成文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String file_path = source_file + fileName + ".pdf";
        String save_path = output_path + format + ".xml";

        //不要用静态块进行初始化 在方法中进行初始化  如果买不到永久序列号 就在这里进行初始化 并且 更改本地时间
        try {
            //首先修改本地时间
            changeLocalTime(FALLBACK_DATE);
            init();
            Integer error_code = null;
            for (Integer i = 0; i < FUXIN_INIT_LOOP; i++) {
                error_code = Library.initialize(sn, key);
                if (error_code != e_ErrSuccess) {
                    System.out.println("SDK初始化失败");
                } else {
                    System.out.println("SDK初始化成功");
                    break;
                }
            }
            boolean isToXMl = Convert.toXML(file_path, "", save_path, "", true);
            System.out.println(isToXMl ? "转换成功" : "转换失败");
            //转换完后 迅速把时间改回来
            changeLocalTime(foreDate);
            //对容器进行填充
            loadDatas(file_path);
            parseXml(save_path);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //不管怎么样都要把时间改过来
            changeLocalTime(foreDate);
        }
    }

    //以单线程的方式  更改时间
    private static synchronized void changeLocalTime(String dateStr) throws Exception {
        //date  必须是是以-为分隔符的 字符串  并且含有年月日
        String[] times = dateStr.split("-");
        if (times.length != 3) {
            return;
        }
        String os = System.getProperty("os.name").toLowerCase();
        //目的年份 用于福鑫的注册
        if (os.startsWith("win")) {
            String nowPath = bat_path;
            File temDir = new File(nowPath);
            File batFile = new File(nowPath + "Date.bat");
            if (!temDir.exists()) {
                temDir.mkdir();
                batFile.createNewFile();
            }
            FileWriter fw = new FileWriter("Date.bat");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off\n");
            bw.write("%1 mshta vbscript:CreateObject(\"Shell.Application\").ShellExecute(\"cmd.exe\",\"/c %~s0 ::\",\"\",\"runas\",1)(window.close)&&exit\n");
            bw.write("date " + times[0] + "/" + times[1] + "/" + times[2]);
            bw.close();
            fw.close();
            Process process = Runtime.getRuntime().exec("Date.bat");
            process.waitFor();
            //等上面的执行完毕后再删除文件
            batFile.delete();
        } else if (os.startsWith("mac")) {
            Runtime.getRuntime().exec("cmd /c " + times[0] + "-" + times[1] + "-" + times[2]);//Windows 系统
        } else {
            //然后进行时间的恢复
            Runtime.getRuntime().exec(" sudo date -s " + times[0] + "-" + times[1] + "-" + times[2]+"`date +%T`");//linux 系统为tomcat用户分配了权限
        }
    }


    //然后现在需要  读取xml文件 进行解析
    //仅对table 部分进行转换 而且是最内层table
    public String parseXml(String save_path) throws Exception {
        //加载xml文件
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(save_path));
        //获取PDF的长宽
        Element rootElement = document.getRootElement(); // 获取根节点
        //然后得到  table  节点
        //然后先输出
        List<Element> tableList = new ArrayList<>();
        this.analysisList(tableList, rootElement);

        //需要校验   跨页的表格  多个连着的table   就可以认为是跨页的表格
        // 然后我们会把  两个连着的表  进行归类粘合
        //  tableList  里的元素  是有序的  从小到大进行排列的
        String rootXml = rootElement.asXML();
        List<List<Element>>  tbs = new ArrayList<>();
        List<Element> tempList = new ArrayList<>();
        for (Element element : tableList) {
            if(CollectionUtils.isEmpty(tempList)){
                //如果是空的  就加入
                tempList.add(element);
            }else {
                //如果不是空的  就取  最近的一个 然后进行校验
                Element tempEle = tempList.get(tempList.size() - 1);
                //然后取  他的后端坐标
                String befXml = tempEle.asXML();
                //取现有元素的前坐标
                String nowXml = element.asXML();
                Integer befIndex = rootXml.indexOf(befXml) + befXml.length();
                Integer aftIndex = rootXml.indexOf(nowXml);
                //如果相减  等于1  说明是连着的
                if(Objects.equals(aftIndex - befIndex,1)){
                    tempList.add(element);
                }else {
                    //如果不等于1  说明是分开的
                    List<Element>  temp = new ArrayList<>();
                    //然后把  tempList深复制进去
                    CollectionUtils.addAll(temp,new Object[tempList.size()]);
                    Collections.copy(temp,tempList);
                    tbs.add(temp);
                    tempList.clear();
                    tempList.add(element);
                }
            }
        }
        tbs.add(tempList);

        //遍历 然后输出
        //然后就进行excel 导出
        //最小单位为TD  内部的自动合并  进行输出
        //每一个table 对应一个 sheet
        //创建 excel
        //获取当前路劲
        ExcelWriter build = EasyExcel.write(excel_path + UUID.randomUUID().toString().replace("-", "").toUpperCase() + ".xlsx").build();
        for (int num = 0; num < tbs.size(); num++) {
            //创建sheet
            WriteSheet tempSheet = EasyExcel.writerSheet("表" + (num + 1)).build();
            //然后这里获取数据
            //这里需要循环的  从  元素中取出
            List<Element> elements = tbs.get(num);
            List<List<CellInfo>> lists = new ArrayList<>();
            for (Element element : elements) {
                lists.addAll(takeApartElement(element));
            }
            //将纯空行  去掉
            removeBankRow(lists);
            //空的  或者里面的内容都是空的 就直接跳过
            if (CollectionUtils.isEmpty(lists)) {
                continue;
            }
            removeHeader(lists);
            List<List<String>> tableDatas = new ArrayList<>();
            fillMergeRow(lists);
            //进行纵向填充
            //fillMergeCol(lists);
            //然后进行填充
            for (List<CellInfo> infos : lists) {
                List<String> rowDatas = new ArrayList<>();
                for (CellInfo info : infos) {
                    rowDatas.add(info.getVal());
                }
                tableDatas.add(rowDatas);
            }
            //最后写入
            build.write(tableDatas, tempSheet);
        }
        build.finish();

        return "";
    }


    //数据进行横向填充
    private void fillMergeRow(List<List<CellInfo>> lists) {
        //一列的 就  不进行合并了
        if (lists.size() <= 1) {
            return;
        }
        //lists  是经过 填充坐标 并且  模板是完整的
        //用模板来纠正  每个不同行的  下标
        //由左到右进行遍历
        //不能忽略空白行  但是可以裁切  空白行    因为  有的 表格 在解析时 会在  模板行的下方多个空白行
        Integer templateRow = getTemplateRow(lists);
        if (Objects.isNull(templateRow)) {
            return;
        }
        //获取模板行
        List<CellInfo> templateList = lists.get(templateRow);
        //然后进行 比对 确定下标  只确定非空的  确定是某个单行或者双行  的  合并行
        a:
        for (int templateOrder = 0; templateOrder < templateList.size(); templateOrder++) {
            CellInfo nowTemplateCell = templateList.get(templateOrder);
            CellInfo nextTemplateCell = (templateOrder == templateList.size() - 1) ? null : templateList.get(templateOrder + 1);
            //然后循环  list对下标进行判断
            b:
            for (int aimsOrder = 0; aimsOrder < lists.size(); aimsOrder++) {
                if (!Objects.equals(aimsOrder, templateRow)) {
                    List<CellInfo> aimList = lists.get(aimsOrder);
                    //之前  已经去过表头了 也去过  空行 了
                    // 所以  对于单行 的  以  不左对齐  就是跨行
                    if (aimList.size() == 1&&(!isSamRowCol(aimList.get(0).getStrInfo(),templateList.get(0).getStrInfo(),MergeEnum.COL))) {
                        //将序号  设置成  居中
                        aimList.get(0).setColOrder((Float.valueOf(templateList.size()) / 2)-0.5f);
                        aimList.get(0).setColOrderConf(Boolean.TRUE);
                        continue b;
                    }
                    c:
                    for (int aimOrder = 0; aimOrder < aimList.size(); aimOrder++) {
                        //将  colOrder置为 空  然后进行非空坐标的 确定
                        CellInfo aimCell = aimList.get(aimOrder);
                        //aimCell.setColOrder(null);
                        if (Objects.nonNull(aimCell.getStrInfo())
                                &&Objects.nonNull(nowTemplateCell.getStrInfo())
                                &&Objects.equals(Boolean.FALSE,aimCell.getColOrderConf())) {
                            //如果不为空  就进行坐标的 确定
                            if (isSamRowCol(nowTemplateCell.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL)) {
                                //同列 设置下标  然后跳出
                                aimCell.setColOrder(Float.valueOf(templateOrder));
                                aimCell.setColOrderConf(Boolean.TRUE);
                                break c;
                            } else {
                                //如果找不到单行 对标的 就进行双行对标的  并且和下一个不是同一列
                                if (Objects.nonNull(nextTemplateCell)
                                        &&Objects.nonNull(nextTemplateCell.getStrInfo())
                                        && (!isSamRowCol(nextTemplateCell.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL))) {

                                    //求出平均宽度
                                    float width = ((aimCell.getStrInfo().getWidth() / aimCell.getVal().length())
                                            + nextTemplateCell.getStrInfo().getWidth() / nextTemplateCell.getVal().length()) / 2;

                                    //求出两行的 平均值
                                    float v = Math.abs(aimCell.getStrInfo().getXMiddle() - (nowTemplateCell.getStrInfo().getXMiddle() + nextTemplateCell.getStrInfo().getXMiddle()) / 2) / width;
                                    if (v >= COL_ERROR[2][0] && v <= COL_ERROR[2][1]) {
                                        aimCell.setColOrder(Float.valueOf(templateOrder) + 0.5F);
                                        aimCell.setColOrderConf(Boolean.TRUE);
                                        break c;
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

        //上面  已经对坐标进行了初始的确定   对于一些 特别列的行 进行进一步确定
        preMerge(lists);
        //然后进行对应的坐标进行合并  合并逻辑  是  行 尽量填满    不能忽略空白行 但是可以裁剪

        for (List<CellInfo> list : lists) {
            int maxSize = lists.get(templateRow).size();
            for (int rowNo = 0; rowNo < list.size(); rowNo++) {
                CellInfo cellInfo = list.get(rowNo);
                //首先要找到实心的
                if (Objects.nonNull(cellInfo.getStrInfo())) {
                    //向前找到实心的
                    Integer[] forward = getClearance(list, rowNo, Boolean.TRUE);
                    //向后找到实心的
                    Integer[] back = getClearance(list, rowNo, Boolean.FALSE);
                    //统计间隙
                    Float order = cellInfo.getColOrder();
                    Float before = Objects.isNull(forward[0]) ? Float.valueOf(maxSize) - 1f - order - Float.valueOf(forward[1])
                            : list.get(forward[0]).getColOrder() - 1f - Float.valueOf(forward[1]) - order;
                    Float after = Objects.isNull(back[0]) ? order - Float.valueOf(back[1]): order - 1f - list.get(back[0]).getColOrder() - Float.valueOf(back[1]);
/*                    if(after<0&&Math.abs(after)<=back[1]){
                        //出现小于  说明 空格异常  就需要修正  移除尽可能多的 单元格
                        Double moveNo = Math.ceil(Math.abs(after));
                        Integer removeStart = Objects.isNull(back[0])?-1:back[0];
                        for (int i = 1; i <= moveNo.intValue(); i++) {
                            list.remove(removeStart+i);
                            rowNo--;
                            after++;
                        }
                    }*/
                    if(before <0&&Math.abs(before)<=forward[1]){
                        Double moveNo = Math.ceil(Math.abs(before));
                        Integer removeStart = Objects.isNull(forward[0])?maxSize:forward[0];
                        for (int i = 1; i <= moveNo.intValue(); i++) {
                            list.remove(removeStart-i);
                            before++;
                        }
                    }
                    //两个值都必须大于0  才能进行  填充
                    if (before > 0 && after > 0) {
                        //按照最小的 值进行填充
                        Float start = order - Math.min(before, after);
                        Float end = order + Math.min(before, after);

                        //然后从 start  到  end  进行当前节点填充  并且 重置 rowNo
                        //首先移除  当前节点
                        list.remove(rowNo + 0);
                        //然后循环的 插入  list中
                        for (int i = start.intValue(); i <= end.intValue(); i++, rowNo++) {
                            list.add(i, resetCell(cellInfo, Float.valueOf(rowNo), cellInfo.getVal()));
                        }
                        rowNo--;
                    }
                }
            }
            fitUpList(list,maxSize);
        }
    }


    //用空格  对list进行裁切 和填充  使之与模板行  长度一致
    private void fitUpList(List<CellInfo> list ,Integer maxSize){
        //首先是 去掉 空单元格
        Iterator<CellInfo> iterator = list.iterator();
        while(iterator.hasNext()){
            CellInfo next = iterator.next();
            if(StringUtils.isBlank(next.getVal())){
                iterator.remove();
            }
        }
        //遍历  找到实心  以及前距后距  保证每个缝隙都为0
        CellInfo cellInfo = null;
        for (int rowNo = 0; rowNo < list.size(); rowNo++) {
            cellInfo = list.get(rowNo);
            //首先要找到实心的
            if (Objects.nonNull(cellInfo.getStrInfo())) {
                //向后找到实心的
                Integer[] back = getClearance(list, rowNo, Boolean.FALSE);
                //统计间隙
                Float order = cellInfo.getColOrder();
                Float after = Objects.isNull(back[0]) ? order - Float.valueOf(back[1])
                        : order - 1f - list.get(back[0]).getColOrder() - Float.valueOf(back[1]);
                if(after >0){
                    //前面的间隙  大于0  说明少空格了  就需要添加空格进去
                    //从 back[0]开始 到 当前序号为终点  遇到 不连续的 就加进去
                    Integer start = Objects.isNull(back[0])?-1:back[0];
                    Integer end  = order.intValue();
                    for (Integer i = ++start; i < end; i++) {
                        //就将当前序号  对应的单元格 添加进去
                        list.add(i,resetCell(cellInfo,Float.valueOf(i),StringUtils.EMPTY));
                    }
                }
            }
        }
        if(list.size() > maxSize){
            //就进行后几位的截断
            for (int size = list.size()-1; size >= maxSize-1; size--) {
                list.remove(size+0);
            }
        }else {
            //遍历插入
            for (Integer i = list.size(); i < maxSize; i++) {
                list.add(resetCell(cellInfo,Float.valueOf(i),StringUtils.EMPTY));

            }
        }
    }


    //将横向的坐标 全部置为null 下标全部置为  指定  数字
    private CellInfo resetCell(CellInfo info, Float order, String val) {
        StrInfo strInfo = info.getStrInfo();
        StrInfo infos = new StrInfo(Objects.isNull(val) ? info.getVal() : val, null, null, null, strInfo.getWidth(), strInfo.getYAxis(), strInfo.getRowHeight(), strInfo.getPageNum());
        return new CellInfo(Objects.isNull(val) ? info.getVal() : val, order, null, infos,Boolean.FALSE);

    }

    //将横向的坐标 全部置为null 下标全部置为  指定  数字
    private CellInfo resetCell(List<CellInfo> infoList, Float order, String val) {
        //找到list中 第一个坐标信息不为null 单元格
        CellInfo cellInfo = null;
        for (CellInfo info : infoList) {
            if (Objects.nonNull(info.getStrInfo())) {
                cellInfo = info;
                break;
            }
        }
        StrInfo infos = new StrInfo(cellInfo.getVal(), null, null, null, cellInfo.getStrInfo().getWidth(),
                cellInfo.getStrInfo().getYAxis(), cellInfo.getStrInfo().getRowHeight(), cellInfo.getStrInfo().getPageNum());
        return new CellInfo(Objects.isNull(val) ? cellInfo.getVal() : val, order, null, infos,Boolean.FALSE);

    }
    //从某个下标开始  向前 或向后  找  第一个不为空的  并返回之间由多少个空白单元格
    private Integer[] getClearance(List<CellInfo> list, Integer startIndex, Boolean isForward) {
        Integer[] result = new Integer[2];
        Integer index = null;
        Integer bankNo = 0;
        //向前
        if (isForward) {
            for (int order = ++startIndex; order < list.size(); order++) {
                CellInfo info = list.get(order);
                if (Objects.nonNull(info.getStrInfo())) {
                    //如果不是空的   就进行统计
                    index = order;
                    break;
                } else {
                    bankNo++;
                }
            }

        }
        //向后
        else {
            for (int order = --startIndex; order >= 0; order--) {
                CellInfo info = list.get(order);
                if (Objects.nonNull(info.getStrInfo())) {
                    //如果不是空的   就进行统计
                    index = order;
                    break;
                } else {
                    bankNo++;
                }
            }
        }

        //最后  加进去
        result[0] = index;
        result[1] = bankNo;
        return result;
    }


    //对于合并行进行预先的逻辑合并  也可以认为是纠正下标
    //默认 重复的多个单元列 是连续的长度一致地  且单个单元中是不重复的
    private void preMerge(List<List<CellInfo>> list) {
        //首先这个list至少要3行
        if (list.size() < 3) {
            return;
        }
        a:
        for (int num = 1; num < list.size(); num++) {
            List<CellInfo> cellInfos = list.get(num);
            b:
            for (int order = 0; order < cellInfos.size() - 1; order++) {
                CellInfo info = cellInfos.get(order);
                String val = info.getVal();
                if (StringUtils.isBlank(val)) {
                    continue b;
                }
                List<Integer> comCol = new ArrayList<>();
                Boolean isFind = Boolean.FALSE;
                //然后拿着这个值去  list中后半段进行寻找
                c:
                for (int searchNum = order + 1; searchNum < cellInfos.size(); searchNum++) {
                    CellInfo cellInfo = cellInfos.get(searchNum);
                    if (StringUtils.equals(val, cellInfo.getVal())) {
                        isFind = Boolean.TRUE;
                        comCol.add(searchNum);
                    }
                }
                if (isFind) {

                    comCol.add(order);
                    //并把末尾地元素  放到  list首位
                    changeList(comCol);
                    //以第一个元素和第二个元素  的差值为  基准进行筛选  凡是差值不是这个的就进行淘汰  并且这个差值要大于1
                    Integer dValue = comCol.get(1) - comCol.get(0);
                    if (dValue <= 1) {
                        continue;
                    }
                    //校验末尾是否发生越界
                    if((comCol.get(comCol.size()-1)+dValue-1)>=cellInfos.size()){
                        continue b;
                    }
                    List<Integer> needRemoveIndex = new ArrayList<>();
                    d:
                    for (int startIndex = 1; startIndex < comCol.size(); startIndex++) {
                        if (startIndex != comCol.size() - 1) {
                            if (comCol.get(startIndex) - comCol.get(startIndex - 1) != dValue) {
                                needRemoveIndex.add(startIndex);
                            }
                        } else {
                            if (cellInfos.size() - comCol.get(startIndex) != dValue) {
                                needRemoveIndex.add(startIndex);
                            }
                        }
                    }
                    //然后进行移除
                    e:
                    for (int i = needRemoveIndex.size() - 1; i >= 0; i--) {
                        comCol.remove(needRemoveIndex.get(i) + 0);
                    }
                    //移除之后这个元素必须大于1个
                    if (comCol.size() < 2) {
                        continue b;
                    }
                    //进行末尾  单元格的比较
                    String endval = null;

                    //这里是想看下末尾一样不一样
                    f:
                    for (Integer startVal : comCol) {
                        String temStr = cellInfos.get(startVal + dValue - 1).getVal();
                        if (Objects.isNull(endval)) {
                            endval = temStr;
                        } else {
                            if (!StringUtils.equals(endval.trim(), temStr.trim())) {
                                continue b;
                            }
                        }
                    }
                    //校验之后 就进行上一行坐标的纠正
                    //找到上一行中 每个dval里面的 colorder 每个区域里面有且只有一个才行 然后对它的 colOrder进行调整
                    List<CellInfo> infos = list.get(num - 1);
                    for (Integer startMergeNum : comCol) {
                        int endMergeNum = startMergeNum + dValue - 1;
                        Float startNum = cellInfos.get(startMergeNum).getColOrder();
                        Float endNum = cellInfos.get(endMergeNum).getColOrder();
                        //他俩的中间值是
                        Float middleNum = transFloat((startNum + endNum) / 2);
                        //找到位于 开始和结束 之间的 元素  有且只有一个才行
                        Integer rangeNum = 0;
                        Integer correctindex = 0;
                        for (int i = 0; i < infos.size(); i++) {
                            CellInfo cellInfo = infos.get(i);
                            if (cellInfo.getColOrder() >= startNum && endNum >= cellInfo.getColOrder()) {
                                //如果位于这个范围  就进行纠正
                                correctindex = i;
                                rangeNum++;
                            }
                        }
                        if (rangeNum > 1) {
                            continue b;
                        }
                        //然后进行下标的处理 以及查询范围的推进
                        infos.get(correctindex).setColOrder(middleNum);
                    }
                    order = comCol.get(comCol.size() - 1) + dValue - 1;
                }
            }
        }


    }

    //将一个list 末尾放首位 其他的后移
    private void changeList(List<Integer> comCol) {
        //先把尾部拿出来
        Integer endVal = comCol.get(comCol.size() - 1);
        for (int i = 1; i < comCol.size(); i++) {
            comCol.set(i, comCol.get(i - 1));
        }
        //最后把尾部放前面
        comCol.set(0, endVal);
    }

    //将一个float转为 *。5 或者*。0的值
    private float transFloat(Float f) {
        Float v = f * 10;
        int remainder = v.intValue() % 10;
        if (remainder == 0 || remainder == 5) {
            return f;
        } else {
            return f.intValue();
        }

    }

    //获取横行的  模板
    //需要找到最长的 行  然后进行空位地补充
    private Integer getTemplateRow(List<List<CellInfo>> list) {
        Integer maxLenthIndex = null;
        //首先找到最长的  最好是非空的单元格
        a:
        for (int num = 0; num < list.size(); num++) {
            List<CellInfo> rowList = list.get(num);
            //需要对每一行进行 坐标赋值
            //列序号  在生成表格信息的时候 就已经进行赋值了
            this.getStrsInfos(rowList, Boolean.FALSE, MergeEnum.ROW);


            //这里要进行校验  因为  有时候两行 会  当成一行  解析出来
            List<CellInfo> hasTwoRow = isTwoRow(rowList);
            if(Objects.nonNull(hasTwoRow)){
                list.add(num+1,hasTwoRow);
            }
            //这里也有可能找不到坐标
            List<CellInfo> infos = rowList.stream().filter(x -> (Objects.isNull(x.getStrInfo()) && StringUtils.isNotBlank(x.getVal()))).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(infos)){
                continue a;
                //return null;
            }
            if (Objects.isNull(maxLenthIndex)) {
                maxLenthIndex = num;
            } else {
                Integer nowSize = rowList.size();
                Integer oldMaxSize = list.get(maxLenthIndex).size();
                if (Objects.equals(nowSize, oldMaxSize)) {
                    //如果相同就尽量找  非空的单元格
                    //遍历两个统计 bank的数量  选择少的
                    maxLenthIndex = statisBankInList(rowList) < statisBankInList(list.get(maxLenthIndex)) ? num : maxLenthIndex;
                } else if (nowSize > oldMaxSize) {
                    maxLenthIndex = num;
                }
            }
        }
        //然后进行 不同长度的统计
        if (Objects.isNull(maxLenthIndex)) {
            return null;
        }
        List<CellInfo> tempList = list.get(maxLenthIndex);
        List<Integer[]> range = getBankRange(tempList);
        CellInfo tempNotBankCell = tempList.get(firstIndex(tempList, Boolean.FALSE));
        //遍历  间隙范围  进行  填充
        c:
        for (Integer[] rang : range) {
            //获取到 起始坐标
            Integer bankNum = Math.abs(rang[0] - rang[1]) + 1;
            //循环bankNum  找到  在这个间隙的最小单元格
            d:
            for (Integer bank = 0; bank < bankNum; bank++) {
                Integer tempBank = bank;
                //找到 比 xStart 小 但是比 xEnd小的 最小单元格
                e:
                for (int order = 0; order < list.size(); order++) {
                    if (order != maxLenthIndex) {
                        //首先这个  待定行  在这个范围的首末列  都必须  和 模板行的首末列  同列  且必须有同等数量的 单元格
                        Integer startBank = getSameBank(tempList, rang[0], rang[1], list.get(order));
                        if (Objects.nonNull(startBank)) {
                            //如果不为空说明找到了  就依次将 不空的  单元格坐标加入进去
                            f:
                            for (; bank < bankNum; bank++) {
                                StrInfo aimStrInfo = list.get(order).get(startBank + bank).getStrInfo();
                                if (Objects.nonNull(aimStrInfo)) {
                                    //重置rang[0]  并且  将  list的 横坐标信息加入到 tempList 中 纵坐标 用该列的 值
                                    CellInfo tempBankCell = tempList.get(rang[0]);
                                    tempBankCell.setStrInfo(new StrInfo(tempBankCell.getVal()
                                            , aimStrInfo.getXStart()
                                            , aimStrInfo.getXEnd(), aimStrInfo.getXMiddle(), aimStrInfo.getWidth()
                                            , tempNotBankCell.getStrInfo().getYAxis()
                                            , tempNotBankCell.getStrInfo().getRowHeight()
                                            , tempNotBankCell.getStrInfo().getPageNum()));
                                    rang[0] = rang[0] + (++bank);
                                    order = 0;
                                    if (bank == bankNum - 1) {
                                        break d;
                                    }
                                } else {
                                    break f;
                                }
                            }
                        }
                    }
                }
/*                if (Objects.equals(tempBank, bank)) {
                    //说明没找到  就直接抛异常
                    log.error("横向模板初始化失败！！！");
                    return null;
                }*/
            }
        }

        //对模板行 进行序号填充
        List<CellInfo> templateList = list.get(maxLenthIndex);
        for (int i = 0; i < templateList.size(); i++) {
            templateList.get(i).setColOrder(Float.valueOf(i));
        }
        return maxLenthIndex;
    }

    //这里要进行  是否两行  解析为一行的判断  如果是  就截断  该行 并且  创建新行
    private List<CellInfo>  isTwoRow(List<CellInfo>  list){
        //遍历 list 找到  实心但是没有坐标信息的 截断  创建另一行
        if(list.size() <2){
            return null;
        }
        Boolean isTowRow = Boolean.FALSE;
        Integer num = 0;
        for (; num < list.size(); num++) {
            CellInfo info = list.get(num);
            if(StringUtils.isNotBlank(info.getVal().replace(" ", " "))&&Objects.isNull(info.getStrInfo())){
                //找到实心 并且  坐标信息为空的单元格
                isTowRow = Boolean.TRUE;
                break;
            }
        }
        if(!isTowRow){
            return null;
        }
        List<CellInfo> secondList = new ArrayList<>();
        //然后进行截断操作 并且创建新的一行  获取坐标值进行返回
        for (int size = list.size()-1; size >= num; size--) {
            //每加入一个  就移除一个
            secondList.add(list.get(size));
            list.remove(size+0);
        }
        //然后进行倒叙 操作
        Collections.reverse(secondList);
        this.getStrsInfos(secondList, Boolean.FALSE, MergeEnum.ROW);
        return secondList;

    }



    //判断该行是否有同等的间隙
    private Integer getSameBank(List<CellInfo> tempList, Integer bankStartCol, Integer bankEndCol, List<CellInfo> aimList) {
        int bankNum = bankEndCol - bankStartCol;
        //首先不可能跨行    因为之前去过  空行    只能是两列  或者两列以上的  也就是说  bankStartCol  和  bankEndCol
        if (bankStartCol == 0) {
            //那我们就需要确定 后一行 同列就可以了
            CellInfo endTemp = tempList.get(bankEndCol + 1);
            //找到与之同列的    并且判断  数量是否一致
            for (CellInfo aimCell : aimList) {
                //空值就跳过
                if (Objects.nonNull(aimCell.getStrInfo())) {
                    if (isSamRowCol(endTemp.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL) && (aimCell.getColOrder() - bankNum == bankStartCol)) {
                        //如果找到同列的 还要统计数量是否一致
                        return 0;
                    }
                }
            }
        } else if (bankEndCol == tempList.size() - 1) {
            //确定前一列
            CellInfo startTemp = tempList.get(bankStartCol - 1);
            //找到与之同列的    并且判断  数量是否一致
            for (CellInfo aimCell : aimList) {
                //空值就跳过
                if (Objects.nonNull(aimCell.getStrInfo())) {
                    if (isSamRowCol(startTemp.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL) && (aimList.size() - 1 - aimCell.getColOrder() == bankNum)) {
                        //如果找到同列的 还要统计数量是否一致
                        return aimCell.getColOrder().intValue() + 1;
                    }
                }
            }
        } else {
            //最后就是中间的
            //需要确定 前一列 和 后一列
            CellInfo startTemp = tempList.get(bankStartCol - 1);
            CellInfo endTemp = tempList.get(bankEndCol + 1);
            Integer temAimStart = null, temAimEnd = null;
            for (CellInfo aimCell : aimList) {
                //空值就跳过
                if (Objects.nonNull(aimCell.getStrInfo())) {
                    if (isSamRowCol(startTemp.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL)) {
                        //如果找到同列的 还要统计数量是否一致
                        temAimStart = aimCell.getColOrder().intValue();
                    }

                    if (isSamRowCol(endTemp.getStrInfo(), aimCell.getStrInfo(), MergeEnum.COL)) {
                        //如果找到同列的 还要统计数量是否一致
                        temAimEnd = aimCell.getColOrder().intValue();
                    }
                }
            }
            //最后进行  差值的判断
            if (Objects.nonNull(temAimStart) && Objects.nonNull(temAimEnd) && (temAimEnd - temAimStart == bankNum+2)) {
                return temAimStart + 1;
            }
        }
        return null;
    }


    //得到list  空白行的 范围
    private List<Integer[]> getBankRange(List<CellInfo> list) {
        List<Integer[]> bankList = new ArrayList<>();
        Integer start = null;
        Integer end = null;
        for (int num = 0; num < list.size(); num++) {
            CellInfo info = list.get(num);
            if (Objects.isNull(info) || StringUtils.isBlank(info.getVal())) {
                if (Objects.isNull(start)) {
                    start = num++;
                    //然后进行内部循环 直到遇到非空的 就进行跳出
                    if (start == list.size() - 1) {
                        end = start;
                        bankList.add(new Integer[]{start, end});
                        start = null;
                        end = null;
                    } else {
                        for (; num < list.size(); num++) {
                            CellInfo cellInfo = list.get(num);
                            if (Objects.nonNull(cellInfo) && StringUtils.isNotBlank(cellInfo.getVal())) {
                                end = --num;
                                bankList.add(new Integer[]{start, end});
                                start = null;
                                end = null;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return bankList;
    }


    //将纯空行去掉
    private void removeBankRow(List<List<CellInfo>> list) {
        Iterator<List<CellInfo>> it = list.iterator();
        a:
        while (it.hasNext()) {
            List<CellInfo> infos = it.next();
            Boolean isNotBankRow = Boolean.FALSE;
            b:
            for (CellInfo info : infos) {
                String val = info.getVal().replace(" ", " ").trim();
                if (StringUtils.isNotBlank(val)) {
                    isNotBankRow = Boolean.TRUE;
                    break b;
                }
            }
            if (!isNotBankRow) {
                it.remove();
            }
        }

    }

    //进行表头的移除  为了  防止对  纵向模板的选取 的影响
    //如果列表的 第一行 是行左对齐 且仅有一个单元格  就去掉该行
    //如果列表的 第一行 是行居中  且仅有一个单元格  就保留该行
    //判断  中对其 和左对齐  是非左即中

    //问题 ：怎么  确定 该单独地 单元格地坐标 因为 参考系很少  所以
    private void removeHeader(List<List<CellInfo>> list) {
        List<CellInfo> infos = list.get(0).stream().filter(x -> StringUtils.isNotBlank(x.getVal())).collect(Collectors.toList());
        if (infos.size() == 1 && Objects.equals(infos.get(0).getVal(), list.get(0).get(0).getVal())) {
            //然后我们需要判断 这个单元格是不是左对齐  如果是左对齐  就去掉
            //首先获取  list最长的一列
            List<Integer> difList = getDifferent(list);
            if (Objects.nonNull(difList)) {
                //然后获取  列的坐标  并且  找到 得到第一个单元格的坐标
                List<CellInfo> cellInfos = list.get(difList.get(0));
                getStrsInfos(cellInfos, Boolean.FALSE, MergeEnum.ROW);
                //然后去获取  该  首个  单元格的 坐标
                List<StrInfo> strInfos = getStrInfos(infos.get(0).getVal());
                //然后 找到  同页  且  纵坐标  不  小于  该list  的list
                StrInfo info = cellInfos.get(0).getStrInfo();
                Iterator<StrInfo> infoIterator = strInfos.iterator();
                //最后经过筛选的list 如果发现同列的  就移除
                while (infoIterator.hasNext()) {
                    StrInfo next = infoIterator.next();
                    if (Objects.equals(next.getPageNum(), info.getPageNum())
                            || next.getYAxis() >= info.getYAxis()) {
                        //拿到第一个  比较是否是在  同列   或者起始坐标  要远小于  模板的起始坐标
                        if (isSamRowCol(cellInfos.get(0).getStrInfo(), next, MergeEnum.COL)||next.getXStart()<cellInfos.get(0).getStrInfo().getXStart()) {
                            list.remove(0);
                            break;
                        }
                    }
                }
            }
        }
    }


    //这里是默认每个表格都有基本单元格构成的一列
    private void fillMergeCol(List<List<CellInfo>> list) {
        //首先list必须是3行 以上
        if (list.size() < 3) {
            return;
        }
        //然后移除 空白 列
        removeBankCol(list);
        //之前已经使每行的数据量一样了
        //找到 空单元格 最少的 一列
        Integer minBankCol = getTemplateCol(list);
        if (Objects.isNull(minBankCol)) {
            //如果是null值的话  就直接返回就可以
            return;
        }
        //然后就进行位置的纠正

        //真的  和 序号是不一样的
        //也就是说  我们在  拿到了模板和 各列  确定的  序号后
        //和原始的 序号进行比对  如果 一致 就不是合并行
        //如果不一致  就是合并行  并且进行  相应内容的填充
        List<CellInfo> infos = getCol(list, minBankCol);
        //循环的进行比较纠正 序号
        a:
        for (int col = 0; col < list.get(0).size(); col++) {
            if (col != minBankCol) {
                //需要确定坐标的数据
                List<CellInfo> cellInfos = getCol(list, col);

                //记录不一样的 下标 和更正后的对齐标志
                List<Float[]> changeList = new ArrayList<>();

                //循环模板  找模板 跟数据进行对比
                b:
                for (int temIndex = 0; temIndex < infos.size(); temIndex++) {
                    CellInfo temCell = infos.get(temIndex);
                    CellInfo nextTemCell = null;
                    //进行中值的计算 以及最小高度的确定
                    Float middleVal = null;
                    Float minHeight = null;
                    if (temIndex != infos.size() - 1) {
                        nextTemCell = infos.get(temIndex + 1);
                        Float temLength = Float.valueOf(temCell.getStrInfo().getRowHeight().length);
                        Float nextTemLength = Float.valueOf(nextTemCell.getStrInfo().getRowHeight().length);
                        middleVal = ((temCell.getStrInfo().getYAxis() + temCell.getStrInfo().getRowHeight()[0] * (temLength / 2))
                                + (nextTemCell.getStrInfo().getYAxis() - nextTemCell.getStrInfo().getRowHeight()[0] * (nextTemLength / 2))) / 2;
                        minHeight = Math.min(temCell.getStrInfo().getRowHeight()[0], nextTemCell.getStrInfo().getRowHeight()[0]);
                    }
                    c:
                    for (int dataIndex = 0; dataIndex < cellInfos.size(); dataIndex++) {
                        //找到和模板单元格同行的 如果找不到  那么就是两个单元格的合并列
                        CellInfo cellInfo = cellInfos.get(dataIndex);
                        //空单元格 跳过
                        if (StringUtils.isBlank(cellInfo.getVal().replace(" ", " "))) {
                            continue c;
                        }
                        //如果不是空的单元格 就进行坐标的确定
                        if (isSamRowCol(temCell.getStrInfo(), cellInfo.getStrInfo(), MergeEnum.ROW)) {
                            //如果在同一行 就进行下标和序号的比对  如果一致  就不用管  如果不一致 就需要进行记录
                            if (!Objects.equals(cellInfo.getRowOrder(), temCell.getRowOrder())) {
                                Float[] indexOrder = {Float.valueOf(dataIndex), temCell.getRowOrder()};
                                changeList.add(indexOrder);
                            }
                            break c;
                        }
                        //如果不在同一行 就和下一行合并 进行比较
                        if (Objects.nonNull(nextTemCell)) {
                            if (isSameRow(middleVal, minHeight, cellInfo.getStrInfo().getYAxis()
                                    , cellInfo.getStrInfo().getRowHeight()[0])) {
                                //如果使两个行的 合并行 就需要进行记录
                                Float[] indexOrder = {Float.valueOf(dataIndex), temCell.getRowOrder() + 0.5F};
                                changeList.add(indexOrder);
                                break c;
                            }

                        }
                    }
                }

                //这里要进行去重 对于float[0]相同的 只取第一个
                distinctList(changeList);
                //循环完后就进行列的填充  以合并行填充为准则
                for (Float[] floats : changeList) {
                    Float index = floats[0];
                    //数据
                    CellInfo cellInfo = cellInfos.get(index.intValue());
                    Float realOrder = floats[1];
                    Float startOrder = index == 0 ? 0 : (cellInfos.get(index.intValue()).getRowOrder());
                    //以realOrder 为中心  realOrder和上一个order为半径  进行覆盖式填充
                    //填充公式：2*realOrder-startOrder
                    //如果计算出来大于表宽  就按照表宽计算
                    int endOrder = ((int) (2 * realOrder - startOrder) + MERGE_COL_ERROR) >= cellInfos.size() ? cellInfos.size() - 1 : (int) (2 * realOrder - startOrder);
                    for (int order = startOrder.intValue(); order <= endOrder; order++) {
                        //这里要进行空值判断  如果不是空值 就不能进行填充
                        if (order == startOrder.intValue()) {
                            list.get(order).set(col, new CellInfo(cellInfo.getVal(), cellInfo.getColOrder(), Float.valueOf(order), null,cellInfo.getColOrderConf()));
                        } else if (StringUtils.isBlank(list.get(order).get(col).getVal())) {
                            list.get(order).set(col, new CellInfo(cellInfo.getVal(), cellInfo.getColOrder(), Float.valueOf(order), null,cellInfo.getColOrderConf()));
                        } else {
                            break;
                        }

                    }

                }
            }
        }
    }


    //双重for循环进行去重
    private void distinctList(List<Float[]> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            Float[] floats = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Float[] floats1 = list.get(j);
                if (Objects.equals(floats[0], floats1[0])) {
                    //如果相等 就进行剔除
                    list.remove(j-- + 0);
                }
            }
        }
    }


    //找到模板列 首先是  单元格 在误差范围内  非空单元格中点最上   且最先出现 空单元格就优先淘汰
    //这里需要拼凑模板 而非  寻找模板
    private Integer getTemplateCol(List<List<CellInfo>> lists) {
        //记录每行非空单元格最上的坐标   以及第一个出现 空单元格  的行数
        // {“colIndex”,"topAxis","firstBankCellIndex","CellHeight"}
        Float[] template = new Float[4];
        for (int num = 0; num < lists.get(0).size(); num++) {
            List<CellInfo> col = getCol(lists, num);
            //为每行添加坐标值
            getStrsInfos(col, Boolean.FALSE, MergeEnum.COL);
            //为每行添加顺序值
            for (int order = 0; order < col.size(); order++) {
                col.get(order).setRowOrder(Float.valueOf(order));
            }
            Integer index = firstIndex(col, Boolean.FALSE);
            //该列第一个不为空的下标
            if (Objects.isNull(template[0])) {
                template[0] = Float.valueOf(num);
                template[1] = col.get(index).getStrInfo().getYAxis();
                template[2] = Float.valueOf(firstIndex(col, Boolean.TRUE));
                template[3] = col.get(index).getStrInfo().getRowHeight()[0];
            } else {
                //首先要比较是否是同一行  如果是同一行 就比较最先出现 空白单元格的位置
                Float yAxis = col.get(index).getStrInfo().getYAxis();
                Float bankIndex = Float.valueOf(firstIndex(col, Boolean.TRUE));
                Float height = col.get(index).getStrInfo().getRowHeight()[0];
                if (isSameRow(template[1], template[3], yAxis, height)) {
                    if (template[2] < bankIndex) {
                        //就需要重置 存储
                        template = new Float[]{Float.valueOf(num), yAxis, bankIndex, height};
                    }
                    //如果新的数据要高于旧的数据  也要进行变换
                } else if (yAxis > template[0]) {
                    template = new Float[]{Float.valueOf(num), yAxis, bankIndex, height};
                }
            }
        }

        List<CellInfo> templateCol = getCol(lists, template[0].intValue());
        //这里还要进行判断 因为我们得到地模板行 很有可能  不是到底的   我们的模板必须概括所有的
        if (template[2].intValue() != templateCol.size()) {
            //然后我们就需要 把其他非空行 的坐标信息  val继续为空 但是坐标信息拷贝的是  别的列的
            //遍历模板行   遇到  空单元格  就去其他列寻找   如果找不到  就空着
            for (int temOrder = 0; temOrder < templateCol.size(); temOrder++) {
                CellInfo temCell = templateCol.get(temOrder);
                if (Objects.isNull(temCell.getStrInfo())) {
                    //然后就去遍历其他列   找到与之对应的基本单元格
                    for (int orderNum = 0; orderNum < lists.get(0).size(); orderNum++) {
                        if (orderNum != template[0]) {
                            //找到  list  同位置的单元格
                            CellInfo info = getCol(lists, orderNum).get(temOrder);
                            if (template[2].intValue() != templateCol.size() - 1) {
                                //如果不为空  就剔除X坐标 复制Y坐标 进行赋值    并跳出循环
                                if (Objects.nonNull(info.getStrInfo())) {
                                    temCell.setStrInfo(new StrInfo(temCell.getVal(), null
                                            , null, null, null
                                            , info.getStrInfo().getYAxis(), info.getStrInfo().getRowHeight()
                                            , info.getStrInfo().getPageNum()));
                                    break;
                                }
                            } else {
                                CellInfo nextCell = lists.get(orderNum).get(temOrder + 1);
                                if (Objects.nonNull(info.getStrInfo()) && Objects.nonNull(nextCell.getStrInfo())) {
                                    temCell.setStrInfo(new StrInfo(temCell.getVal(), null
                                            , null, null, null
                                            , info.getStrInfo().getYAxis(), info.getStrInfo().getRowHeight()
                                            , info.getStrInfo().getPageNum()));
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        }

        return template[0].intValue();
    }

    //比较两个单元格是否在同一行
    private Boolean isSameRow(Float y1, Float height1, Float y2, Float height2) {
        return (Math.abs(y1 - y2) / Math.min(height1, height2)) < ROW_ERROR[1] ? Boolean.TRUE : Boolean.FALSE;
    }


    //比较两个单元格是否在同一行  /  同一列
    private Boolean isSamRowCol(StrInfo c1, StrInfo c2, MergeEnum status) {
        if (status == MergeEnum.COL) {
            //进行同列判断
            //计算宽度
            float width = ((c1.getWidth() / c1.getVal().length()) + c2.getWidth() / c2.getVal().length()) / 2;
            //前重合
            return (Math.abs(c1.getXStart() - c2.getXStart()) / width >= COL_ERROR[0][0]
                    && Math.abs(c1.getXStart() - c2.getXStart()) / width <= COL_ERROR[0][1]) ? Boolean.TRUE
                    : (Math.abs(c1.getXEnd() - c2.getXEnd()) / width >= COL_ERROR[1][0] &&
                    Math.abs(c1.getXEnd() - c2.getXEnd()) / width <= COL_ERROR[1][1]) ? Boolean.TRUE
                    : (Math.abs(c1.getXMiddle() - c2.getXMiddle()) / width >= COL_ERROR[2][0] &&
                    Math.abs(c1.getXMiddle() - c2.getXMiddle()) / width <= COL_ERROR[2][1]) ? Boolean.TRUE : Boolean.FALSE;
        } else {
            //进行同行判断
            System.out.println(c1.getVal() + "====" + c2.getVal());
            return (Math.abs(c1.getYAxis() - c2.getYAxis())
                    / Math.min(c1.getRowHeight()[0], c2.getRowHeight()[0])) < ROW_ERROR[1] ? Boolean.TRUE : Boolean.FALSE;
        }
    }


    //找出list中第一个空/非空的单元格  下标
    private Integer firstIndex(List<CellInfo> col, Boolean isBank) {
        Integer index = null;
        for (int num = 0; num < col.size(); num++) {
            String val = col.get(num).getVal().replace(" ", " ").trim();
            if (isBank) {
                if (StringUtils.isBlank(val)) {
                    index = num;
                    break;
                }
            } else {
                if (StringUtils.isNotBlank(val)) {
                    index = num;
                    break;
                }
            }
        }
        return Objects.isNull(index) ? col.size() : index;
    }


    //移除空白列
    private void removeBankCol(List<List<CellInfo>> list) {
        List<Integer> removes = new ArrayList<>();
        //找出list中 长度最小的list
        List<CellInfo> min = Collections.min(list, new Comparator<List<CellInfo>>() {
            @Override
            public int compare(List<CellInfo> o1, List<CellInfo> o2) {
                int dVal = o1.size() - o2.size();
                if (dVal < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        for (int num = 0; num < min.size(); num++) {
            List<CellInfo> col = getCol(list, num);
            Integer bankInList = statisBankInList(col);
            if (Objects.equals(col.size(), bankInList)) {
                //如果相同就移除
                removes.add(num);
            }
        }
        //倒序
        Collections.reverse(removes);
        //最后进行移除  也就是说每行都必须把这个移除掉
        for (Integer remove : removes) {
            for (List<CellInfo> cellInfos : list) {
                cellInfos.remove(remove + 0);
            }
        }
    }


    //找到长度 最长的 list 以及 与之长度不同的list
    //第一个  元素为最长的 元素的下表 之后的元素为与之长度不同的元素的下标
    private List<Integer> getDifferent(List<List<CellInfo>> list) {
        List<Integer> result = new ArrayList<>();
        Integer maxLenthIndex = null;
        //首先找到最长的  最好是非空的单元格
        for (int num = 0; num < list.size(); num++) {
            if (Objects.isNull(maxLenthIndex)) {
                maxLenthIndex = num;
            } else {
                Integer nowSize = list.get(num).size();
                Integer oldMaxSize = list.get(maxLenthIndex).size();
                if (Objects.equals(nowSize, oldMaxSize)) {
                    //如果相同就尽量找  非空的单元格
                    //遍历两个统计 bank的数量  选择少的
                    maxLenthIndex = statisBankInList(list.get(num)) < statisBankInList(list.get(maxLenthIndex)) ? num : maxLenthIndex;


                } else if (nowSize > oldMaxSize) {
                    maxLenthIndex = num;
                }
            }
        }
        //然后进行 不同长度的统计
        if (Objects.isNull(maxLenthIndex)) {
            return null;
        }
        result.add(maxLenthIndex);
        for (int order = 0; order < list.size(); order++) {
            if (list.get(order).size() != list.get(maxLenthIndex).size()) {
                result.add(order);
            }
        }
        return result;
    }


    //对list中  空字段进行统计
    private Integer statisBankInList(List<CellInfo> list) {
        Integer bankNum = 0;
        for (CellInfo info : list) {
            String val = info.getVal().replace(" ", " ").trim();
            if (StringUtils.isBlank(val) || Objects.isNull(val)) {
                bankNum++;
            }
        }
        return bankNum;
    }

    private Integer statisBankCellInList(List<StrInfo> list) {
        Integer bankNum = 0;
        for (StrInfo strInfo : list) {
            if (Objects.isNull(strInfo)) {
                bankNum++;
            }
        }
        return bankNum;
    }


    //将table 元素进行分解   td为最小单元  tr为一个list
    private static List<List<CellInfo>> takeApartElement(Element element) {
        List<List<CellInfo>> result = new ArrayList<>();
        if (StringUtils.equalsAny(element.getName(), PRIMARY_LABEL)) {
            List<Element> trList = new ArrayList<>();
            //如果是Table标签的话  才可以进行  分解
            //因为上面已经筛选过，这个是最小的Table 单元 不需要担心表格的嵌套现象
            toTrTd(trList, element, SECONDARY_LABEL);
            //填充完后  就再进行遍历 进行内层的填充
            for (Element ele : trList) {
                List<CellInfo> trStrs = new ArrayList<>();
                List<Element> tdList = new ArrayList<>();
                //每个tr行的   td集合
                toTrTd(tdList, ele, TERTIARY_LABEL);
                //然后我们把每个td都进行转化
                for (int num = 0; num < tdList.size(); num++) {
                    List<String> strList = new ArrayList<>();
                    Element trEle = tdList.get(num);
                    collectTd(strList, trEle);
                    //然后将 StringList 转为字符串
                    String str = StringUtils.join(strList, StringUtils.EMPTY);
                    if (!StringUtils.containsAny(str, IGNORE_WORD)) {
                        trStrs.add(new CellInfo(str, Float.valueOf(num), null, null,Boolean.FALSE));
                    }
                }
                result.add(trStrs);
            }
        }
        return result;
    }


    /**
     * 通过递归  将table元素  分解为多个  tr元素  或者将 tr元素 分解成多个 td元素
     *
     * @param list     结果容器
     * @param element  要分解的元素
     * @param eleNames 要分解成的元素名
     */
    private static void toTrTd(List<Element> list, Element element, String[] eleNames) {
        //结束条件
        //没有子元素了，并且该元素 也不是tr/td元素 不做任何操作
        //本元素是tr/td元素  就加入到list中


        //递归条件
        //本元素不是tr/td元素  但是还是含有子元素
        String elementName = element.getName();
        List<Element> elementList = element.elements();
        if (StringUtils.equalsAny(elementName, eleNames)) {
            list.add(element);
        } else {
            if (elementList.size() != 0) {
                for (Element ele : elementList) {
                    toTrTd(list, ele, eleNames);
                }
            }
        }
    }

    //把TD元素  进行递推 把所有的  元素相加
    private static void collectTd(List<String> list, Element element) {
        //结束条件
        //不含任何元素  就把该元素的值 添加到list中

        //递归条件
        //含有元素 一直递归下去


        List<Element> elementList = element.elements();
        if (elementList.size() == 0) {
            list.add(element.getStringValue());
        } else {
            for (Element ele : elementList) {
                collectTd(list, ele);
            }
        }

    }


    //递推函数
    private static void analysisList(List<Element> tables, Element element) {
        Integer tableNum = StringUtils.countMatches(element.asXML(), "<Table>");
        if (element.getName().equals("Table") && tableNum == 1&&(!StringUtils.containsAny(element.asXML(),IGNORE_ELEMENT))) {
            tables.add(element);
        } else if (tableNum >= 1) {
            //最后一种情况就是   含有多个<Table>标签  就需要进行分解 递归
            for (Element ele : element.elements()) {
                //递归调用
                analysisList(tables, ele);
            }
        } else {

        }
    }

    @Override
    protected void loadDatas(String filePath) throws IOException {
        PdfReader pdfReader = new PdfReader(filePath);

        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            //获取每一页的字符集
            CustomerRenderListener customerRenderListener = new CustomerRenderListener(i);
            pdfReaderContentParser.processContent(i, customerRenderListener);
            //每一页的字符以及字符坐标
            List<WordVO> wordVOS = customerRenderListener.getWordVOS();
            subjectList.add(wordVOS);
        }
    }


    /**
     * @param strs          需要获取坐标的字符串
     * @param clearColOrder 是否清除 列顺序
     * @param isXaxis       是都是横向统计
     */
    @Override
    protected void getStrsInfos(List<CellInfo> strs, Boolean clearColOrder, MergeEnum isXaxis) {
        //循环的 进入容器中进行寻找 然后选出最好的一组
        List<List<StrInfo>> list = new ArrayList<>();
        for (CellInfo str : strs) {
            list.add(getStrInfos(str.getVal()));
        }
        //首先找出他们都还有的公共的页数
        //如果这个都不能进行筛选完 因为一个list通常是一片连续的区域 所以  再次就是Y坐标 再次就是X坐标
        List<List<StrInfo>> template = new ArrayList<>();

        //挑选出 list中 长度不为0的
        Integer fillTemplate = fillTemplate(list, template);

        //如果找不到长度不为0的  说明 list中所有的元素 都是空的  我们对他们的下标向后 推迟偏移量即可
        if (Objects.isNull(fillTemplate)) {
            for (CellInfo info : strs) {
                info.setColOrder(info.getColOrder() + EMPTY_ROW_OFFSET);
            }
            return;
        }
        //以模板为中心  进行扩散 选取半径最小的 点  优先同行或者同列
        //先进行向0开始扩散
        a:
        for (Integer reverse = fillTemplate - 1; reverse >= 0; reverse--) {
            List<StrInfo> needOrder = list.get(reverse);
            //拿模板 循环
            b:
            for (List<StrInfo> modulList : template) {
                //拿到模板最近的 非null的元素
                StrInfo strInfo = maxIndexStrinfo(modulList);
                //同 待检测  的 list每个数据进行比对
                //如果 needOrder  是个空的  也要加个空的进去
                //如果不是空的  就比较半径 优先 同行列
                //两个元素  分别是最小的 半径值 以及对应的 是否是同行列  1 表示同行 或者同列 0则相反
                Double[] radiusCoor = new Double[2];
                StrInfo teminfo = null;
                //找到同行/列的就不用异行/列的了
                Boolean findSameRowCol = Boolean.FALSE;
                c:
                for (StrInfo info : needOrder) {
                    //这里要找到同页同行/列的  并记录距离值
                    if (Objects.nonNull(info) && Objects.equals(info.getPageNum(), strInfo.getPageNum())) {
                        findSameRowCol = isSamRowCol(info, strInfo, isXaxis) ? Boolean.TRUE : Boolean.FALSE;
                        Float tempYDval = info.getYAxis() - strInfo.getYAxis();
                        Float tempXDval = info.getXMiddle() - strInfo.getXMiddle();
                        Double radius = Math.pow(tempYDval, 2) + Math.pow(tempXDval, 2);

                        //这里要进行数据的 排除
                        if ((isXaxis == MergeEnum.COL && tempYDval <= 0) || (isXaxis == MergeEnum.ROW && tempXDval >= 0)) {
                            continue c;
                        }
                        if (Objects.isNull(teminfo)) {
                            teminfo = info;
                            //并且将坐标记录进去
                            radiusCoor[0] = radius;
                            radiusCoor[1] = findSameRowCol ? 1d : 0d;
                        } else {
                            //如果不为空  就进行比较
                            if (findSameRowCol && Objects.equals(radiusCoor[1], 1d)) {
                                //如果两个 都为  true 就进行半径的比较  选取半径最小的
                                if (radius < radiusCoor[0]) {
                                    teminfo = info;
                                    radiusCoor[0] = radius;
                                    radiusCoor[1] = 1d;
                                }
                            } else if (findSameRowCol && Objects.equals(radiusCoor[1], 0d)) {
                                //如果新的 是同行的  旧的 是异行 的 不要犹豫 直接进行赋值
                                teminfo = info;
                                radiusCoor[0] = radius;
                                radiusCoor[1] = 1d;
                            } else if ((!findSameRowCol) && Objects.equals(radiusCoor[1], 0d)) {
                                //如果新的 是 异行的 旧的 是同行的 直接跳过
                                //如果新的 是异行的 旧的是异行的 就进行半径的比较
                                if (radius < radiusCoor[0]) {
                                    teminfo = info;
                                    radiusCoor[0] = radius;
                                    radiusCoor[1] = 0d;
                                }
                            }
                        }
                    }
                }
                //不管怎么样 都要加个进去
                modulList.add(teminfo);
            }
        }
        reverseList(template, 0);
        //再进行向 list终点进行扩散
        for (int forward = fillTemplate + 1; forward < list.size(); forward++) {
            List<StrInfo> needOrder = list.get(forward);
            //拿模板 循环
            b:
            for (List<StrInfo> modulList : template) {
                //拿到模板最近的 非null的元素
                StrInfo strInfo = maxIndexStrinfo(modulList);
                //同 待检测  的 list每个数据进行比对
                //如果 needOrder  是个空的  也要加个空的进去
                //如果不是空的  就比较半径 优先 同行列
                //两个元素  分别是最小的 半径值 以及对应的 是否是同行列  1 表示同行 或者同列 0则相反
                Double[] radiusCoor = new Double[2];
                StrInfo teminfo = null;
                //找到同行/列的就不用异行/列的了
                Boolean findSameRowCol = Boolean.FALSE;
                c:
                for (StrInfo info : needOrder) {
                    //这里要找到同页同行/列的  并记录距离值
                    if (Objects.nonNull(info) && Objects.equals(info.getPageNum(), strInfo.getPageNum())) {
                        findSameRowCol = isSamRowCol(info, strInfo, isXaxis) ? Boolean.TRUE : Boolean.FALSE;
                        Float tempYDval = info.getYAxis() - strInfo.getYAxis();
                        Float tempXDval = info.getXStart() - strInfo.getXEnd();
                        Double radius = Math.pow(tempYDval, 2) + Math.pow(tempXDval, 2);

                        //这里要进行数据的 排除
                        //在同一行  就不考察  差值了 不在同一行 就需要考察差值
                        if ( (isXaxis == MergeEnum.COL && tempYDval >= 0) || (isXaxis == MergeEnum.ROW && tempXDval <= 0)) {
                            continue c;
                        }
                        if (Objects.isNull(teminfo)) {
                            teminfo = info;
                            //并且将坐标记录进去
                            radiusCoor[0] = radius;
                            radiusCoor[1] = findSameRowCol ? 1d : 0d;
                        } else {
                            //如果不为空  就进行比较
                            if (findSameRowCol && Objects.equals(radiusCoor[1], 1d)) {
                                //如果两个 都为  true 就进行半径的比较  选取半径最小的
                                if (radius < radiusCoor[0]) {
                                    teminfo = info;
                                    radiusCoor[0] = radius;
                                    radiusCoor[1] = 1d;
                                }
                            } else if (findSameRowCol && Objects.equals(radiusCoor[1], 0d)) {
                                //如果新的 是同行的  旧的 是异行 的 不要犹豫 直接进行赋值
                                teminfo = info;
                                radiusCoor[0] = radius;
                                radiusCoor[1] = 1d;
                            } else if ((!findSameRowCol) && Objects.equals(radiusCoor[1], 0d)) {
                                //如果新的 是 异行的 旧的 是同行的 直接跳过
                                //如果新的 是异行的 旧的是异行的 就进行半径的比较
                                if (radius < radiusCoor[0]) {
                                    teminfo = info;
                                    radiusCoor[0] = radius;
                                    radiusCoor[1] = 0d;
                                }
                            }
                        }
                    }
                }
                //不管怎么样 都要加个进去
                modulList.add(teminfo);
            }
        }

        //应该是取空单元格 最少的列  然后再取  极差 最小的 列
        //如果是横向排序  就找 Y坐标极差 最小的列
        //如果是纵向排序  就找X坐标 极差 最小的列
        //这里还要进行顺序  排序
        Integer bankNum = 10000;
        Float Range = 10000F;
        List<StrInfo> resultList = new ArrayList<>();
        for (List<StrInfo> infos : template) {
            //if (isNormalSort(infos,isXaxis)) {
                Integer bankCellInList = statisBankCellInList(infos);
                if (bankNum > bankCellInList) {
                    resultList = infos;
                    bankNum = bankCellInList;
                    //并且  记录它的极差
                    Range = getRange(infos, isXaxis);
                } else if (Objects.equals(bankNum, bankCellInList)) {
                    //比较极差
                    //计算  现有的  Y极差    并且比较
                    Float tempYRange = getRange(infos, isXaxis);
                    //选取极差小的
                    if (tempYRange < Range) {
                        Range = tempYRange;
                        resultList = infos;
                    }
                }
            //}
        }

        //取出最长的list  且 该list的数目 必须 和传入的  长度一致
        if (strs.size() == resultList.size()) {
            for (int num = 0; num < strs.size(); num++) {
                CellInfo cellInfo = strs.get(num);
                StrInfo strInfo = resultList.get(num);
                cellInfo.setStrInfo(strInfo);
                if (clearColOrder) {
                    if (Objects.isNull(cellInfo.getStrInfo())) {
                        //如果是空的  就将序号往后面标即可
                        cellInfo.setColOrder(cellInfo.getColOrder() + EMPTY_ROW_OFFSET);
                    } else {
                        cellInfo.setColOrder(null);
                    }
                }
            }
        }
    }


    //判断list 是否符合  横向排序或者  纵向排序
    private Boolean isNormalSort(List<StrInfo> list, MergeEnum isXaxis){
        for (int num = 0; num < list.size()-1; num++) {
            //然后取下一个进行比较
            StrInfo strInfo = list.get(num);
            StrInfo nextInfo = list.get(num + 1);
            if(Objects.nonNull(strInfo)&&Objects.nonNull(nextInfo)){
                if(isXaxis == MergeEnum.ROW){
                    //同行的话  就对  Xstart是否符合顺序
                    if(nextInfo.getXStart() < strInfo.getXStart()){
                        return Boolean.FALSE;
                    }
                }else {
                    if(nextInfo.getYAxis() > strInfo.getYAxis()){
                        return Boolean.FALSE;
                    }
                }
            }

        }
        return Boolean.TRUE;
    }


    //求 list 的  x轴 极差  和  y轴的极差
    private Float getRange(List<StrInfo> list, MergeEnum isXaxis) {
        if (isXaxis == MergeEnum.ROW) {
            //同行的话  就 对  y的极差进行统计
            //因为之前  去掉空行了  所以是不会出现  空行的
            Float minY = null;
            Float maxY = null;
            for (StrInfo info : list) {
                if (Objects.nonNull(info)) {
                    if (Objects.isNull(minY)) {
                        minY = info.getYAxis();
                        maxY = info.getYAxis();
                    } else {
                        //进行比较
                        if (minY > info.getYAxis()) {
                            minY = info.getYAxis();
                        }
                        if (maxY < info.getYAxis()) {
                            maxY = info.getYAxis();
                        }
                    }
                }
            }
            return Math.abs(minY - maxY);
        } else {
            //同列的话  就对  x的极差 进行统计
            //因为之前  去掉空列了  所以是不会出现  空列的
            Float minX = null;
            Float maxX = null;
            for (StrInfo info : list) {
                if (Objects.nonNull(info)) {
                    if (Objects.isNull(minX)) {
                        minX = info.getXStart();
                        maxX = info.getXStart();
                    } else {
                        //进行比较
                        if (minX > info.getXStart()) {
                            minX = info.getXStart();
                        }
                        if (maxX < info.getXStart()) {
                            maxX = info.getXStart();
                        }
                    }
                }
            }
            return Math.abs(minX - maxX);
        }
    }


    //对list  第N个节点后进行倒序,起始于0
    public static void reverseList(List<List<StrInfo>> list, Integer num) {
        for (List<StrInfo> strings : list) {
            List<StrInfo> needResverse = strings.subList(num, strings.size());
            //然后我们对获取到的进行倒序
            Collections.reverse(needResverse);
        }
    }

    //找到list中  非null元素的最大值 第一个元素肯定不是空的
    private StrInfo maxIndexStrinfo(List<StrInfo> list) {
        StrInfo result = null;
        //倒叙  找到非空的最大值
        for (int i = list.size() - 1; i >= 0; i--) {
            StrInfo info = list.get(i);
            if (Objects.nonNull(list.get(i))) {
                result = info;
                break;
            }
        }
        return result;
    }

    //获取表格 list的 第 n 列
    private List<CellInfo> getCol(List<List<CellInfo>> lists, Integer colNum) {
        List<CellInfo> result = new ArrayList<>();
        for (List<CellInfo> infos : lists) {
            result.add(infos.get(colNum));
        }
        return result;
    }

    //找出list中长度 不为0的 添加到 模板list中  并且返回下标
    private Integer fillTemplate(List<List<StrInfo>> list, List<List<StrInfo>> template) {
        Integer result = null;
        for (int num = 0; num < list.size(); num++) {
            List<StrInfo> infos = list.get(num);
            if (infos.size() > 0) {
                //然后把他存入模板中
                for (StrInfo info : infos) {
                    List<StrInfo> temList = new ArrayList<>();
                    temList.add(info);
                    template.add(temList);
                }
                result = num;
                break;
            }
        }
        return result;
    }

    @Override
    protected List<StrInfo> getStrInfos(String str) {
        //将两端的 空格去掉即可
        String[] keys = str.replace(" ", " ").trim().split("");

        List<WordVO> filterList = new ArrayList<>();
        //筛选出每页符合当前关键词首个字符的元素
        for (int i = 0; i < subjectList.size(); i++) {
            //这里直接匹配出文档中所有等于字符首字母的元素
            filterList.addAll(subjectList.get(i).stream().filter(word -> word.getWord().equals(keys[0])).collect(Collectors.toList()));
        }
        List<StrInfo> result = new ArrayList<>();
        //获取符合关键字的结果
        for (WordVO wordVO : filterList) {
            List<WordVO> wordVOs = new ArrayList<>();
            List<WordVO> pageWordVO = subjectList.get(wordVO.getPageNo() - 1);
            //如果关键字长度只有1，表示未分割就去匹配，如果匹配到了直接返回结果就行
            if (keys.length == 1) {
                wordVOs.add(wordVO);
            } else {
                for (int i = 1; i < keys.length; i++) {
                    //如果第二个字符不是我们想要的 直接跳过
                    if (pageWordVO.size() <= (wordVO.getIndex() + i) || !keys[i].equals(pageWordVO.get(wordVO.getIndex() + i).getWord())) {
                        break;
                    }
                    //表示最后一个字符都符合了，表示已经符合我我们给出的关键字标准
                    if (i == keys.length - 1) {
                        for (int j = 0; j < keys.length; j++) {
                            wordVOs.add(pageWordVO.get(wordVO.getIndex() + j));
                        }
                    }
                }
            }

/*
            //这里进行检测  长度向前加一   得到的字符串   是不存在于xml的字符串中的
            if(wordVO.getIndex()!=0){
                //向前移动一位  在xml中遍历寻找  找不到才正常  找得到就不计入统计
                String trim = pageWordVO.get(wordVO.getIndex() - 1).getWord().trim();
                if(StringUtils.isNotBlank(trim)){
                    trim = trim+str;
                    //然后到xml中寻找

                }

            }
*/
            //这里进行转化
            if (!CollectionUtils.isEmpty(wordVOs)) {
                result.add(totalPos(wordVOs));
            }
        }
        return result;
    }

    @Override
    protected StrInfo totalPos(List<WordVO> list) {
        //统计 字符串 信息
        StringBuilder str = new StringBuilder(StringUtils.EMPTY);
        //将零碎的字符信息  转换为
        Float xStart = list.get(0).getX();
        Float xEnd = 0F;
        //然后就是  对文字宽度进行统计 Y值不同  就进行宽度统计
        List<Float> heights = new ArrayList<>();
        //遍历 list 当Y值不一样时就进元素+1
        Float temHeight = 0F;
        for (WordVO vo : list) {
            if (!Objects.equals(temHeight, vo.getY())) {
                heights.add(vo.getHeight());
                temHeight = vo.getY();
            }
            //这里还需要统计出最大值
            xEnd = xEnd < vo.getX() ? (vo.getX() + vo.getWidth()) : xEnd;
            str.append(vo.getWord());
        }
        Float xMiddle = (xStart + xEnd) / 2;
        Float width = Math.abs(xStart - xEnd);
        //这里是忽略高度影响  直接取中间值
        Float yAxis = (list.get(0).getY() + list.get(list.size() - 1).getY()) / 2;
        //最后再进行list到 数组的转换
        Float[] rowHeights = heights.toArray(new Float[heights.size()]);
        //页数直接进行取就行了
        Integer pageNum = list.get(0).getPageNo();
        //最后进行文字的统计
        return new StrInfo(str.toString(), xStart, xEnd, xMiddle, width, yAxis, rowHeights, pageNum);
    }


}
