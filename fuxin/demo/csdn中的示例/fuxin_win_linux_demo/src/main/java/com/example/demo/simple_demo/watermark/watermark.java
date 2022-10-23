// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add watermarks (in different types)
// into PDF files.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.*;
import com.foxit.sdk.pdf.Watermark;
import com.foxit.sdk.pdf.WatermarkSettings;
import com.foxit.sdk.pdf.WatermarkTextProperties;

import java.io.File;

import com.foxit.sdk.common.Constants;

import static com.foxit.sdk.common.Constants.e_AlignmentCenter;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Font.e_StdIDTimesB;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.WatermarkSettings.*;
import static com.foxit.sdk.pdf.WatermarkTextProperties.e_FontStyleNormal;


public class watermark {


    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/watermark/";
    private static String input_path = "../input_files/";
    private static String input_file = input_path + "AboutFoxit.pdf";
    private static String output_file = output_path + "watermark_add.pdf";
    private static String output_remove_file = output_path + "watermark_remove.pdf";

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

    static void addTextWatermark(PDFDoc doc, PDFPage page) throws PDFException {
        WatermarkSettings settings = new WatermarkSettings();
        settings.setFlags(e_FlagASPageContents | e_FlagOnTop);
        settings.setOffset_x(0);
        settings.setOffset_y(0);
        settings.setOpacity(90);
        settings.setPosition(Constants.e_PosTopRight);
        settings.setRotation(-45.f);
        settings.setScale_x(1.f);
        settings.setScale_y(1.f);

        WatermarkTextProperties text_properties = new WatermarkTextProperties();
        text_properties.setAlignment(e_AlignmentCenter);
        text_properties.setColor(0xF68C21);
        text_properties.setFont_size(e_FontStyleNormal);
        text_properties.setLine_space(1);
        text_properties.setFont_size(12.f);
        text_properties.setFont(new Font(e_StdIDTimesB));

        Watermark watermark = new Watermark(doc, "Foxit PDF SDK\nwww.foxitsoftware.com", text_properties, settings);
        watermark.insertToPage(page);

    }

    static void addBitmapWatermark(PDFDoc doc, PDFPage page, String bitmap_file) throws PDFException {
        WatermarkSettings settings = new WatermarkSettings();
        settings.setFlags(e_FlagASPageContents | e_FlagOnTop);
        settings.setOffset_y(0.f);
        settings.setOffset_y(0.f);
        settings.setOpacity(60);
        settings.setPosition(Constants.e_PosCenterLeft);
        settings.setRotation(90.f);

        Image image = new Image(bitmap_file);
        Bitmap bitmap = image.getFrameBitmap(0);
        settings.setScale_x(page.getHeight() * 1.0f / bitmap.getWidth());
        settings.setScale_y(settings.getScale_x());
        Watermark watermark = new Watermark(doc, bitmap, settings);
        watermark.insertToPage(page);
    }


    static void addImageWatermark(PDFDoc doc, PDFPage page, String image_file) throws PDFException {
        WatermarkSettings settings = new WatermarkSettings();
        settings.setFlags(e_FlagASPageContents | e_FlagOnTop);
        settings.setOffset_x(0.f);
        settings.setOffset_y(0.f);
        settings.setOpacity(20);
        settings.setPosition(Constants.e_PosCenter);
        settings.setRotation(0.0f);

        Image image = new Image(image_file);
        Bitmap bitmap = image.getFrameBitmap(0);
        settings.setScale_x(page.getWidth() * 0.618f / bitmap.getWidth());
        settings.setScale_y(settings.getScale_x());

        Watermark watermark = new Watermark(doc, image, 0, settings);
        watermark.insertToPage(page);
    }

    static void addSingleWatermark(PDFDoc doc, PDFPage page) throws PDFException {
        WatermarkSettings settings = new WatermarkSettings();
        settings.setFlags(e_FlagASPageContents | e_FlagOnTop);
        settings.setOffset_x(0.f);
        settings.setOffset_y(0.f);
        settings.setOpacity(90);
        settings.setPosition(Constants.e_PosBottomRight);
        settings.setRotation(0.0f);
        settings.setScale_x(0.1f);
        settings.setScale_y(0.1f);

        Watermark watermark = new Watermark(doc, page, settings);
        watermark.insertToPage(page);
    }


    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }



        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);

            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }
            int nCount = doc.getPageCount();
            for (int i = 0; i < nCount; i++) {
                PDFPage page = doc.getPage(i);
                page.startParse(e_ParsePageNormal, null, false);

                addTextWatermark(doc, page);
                String wm_bmp = input_path + "watermark.bmp";
                addBitmapWatermark(doc, page, wm_bmp);
                String wm_image = input_path + "sdk.png";
                addImageWatermark(doc, page, wm_image);
                addSingleWatermark(doc, page);
            }

            doc.saveAs(output_file, e_SaveFlagNoOriginal);
            System.out.println("Add watermarks to PDF file.");

            nCount = doc.getPageCount();
            for (int i = 0; i < nCount; i++)
            {
                PDFPage page = doc.getPage(i);
                page.startParse(e_ParsePageNormal, null, false);
                if (page.hasWatermark()) {
                    page.removeAllWatermarks();
                }
            }

            doc.saveAs(output_remove_file, e_SaveFlagNoOriginal);
            System.out.println("Remove watermarks from PDF file.");

        } catch (Exception e) {
            e.printStackTrace();

        }
        Library.release();
        return;
    }
}
