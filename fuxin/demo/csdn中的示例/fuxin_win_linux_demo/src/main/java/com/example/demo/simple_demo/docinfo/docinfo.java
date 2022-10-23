// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to get/set attributes of viewer preference
// and values of metadata in a PDF file.

import java.io.File;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.*;
import com.foxit.sdk.pdf.*;


import java.util.Calendar;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.*;
import static com.foxit.sdk.pdf.PDFDoc.*;
import static com.foxit.sdk.pdf.DocViewerPrefs.*;

public class docinfo {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/docinfo/";
    private static String input_path = "../input_files/";
    private static String[] kMetadataItems = {"Title", "Author", "Subject", "Keywords",
            "Creator", "Producer", "CreationDate", "ModDate"};
    private static String[] kDisplayModeStrings = {"UseNone", "UseOutlines", "UseThumbs", "FullScreen", "UseOC", "UseAttachment"};
    private static int[] kUIItems = {e_HideToolbar, e_HideMenubar, e_HideWindowUI, e_FitWindow,
            e_CenterWindow, e_DisplayDocTitle};
    private static int[] kDisplayModes = {e_DisplayUseNone, e_DisplayUseOutlines, e_DisplayUseThumbs,
            e_DisplayFullScreen, e_DisplayUseOC, e_DisplayUseAttachment};

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

    private static DateTime GetLocalDateTime() {
        Calendar c = Calendar.getInstance();

        DateTime datetime = new DateTime();
        datetime.setYear(c.get(Calendar.YEAR));
        datetime.setMonth(c.get(Calendar.MONTH) + 1);
        datetime.setDay(c.get(Calendar.DATE));
        datetime.setHour(c.get(Calendar.HOUR));
        datetime.setMinute(c.get(Calendar.MINUTE));
        datetime.setSecond(c.get(Calendar.SECOND));
        
        java.util.TimeZone timeZone = c.getTimeZone();
        int offset = timeZone.getRawOffset();
        int gmt = offset/(3600*1000);
        
        datetime.setUtc_hour_offset((short)gmt);
        datetime.setUtc_minute_offset(offset%(3600*1000) / 60);
        return datetime;
    }

    private static String GetPageBoxName(int type) {
        switch (type) {
            case e_MediaBox:
                return "MediaBox";
            case e_CropBox:
                return "CropBox";
            case e_TrimBox:
                return "TrimBox";
            case e_ArtBox:
                return "ArtBox";
            case e_BleedBox:
                return "BleedBox";
        }
        return "";
    }

    private static String GetViewerPrefName(int ui_item) {
        switch (ui_item) {
            case e_HideToolbar:
                return "HideToolbar";
            case e_HideMenubar:
                return "HideMenubar";
            case e_HideWindowUI:
                return "HideWindowUI";
            case e_FitWindow:
                return "FitWindow";
            case e_CenterWindow:
                return "CenterWindow";
            case e_DisplayDocTitle:
                return "DisplayDocTitle";
        }
        return "";
    }

    private static void ShowDocViewerPrefsInfo(PDFDoc doc, String file_name) throws PDFException, IOException {
        String file_info = output_path + "DocViewerPrefsInfo_" + file_name;
        FileWriter writer = new FileWriter(file_info, false);
        writer.write("Document viewer preferences information:\r\n");

        DocViewerPrefs prefs = new DocViewerPrefs(doc, null);

        // Get UI visibility status.
        for (int i = 0; i < kUIItems.length; i++) {
            String status = (prefs.getUIDisplayStatus(kUIItems[i])) ? "Yes" : "No";
            writer.write("Visibility of " + kUIItems[i] + ":\t" + status + "\r\n");
        }
        // Get display mode for non full-screen mode.
        int mode = prefs.getNonFullScreenPageMode();
        String display_mode = null;
        for (int i = 0; i < kDisplayModes.length; i++) {
            if (mode == kDisplayModes[i]) {
                display_mode = kDisplayModeStrings[i];
                break;
            }
        }
        writer.write("None full screen page mode:\t" + display_mode + "\r\n");
        // Get reading direction.
        String direction = prefs.getReadingDirection() ? "left to right" : "right to left";
        writer.write("Reading direction:\t" + direction + "\r\n");
        // Get the type of area item.
        int type = prefs.getPrintClip();
        writer.write("The GetPrintClip returned:\t" + GetPageBoxName(type) + "\r\n");
        type = prefs.getPrintArea();
        writer.write("The GetPrintArea returned:\t" + GetPageBoxName(type) + "\r\n");
        type = prefs.getViewArea();
        writer.write("The GetViewArea returned:\t" + GetPageBoxName(type) + "\r\n");
        type = prefs.getViewClip();
        writer.write("The GetViewClip returned:\t" + GetPageBoxName(type) + "\r\n");

        // Get page scaling option.
        writer.write("Page scaling option:\t" + prefs.getPrintScale() + "\r\n");
        // Get the number of copies to be printed.
        writer.write("The number of copies to be printed:\t" + prefs.getPrintCopies() + "\r\n");
        // Get page ranges which allowed to print
        writer.write("Page ranges for printing:\r\n");
        Range print_ranges = prefs.getPrintRange();
        if (!print_ranges.isEmpty()) {
            for (int i = 0; i < print_ranges.getSegmentCount(); i = i + 1) {
                writer.write("\tfirst:\t" + print_ranges.getSegmentStart(i) + "\tlast:\t" + print_ranges.getSegmentEnd(i) + "\r\n");
            }
        }
        writer.close();
    }

