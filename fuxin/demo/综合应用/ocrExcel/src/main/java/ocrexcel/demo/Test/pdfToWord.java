package ocrexcel.demo.Test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @author: Serendipity
 * Date: 2022/3/17 10:58
 * Description:
 */
public class pdfToWord {
    public static void main(String[] args) {
        try {
            //输入pdf的路径
            String pdfFile = "D://PDF//pdf//1.pdf";
            //将pdf加载到对象中去
            PDDocument doc = PDDocument.load(new File(pdfFile));
            //得到pdf的页数
            int pagenumber = doc.getNumberOfPages();
            //设置转换后的名字
//            pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
//            String fileName = pdfFile + ".doc";
            String fileName="word.xml";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            //设置输出字符集为UTF-8 因此该word应该使用UTF-8格式打开 如果你出现乱码那么你可以自己修改一下这里的格式
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);// 排序
            stripper.setStartPage(1);// 设置转换的开始页
            stripper.setEndPage(pagenumber);// 设置转换的结束页
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
            System.out.println("pdf转换word成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

