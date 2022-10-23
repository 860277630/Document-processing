// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file is a demo to demonstrate how to use Foxit PDF SDK to do output preview.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.OutputPreview;

import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.foxit.sdk.common.Bitmap.e_DIBInvalid;
import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Bitmap.e_DIBRgb32;
import static com.foxit.sdk.common.Constants.e_Rotation0;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class output_preview {
	private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
	private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
	private static String output_path = "../output_files/output_preview/";
	private static String input_path = "../input_files/";
	private static String input_file = input_path + "page_organization_123.pdf";

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

		// "default_icc_folder_path" is the path of the folder which contains default icc profile files. Please refer to Developer Guide for more details.
		String default_icc_folder_path = "";
		if (default_icc_folder_path.length()<1) {
			System.out.println("default_icc_folder_path is still empty. Please set it with a valid path to the folder which contains default icc profile files.");
			return ;
		}
		// Set folder path which contains default icc profile files.
		Library.setDefaultICCProfilesPath(default_icc_folder_path);

		PDFDoc pdf_doc = new PDFDoc(input_file);
		pdf_doc.load(null);
		PDFPage pdf_page = pdf_doc.getPage(0);
		pdf_page.startParse(e_ParsePageNormal, null, false);
		float page_width = pdf_page.getWidth();
		float page_height = pdf_page.getHeight();
		int bitmap_width = (int)page_width;
		int bitmap_height = (int)page_height;
		int bitmap_format = e_DIBInvalid;
		long background_color = 0x000000;
		if (pdf_page.hasTransparency()) {
			background_color = 0x000000;
			bitmap_format = e_DIBArgb;
		} else {
			background_color = 0xFFFFFF;
			bitmap_format = e_DIBRgb32;
		}
		Matrix2D display_matrix = pdf_page.getDisplayMatrix(0, 0, bitmap_width, bitmap_height, e_Rotation0);

		Bitmap render_bitmap = new Bitmap(bitmap_width, bitmap_height, bitmap_format, null, 0);
		render_bitmap.fillRect(background_color, null);
		Renderer renderer = new Renderer(render_bitmap, false);

		OutputPreview output_preview = new OutputPreview(pdf_doc);
		String simulation_icc_file_path = input_path + "icc_profile/USWebCoatedSWOP.icc";
		output_preview.setSimulationProfile(simulation_icc_file_path);
		output_preview.setShowType(OutputPreview.e_ShowAll);
		ArrayList<String> process_plates = output_preview.getPlates(OutputPreview.e_ColorantTypeProcess);
		ArrayList<String> spot_plates = output_preview.getPlates(OutputPreview.e_ColorantTypeSpot);

		// Set check status of spot plate to be true, if there's any spot plate.
		for (int i = 0; i < (int)spot_plates.size(); i++) {
			output_preview.setCheckStatus(spot_plates.get(i), true);
		}

		// Only set one process plate to be checked each time and generate the preview bitmap.
		for (int i = 0; i < (int)process_plates.size(); i++) {
			if (0 != i)
				output_preview.setCheckStatus(process_plates.get(i - 1), false);
			output_preview.setCheckStatus(process_plates.get(i), true);

			Bitmap preview_bitmap = output_preview.generatePreviewBitmap(pdf_page, display_matrix, renderer);

			Image result_image = new Image();
			result_image.addFrame(preview_bitmap);
			String saved_file_path = output_path + "preview_result_ProcessPlate[" + i + "]_true.bmp";
			result_image.saveAs(saved_file_path);
		}
		System.out.println("[END] demo output_preview.");
		Library.release();
	}

	private static void createResultFolder(String output_path) {
		File myPath = new File(output_path);
		if (!myPath.exists()) {
			myPath.mkdir();
		}
	}
}
