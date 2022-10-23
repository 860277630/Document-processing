package ocrexcel.demo.Test.cn.isuyu.demo.keyword.port;



import ocrexcel.demo.Test.cn.isuyu.demo.keyword.port.utils.PdfHelper;
import ocrexcel.demo.Test.vos.KeyVO;

import java.io.IOException;
import java.util.List;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 上午11:51
 */

public class Main {
    public static void main(String[] args) throws IOException {
        //每次读取的单个字符
        //PdfHelper.getWordPort("甲方签字","./data/test.pdf");
        //这个文档读取的是多个字符
        List<KeyVO> wordPort = PdfHelper.getWordPort("2021", "D:/PDF/pdf/1.pdf");
        for (KeyVO vo : wordPort) {
            System.out.println("*****"+vo.toString());
        }
    }
}
