package ocrexcel.demo.fuxin;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.foxit.sdk.addon.conversion.Convert;
import com.foxit.sdk.common.Library;
import lombok.extern.slf4j.Slf4j;
import ocrexcel.demo.fuxin.config.PaddleOcr;
import ocrexcel.demo.fuxin.moddle.*;
import ocrexcel.demo.fuxin.utils.PdfStrAxis;
import ocrexcel.demo.fuxin.utils.ThreadPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

/**
 * @program: RestDemo
 * @description: 福昕 进行转换 paddle进行定位
 * @author: wjl
 * @create: 2022-06-27 16:20
 **/

@Slf4j
public class PdfExcelPaddle extends PdfStrAxis {


    //table  面线点的 切割标签名
    //一级目录
    private static final String[] PRIMARY_LABEL = {"Table"};
    //二级标签
    private static final String[] SECONDARY_LABEL = {"TR"};
    //三级标签
    private static final String[] TERTIARY_LABEL = {"TD","TH"};

    //判断时候是同一列的  误差值  误差依次为：①前对齐误差  ② 后对齐误差  ③  居中误差
    //计算公式： （1）(xStart_1 - xStart_2)/fontSize    (2)(xEnd_1 - xEnd_2)/fontSize   (3)(xMiddle_1 - xMiddle_2)/fontSize
    private static final Float[] COL_ERROR ={0.9F,0.9F,0.9F};

    //判断是否是 某两列的 合并列的差值阈值  高于这个阈值  就进入到了两列合并的判断
    //合并列仅仅适用于  中对称 计算公式 ：(xMiddle_1 - xMiddle_2)/fontSize
    private static final Float IN_MERGE_COL = 0.9F;

    private static String output_path = "src/main/java/com/example/demo/fuxin/filePackage/";
    private static String key = "8f0YFcONvRkN+ldwlpAFW0NF+Q/jOhOBvj1efQAKLLKux+nGeHqbsdctYiw0TuZREd2ungiuZk2tsbLXEusM2l+cVxG18jYetvFudmoPiOHiDqfKr2MseAswJLWvtMr+i9zTHDYhDuZd9ESe2YJ6rQxX1i9OK/ahiUGyOcwQKMl4p9zkJ0PYiW6nJlf6YQ2uufXpn4jvJ8crJIPZ0GG561aEKjw5GmrEeaEgpnEsMXzDm3lY8gak9qCq4iHP7Rt/gLYgN81sXm+LiDjkBUJ9458JXwOJMR/CIVqLCceOOK/xVH9tKhWM38L+LZHDPcDKUPl5Q3z/bGam9xWQUjIGFcEzN/aMAsVXohDoX7yFSvGMTx7KXklqx/sJinOfQB2w3NZuQWBhC8FyaqxDbnN8/61jEc7waH/6JBjg2sovJUtAmIJYcTBJXBiaAFhpJ3sODTnVCLrkO90ZS2ujrdoLgoyQb/LizDjjuQL/pGWnp+KGc+OL5QrkU6CIEszPNeUfWzAFLHvIS0KJOkU+FhDh3woANn4KdNxih4fQnOhDSIYuFsf8bLdd2cmRf+c6Kh1Hlz4k5TprGgYHgeR7/fKVFzQU8ThnUgEgmIE69+2l5c89RYwnl8IcCNM9Zkqfs7Q/swaQ8qzSn/lzUH1s9lgLosIp+B9wiAHptqnLdel7nHq7k4CZbPZbGyk2vngYhmterOmM+7ErS3wTlQfr3Xq4INvYgenUbOmazGTHOQ6KMdWiX9a7GyuCVEDtLwiDtvwogj6Hz0L8FBu/wJ5YVG9O8R/4AvJ5vI0rHtPXQm2JijV1MqlUuSX5EXF3TTflhDGrxjyl0WTZePvcFIJgP26SwZhzAfAaLDz9qejSZakPSqFuUY0q201DB8tw4fyITj8hVIruR7IGvq+bnEGSuwI/zKSPNrtpEZPkvhUyitQlebK2AIF6Xc6vlXHnusb4D2N8YGjCTom1BSaNZGcVOmOD9cj6GPw4/uDaKffDfvFYChfN4U1wELZAsvkbY8Gl9oVFqRosOWkfm3kCg7q7+LwtUdgYFSVTqnFgO4zyNNC9Cje/aNrEL775/KVuZcWZTpEggxYuldML8qw8z6sHxO84/W4u/t9ZNBMGfp+g3RSEVGmE8iDRE2ehhIqo26injEF+d4f62BF5bjJfrzUZI6vbMkwUrj/0y5HF095aLx9nSGTYqcrP/4V53wgwBRnKCu76sHXGD+F+6FLzhN4PvMDaY+V7lYJj9J6fR0qbuGh9nokSbCWUMQMEjAyRCGhzQ28DP6ewi46MSVRzbZvdTolWnN/qsDs9Q593SE8YdswZxicGwsCZkCspArE=";
    private static String sn = "Izt0owtF5aH9zVuoR62u2FasDHg1UPvSg/JURm8gJMUB5Tb1eDgiUg==";
    static {
        String os = System.getProperty("os.name").toLowerCase();
        String lib = "fsdk_java_";
        if (os.startsWith("win")) {
            lib += "win";
        } else if (os.startsWith("mac")) {
            lib += "mac";
        } else {
            lib += "linux";
        }
        if (System.getProperty("sun.arch.data.model").equals("64")) {
            lib += "64";
        } else {
            lib += "32";
        }
        System.loadLibrary(lib);
    }

