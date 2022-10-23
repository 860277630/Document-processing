// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add, delete and modify 
// the header-footer of PDF file.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.PageNumberRange;
import com.foxit.sdk.pdf.HeaderFooter;
import com.foxit.sdk.pdf.HeaderFooterContent;
import com.foxit.sdk.pdf.HeaderFooterContentGenerator;
import com.foxit.sdk.common.fxcrt.RectF;

import java.io.*;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.pdf.HeaderFooterContentGenerator.e_DateFormatMSlashDSlashYYYY;

public class pdfheaderfooter {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfheaderfooter/";
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

    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);

        String input_file = input_path + "HeaderFooter.pdf";
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            // Load PDF document.
            PDFDoc pdfDoc = new PDFDoc(input_file);
            error_code = pdfDoc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("[Failed] Cannot load PDF document: %s.\r\nError Code: %d\r\n", input_file, error_code));
            }

            HeaderFooter headerfooter = pdfDoc.getEditableHeaderFooter();
            // Update header-footer
            HeaderFooterContentGenerator contentgenerator = new HeaderFooterContentGenerator();
            contentgenerator.addDate(e_DateFormatMSlashDSlashYYYY);
            contentgenerator.addString(" foxit");
            HeaderFooterContent content = headerfooter.getContent();
            content.setHeader_right_content(contentgenerator.generateContent());
            headerfooter.setContent(content);
            pdfDoc.updateHeaderFooter(headerfooter);
            String output_file = output_path + "HeaderFooter_UpdateHeaderFooter.pdf";
            pdfDoc.saveAs(output_file, e_SaveFlagNoOriginal);
            System.out.println("Update PDF document header-footer.");
            
            // Add header-footer
            headerfooter.setText_color(0x00FFFF);
            headerfooter.setPage_range(new PageNumberRange(1, pdfDoc.getPageCount(), com.foxit.sdk.common.Range.e_Odd));
            headerfooter.setHas_fixedsize_for_print(false);
            headerfooter.setHas_text_shrinked(false);
            headerfooter.setStart_page_number(2);
            headerfooter.setPage_margin(new RectF(36,70,36,70));
            headerfooter.setText_size(8);
            pdfDoc.addHeaderFooter(headerfooter);
            output_file = output_path + "HeaderFooter_AddHeaderFooter.pdf";
            pdfDoc.saveAs(output_file, e_SaveFlagNoOriginal);
            System.out.println("Add PDF document header-footer.");
            
            //Remove all header-footers
            pdfDoc.removeAllHeaderFooters();
            output_file = output_path + "HeaderFooter_RemoveAll.pdf";
            pdfDoc.saveAs(output_file, e_SaveFlagNoOriginal);
            System.out.println("Remove PDF document all header-footers.");
            
            System.out.println("pdfheaderfooter test.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
        return;
    }

}

