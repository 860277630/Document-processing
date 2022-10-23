package com.example.demo.simple_demo.pdf2image;// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file is a demo demonstrate how to convert a PDF file to one or multiple image files.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;

import java.io.*;
import java.io.File;
import java.io.FileWriter;

import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class pdf2image {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdf2image/";
    private static String input_path = "../input_files/";
    private static String input_file = input_path + "AboutFoxit.pdf";
    private static String[] support_image_extends = {".bmp", ".jpg", ".jpeg", ".png", ".jpx", ".jp2"};
    private static String[] support_multi_image = {".tif", ".tiff"};

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

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }
        {
			PDFDoc doc = new PDFDoc(input_file);
			error_code = doc.load(null);
			if (error_code != e_ErrSuccess) {
				System.out.println("The Doc " + input_file + " Error: " + error_code);
				return;
			}
			PDF2Image(doc, false);
		}
		{
			File file = new File(input_file);  
			Long filelength = file.length();  
			byte[] filecontent = new byte[filelength.intValue()];  
			try {  
				FileInputStream in = new FileInputStream(file);  
				in.read(filecontent);  
				in.close();  
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} catch (IOException e) {  
				e.printStackTrace();  
			} 
			PDFDoc doc = new PDFDoc(filecontent);
			error_code = doc.load(null);
			if (error_code != e_ErrSuccess) {
				System.out.println("The Doc " + input_file + " Error: " + error_code);
				return;
			}
			PDF2Image(doc, true);
		}
        Library.release();
    }

	private static void PDF2Image(PDFDoc doc, boolean from_memory) throws PDFException {
        Image image = new Image();
        // Get page count
        int nPageCount = doc.getPageCount();
        for (int i = 0; i < nPageCount; i++) {
            PDFPage page = doc.getPage(i);
            // Parse page.
            page.startParse(e_ParsePageNormal, null, false);

            int width = (int) page.getWidth();
            int height = (int) page.getHeight();
            Matrix2D matrix = page.getDisplayMatrix(0, 0, width, height, page.getRotation());

            // Prepare a bitmap for rendering.
            Bitmap bitmap = new Bitmap(width, height, e_DIBArgb, null, 0);
            bitmap.fillRect(0xFFFFFFFF, null);

            // Render page
            Renderer render = new Renderer(bitmap, false);
            render.startRender(page, matrix, null);
            image.addFrame(bitmap);
            for (int j = 0; j < support_image_extends.length; j++) {
                String extend = support_image_extends[j];
                Save2Image(bitmap, i, extend, from_memory);
            }
        }
        for (int j = 0; j < support_multi_image.length; j++) {
            String extend = support_multi_image[j];
            Save2Image(image, extend, from_memory);
        }
	}
	
    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            myPath.mkdir();
        }
    }

    private static void Save2Image(Bitmap bitmap, int page_index, String sExt, boolean from_memory) throws PDFException {
        // Add the bitmap to image and save the image.
        Image image = new Image();
        image.addFrame(bitmap);
        String s = "AboutFoxit_" + page_index + (from_memory ? "_from_memory":"");
        s = output_path + s + sExt;
        image.saveAs(s);

        System.out.println("Save page " + page_index + " into a picture of " + sExt + " format.");
    }

    private static void Save2Image(Image image, String sExt, boolean from_memory) throws PDFException {
        String s = "AboutFoxit" + (from_memory ? "_from_memory":"");
        s = output_path + s + sExt;
        image.saveAs(s);

        System.out.println("Save pdf file into a picture of " + sExt + " format.");
    }
}
