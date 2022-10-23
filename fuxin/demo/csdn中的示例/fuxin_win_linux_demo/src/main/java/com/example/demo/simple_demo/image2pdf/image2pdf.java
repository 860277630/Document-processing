package com.example.demo.simple_demo.image2pdf;// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to convert images to PDF files.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class image2pdf {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/image2pdf/";
    private static String input_path = "../input_files/";

    // You can also use System.load("filename") instead. The filename argument must be an absolute path name.
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

    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            myPath.mkdir();
        }
    }


    static void image2PDF(String input_file, String output_file) throws PDFException {
        Image image = new Image(input_file);
        int count = 1;
        if (image.getType() != Image.e_JBIG2) {
            count = image.getFrameCount();
        }

        PDFDoc doc = new PDFDoc();
        for (int i = 0; i < count; i++) {
            PDFPage page = doc.insertPage(i, image.getWidth(), image.getHeight());
            page.startParse(e_ParsePageNormal, null, false);
            // Add image to page
            page.addImage(image, i, new PointF(0, 0), image.getWidth(), image.getHeight(), true);
        }

        doc.saveAs(output_file, e_SaveFlagNoOriginal);

    }

    public static void main(String[] args) throws PDFException {
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }


        try {
            createResultFolder(output_path);
            {
                // Convert .bmp file to PDF document.
                String input_file = input_path + "watermark.bmp";
                String output_file = output_path + "watermark_bmp.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert BMP file to PDF file.");
            }
            {
                // Convert .jpg file to PDF document.
                String input_file = input_path + "image_samples.jpg";
                String output_file = output_path + "image_samples_jpg.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert JPG file to PDF file.");
            }
            {
                // Convert .tif file to PDF document.
                String input_file = input_path + "TIF2Pages.tif";
                String output_file = output_path + "TIF2Pages_tif.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert TIF file to PDF file.");
            }
            {
                // Convert .gif file to PDF document.
                String input_file = input_path + "image005.gif";
                String output_file = output_path + "image005_gif.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert GIF file to PDF file.");
            }
            {
                // Convert .png file to PDF document.
                String input_file = input_path + "1.png";
                String output_file = output_path + "1_png.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert PNG file to PDF file.");
            }
            {
                // Convert .jb2 file to PDF document.
                String input_file = input_path + "STR_039.jb2";
                String output_file = output_path + "STR_039_jb2.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert JB2 file to PDF file.");
            }
            {
                // Convert .jp2 file to PDF document.
                String input_file = input_path + "1.jp2";
                String output_file = output_path + "1_jp2.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert JP2 file to PDF file.");
            }
            {
                // Convert .jpx file to PDF document.
                String input_file = input_path + "1.jpx";
                String output_file = output_path + "1_jpx.pdf";
                image2PDF(input_file, output_file);
                System.out.println("Convert JPX file to PDF file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
    }
}
