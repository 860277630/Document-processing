package com.example.demo.simple_demo.html2pdf;//Copyright (C) 2003-2022, Foxit Software Inc..
//All Rights Reserved.
//
//http://www.foxitsoftware.com
//
//The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
//You cannot distribute any part of Foxit PDF SDK to any third party or general public,
//unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
//This file contains an example to demonstrate how to use Foxit PDF SDK to convert from html to pdf.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.addon.conversion.HTML2PDFSettingData;

import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.util.Random;
import java.io.*;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class html2pdf {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";;
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";

    private static String output_directory = "../output_files/html2pdf/";
    private static int timeout = 15;
	
	private static String url_or_html = "../input_files/AboutFoxit.html";
    // "engine_path" is the path of the engine file "fxhtml2pdf" which is used to converting html to pdf. Please refer to Developer Guide for more details.
    private static String engine_path = "";
    // "cookies_path" is the path of the cookies file exported from the web pages that you want to convert. Please refer to Developer Guide for more details.
    private static String cookies_path = "";
    private static String output_pdf_path = output_directory + "html2pdf_result.pdf";

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
	
	public static void main(String[] args) throws PDFException {
    	int length=args.length;
    	createResultFolder(output_directory);
        // Initialize library       
        if (length > 0 && args[0].equals("--help"))
        {
        	System.out.println("Usage:");
        	System.out.println("html2pdf_xxx <-html <The url or html path>> <-o <output pdf path>> <-engine <htmltopdf engine path>>");
        	System.out.println(" [-w <page width>] [-h <page height>]  [-r <page rotate degree>] [-mode <page mode>] [-scale <whether scale page>]");
        	System.out.println(" [-ml <margin left>] [-mr <margin right>] [-mt <margin top>] [-mb <margin bottom>] [-link <whether convert link>]");
        	System.out.println(" [-tag <whether generate tag>] [-bookmarks <whether to generate bookmarks>] [-cookies <cookies file path>] [-timeout <timeout>]\r\n\r\n");
        	System.out.println("-html The url or html file path. for examples '-html www.foxitsoftware.com'");
        	System.out.println("-o The output pdf path." );
        	System.out.println("-engine The html to pdf engine path.");
        	System.out.println("-w The page width.");
        	System.out.println("-h The page height.");
        	System.out.println("-r The page roate degree. '0' means 0 degree, '1' means 90 degree, '2' means 180 degree, '3' means 270 degree.");
			System.out.println("-ml The page margin distance of left.");
            System.out.println("-mr The page margin distance of right.");
            System.out.println("-mt The page margin distance of top.");
            System.out.println("-mb The page margin distance of bottom.");
        	System.out.println("-mode The page mode. 0 means single page mode and 1 means multiple mode");
        	System.out.println("-scale The scaling mode. '0' means no scale, '1' means scale page, '2' means enlarge page.");
        	System.out.println("-link Whether to covert link. 'yes' means to convert link, 'no' means no need to covert.");
        	System.out.println("-tag Whether to generate tag. 'yes' means to generate tag, 'no' means no need to generate.");
        	System.out.println("-bookmarks Whether to generate bookmarks.'yes' means generate bookmarks, 'no' means no need to generate.");
            System.out.println("-encoding The HTML encoding format. '0' means auto encoding and '1'-'73' means other encodings");
            System.out.println("-render_images Whether to render images or not.'yes' means to render images, 'no' means no need to render.");
            System.out.println("-remove_underline_for_link Whether to remove underline for link. 'yes' means to remove underline for link, 'no' means no need to remove.");
            System.out.println("-headerfooter Whether to generate headerfooter. 'yes' means to generate headerfooter, 'no' means no need to generate.");
            System.out.println("-headerfooter_title The headerfooter title.");
            System.out.println("-headerfooter_url The headerfooter url.");
            System.out.println("-bookmark_root_name The bookmark root name.");
            System.out.println("-resize_objects Whether to enable the Javascripts related resizing of the objects during rendering process. 'yes' means to enable, 'no' means to disable.");
            System.out.println("-print_background Whether to print background. 'yes' means to print background, 'no' means no need to print background.");
            System.out.println("-optimize_tag Whether to optimize tag tree. 'yes' means to optimize tag tree, 'no' means no need to optimize tag tree.");
            System.out.println("-media The media style. '0' means screen media style, '1' means print media style.");
        	System.out.println("-cookies The cookies file path.");
        	System.out.println("-timeout The timeout of loading webpages.");
        	return;
        }
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        try {
        	com.foxit.sdk.addon.conversion.HTML2PDFSettingData pdf_setting_data=new com.foxit.sdk.addon.conversion.HTML2PDFSettingData();	
        	pdf_setting_data.setIs_convert_link(true);
        	pdf_setting_data.setIs_generate_tag(true); 
        	pdf_setting_data.setTo_generate_bookmarks(true); 
        	pdf_setting_data.setRotate_degrees(0);    
        	pdf_setting_data.setPage_width(900);
        	pdf_setting_data.setPage_height(640);	
        	pdf_setting_data.setPage_mode(HTML2PDFSettingData.e_PageModeSinglePage); 
            pdf_setting_data.setScaling_mode(HTML2PDFSettingData.e_ScalingModeScale);
            pdf_setting_data.setTo_print_background(true);
            pdf_setting_data.setTo_optimize_tag_tree(false);
            pdf_setting_data.setMedia_style(HTML2PDFSettingData.e_MediaStyleScreen);
    		
			if (!AnalysisParameter(length, args, pdf_setting_data)) return;
    		com.foxit.sdk.addon.conversion.Convert.fromHTML(url_or_html, engine_path, cookies_path, pdf_setting_data, output_pdf_path, timeout);
            System.out.println("Convert HTML to PDF successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Library.release();
    }

    static boolean AnalysisParameter(int length, String[] args, HTML2PDFSettingData pdf_setting_data)
    {
    	for (int i = 0; i < length; i = i + 2) {
    		String args_key = args[i];
    		String args_value;
    		if (length <= i + 1) {
			  System.out.println("Please make sure the key " + args_key + " is valid and it has value!\n");
			  System.out.println("Usage: html2pdf_xxx <-html <The url or html path>> <-o <output pdf path>> ...\nPlease try 'html2pdf_xxx --help' for more information.\n");
			  return false;
			};
    		args_value = (args[i + 1]);
    		if (args_key.equals("-w")) pdf_setting_data.setPage_width(Float.parseFloat(args_value)) ;
    		else if (args_key.equals("-h")) pdf_setting_data.setPage_height(Float.parseFloat(args_value));
    		else if (args_key.equals("-r")) pdf_setting_data.setRotate_degrees(Integer.parseInt(args_value));
    		else if (args_key.equals("-ml")) {
				pdf_setting_data.getPage_margin().setLeft(Float.parseFloat(args_value));
			}
    		else if (args_key.equals("-mr")) {
				pdf_setting_data.getPage_margin().setRight(Float.parseFloat(args_value));
			}
    		else if (args_key.equals("-mt")) {
				pdf_setting_data.getPage_margin().setTop(Float.parseFloat(args_value));
			}
    		else if (args_key.equals("-mb")) {
				pdf_setting_data.getPage_margin().setBottom(Float.parseFloat(args_value));
			}
    		else if (args_key.equals("-scale")) pdf_setting_data.setScaling_mode(Integer.parseInt(args_value));
    		else if (args_key.equals("-link")) pdf_setting_data.setIs_convert_link (args_value.equalsIgnoreCase("yes") ? true : false);
    		else if (args_key.equals("-tag")) pdf_setting_data.setIs_generate_tag(args_value.equalsIgnoreCase("yes") ? true : false);
    		else if (args_key.equals("-bookmarks")) pdf_setting_data.setTo_generate_bookmarks(args_value.equalsIgnoreCase("yes") ? true : false);
    		else if (args_key.equals("-mode")) pdf_setting_data.setPage_mode(Integer.parseInt(args_value));
            else if (args_key.equals("-encoding")) pdf_setting_data.setEncoding_format(Integer.parseInt(args_value));
            else if (args_key.equals("-render_images")) pdf_setting_data.setTo_render_images(args_value.equalsIgnoreCase("yes") ? true : false);
            else if (args_key.equals("-remove_underline_for_link")) pdf_setting_data.setTo_remove_underline_for_link(args_value.equalsIgnoreCase("yes") ? true : false);
            else if (args_key.equals("-headerfooter")) pdf_setting_data.setTo_set_headerfooter(args_value.equalsIgnoreCase("yes") ? true : false);
            else if (args_key.equals("-headerfooter_title")) pdf_setting_data.setHeaderfooter_title(args_value);
            else if (args_key.equals("-headerfooter_url")) pdf_setting_data.setHeaderfooter_url(args_value);
            else if (args_key.equals("-bookmark_root_name")) pdf_setting_data.setBookmark_root_name(args_value);
            else if (args_key.equals("-resize_objects")) pdf_setting_data.setTo_resize_objects(args_value.equalsIgnoreCase("yes") ? true : false);
		    else if (args_key.equals("-print_background")) pdf_setting_data.setTo_print_background(args_value.equalsIgnoreCase("yes") ? true : false);
		    else if (args_key.equals("-optimize_tag")) pdf_setting_data.setTo_print_background(args_value.equalsIgnoreCase("yes") ? true : false);
		    else if (args_key.equals("-media")) pdf_setting_data.setMedia_style(Integer.parseInt(args_value));
    		else if (args_key.equals("-engine")) engine_path = args_value;
    		else if (args_key.equals("-cookies")) cookies_path = args_value;
    		else if (args_key.equals("-o")) output_pdf_path = args_value;
    		else if (args_key.equals("-html")) url_or_html = args_value;
    		else if (args_key.equals("-timeout")) timeout = Integer.parseInt(args_value);
			else {
				System.out.println("Please make sure the key " + args_key + " is valid and it has value!\n");
				System.out.println("Usage: html2pdf_xxx <-html <The url or html path>> <-o <output pdf path>> ...\nPlease try 'html2pdf_xxx --help' for more information.\n");
				return false;
			}
    	} 	
		return true;
    }
}