    private static void SetDocViewerPrefsInfo(PDFDoc doc) throws PDFException, IOException {

        doc.setDisplayMode(e_DisplayFullScreen);
        DocViewerPrefs prefs = new DocViewerPrefs(doc, null);
        // Set UI visibility status.
        for (int i = 0; i < kUIItems.length; i++) {
            prefs.setUIDisplayStatus(kUIItems[i], true);
        }
        // Set display mode for non full-screen mode.
        prefs.setNonFullScreenPageMode(e_DisplayUseOutlines);
        // Set reading direction.
        prefs.setReadingDirection(false);
        // Set the type of area item.

        prefs.setViewArea(e_CropBox);
        prefs.setViewClip(e_CropBox);
        prefs.setPrintArea(e_CropBox);
        prefs.setPrintClip(e_CropBox);
        // Set page scaling option.
        prefs.setPrintScale(e_PrintScaleNone);
        // Set the number of copies to be printed.
        prefs.setPrintCopies(4);
        Range print_range = new Range(0, doc.getPageCount() / 2, Range.e_All);
        prefs.setPrintRange(print_range);
    }

    private static void ShowMetaDataInfo(PDFDoc doc, String file_name) throws PDFException, IOException {
        String output_file = output_path + "MetaDataInfo_" + file_name;
        FileWriter writer = new FileWriter(output_file, false);
        writer.write("Metadata information:\r\n");
        Metadata metadata = new Metadata(doc);
        for (int i = 0; i < kMetadataItems.length; i++) {
            writer.write(kMetadataItems[i] + ": ");
            WStringArray value = metadata.getValues(kMetadataItems[i]);
            for (int j = 0; j < value.getSize(); j++) {
                writer.write(value.getAt(j));
            }
            writer.write("\r\n");
        }
        writer.close();
    }

    private static void SetMetaDataInfo(PDFDoc doc) throws PDFException {
        String[] kMetadataValues = {"Title set by simple demo", "Simple demo",
                "Subject set by simple demo", "Keywords set by simple demo",
                "Foxit PDF SDK", "Foxit"};
        Metadata metadata = new Metadata(doc);
        for (int i = 0; i < kMetadataValues.length; i++) {
            WStringArray MetadataValues = new WStringArray();
            MetadataValues.add(kMetadataValues[i]);
            metadata.setValues(kMetadataItems[i], MetadataValues);
        }
        metadata.setCreationDateTime(GetLocalDateTime());
        metadata.setModifiedDateTime(GetLocalDateTime());
    }

    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);
        // Initialize Library.
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";

        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);

            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("[Failed] Cannot load PDF document [%s].\r\nError Message: %d\n", input_file, error_code));
                return;
            }

            // Show original information.
            ShowDocViewerPrefsInfo(doc, "original.txt");
            ShowMetaDataInfo(doc, "original.txt");

            // Set information.
            SetDocViewerPrefsInfo(doc);
            SetMetaDataInfo(doc);

            // Show new information.
            ShowDocViewerPrefsInfo(doc, "new.txt");
            ShowMetaDataInfo(doc, "new.txt");

            String output_file = output_path + "AboutFoxit_docinfo.pdf";
            doc.saveAs(output_file, e_SaveFlagNoOriginal);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
        return;
    }
}
