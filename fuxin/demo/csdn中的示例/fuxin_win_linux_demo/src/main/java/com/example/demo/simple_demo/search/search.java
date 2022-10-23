// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to search text in all pages in PDF document.


import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.RectFArray;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.TextSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.TextPage.e_ParseTextNormal;
import static com.foxit.sdk.pdf.TextSearch.*;

public class search {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/search/";
    private static String input_path = "../input_files/";
    private static String input_file = input_path + "AboutFoxit.pdf";
    private static String output_file = output_path + "search.txt";

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

    static void wrapper_write(FileWriter text_out, String formatbuff) throws IOException {
        System.out.println(formatbuff);
        text_out.write(formatbuff);
    }

    static void OutputMatchedInfo(FileWriter text_out, TextSearch search, int matched_index) throws PDFException, IOException {
        int page_index = search.getMatchPageIndex();
        wrapper_write(text_out, "Index of matched pattern:\t" + matched_index + "\r\n");
        wrapper_write(text_out, "\tpage:\t" + page_index + "\r\n");
        wrapper_write(text_out, "\tmatch char start index:\t" + search.getMatchStartCharIndex() + "\r\n");
        wrapper_write(text_out, "\tmatch char end index:\t" + search.getMatchEndCharIndex() + "\r\n");
        wrapper_write(text_out, "\tmatch sentence start index:\t" + search.getMatchSentenceStartIndex() + "\r\n");
        wrapper_write(text_out, "\tmatch sentence:\t" + search.getMatchSentence() + "\r\n");
        RectFArray rect_array = search.getMatchRects();
        int rect_count = rect_array.getSize();
        wrapper_write(text_out, "\tmatch rectangles count:\t" + rect_count + "\r\n");
        for (int i = 0; i < rect_count; i++) {
            RectF rect = rect_array.getAt(i);
            wrapper_write(text_out, String.format("\trectangle(in PDF space) :%d\t[left = %.4f, bottom = %.4f, right = %.4f, top = %.4f]\r\n", i, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop()));
        }
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
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                return;
            }
            FileWriter text_out = new FileWriter(output_file, false);

            // sample 1: search for all pages of doc.
            TextSearch search = new TextSearch(doc, null, (int)e_ParseTextNormal);

            int start_index = 0, end_index = doc.getPageCount() - 1;
            search.setStartPage(0);
            search.setEndPage(doc.getPageCount() - 1);

            String pattern = "Foxit";
            search.setPattern(pattern);

            int flags = e_SearchNormal;
            // If want to specify flags, you can do as followings:
            // flags |= TextSearch::e_SearchMatchCase;
            // flags |= TextSearch::e_SearchMatchWholeWord;
            // flags |= TextSearch::e_SearchConsecutive;
            search.setSearchFlags(flags);
            wrapper_write(text_out, "Begin search " + pattern + " at " + input_file + ".\n");
            wrapper_write(text_out, "Start index:\t" + start_index + "\r\n");
            wrapper_write(text_out, "End index:\t" + end_index + "\r\n");
            wrapper_write(text_out, "Match key:\t" + pattern + "\r\n");
            String match_case = ((flags & TextSearch.e_SearchMatchCase) != 0) ? "Yes" : "No";
            wrapper_write(text_out, "Match Case\t" + match_case + "\r\n");
            String match_whole_word = ((flags & TextSearch.e_SearchMatchWholeWord) != 0) ? "Yes" : "No";
            wrapper_write(text_out, "Match whole word:\t" + match_whole_word + "\r\n");
            String match_consecutive = ((flags & TextSearch.e_SearchConsecutive) != 0) ? "Yes" : "No";
            wrapper_write(text_out, "Consecutive:\t" + match_consecutive + "\r\n");
            int match_count = 0;
            while (search.findNext()) {
                RectFArray rect_array = search.getMatchRects();
                OutputMatchedInfo(text_out, search, match_count);
                match_count++;
            }
            text_out.close();
            System.out.println("Marched " + match_count + "counts.");
            // end of sample 1.

            System.out.println("Search demo finished.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
    }

}

