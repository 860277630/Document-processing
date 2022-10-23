// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to verify if a PDF file is PDFA-1a version
// or convert a PDF file to PDFA-1a version.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.addon.compliance.*;
import com.foxit.sdk.common.WStringArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_ErrInvalidLicense;

public class compliance {


    static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn"; 
    static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    static String output_path = "../output_files/compliance/";
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

    static void outputPDFAHitData(ResultInformation result_info, String output_txt_path) throws PDFException, IOException {
        FileWriter text_doc = new FileWriter(output_txt_path, false);

        int hit_data_count = result_info.getHitDataCount();
        text_doc.write(String.format("== Hit Data, count:%d ==\r\n", hit_data_count));
        for (int i = 0; i<hit_data_count; i++) {
            HitData hit_data = result_info.getHitData(i);
            text_doc.write(String.format("Triggered count:%d\r\n", hit_data.getTriggered_count()));

            text_doc.write("Name:");
            text_doc.write(hit_data.getName());
            text_doc.write("\r\n");

            text_doc.write("Comment:");
            text_doc.write(hit_data.getComment());
            text_doc.write("\r\n");
    
            text_doc.write("Trigger value:");
            WStringArray trigger_values = hit_data.getTrigger_values();
            long trigger_value_count = trigger_values.getSize();
            if (trigger_value_count < 1) {
                text_doc.write("\r\n");
            } else {
                for (long z = 0; z<trigger_value_count; z++) {
                    text_doc.write("\t");
                    text_doc.write(trigger_values.getAt(z));
                    text_doc.write("\r\n");
                }
            }

            text_doc.write(String.format("Check severity:%d\r\nPage index:%d\r\n", hit_data.getSeverity(), hit_data.getPage_index()));
    
            text_doc.write("\r\n");
        }
        text_doc.close();
    }

