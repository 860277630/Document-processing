// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to enumerate and modify bookmarks
// in PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.Bookmark;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.actions.Destination;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.Bookmark.e_PosLastChild;
import static com.foxit.sdk.pdf.Bookmark.e_StyleItalic;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;

public class bookmark {


    static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    static String output_path = "../output_files/bookmark/";
    static String input_path = "../input_files/";

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

    static void showBookmarkInfo(Bookmark bookmark, FileWriter doc, int depth) throws PDFException, IOException {
        if (depth > 32)
            return;
        if (bookmark.isEmpty())
            return;
        for (int i = 0; i < depth; i++) {
            doc.write("\t");
        }
        // Show bookmark's title.
        doc.write(String.format("%s\t", bookmark.getTitle()));

        // Show bookmark's color.
        doc.write(String.format("color: %x\r\n", bookmark.getColor()));
        showBookmarkInfo(bookmark.getFirstChild(), doc, depth + 1);
        showBookmarkInfo(bookmark.getNextSibling(), doc, depth);
    }

    static void showBookmarksInfo(PDFDoc doc, String info_path) throws IOException, PDFException {
        FileWriter text_doc = new FileWriter(info_path, false);
        Bookmark root = doc.getRootBookmark();
        if (!root.isEmpty()) {
            showBookmarkInfo(root, text_doc, 0);
        } else {
            text_doc.write("No bookmark information!\r\n");
        }
        text_doc.close();
    }


    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";
        String output_file1 = output_path + "bookmark_add.pdf";
        String output_file2 = output_path + "bookmark_change.pdf";
        String bookmark_info_file = output_path + "bookmark_info.txt";

        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                return;
            }

            // Show original bookmark information.
            showBookmarksInfo(doc, bookmark_info_file);

            // Get bookmark root node or Create new bookmark root node.
            Bookmark root = doc.getRootBookmark();
            if (root.isEmpty()) {
                root = doc.createRootBookmark();
            }
            for (int i = 0; i < doc.getPageCount(); i += 2) {
                Destination dest = Destination.createFitPage(doc, i);

                String ws_title = String.format("A bookmark to a page (index: %d)", i);
                Bookmark child = root.insert(ws_title,
                        e_PosLastChild);
                child.setDestination(dest);
                child.setColor(i * 0xF68C21);
            }
            doc.saveAs(output_file1, e_SaveFlagNoOriginal);

            // Get first bookmark and change properties.
            Bookmark first_bookmark = root.getFirstChild();
            first_bookmark.setStyle(e_StyleItalic);
            first_bookmark.setColor(0xFF0000);
            first_bookmark.setTitle("Change bookmark title, style, and color");

            // Remove next sibling bookmark
            if (!first_bookmark.getNextSibling().isEmpty()) {
                doc.removeBookmark(first_bookmark.getNextSibling());
            }

            bookmark_info_file = output_path + "bookmark_info1.txt";
            showBookmarksInfo(doc, bookmark_info_file);

            doc.saveAs(output_file2, e_SaveFlagNoOriginal);
            System.out.println("Bookmark demo.");


        } catch (Exception e) {
            e.printStackTrace();

        }
        Library.release();
        return;
    }

}
