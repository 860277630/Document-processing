package com.example.demo.test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFTextDetection {
    public static void main(String[] args) {
        try {
            // 读取PDF文件
            PDDocument document = PDDocument.load(new File("D:/1.pdf"));
            // 创建PDFTextStripper对象
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            int pages = document.getNumberOfPages();
            for (int page = 0; page <=pages; page++) {
                pdfTextStripper.setStartPage(page);
                pdfTextStripper.setEndPage(page);
                String text = pdfTextStripper.getText(document);
                System.out.println("========================"+page+"==================================");
                System.out.println("获取文本内容: " + text.replace("/n",""));
                System.out.println("========================"+page+"==================================");
            }
            // 关闭PDF文档
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

