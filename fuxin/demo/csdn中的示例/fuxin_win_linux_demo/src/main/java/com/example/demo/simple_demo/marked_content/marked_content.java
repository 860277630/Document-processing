// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to mark PDF content and
// get marked content from graphics objects.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.graphics.GraphicsObject;
import com.foxit.sdk.pdf.graphics.MarkedContent;
import com.foxit.sdk.pdf.graphics.TextObject;
import com.foxit.sdk.pdf.graphics.TextState;
import com.foxit.sdk.pdf.objects.PDFDictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Font.e_StdIDTimesB;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeText;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFill;

public class marked_content {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/marked_content/";

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

    static void CreateMarkedContent(PDFPage page) throws PDFException {
        long position = page.getLastGraphicsObjectPosition(e_TypeText);
        TextObject text_object = TextObject.create();
        text_object.setFillColor(0xFF83AF9B);

        // Prepare text state
        TextState state = new TextState();
        state.setFont_size(72.f);
        state.setFont(new Font(e_StdIDTimesB));
        state.setTextmode(e_ModeFill);

        text_object.setTextState(page, state, false, 750);

        text_object.setText("Marked-Content");
        page.insertGraphicsObject(position, text_object);

        RectF rect = text_object.getRect();
        float offset_x = (page.getWidth() - (rect.getRight() - rect.getLeft())) / 2;
        float offset_y = page.getHeight() * 0.618f - (rect.getTop() - rect.getBottom()) / 2;
        text_object.transform(new Matrix2D(1, 0, 0, 1, offset_x, offset_y), false);

        MarkedContent content = text_object.getMarkedContent();
        PDFDictionary span_dict = PDFDictionary.create();
        span_dict.setAtInteger("Direct", 55);
        content.addItem("Span", span_dict);

        // Generate content
        page.generateContent();
    }

    static void GetMarkedContentObject(String pdf_file) throws PDFException, IOException {
        PDFDoc doc = new PDFDoc(pdf_file);
        int ret = doc.load(null);
        if (ret != e_ErrSuccess) {
            System.out.println(String.format("[Failed] Cannot load PDF document %s.\r\n Error message:%d\r\n",
                    pdf_file, ret));
            return;
        }
        PDFPage page = doc.getPage(0);
        page.startParse(e_ParsePageNormal, null, false);
        long position = page.getFirstGraphicsObjectPosition(e_TypeText);
        GraphicsObject text_obj = page.getGraphicsObject(position);
        MarkedContent content = text_obj.getMarkedContent();

        int nCount = content.getItemCount();
        String file_info = output_path + "markedcontent.txt";
        FileWriter text_doc = new FileWriter(file_info, false);
        // Get marked content property
        for (int i = 0; i < nCount; i++) {
            text_doc.write(String.format("index: %d\r\n", i));
            String tag_name = content.getItemTagName(i);
            text_doc.write(String.format("Tag name: %s\r\n", tag_name));
            int mcid = content.getItemMCID(i);
            text_doc.write(String.format("mcid: %d\r\n", mcid));
        }
        text_doc.close();
    }

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            PDFDoc doc = new PDFDoc();
            PDFPage page = doc.insertPage(0, PDFPage.e_SizeLetter);
            CreateMarkedContent(page);

            String output_file = output_path + "marked-content.pdf";
            doc.saveAs(output_file, e_SaveFlagNormal);

            GetMarkedContentObject(output_file);
            System.out.println("Add marked content to PDF file.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
    }
}
