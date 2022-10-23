// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to extract attached files from PDF files and
// add files as attachments to PDF files.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.Attachments;
import com.foxit.sdk.pdf.FileSpec;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.objects.PDFNameTree;

import java.io.File;
import java.io.FileWriter;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;

public class attachment {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/attachment/";
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

    static String DateTimeToString(DateTime datetime) {

        return String.format("%d/%d/%d-%d:%d:%d %s%d:%d", datetime.getYear(), datetime.getMonth(), datetime.getDay(), datetime.getHour(),
                datetime.getMinute(), datetime.getSecond(), datetime.getUtc_hour_offset() > 0 ? "+" : "-", datetime.getUtc_hour_offset(),
                datetime.getUtc_minute_offset());
    }

    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            myPath.mkdir();
        }
    }

    public static void main(String[] args) throws PDFException {

        createResultFolder(output_path);
        String input_file = input_path + "attachment.pdf";
        // Initialize library.
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d", error_code));
            return;
        }

        String file_info = output_path + "attachMent_info.txt";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d", input_file, error_code));
                return;
            }

            PDFNameTree empty_nametree = new PDFNameTree();
            {
                FileWriter text_doc = new FileWriter(file_info, false);
                // Get information of attachments.
                Attachments attachments = new Attachments(doc, empty_nametree);
                int count = attachments.getCount();
                for (int i = 0; i < count; i++) {
                    String key = attachments.getKey(i);
                    text_doc.write(String.format("key:\t\t%s\r\n", key));

                    FileSpec file_spec = attachments.getEmbeddedFile(key);
                    if (!file_spec.isEmpty()) {
                        String name = file_spec.getFileName();
                        text_doc.write(String.format("Name:\t\t%s\r\n", name));
                        text_doc.write(String.format("Size:\t\t%d\r\n", file_spec.getFileSize()));
                        text_doc.write(String.format("Is embedded:\t%s\r\n", file_spec.isEmbedded() ? "true" : "false"));
                        text_doc.write(String.format("Creation time:\t%s\r\n", DateTimeToString(file_spec.getCreationDateTime())));
                        text_doc.write(String.format("Modified time:\t%s\r\n", DateTimeToString(file_spec.getModifiedDateTime())));
                        text_doc.write("\r\n");

                        if (file_spec.isEmbedded()) {
                            String export_file_path = output_path + name;
                            boolean is_success = file_spec.exportToFile(export_file_path);
                            if (is_success)
                                System.out.println(String.format("File %s. Exported to File: success", name));
                            else
                                System.out.println(String.format("File %s. Exported to File: failed", name));
                        }
                    }
                }
                text_doc.close();
            }
            
            {
                // Remove all attachments
                Attachments attachment = new Attachments(doc, empty_nametree);
                attachment.removeAllEmbeddedFiles();
                String newPdf = output_path + "attachment_remove.pdf";
                doc.saveAs(newPdf, e_SaveFlagNoOriginal);
                System.out.println("Remove all attachments.");

            }
            
            {
                String text_path = input_path + "AboutFoxit.pdf";
                Attachments attachment = new Attachments(doc, empty_nametree);
                // Add new attachment to PDF document.
                attachment.addFromFilePath("OriginalAttachmentsInfo", text_path);
                String newPdf = output_path + "attachment_add.pdf";
                doc.saveAs(newPdf, e_SaveFlagNoOriginal);
                System.out.println("Add a new attachment to PDF document.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Library.release();
    }
}

