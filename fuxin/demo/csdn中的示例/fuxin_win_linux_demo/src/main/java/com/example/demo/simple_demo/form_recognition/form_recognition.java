//Copyright (C) 2003-2022, Foxit Software Inc..
//All Rights Reserved.
//
//http://www.foxitsoftware.com
//
//The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
//You cannot distribute any part of Foxit PDF SDK to any third party or general public,
//unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to recognize form in a PDF document.

import com.foxit.sdk.pdf.interform.Form;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.interform.Field;
import com.foxit.sdk.common.Constants;
import java.io.File;

public class form_recognition {

	private static void createResultFolder(String output_path) {
		File myPath = new File(output_path);
		if (!myPath.exists()) {
			myPath.mkdir();
		}
	}

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

	public static void main(String[] args) {
		String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
		String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
		String output_path = "../output_files/form_recognition/";
		String input_path = "../input_files/";
		createResultFolder(output_path);
		// Initialize library
		int error_code = Library.initialize(sn, key);
		if (error_code != Constants.e_ErrSuccess) {
			System.out.printf("Library Initialize Error: %d\n", error_code);
			return;
		}

		try {
			String input_file = input_path + "ElectricTable.pdf";
			PDFDoc doc = new PDFDoc(input_file);
			doc.load(null);
			Form form = new Form(doc);
			int count = form.getFieldCount("");
			System.out.printf("Field count: %d\n", count);
			System.out.printf("===Start Recognize Form===\n");
		    //Start to recognize form in current PDF document.
			doc.startRecognizeForm(null);
			System.out.printf("===End===\n");
			count = form.getFieldCount("");
			System.out.printf("Field count: %d\n", count);
			for (int i = 0; i < count; i++) {
			    Field field = form.getField(i, "");
			    String str = String.format("NO.%d",i+1);			    
			    field.setValue(str);
			}

			String newPdf = output_path + "ElectricTable_StartRecognizeForm.pdf";
			doc.saveAs(newPdf, PDFDoc.e_SaveFlagNoOriginal);
		} catch (PDFException e) {
			System.out.println(e.getMessage());
			return;
		}
		Library.release();
	}
}