    public void PdfToExcel(RestTemplate restTemplate, PaddleOcr paddleOcr)throws Exception{
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            return;
        }
        //时间戳形成文件名
        String save_path = output_path+getDateStr()+".xml";
        String file_path = "D:/PDF/pdf/4.pdf";
        boolean isToXMl = Convert.toXML(file_path, "", save_path, "", true);
        log.info(isToXMl?"转换成功":"转换失败");
        //查询容器的填充
        //this.loadDatas(file_path,restTemplate,paddleOcr);
        parseXml_ForTest(save_path);


    }
    //仅对table 部分进行转换 而且是最内层table
    public String parseXml_ForTest(String save_path) throws Exception {
        //加载xml文件
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(save_path));
        Element rootElement = document.getRootElement(); // 获取根节点
        //然后得到  table  节点
        //然后先输出
        List<Element> tableList = new ArrayList<>();
        analysisList(tableList,rootElement);
        ExcelWriter build = EasyExcel.write("./src/main/resources/static/excel/"+UUID.randomUUID().toString().replace("-", "").toUpperCase()+".xlsx").build();
        for (int num = 0; num < tableList.size(); num++) {
            //创建sheet
            WriteSheet tempSheet = EasyExcel.writerSheet("表" + (num + 1)).build();
            //然后这里获取数据   每一个table 元素
            List<List<TrCellInfo>> lists = takeApartElement(tableList.get(num));
            List<List<String>> result =  new ArrayList<>();
            //这里将 泛型转为 String
            for (List<TrCellInfo> list : lists) {
                List<String> temList = new ArrayList<>();
                for (TrCellInfo info : list) {
                    temList.add(info.getVal());
                }
                result.add(temList);
            }
            //最后写入
            build.write(result,tempSheet);
        }
        build.finish();
        return "";
    }




        //仅对table 部分进行转换 而且是最内层table
    public String parseXml(String save_path) throws Exception {
        //加载xml文件
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(save_path));
        Element rootElement = document.getRootElement(); // 获取根节点
        //然后得到  table  节点
        //然后先输出
        List<Element> tableList = new ArrayList<>();
        analysisList(tableList,rootElement);
        //遍历 然后输出
        //然后就进行excel 导出
        //最小单位为TD  内部的自动合并  进行输出
        //每一个table 对应一个 sheet
        //创建 excel
        //获取当前路劲
        ExcelWriter build = EasyExcel.write("./src/main/resources/static/excel/"+UUID.randomUUID().toString().replace("-", "").toUpperCase()+".xlsx").build();
        for (int num = 0; num < tableList.size(); num++) {
            //创建sheet
            WriteSheet tempSheet = EasyExcel.writerSheet("表"+(num+1)).build();
            //然后这里获取数据   每一个table 元素
            List<List<TrCellInfo>> lists = takeApartElement(tableList.get(num));

            //这里就可以进行跨行跨列的处理
            //因为解析出来的都是单个单元格的数据
            //应该统计合并的单元格  然后进行统一合并

            //大致思路是：
            //找出最长的一个 list  la
            //以la为模板  与之不同长度的 进行OCR比对  确定同列
            //找出行最多的列 进行同行的判断

            //找到最长的 list  以及与之不同长度的list
            List<Integer> difList = getDifferent(lists);
            if(Objects.nonNull(difList)&&difList.size()>1){
                //进行排列
                //1.首先不一样 可能是标题行 跨列的 对称情况
                //先进性横向整理 进行列的统计
                //横向整理的话 ：如果中点在误差范围  就认为是一行 如果存在左右对称的空格 就进行左右延申

                //1.首先进行坐标的整理
                //找同页 相差最小的坐标

                //拿到模板   对于不同的行 依次进行比对
                //先以  最长的  行 为模板 进行整理 确定 同一列  还是  某两列的合并列
                //也就是说 需要设立  同列误差
                //统计 横向的合并空间 Map<row,[colStart][colEnd]>
                //row 始于 0  col 始于 0
                Map<Integer,Integer[]> xMergeArea = new HashMap<>();
                List<TrCellInfo> list = lists.get(difList.get(0));
                //得到坐标信息
                this.getStrsInfos(list,Boolean.FALSE);
                //从第二个元素 开始迭代  为 每一个 相异的列进行编码
                dif:for (int difNum = 1; difNum < difList.size(); difNum++) {
                    List<TrCellInfo> temList = lists.get(difList.get(difNum));
                    this.getStrsInfos(temList,Boolean.TRUE);
                    //对拿到的list 进行迭代  然后重新进行编码
                    tem:for (TrCellInfo cellInfo : temList) {
                        //找到和它同行  或者是某两行合并的
                        mou:for (int order = 0; order < list.size(); order++) {
                            //先找  单行  后找合并行
                            //单行  先 前重合 后重合  中重合
                            TrCellInfo info = list.get(order);
                            //大概一个字符的大小 取最大的那个
                            double fontWidth = Math.max((cellInfo.getStrInfo().getRowInfos().get(0).getWidth())
                                            / ((cellInfo.getStrInfo().getRowInfos().get(0).getStrText().length())),
                                    (info.getStrInfo().getRowInfos().get(0).getWidth())
                                            / (info.getStrInfo().getRowInfos().get(0).getStrText().length()));

                            //前重合
                            if(Math.abs(cellInfo.getStrInfo().getLeft() - info.getStrInfo().getLeft())/fontWidth>COL_ERROR[0]){
                                //发现重合  就  记录位置 跳出循环
                                info.setColOrder(Float.valueOf(order));
                                break mou;
                            }

                            if(Math.abs(cellInfo.getStrInfo().getLeft()+cellInfo.getStrInfo().getWidth()
                                    - info.getStrInfo().getLeft() - info.getStrInfo().getWidth())/fontWidth>COL_ERROR[1]){
                                //发现重合  就  记录位置 跳出循环
                                info.setColOrder(Float.valueOf(order));
                                break mou;
                            }

                            if(Math.abs(cellInfo.getStrInfo().getLeft() - info.getStrInfo().getLeft()
                                    +(cellInfo.getStrInfo().getWidth()-info.getStrInfo().getWidth())/2)/fontWidth>COL_ERROR[2]){
                                //发现重合  就  记录位置 跳出循环
                                info.setColOrder(Float.valueOf(order));
                                break mou;
                            }

                            //如果 前后中 都发现  无法同一行  就进行双列合并的 判断
                            if(Math.abs(cellInfo.getStrInfo().getLeft() - info.getStrInfo().getLeft()
                                    +(cellInfo.getStrInfo().getWidth()-info.getStrInfo().getWidth())/2)/fontWidth>IN_MERGE_COL){
                                //如果在范围内  就看下一个
                                if(order<list.size()-1){
                                    TrCellInfo nextCellInfo = list.get(order + 1);
                                    Double v = Math.abs(cellInfo.getStrInfo().getLeft()
                                            +(cellInfo.getStrInfo().getWidth()-info.getStrInfo().getLeft()-nextCellInfo.getStrInfo().getLeft())/2
                                            - (info.getStrInfo().getWidth() + nextCellInfo.getStrInfo().getWidth()) / 4) / fontWidth;
                                    if(v>COL_ERROR[2]){
                                        info.setColOrder(order+0.5F);
                                        break mou;
                                    }
                                }
                            }
                        }
                        //如果出现未匹配的  就直接返回原始的表格即可
                        if(Objects.isNull(cellInfo.getColOrder())){
                            log.error("表格处理异常！！！");
                            throw  new RuntimeException("表格处理异常");
                        }
                    }
                    //每遍历一行 就进行一个合并列统计
                    //xMergeArea
                }


                //这里进行 合并 行的整理

            }


            //最后写入
            build.write(lists,tempSheet);
        }
        build.finish();

        //这里要进行跨行跨列的标题处理

        return "";
    }


    //找到长度 最长的 list 以及 与之长度不同的list
    //第一个  元素为最长的 元素的下表 之后的元素为与之长度不同的元素的下标
    private static List<Integer> getDifferent(List<List<TrCellInfo>> list){
        List<Integer> result = new ArrayList<>();
        Integer maxLenthIndex = null;
        //首先找到最长的
        for (int num = 0; num < list.size(); num++) {
            if(Objects.isNull(maxLenthIndex)){
                maxLenthIndex = num;
            }else {
                maxLenthIndex = list.get(num).size()>list.get(maxLenthIndex).size()?num:maxLenthIndex;
            }
        }
        //然后进行 不同长度的统计
        if(Objects.isNull(maxLenthIndex)){
            return null;
        }
        result.add(maxLenthIndex);
        for (int order = 0; order < list.size(); order++) {
            if(list.get(order).size() != list.get(maxLenthIndex).size()){
                result.add(order);
            }
        }
        return result;
    }
    //递推函数
    private static void analysisList(List<Element> tables,Element element){
        Integer tableNum = StringUtils.countMatches(element.asXML(), "<Table>");
        if(element.getName().equals("Table")&&tableNum == 1){
            tables.add(element);
        }else if(tableNum >= 1) {
            //最后一种情况就是   含有多个<Table>标签  就需要进行分解 递归
            for (Element ele : element.elements()) {
                //递归调用
                analysisList(tables,ele);
            }
        }else{

        }
    }

    //将table 元素进行分解   td为最小单元  tr为一个list
    private static List<List<TrCellInfo>> takeApartElement(Element element){
        List<List<TrCellInfo>> result  = new ArrayList<>();
        if(StringUtils.equalsAny(element.getName(),PRIMARY_LABEL)){
            List<Element> trList = new ArrayList<>();
            //如果是Table标签的话  才可以进行  分解
            //因为上面已经筛选过，这个是最小的Table 单元 不需要担心表格的嵌套现象
            toTrTd(trList,element,SECONDARY_LABEL);
            //填充完后  就再进行遍历 进行内层的填充
            for (Element ele : trList) {
                List<TrCellInfo> trStrs = new ArrayList<>();
                List<Element> tdList = new ArrayList<>();
                //每个tr行的   td集合
                toTrTd(tdList,ele,TERTIARY_LABEL);
                //然后我们把每个td都进行转化
                for (int num = 0; num < tdList.size(); num++) {
                    List<String> strList = new ArrayList<>();
                    collectTd(strList,tdList.get(num));
                    //然后将 StringList 转为字符串
                    String str = StringUtils.join(strList, StringUtils.EMPTY);
                    trStrs.add(new TrCellInfo(str,Float.valueOf(num),null));
                }
                result.add(trStrs);
            }
        }
        return result;
    }

    //把TD元素  进行递推 把所有的  元素相加
    private static void collectTd(List<String> list,Element element){
        //结束条件
        //不含任何元素  就把该元素的值 添加到list中

        //递归条件
        //含有元素 一直递归下去
        List<Element> elementList = element.elements();
        if(elementList.size() == 0){
            list.add(element.getStringValue());
        }else{
            for (Element ele : elementList) {
                collectTd(list,ele);
            }
        }

    }




    /**
     * 通过递归  将table元素  分解为多个  tr元素  或者将 tr元素 分解成多个 td元素
     * @param list   结果容器
     * @param element   要分解的元素
     * @param eleNames   要分解成的元素名
     */
    private static void toTrTd(List<Element> list ,Element element,String[] eleNames){
        //结束条件
        //没有子元素了，并且该元素 也不是tr/td元素 不做任何操作
        //本元素是tr/td元素  就加入到list中

        //递归条件
        //本元素不是tr/td元素  但是还是含有子元素
        String elementName = element.getName();
        List<Element> elementList = element.elements();
        if(StringUtils.equalsAny(elementName,eleNames)){
            list.add(element);
        }else{
            if(elementList.size() != 0){
                for (Element ele : elementList) {
                    toTrTd(list,ele,eleNames);
                }
            }
        }
    }






    //形成 以时间戳字符串
    private String getDateStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return  sdf.format(new Date());
    }

    //RestTemplate  进行访问   获取返回值
    private static ResponseEntity<String> getInfo(RestTemplate restTemplate, PaddleOcr paddleOcr, File file){
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        return restTemplate.postForEntity(paddleOcr.getUrl(), files, String.class);

    }

    //并发 请求 paddle 进行 坐标的定位
    private CompletableFuture<List<CharPosInfo>> perPageData(int pageNUm, PDFRenderer renderer, ExecutorService executor, RestTemplate restTemplate, PaddleOcr paddleOcr) {
        CompletableFuture<List<CharPosInfo>> result = CompletableFuture.supplyAsync(() -> {
            List<CharPosInfo> list =  new ArrayList<>();
            try {
                BufferedImage image = renderer.renderImageWithDPI(pageNUm, 296);
                //然后把截取的图片 传入到 paddle 中 进行 识别
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", stream);
                byte[] bytes = stream.toByteArray();
                //bytes  转  file  用restTemplate  进行  访问获取返回值
                File file = bytesToFile(bytes);
                ResponseEntity<String> info = getInfo(restTemplate, paddleOcr, file);
                file.delete();
                //然后对 info 进行解析
                List<PaddleDTO> dtos = paddleData(info.getBody());
                list = getCharInfo(pageNUm, dtos);
            }catch (Exception e){}
            return list;
        }, executor).handle((outCome, exception) -> {
            if (Objects.nonNull(outCome)) {
                return outCome;
            }
            if (Objects.nonNull(exception)) {
                return new ArrayList<>();
            }
            return new ArrayList<>();
        });
        return result;
    }

    /**
     * 将 封装好的字符串信息 拆解为 单个字符的信息
     * @param page
     * @param list
     * @return
     */
    private List<CharPosInfo> getCharInfo(Integer page,List<PaddleDTO> list){
        List<CharPosInfo> resultList = new ArrayList<>();
        for (PaddleDTO dto : list) {
            //将字符串 拆解为单个字符   并进行信息的封装
            //首先算出  宽度 高度 字符长度
            Double strWidth  = Math.abs(dto.getTopLeft().getXAxis() - dto.getTopRight().getXAxis());
            Double strHeight = Math.abs(dto.getTopLeft().getYAxis() - dto.getDownLeft().getYAxis());
            Integer charSize = dto.getWords().length();
            //单个字符长度
            Double charWidth = strWidth/charSize;
            //然后我们对这个字符串 循环的进行录入
            for (Integer index = 0; index < charSize; index++) {
                //计算单个字符的 左上 宽 高 等信息
                Double left = dto.getTopLeft().getXAxis()+charWidth*index;
                Double top  = dto.getTopLeft().getYAxis();
                Double width = charWidth;
                Double height = strHeight;
                Double angle = dto.getReliability();
                String charStr = String.valueOf(dto.getWords().charAt(index));
                Integer pageNum = page;
                Integer infoIndex = resultList.size();
                resultList.add(new CharPosInfo(left,top,width,height,angle,charStr,pageNum,infoIndex));
            }
        }
        //循环完毕 返回即可
        return resultList;
    }

    //将paddleOcr返回值转换为list
    private  List<PaddleDTO> paddleData(String paddleStr){
        //遍历   被[]包裹的是坐标  依次是  左上、右上、右下、左下
        //被''  包裹的是 字符串
        //被,)  包裹的是  确信度
        //先通过分隔符)],  对字符串进行截取
        //这里要对  每一个字符串进行遍历    所以  比较消耗性能  考虑优化
        //由于info.getBody   是混合的  所以  需要进行截取
        List<PaddleDTO> result = new ArrayList<>();
        String[] strings = StringUtils.substringsBetween(paddleStr, "INFO: [", "]\\n\\n");
        for (String entStr : strings) {
            PaddleDTO dto = new PaddleDTO();
            //然后再截取  分隔符是  ]], (
            String[] axisWords = entStr.split("]], \\(");
            if(axisWords.length != 2){
                throw new RuntimeException("解析错误！！！");
            }
            //前后两段  分情况进行处理
            getAxies(dto,axisWords[0]);
            getWords(dto,axisWords[1]);
            result.add(dto);
        }
        return result;
    }

    //进行字符串前半段处理
    private static void getAxies(PaddleDTO dto,String str){
        String regx = "([0-9]{1,}[.][0-9]*)";
        Pattern pattern=Pattern.compile(regx);
        Matcher ma=pattern.matcher(str);
        Integer counter = 0;
        PaddleNode tempNode = null;
        while(ma.find()){
            if(counter++%2 == 0){
                tempNode = new PaddleNode();
                tempNode.setXAxis(Double.valueOf(ma.group()));
            }else{
                tempNode.setYAxis(Double.valueOf(ma.group()));
                switch (counter){
                    case 2:
                        dto.setTopLeft(tempNode);
                    case 4:
                        dto.setTopRight(tempNode);
                    case 6:
                        //顺时针旋转  所以先填充 右下
                        dto.setDownRight(tempNode);
                    case 8:
                        dto.setDownLeft(tempNode);
                }
                tempNode = null;
            }
        }
    }

    //进行字符串后半段处理
    private static void getWords(PaddleDTO dto,String str){
        //这个包括字符  和  识别准确度  以 "', "为分隔符
        String regx = "([0-9]{1,}[.][0-9]*)";
        Pattern pattern=Pattern.compile(regx);
        String[] strs = str.split("', ");
        if(strs.length!=2){
            throw new RuntimeException("文件解析错误！！！");
        }
        //前半段提取字符  后半段提取double  数值
        Matcher ma=pattern.matcher(strs[1]);
        dto.setWords(strs[0].substring(strs[0].indexOf("'")));
        while (ma.find()){
            dto.setReliability(Double.valueOf(ma.group()));
        }
    }


    //数据加载
    @Override
    protected void loadDatas(String filePath, RestTemplate restTemplate, PaddleOcr paddleOcr) throws Exception {
        PDDocument doc = PDDocument.load(new File(filePath));
        PDFRenderer renderer = new PDFRenderer(doc);
        Integer pageCount = doc.getNumberOfPages();
        List<CompletableFuture<List<CharPosInfo>>> paddleModel = new ArrayList<>();
        ExecutorService poolExecutor = ThreadPoolUtil.getThreadPoolExecutor();
        //并行的 进行 解析
        for (Integer num = 0; num < pageCount; num++) {
            paddleModel.add(perPageData(num,renderer,poolExecutor,restTemplate,paddleOcr));
        }
        CompletableFuture.allOf(paddleModel.toArray(new CompletableFuture[pageCount]));
        //然后进行统一处理
        fillCon(paddleModel);
    }

    //将数据填充到容器中
    private void fillCon(List<CompletableFuture<List<CharPosInfo>>> list) throws Exception {
        for (CompletableFuture<List<CharPosInfo>> future : list) {
           subjectList.add(future.get());
        }
    }

    //bytes转file
    private static File bytesToFile(byte[] bytes){
        String filePath = "RestDemo/src/main/resources/imgs";
        DateTimeFormatter formatStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formatDate = formatStr.format(LocalDateTime.now());

        String fileName  = formatDate+".jpg";
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File("RestDemo/src/main/resources/imgs");
            // 判断文件目录是否存在
            if (!dir.exists() ) {
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            //输出流
            fos = new FileOutputStream(file);
            //缓冲流
            bos = new BufferedOutputStream(fos);
            //将字节数组写出
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 找出 字符串对应的坐标
     * @param strs
     * @param clearColOrder  是否清空顺序
     */
    @Override
    protected void getStrsInfos(List<TrCellInfo> strs, Boolean clearColOrder) {
        //循环的 进入容器中进行寻找 然后选出最好的一组
        List<List<TrStrInfo>> list = new ArrayList<>();
        for (TrCellInfo str : strs) {
            list.add(getStrInfos(str.getVal()));
        }
        //首先找出他们都还有的公共的页数
        //如果这个都不能进行筛选完 因为一个list通常是一片连续的区域 所以  再次就是Y坐标 再次就是X坐标
        List<List<TrStrInfo>> template = new ArrayList<>();
        for (int num = 0; num < list.size(); num++) {
            //以第一行为模板进行 进行相似数据的统计
            if(num == 0){
                List<TrStrInfo> infos = list.get(num);
                //然后把他存入模板中
                for (TrStrInfo info : infos) {
                    List<TrStrInfo> temList = new ArrayList<>();
                    temList.add(info);
                    template.add(temList);
                }
            }else{
                //然后每到一页 就 进行详细信息的比对  最后取最长的那个即可
                List<TrStrInfo> infos = list.get(num);
                for (List<TrStrInfo> infoList : template) {
                    //拿到模板最近的一个值的 在该列 进行筛选
                    TrStrInfo strInfo = infoList.get(infoList.size() - 1);
                    //找和它同页的 Y坐标最近的一个值
                    Double yDValue = null;
                    TrStrInfo temElem = null;
                    for (TrStrInfo info : infos) {
                        if(Objects.equals(strInfo.getPage(),info.getPage())){
                            if(Objects.isNull(yDValue)){
                                temElem = info;
                                yDValue = Math.abs(info.getTop() - strInfo.getTop());
                            }else {
                                //如果不是 就取最小值
                                Double temVal = Math.abs(info.getTop() - strInfo.getTop());
                                if(temVal<yDValue){
                                    temElem = info;
                                }
                            }
                        }
                        //如果不是空 就在模板的末尾进行添加
                        if(Objects.nonNull(temElem)){
                            infoList.add(temElem);
                        }
                    }
                }

            }
        }
        //通过这个for循环  我们就把  模板 进行了填充 然后 取出最长的一个 list即可
        Integer listSizeMax = 0;
        List<TrStrInfo> resultList = new ArrayList<>();
        for (List<TrStrInfo> infos : template) {
            if (infos.size()>listSizeMax) {
                listSizeMax = infos.size();
                resultList = infos;
            }
        }
        //取出最长的list  且 该list的数目 必须 和传入的  长度一致
        if(strs.size() == resultList.size()){
            for (int num = 0; num < strs.size(); num++) {
                TrCellInfo cellInfo = strs.get(num);
                TrStrInfo strInfo = resultList.get(num);
                cellInfo.setStrInfo(strInfo);
                if(clearColOrder){
                    cellInfo.setColOrder(null);
                }
            }
        }

    }

    /**
     * 获取字符串 的坐标信息
     * @param str
     * @return
     */
    @Override
    protected List<TrStrInfo> getStrInfos(String str) {
        //将两端的 空格去掉即可
        String[] keys = str.trim().split("");
        List<CharPosInfo> filterList = new ArrayList<>();
        List<TrStrInfo> result = new ArrayList<>();
        //筛选出每页符合当前关键词首个字符的元素
        for (int i = 0; i < subjectList.size(); i++) {
            filterList.addAll(subjectList.get(i).stream().filter(word->word.getCharStr().equals(keys[0])).collect(Collectors.toList()));
        }

        for (CharPosInfo info : filterList) {
            List<CharPosInfo> temCharInfo  = new ArrayList<>();
            //如果关键字长度只有1，表示未分割就去匹配，如果匹配到了直接返回结果就行
            if  (keys.length == 1) {
                temCharInfo.add(info);
            } else {
                for (int i = 1; i < keys.length; i++) {
                    List<CharPosInfo> pageVo =  subjectList.get(info.getPageNum());
                    //如果第二个字符不是我们想要的 直接跳过
                    if (!keys[i].equals(pageVo.get(info.getIndex()+i).getCharStr())) {
                        break;
                    }
                    //表示最后一个字符都符合了，表示已经符合我们给出的关键字标准
                    if (i == keys.length -1) {
                        for (int j = 0; j < keys.length; j++) {
                            temCharInfo.add(pageVo.get(info.getIndex()+j));
                        }
                    }
                }
            }
            //在这里进行转化
            result.add(totalPos(temCharInfo));
        }
        return null;
    }

    /**
     * 将 零星字节的位置信息 拼接为 完成的 字符串位置信息
     * @param list
     * @return
     */
    @Override
    protected TrStrInfo totalPos(List<CharPosInfo> list) {
        //遍历 找到 最上最下 最左 最右 合并为 位置信息
        Double topMost = 0.0D;
        Double lowEst = 0.0D;
        Double leftMost = 0.0D;
        Double rightMost = 0.0D;
        StringBuilder str = new StringBuilder("");
        List<PerRowInfo> rowInfos = new ArrayList<>();
        for (CharPosInfo posInfo : list) {
            str.append(posInfo.getCharStr());
            //取最小值
            topMost = topMost>posInfo.getTop()?posInfo.getTop():topMost;
            //取最大值
            lowEst = lowEst<(posInfo.getTop()+posInfo.getHeight())?(posInfo.getTop()+posInfo.getHeight()):lowEst;
            //取最小值
            leftMost = leftMost>posInfo.getLeft()?posInfo.getLeft():leftMost;
            //取最大值
            rightMost = rightMost<(posInfo.getLeft() + posInfo.getWidth())?(posInfo.getTop()+posInfo.getHeight()):rightMost;
            //统计每层信息
            //paddle 一般都是 整行进行识别的 所以 Y坐标不需要误差
            if(CollectionUtils.isEmpty(rowInfos)){
                rowInfos.add(new PerRowInfo(posInfo.getLeft(),posInfo.getTop(),posInfo.getWidth(),posInfo.getHeight(),posInfo.getCharStr()));
            }else{
                //取出最近的一个 观察是否是同一行  是同一行就进行合并 不是同一行 就 新加
                PerRowInfo rowInfo = rowInfos.get(rowInfos.size() - 1);
                //比较Y坐标
                if(Objects.equals(rowInfo.getTop(),posInfo.getTop())){
                    //如果一致 就对width和text进行叠加
                    double width = rowInfo.getWidth() + posInfo.getWidth();
                    String text = rowInfo.getStrText()+posInfo.getCharStr();
                    rowInfo.setWidth(width);
                    rowInfo.setStrText(text);

                }else {
                    //如果不一致 就新建
                    rowInfos.add(new PerRowInfo(posInfo.getLeft(),posInfo.getTop(),posInfo.getWidth(),posInfo.getHeight(),posInfo.getCharStr()));
                }
            }
        }

        Double height = Math.abs(topMost - lowEst);
        Double width = Math.abs(rightMost - leftMost);
        Integer page = list.get(0).getPageNum();
        return new TrStrInfo(leftMost,topMost,width,height,str.toString(),page,rowInfos);
    }
}
