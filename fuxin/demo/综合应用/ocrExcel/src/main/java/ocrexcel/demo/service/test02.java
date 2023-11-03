package ocrexcel.demo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.foxit.sdk.addon.conversion.Convert;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import lombok.extern.slf4j.Slf4j;
import ocrexcel.demo.Test.cn.isuyu.demo.keyword.port.utils.CustomerRenderListener;
import ocrexcel.demo.Test.vos.WordVO;
import ocrexcel.demo.fuxin.enums.MergeEnum;
import ocrexcel.demo.fuxin.moddle.CellInfo;
import ocrexcel.demo.fuxin.moddle.StrInfo;
import ocrexcel.demo.fuxin.utils.PdfStrAxisUtil;
import org.apache.commons.collections.CollectionUtils;
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
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.TextPage.e_ParseTextNormal;

@Slf4j
public class test02 extends PdfStrAxisUtil {


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
            PDFDoc doc = new PDFDoc(file_path);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc  Error: " + error_code);
                return;
            }
            int pageCount = doc.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PDFPage page = doc.getPage(i);
                TextPage textpage = new TextPage(page, TextPage.e_ParseTextNormal);
                int nCharCount = textpage.getCharCount();
                String texts = textpage.getChars(0, nCharCount);
                System.out.println("=========="+texts);
            }
            //转换完后 迅速把时间改回来
            changeLocalTime(foreDate);

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


    @Override
    protected void loadDatas(String filePath) throws IOException {

    }

    @Override
    protected void getStrsInfos(List<CellInfo> strs, Boolean clearColOrder, MergeEnum isXaxis) {

    }

    @Override
    protected List<StrInfo> getStrInfos(String str) {
        return null;
    }

    @Override
    protected StrInfo totalPos(List<WordVO> list) {
        return null;
    }
}