    static void outputPDFAFixupData(ResultInformation result_info, String output_txt_path) throws PDFException, IOException {
        FileWriter text_doc = new FileWriter(output_txt_path, false);

        int fixup_count = result_info.getFixupDataCount();
        text_doc.write(String.format("== Fixup Data, count:%d ==\r\n", fixup_count));
        for (int i = 0; i<fixup_count; i++) {
            FixupData fixup_data = result_info.getFixupData(i);
            text_doc.write(String.format("Used count:%d\r\n", fixup_data.getUsed_count()));

            text_doc.write("Name:");
            text_doc.write(fixup_data.getName());
            text_doc.write("\r\n");

            text_doc.write("Comment:");
            text_doc.write(fixup_data.getComment());
            text_doc.write("\r\n");

            text_doc.write("Reason:");
            WStringArray reasons = fixup_data.getReasons();
            long reason_count = reasons.getSize();
            if (reason_count < 1) {
                text_doc.write("\r\n");
            } else {
                for (long z = 0; z<reason_count; z++) {
                    text_doc.write("\t");
                    text_doc.write(reasons.getAt(z));
                    text_doc.write("\r\n");
                }
            }

            text_doc.write(String.format("State value:%d\r\n", fixup_data.getState()));

            text_doc.write("\r\n");
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

        try {
            // "compliance_resource_folder_path" is the path of compliance resource folder. Please refer to Developer Guide for more details.
            String compliance_resource_folder_path = "";
            // If you use an authorization key for Foxit PDF SDK, please set a valid unlock code string to compliance_engine_unlockcode for ComplianceEngine.
            // If you use an trial key for Foxit PDF SDK, just keep compliance_engine_unlockcode as an empty string.
            String compliance_engine_unlockcode = "";
            
            if (compliance_resource_folder_path.length()<1) {
                System.out.println("compliance_resource_folder_path is still empty. Please set it with a valid path to compliance resource folder path.");
                return ;
            }
            // Initialize compliance engine.
            error_code = ComplianceEngine.initialize(compliance_resource_folder_path, compliance_engine_unlockcode);
            if (error_code != e_ErrSuccess) {
              switch (error_code) {
              case e_ErrInvalidLicense:
                System.out.println("[Failed] Compliance module is not contained in current Foxit PDF SDK keys.\n");
                break;
              default:
                System.out.println(String.format("[Failed] Fail to initialize compliance engine. Error: %d\n", error_code));
                break;
              }
              return ;
            }
            System.out.println("ComplianceEngine is initialized.");
            
            // Set custom temp folder path for ComplianceEngine. 
            //ComplianceEngine.setTempFolderPath("");
            // Set languages. If not set language to ComplianceEngine, "English" will be used as default.
            //ComplianceEngine.setLanguage("Chinese-Simplified");

            PDFACompliance pdfa_compliance = new PDFACompliance();
            {
                // Verify original PDF file.
                System.out.println("======== PDFACompliance: Verify before convert ========");
                MyComplianceProgressCallback verify_progress_callback = new MyComplianceProgressCallback(output_path + "0_pdfacompliance_verify_before_convert_progressdata.txt");
                ResultInformation verify_result_info = pdfa_compliance.verify(PDFACompliance.e_VersionPDFA1a, input_file, 0, -1, verify_progress_callback);
                outputPDFAHitData(verify_result_info, output_path + "0_pdfacompliance_verify_before_convert_hitdata.txt");
            }
            
            String save_pdf_path = output_path + "1_pdfacompliance_convert_to_pdf1a.pdf";
            {
              // Convert original PDF file to PDFA-1a version.
                System.out.println("======== PDFACompliance: Convert ========");
                MyComplianceProgressCallback convert_progress_callback = new MyComplianceProgressCallback(output_path + "1_1_pdfacompliance_convert_progressdata.txt");
                ResultInformation convert_result_info = pdfa_compliance.convertPDFFile(input_file, save_pdf_path,
                                                                                       PDFACompliance.e_VersionPDFA1a,
                                                                                       convert_progress_callback);
                outputPDFAFixupData(convert_result_info, output_path+ "1_1_pdfacompliance_convert_pdfa1a_fixupdata.txt");
                outputPDFAHitData(convert_result_info, output_path + "1_1_pdfacompliance_convert_pdfa1a_hitdata.txt");
            }
            
            {
                // Verify converted result PDF file, which is not expected to be PDFA-1a compliant as the original did not have accessibility structures.
                System.out.println("======== PDFACompliance: Verify after convert ========");
                MyComplianceProgressCallback verify_progress_callback_2 = new MyComplianceProgressCallback(output_path + "1_2_pdfacompliance_verify_after_convert_progressdata.txt");
                ResultInformation verify_result_info_2 = pdfa_compliance.verify(PDFACompliance.e_VersionPDFA1a, save_pdf_path, 0, -1, verify_progress_callback_2);
                outputPDFAHitData(verify_result_info_2, output_path + "1_2_pdfacompliance_verify_after_convert_hitdata.txt");
            }

            input_file = input_path + "AboutFoxit_Structured.pdf";
            {
                // Verify original PDF file.
                System.out.println("======== PDFACompliance: Verify before convert ========");
                MyComplianceProgressCallback verify_progress_callback = new MyComplianceProgressCallback(output_path + "2_pdfacompliance_verify_before_convert_progressdata.txt");
                ResultInformation verify_result_info = pdfa_compliance.verify(PDFACompliance.e_VersionPDFA1a, input_file, 0, -1, verify_progress_callback);
                outputPDFAHitData(verify_result_info, output_path + "2_pdfacompliance_verify_before_convert_hitdata.txt");
            }

            save_pdf_path = output_path + "3_pdfacompliance_convert_to_pdf1a.pdf";
            {
              // Convert original PDF file to PDFA-1a version.
                System.out.println("======== PDFACompliance: Convert ========");
                MyComplianceProgressCallback convert_progress_callback = new MyComplianceProgressCallback(output_path + "3_1_pdfacompliance_convert_progressdata.txt");
                ResultInformation convert_result_info = pdfa_compliance.convertPDFFile(input_file, save_pdf_path,
                                                                                       PDFACompliance.e_VersionPDFA1a,
                                                                                       convert_progress_callback);
                outputPDFAFixupData(convert_result_info, output_path+ "3_1_pdfacompliance_convert_pdfa1a_fixupdata.txt");
                outputPDFAHitData(convert_result_info, output_path + "3_1_pdfacompliance_convert_pdfa1a_hitdata.txt");
            }

            {
                // Verify converted result PDF file, which is expected to be PDFA-1a version.
                System.out.println("======== PDFACompliance: Verify after convert ========");
                MyComplianceProgressCallback verify_progress_callback_2 = new MyComplianceProgressCallback(output_path + "3_2_pdfacompliance_verify_after_convert_progressdata.txt");
                ResultInformation verify_result_info_2 = pdfa_compliance.verify(PDFACompliance.e_VersionPDFA1a, save_pdf_path, 0, -1, verify_progress_callback_2);
                outputPDFAHitData(verify_result_info_2, output_path + "3_2_pdfacompliance_verify_after_convert_hitdata.txt");
            }

            PDFCompliance pdf_compliance = new PDFCompliance();
            input_file = input_path + "AF_ImageXObject_FormXObject.pdf";
            {
                // Convert original PDF file to PDF-1.4.
                System.out.println("======== PDFCompliance: Convert ========");
                MyComplianceProgressCallback convert_progress_callback= new MyComplianceProgressCallback(output_path + "4_pdfcompliance_convert_1_4_progressdata.txt");
                save_pdf_path = output_path + "4_pdfcompliance_convert_to_1_4.pdf";
                ResultInformation convert_result_info = pdf_compliance.convertPDFFile(input_file, save_pdf_path, 14, convert_progress_callback);
                outputPDFAFixupData(convert_result_info, output_path + "4_pdfcompliance_convert_1_4_fixupdata.txt");
                outputPDFAHitData(convert_result_info, output_path + "4_pdfcompliance_convert_1_4_hitdata.txt");
            }
            {
                // Convert original PDF file to PDF-1.7.
                System.out.println("======== PDFCompliance: Convert ========");
                MyComplianceProgressCallback convert_progress_callback = new MyComplianceProgressCallback(output_path + "5_pdfcompliance_convert_1_7_progressdata.txt");
                save_pdf_path = output_path + "5_pdfcompliance_convert_to_1_7.pdf";
                ResultInformation convert_result_info = pdf_compliance.convertPDFFile(input_file, save_pdf_path, 17, convert_progress_callback);
                outputPDFAFixupData(convert_result_info, output_path + "5_pdfcompliance_convert_1_7_fixupdata.txt");
                outputPDFAHitData(convert_result_info, output_path + "5_pdfcompliance_convert_1_7_hitdata.txt");
            }

            // Release compliance engine.
            ComplianceEngine.release();
            
            System.out.println("== End: Compliance demo. ==");


        } catch (Exception e) {
            e.printStackTrace();

        }
        Library.release();
        return;
    }

}
