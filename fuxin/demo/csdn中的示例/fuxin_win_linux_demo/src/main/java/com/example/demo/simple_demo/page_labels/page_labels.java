// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to get/set page label information
// in a PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PageLabels;

import java.io.File;
import java.io.FileWriter;

import static com.foxit.sdk.pdf.PageLabels.*;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class page_labels {

	private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
	private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
	private static String output_path = "../output_files/page_labels/";
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

	private static String StyleCodeToString(int style) {
		switch (style) {
		case e_None: {
			return "None";
		}
		case e_DecimalNums: {
			return "DecimalNums";
		}
		case e_UpperRomanNums: {
			return "UpperRomanNums";
		}
		case e_LowerRomanNums: {
			return "LowerRomanNums";
		}
		case e_UpperLetters: {
			return "UpperLetters";
		}
		case e_LowerLetters: {
			return "LowerLetters";
		}
		}
		return "";
	}

	private static void ShowPageLabelsInfo(PDFDoc doc, String text_doc_path) {
		try {
			FileWriter writer = new FileWriter(text_doc_path, false);
			PageLabels page_labels = new PageLabels(doc);
			int count = doc.getPageCount();
			for (int i = 0; i < count; i++) {
				writer.write("page index: " + i + "\tstyle: " + StyleCodeToString(page_labels.getPageLabelStyle(i))
						+ "\ttitle: " + page_labels.getPageLabelTitle(i) + "\tprefix: "
						+ page_labels.getPageLabelPrefix(i) + "\r\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws PDFException {
		createResultFolder(output_path);
		// Initialize library
		int error_code = Library.initialize(sn, key);
		if (error_code != e_ErrSuccess) {
			System.out.println("Library Initialize Error: " + error_code);
			return;
		}
		String input_file = input_path + "page_labels.pdf";
		try {
			PDFDoc doc = new PDFDoc(input_file);
			error_code = doc.load(null);
			if (error_code != e_ErrSuccess) {
				System.out.println("The Doc " + input_file + " Error: " + error_code);
				return;
			}
			String text_path = output_path + "page_labels_original_page_labels_info.txt";
			ShowPageLabelsInfo(doc, text_path);

			// Add new page labels
			PageLabels page_labels = new PageLabels(doc);
			page_labels.removeAll();
			String pdf_removeall = output_path + "removeall_page_labels.pdf";
			doc.saveAs(pdf_removeall, e_SaveFlagNoOriginal);

			int count = doc.getPageCount();
			if (count > 0) {
				page_labels.setPageLabel(0, e_UpperRomanNums, 1, "Cover-");
			}
			if (count > 1) {
				page_labels.setPageLabel(1, e_LowerRomanNums, 1, "Catolog-");
			}
			if (count > 2) {
				page_labels.setPageLabel(2, e_DecimalNums, 1, "Contents-");
			}
			if (count > 5) {
				page_labels.setPageLabel(count - 1, e_UpperLetters, 1, "Appendix-");
			}

			String text_path_new = output_path + "page_labels_new_page_labels_info.txt";
			ShowPageLabelsInfo(doc, text_path_new);
			String pdf_new = output_path + "new_page_labels.pdf";
			doc.saveAs(pdf_new, e_SaveFlagNoOriginal);

			System.out.println("Page label test.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Library.release();
		return;
	}
}
