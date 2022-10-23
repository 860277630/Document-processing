// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to redact some content in a PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.RectFArray;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;
import com.foxit.sdk.pdf.TextSearch;
import com.foxit.sdk.addon.Redaction;
import com.foxit.sdk.pdf.annots.Redact;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class pdfredact {


    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfredact/";
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

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }
            Redaction redaction = new Redaction(doc);
            PDFPage page = doc.getPage(0);
            // Parse PDF page.
            page.startParse(PDFPage.e_ParsePageNormal,  null,  false);
            TextPage text_page = new TextPage(page, TextPage.e_ParseTextNormal);
            TextSearch text_search = new TextSearch(text_page);
            text_search.setPattern("PDF");
            RectFArray matched_rect_array = new RectFArray();
            while (text_search.findNext()) {
                RectFArray temp_rect_array = text_search.getMatchRects();
                for (int z=0; z<temp_rect_array.getSize(); z++)
                    matched_rect_array.add(temp_rect_array.getAt(z));
            }
            if (matched_rect_array.getSize()>0) {
                Redact redact = redaction.markRedactAnnot(page, matched_rect_array);
                redact.resetAppearanceStream();
                doc.saveAs(output_path + "AboutFoxit_redected_default.pdf", PDFDoc.e_SaveFlagNormal);

                // Set border color to green.
                redact.setBorderColor((long)0x00FF00);
                // Set fill color to blue.
                redact.setFillColor((long)0x0000FF);
                // Set rollover fill color to red.
                redact.setApplyFillColor((long)0xFF0000);
                redact.resetAppearanceStream();
                doc.saveAs(output_path + "AboutFoxit_redected_setColor.pdf", PDFDoc.e_SaveFlagNormal);

                redact.setOpacity((float)0.5);
                redact.resetAppearanceStream();
                doc.saveAs(output_path+"AboutFoxit_redected_setOpacity.pdf", PDFDoc.e_SaveFlagNormal);

                redaction.apply();
            }
            doc.saveAs(output_path + "AboutFoxit_redected_apply.pdf", PDFDoc.e_SaveFlagNormal);
            System.out.println("Redact test.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Library.release();
    }
}
