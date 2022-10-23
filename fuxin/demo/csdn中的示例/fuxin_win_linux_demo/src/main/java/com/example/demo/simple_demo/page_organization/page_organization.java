// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to do PDF page organization,
// such as inserting, removing, and so on.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.common.Range;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.graphics.TextObject;
import com.foxit.sdk.pdf.graphics.TextState;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Font.e_StdIDTimes;
import static com.foxit.sdk.common.Progressive.e_Finished;
import static com.foxit.sdk.pdf.PDFDoc.e_ImportFlagNormal;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFDoc.e_ExtractPagesOptionAnnotation;
import static com.foxit.sdk.pdf.PDFDoc.e_InsertDocOptionAttachments;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeText;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFillStrokeClip;

public class page_organization {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/page_organization/";
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

    static void AddTextObjects(PDFPage page, String content) throws PDFException {
        long position = page.getLastGraphicsObjectPosition(e_TypeText);
        TextObject text_object = TextObject.create();

        text_object.setFillColor(0xFFAAAAAA);
        text_object.setStrokeColor(0xFFF68C21);

        // Prepare text state
        TextState state = new TextState();
        state.setFont_size(64.0f);
        state.setFont(new Font(e_StdIDTimes));
        state.setTextmode(e_ModeFillStrokeClip);

        text_object.setTextState(page, state, false, 750);

        // Set content
        text_object.setText(content);
        page.insertGraphicsObject(position, text_object);

        // Transform to center
        RectF rect = text_object.getRect();
        float offset_x = (page.getWidth() - (rect.getRight() - rect.getLeft())) / 2;
        float offset_y = (page.getHeight() - (rect.getTop() - rect.getBottom())) / 2;
        text_object.transform(new Matrix2D(1, 0, 0, 1, offset_x, offset_y), false);

        // Generator content
        page.generateContent();
    }

    static void InsertPage() throws PDFException {
        String input_file = input_path + "page_organization_123.pdf";
        PDFDoc doc = new PDFDoc(input_file);
        int code = doc.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, code));
            return;
        }

        PDFPage page = doc.insertPage(-1, PDFPage.e_SizeLetter);
        AddTextObjects(page, "insert in first");
        page = doc.insertPage(2, PDFPage.e_SizeLetter);
        AddTextObjects(page, "insert in 2 (based 0)");
        page = doc.insertPage(doc.getPageCount(), PDFPage.e_SizeLetter);
        AddTextObjects(page, "insert in last");
        String output_file = output_path + "insert_page.pdf";
        doc.saveAs(output_file, e_SaveFlagNormal);
        System.out.println("Insert pages.");

    }

    static void RemovePage() throws PDFException {
        String input_file = input_path + "page_organization_123.pdf";
        PDFDoc doc = new PDFDoc(input_file);
        int code = doc.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, code));
            return;
        }

        while (doc.getPageCount() > 1) {
            doc.removePage(0);
        }
        String output_file = output_path + "remove_all_pages_exceptt_the_last_page.pdf";
        doc.saveAs(output_file, e_SaveFlagNormal);
        System.out.println("Remove pages.");

    }

    // You can implement the functionality of exporting pages by creating an empty document
    // and importing part of the source document into the empty document.
    // You can also import the target document as a source document
    // to implement the copy page functionality.
    static void ImportPage() throws PDFException {
        String file_dest = input_path + "page_organization_123.pdf";
        String file_src = input_path + "page_organization_abc.pdf";
        PDFDoc doc_dest = new PDFDoc(file_dest);
        int code = doc_dest.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", file_dest, code));
            return;
        }
        PDFDoc doc_src = new PDFDoc(file_src);
        code = doc_src.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", file_src, code));
            return;
        }
        Range import_ranges = new Range(0);
        import_ranges.addSingle(doc_src.getPageCount() - 1);
        // Import page from PDFDoc object.
        Progressive progressive =
                doc_dest.startImportPages(-1, doc_src, e_ImportFlagNormal, "abc", import_ranges, null);
        while (progressive.resume() != e_Finished) {
        }
        String output_file = output_path + "page_organization_abc_to_123_import_pages.pdf";
        doc_dest.saveAs(output_file, e_SaveFlagNormal);

        doc_dest = new PDFDoc(file_dest);
        code = doc_dest.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", file_dest, code));
            return;
        }
        // Import page from file path.
        progressive = doc_dest.startImportPagesFromFilePath(-1, file_src, null, e_ImportFlagNormal, "abc",
                import_ranges, null);

        while (progressive.resume() != e_Finished) {
        }
        output_file = output_path + "abc_to_123_import_pages_form_filepath.pdf";
        doc_dest.saveAs(output_file, e_SaveFlagNormal);
        System.out.println("Import pages.");
    }

    // This example shows how to use the use the functions StartExtractPages and InsertDocument together,
    // instead of the StartImportPagesFromFilePath function. The execution efficiency of importing pages has
    // been optimized in these new functions.
    static void ImportPageOptimized() throws PDFException {
        String file_dest = input_path + "page_organization_123.pdf";
        String file_src = input_path + "page_organization_abc.pdf";
        PDFDoc doc_dest = new PDFDoc(file_dest);
        int code = doc_dest.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", file_dest, code));
            return;
        }
        PDFDoc doc_src = new PDFDoc(file_src);
        code = doc_src.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", file_src, code));
            return;
        }
        // Extract pages to temporary document
        Range import_ranges = new Range(0);
        import_ranges.addSingle(doc_src.getPageCount() - 1);
        String file_temp = output_path + "page_organization_import_extracted_pages.pdf";
        Progressive progressive =
            doc_src.startExtractPages(file_temp, e_ExtractPagesOptionAnnotation, import_ranges, null);
        while (progressive.resume() != e_Finished) {
        }

        PDFDoc doc_temp = new PDFDoc(file_temp);
        code = doc_temp.load(null);
        if (code != e_ErrSuccess) {
          System.out.println(String.format("The Doc [%s] Error: %d\n", file_temp, code));
          return;
        }

        // Insert temporary document into destination
        doc_dest.insertDocument(-1, doc_temp, e_InsertDocOptionAttachments);
        String output_file = output_path + "abc_to_123_insert_document.pdf";
        doc_dest.saveAs(output_file, e_SaveFlagNormal);

        System.out.println("Import pages optimized.");
    }

    static void MovePages() throws PDFException {
        String input_file = input_path + "page_organization_123.pdf";
        String input_file1 = input_path + "page_organization_abc.pdf";
        // Move page
        PDFDoc doc = new PDFDoc(input_file);
        int code = doc.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, code));
            return;
        }
        int count = doc.getPageCount();
        PDFPage page = doc.getPage(0);
        doc.movePageTo(page, doc.getPageCount() - 1);
        String output_file1 = output_path + "move_first_page_to_last.pdf";
        doc.saveAs(output_file1, e_SaveFlagNormal);

        doc = new PDFDoc(input_file1);
        code = doc.load(null);
        if (code != e_ErrSuccess) {
            return;
        }
        Range page_set = new Range();
        for (int i = 0; i < count / 2; i++) {
            page_set.addSingle(2 * i);
        }
        doc.movePagesTo(page_set, doc.getPageCount() - 1);
        String output_file = output_path + "move_pages.pdf";
        doc.saveAs(output_file, e_SaveFlagNormal);
        System.out.println("Move pages.");
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
            InsertPage();
            RemovePage();
            ImportPage();
            ImportPageOptimized();
            MovePages();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Library.release();
        return;
    }
}
