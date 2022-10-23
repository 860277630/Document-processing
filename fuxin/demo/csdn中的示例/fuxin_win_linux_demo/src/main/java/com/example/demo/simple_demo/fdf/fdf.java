// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to import annotations from FDF/XFDF files
// and export annotations to FDF/XFDF files.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Range;
import com.foxit.sdk.fdf.FDFDoc;
import com.foxit.sdk.pdf.PDFDoc;

import java.io.*;
import java.io.File;
import java.io.FileWriter;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.fdf.FDFDoc.e_FDF;
import static com.foxit.sdk.fdf.FDFDoc.e_XFDF;
import static com.foxit.sdk.pdf.PDFDoc.e_Annots;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;

public class fdf {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/fdf/";
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
        try {
            Range empty_range = new Range();
            {
                String input_file = input_path + "AboutFoxit.pdf";
                String fdf_file = input_path + "AnnotationData.fdf";
                PDFDoc pdf_doc = new PDFDoc(input_file);
                error_code = pdf_doc.load(null);
                if (error_code != e_ErrSuccess) {
                    System.out.println("The Doc " + input_file + " Error: " + error_code);
                    return;
                }
                FDFDoc fdf_doc = new FDFDoc(fdf_file);
                pdf_doc.importFromFDF(fdf_doc, e_Annots, empty_range);
                System.out.println("Import annotations from fdf with file.");
                String output_file = output_path + "AboutFoxit_importFDF.pdf";
                pdf_doc.saveAs(output_file, e_SaveFlagNoOriginal);
            }

            {
                String input_file = input_path + "AboutFoxit.pdf";
                String fdf_file = input_path + "AnnotationData.fdf";
                PDFDoc pdf_doc = new PDFDoc(input_file);
                error_code = pdf_doc.load(null);
                if (error_code != e_ErrSuccess) {
                    System.out.println("The Doc " + input_file + " Error: " + error_code);
                    return;
                }
        File file = new File(fdf_file);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
          FileInputStream in = new FileInputStream(file);  
          in.read(filecontent);  
          in.close();  
        } catch (FileNotFoundException e) {  
          e.printStackTrace();  
        } catch (IOException e) {  
                e.printStackTrace();  
        }  
      
                FDFDoc fdf_doc = new FDFDoc(filecontent);
                pdf_doc.importFromFDF(fdf_doc, e_Annots, empty_range);
                System.out.println("Import annotations from fdf with memory buffer.");
                String output_file = output_path + "AboutFoxit_importFDF_FromMemory.pdf";
                pdf_doc.saveAs(output_file, e_SaveFlagNoOriginal);
            }
      
            {
                // Export FDF file
                String input_file = input_path + "AnnotationDataExport.pdf";
                String output_fdf = output_path + "AnnotationDataExport_fdf.fdf";


                PDFDoc pdf_doc = new PDFDoc(input_file);
                error_code = pdf_doc.load(null);
                if (error_code != e_ErrSuccess) {
                    System.out.println("The Doc " + input_file + "Error: " + error_code);
                    return;
                }

                FDFDoc fdf_doc = new FDFDoc(e_FDF);
                pdf_doc.exportToFDF(fdf_doc, e_Annots, empty_range);
                System.out.println("Export a fdf file.");
                fdf_doc.saveAs(output_fdf);
            }

            {
                // Export XFDF file
                String input_file = input_path + "AnnotationDataExport.pdf";
                String output_xfdf = output_path + "AnnotationDataExport_xfdf.xfdf";

                PDFDoc pdf_doc = new PDFDoc(input_file);
                error_code = pdf_doc.load(null);
                if (error_code != e_ErrSuccess) {
                    System.out.println("The Doc " + input_file + " Error: " + error_code);
                    return;
                }

                FDFDoc fdf_doc = new FDFDoc(e_XFDF);
                pdf_doc.exportToFDF(fdf_doc, e_Annots, empty_range);
                System.out.println("Export an xfdf file.");
                fdf_doc.saveAs(output_xfdf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Library.release();
    }
}
