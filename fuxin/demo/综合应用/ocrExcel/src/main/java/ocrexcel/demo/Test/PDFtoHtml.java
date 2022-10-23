package ocrexcel.demo.Test;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class PDFtoHtml {
    public static void main(String[] args) {


        //加载PDF测试文档


        PdfDocument pdf = new PdfDocument();


        pdf.loadFromFile("D:/PDF/pdf/1.pdf");




        //保存为html格式的文件

        pdf.saveToFile("PDFtoHtml.xlsx", FileFormat.XLSX);


        pdf.dispose();


    }
}
