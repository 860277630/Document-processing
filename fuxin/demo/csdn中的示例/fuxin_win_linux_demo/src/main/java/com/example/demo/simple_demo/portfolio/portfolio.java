// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to create or open a portfolio PDF file.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.Portfolio;
import com.foxit.sdk.pdf.SchemaField;
import com.foxit.sdk.pdf.SchemaFieldArray;
import com.foxit.sdk.pdf.PortfolioNode;
import com.foxit.sdk.pdf.PortfolioNodeArray;
import com.foxit.sdk.pdf.PortfolioFileNode;
import com.foxit.sdk.pdf.PortfolioFolderNode;
import com.foxit.sdk.pdf.FileSpec;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.common.DateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class portfolio {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/portfolio/";
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
    
    private static boolean creatPortfolioPDF(String saved_portfolio_pdf_path) throws PDFException {
        // Create a new blank portfolio PDF.
        Portfolio new_portfolio = Portfolio.createPortfolio();
        if (true == new_portfolio.isEmpty()) {
            System.out.println("[FAILED] Fail to create a new portfolio PDF.\r\n");
            return false;
        }

        // Get the root node.
        PortfolioNode root_node = new_portfolio.getRootNode();
        if (true == root_node.isEmpty()) {
            System.out.println("[FAILED] Fail to get the root node.\r\n");
            return false;
        }
        // The root node should be a folder node, so transfer to use PortfolioFolderNode.
        PortfolioFolderNode root_folder = new PortfolioFolderNode(root_node);

        // Pre-load the PDF file which is to be added to new sub folder in portfolio PDF.
        // ATTENTION: please keep the PDF document object valid until portfolio PDF is saved or ends its life-cycle.
        String input_pdf_filename = "AboutFoxit.pdf";
        String input_pdf_file_path = input_path + input_pdf_filename;
        PDFDoc pdf_doc = new PDFDoc(input_pdf_file_path);
        int error_code = pdf_doc.load(null);
        if (error_code != Constants.e_ErrSuccess) {
            System.out.println(String.format("[FAILED] Fail to load PDF file %s. Error: %d\n", input_pdf_file_path, error_code));
        }

        // Add a sub folder to root folder node.
        PortfolioFolderNode new_sub_foldernode = root_folder.addSubFolder("Sub Folder-1");
        if (true == new_sub_foldernode.isEmpty()) {
            System.out.println("[FAILED] Fail to add sub folder.\r\n");
        } else {
            new_sub_foldernode.setDescription("This is a sub folder added to portfolio PDF file.");

            if (false == pdf_doc.isEmpty()) {
                // Add a valid PDF document object to current folder node.
                // ATTENTION: please keep the PDF document object valid until portfolio PDF is saved or ends its life-cycle.
                PortfolioFileNode new_filenode = new_sub_foldernode.addPDFDoc(pdf_doc, input_pdf_filename);
                if (true == new_filenode.isEmpty()) {
                    System.out.println(String.format("[FAILED] Fail to add PDF file %s.\r\n", input_pdf_file_path));
                } else {
                    FileSpec file_spec = new_filenode.getFileSpec();
                    file_spec.setDescription("This is a common PDF file added to portfolio PDF file");
                }
            }
        }

        // Add a non-PDF file to root folder node.
        String input_file_path = input_path + "FoxitLogo.jpg";
        PortfolioFileNode new_sub_filenode = root_folder.addFile(input_file_path);
        if (true == new_sub_filenode.isEmpty()) {
            System.out.println(String.format("[FAILED] Fail to add file %s.\r\n", input_file_path));
        } else {
            FileSpec file_spec = new_sub_filenode.getFileSpec();
            file_spec.setDescription("This is a non-PDF file added to portfolio PDF file.");
        }
  
        // User can update schema field and other properties by class Portfolio, if necessary.
  
        // Save the new portfolio PDF file.
        PDFDoc portfolio_pdf_doc = new_portfolio.getPortfolioPDFDoc();
        if (true == portfolio_pdf_doc.isEmpty()) {
            System.out.println("[FAILED] Fail to get portfolio PDF document object.\r\n");
            return false;
        } else {
            return portfolio_pdf_doc.saveAs(saved_portfolio_pdf_path, PDFDoc.e_SaveFlagNormal);
        }
    }

    private static void outputTab(FileWriter output_txt_doc, int nTabCount) throws IOException, PDFException {
        for (int i = 0; i<nTabCount; i++)
            output_txt_doc.write("\t");
    }

    private static void outputFileNodeInfo(FileWriter output_txt_doc, PortfolioFileNode node, int tab_count) throws IOException, PDFException {
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write("Type:File\r\n");

        String key_name = node.getKeyName();
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write(String.format("Key Name:%s\r\n", key_name));

        FileSpec file_spec = node.getFileSpec();
        String file_name = file_spec.getFileName();
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write(String.format("File Name:%s\r\n", file_name));

        String description = file_spec.getDescription();
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write(String.format("Description:%s\r\n", description));
    }

    private static void outputFolderNodeInfo(FileWriter output_txt_doc, PortfolioFolderNode node, int tab_count) throws IOException, PDFException {
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write("Type:Folder\r\n");

        String name = node.getName();
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write(String.format("Name:%s\r\n", name));

        String description = node.getDescription();
        if (tab_count>0)
            outputTab(output_txt_doc, tab_count);
        output_txt_doc.write(String.format("Description:%s\r\n", description));

        PortfolioNodeArray sub_nodes = node.getSortedSubNodes();
        outputSubNodesInfo(output_txt_doc, sub_nodes, tab_count + 1);
    }

    private static void outputNodeInfo(FileWriter output_txt_doc, PortfolioNode node, int tab_count) throws IOException, PDFException {
        switch (node.getNodeType()) {
            case PortfolioNode.e_TypeFolder:
                outputFolderNodeInfo(output_txt_doc, new PortfolioFolderNode(node), tab_count);
                break;
            case PortfolioNode.e_TypeFile:
                outputFileNodeInfo(output_txt_doc, new PortfolioFileNode(node), tab_count);
                break;
            default:
                break;
        }
    }

    private static void outputSubNodesInfo(FileWriter output_txt_doc, PortfolioNodeArray sub_nodes, int tab_count) throws IOException, PDFException {
        for (int index = 0; index < sub_nodes.getSize(); index++) {
            if (tab_count > 0)
                outputTab(output_txt_doc, tab_count);
            output_txt_doc.write(String.format("Sorted Index (under current folder):%d\r\n", index));
            outputNodeInfo(output_txt_doc, sub_nodes.getAt(index), tab_count);
            output_txt_doc.write("========\r\n");
        }
    }

    private static void outputSchemaFields(FileWriter output_txt_doc, SchemaFieldArray field_array) throws IOException, PDFException {
        if (field_array.getSize() <= 0) return;
            output_txt_doc.write("==== Schema Fields ====\r\n");

        for (int i = 0; i < field_array.getSize(); i++) {
            output_txt_doc.write(String.format("Field index:%d\r\n", i));
            SchemaField field = field_array.getAt(i);
            if (true == field.isEmpty()) continue;
            String key_name = field.getKeyName();
            output_txt_doc.write(String.format("Key name: %s\r\n", key_name));

            String subtype_name = field.getSubtypeName();
            output_txt_doc.write(String.format("Subtype name: %s\r\n", subtype_name));

            String display_name = field.getDisplayName();
            output_txt_doc.write(String.format("Display name: %s\r\n", display_name));

            boolean is_visible = field.isVisible();
            output_txt_doc.write(String.format("Visibility: %s\r\n", is_visible ? "true" : "false"));

            output_txt_doc.write("========\r\n");
        }
    }

    private static void outputPortfolioProperties(FileWriter output_txt_doc, Portfolio portfolio) throws IOException, PDFException {
        output_txt_doc.write("==== Portfolio Properties ====\r\n");
        String initial_filespec_keyname = portfolio.getInitialFileSpecKeyName();
        output_txt_doc.write(String.format("Initial FileSpec Key Name:%s\r\n", initial_filespec_keyname));

        int view_mode = portfolio.getInitialViewMode();
        String view_mode_str = "";
        switch (view_mode) {
            case Portfolio.e_InitialViewUnknownMode:
                view_mode_str = "Unknown";
                break;
            case Portfolio.e_InitialViewDetailMode:
                view_mode_str = "Detail";
                break;
            case Portfolio.e_InitialViewTileMode:
                view_mode_str = "Tile";
                break;
            case Portfolio.e_InitialViewHidden:
                view_mode_str = "Hidden";
                break;
        }
        output_txt_doc.write(String.format("Initial View Mode:%s\r\n", view_mode_str));


        boolean is_ascending = portfolio.isSortedInAscending();
        output_txt_doc.write(String.format("Sorting Order:%s\r\n", is_ascending? "Ascending" : "Descending" ));

        String sorting_field_name = portfolio.getSortingFieldKeyName();
        output_txt_doc.write(String.format("Sorting Field Key Name:%s\r\n", sorting_field_name));

        SchemaFieldArray field_array = portfolio.getSchemaFields();
        outputSchemaFields(output_txt_doc, field_array);
    }
    
    private static void outputPortfolioPDFInfo(String portfolio_file_path, FileWriter output_txt_doc) throws IOException, PDFException {
        PDFDoc pdf_doc = new PDFDoc(portfolio_file_path);
        int error_code = pdf_doc.load(null);
        if (error_code != Constants.e_ErrSuccess) {
            System.out.println(String.format("[FAILED] Fail to load Portfolio PDF file %s. Error: %d\n", portfolio_file_path, error_code));
            return;
        }

        if (false == pdf_doc.isPortfolio()) {
            System.out.println(String.format("[FAILED] Fail to output portfolio information for PDF file %s, because it is not a portfolio PDF file.\r\n", portfolio_file_path));
            return;
        }

        Portfolio exist_portfolio = Portfolio.createPortfolio(pdf_doc);
        if (true == exist_portfolio.isEmpty()) {
            System.out.println("[FAILED] Fail to create a portfolio object based an existed portfolio PDF document.\r\n");
            return;
        }

        // Output portfolio properties.
        outputPortfolioProperties(output_txt_doc, exist_portfolio);
        output_txt_doc.write("======================================================================\r\n");

        // Output all nodes.
        output_txt_doc.write("==== Nodes Information ====\r\n");
        PortfolioNode root_node = exist_portfolio.getRootNode();
        PortfolioFolderNode root_folder = new PortfolioFolderNode(root_node);
        PortfolioNodeArray sub_nodes = root_folder.getSortedSubNodes();
        outputSubNodesInfo(output_txt_doc, sub_nodes, 0);
    }

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != Constants.e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        try {
            String saved_portfolio_pdf_path = output_path + "new_portfolio.pdf";
            // Create a new portfolio PDF file
            if (creatPortfolioPDF(saved_portfolio_pdf_path)) {
                String output_info_file_path = output_path + "new_portfolio_info.txt";
                FileWriter text_doc= new FileWriter(output_info_file_path, false);
                // Show information of the new portfolio PDF file.
                outputPortfolioPDFInfo(saved_portfolio_pdf_path, text_doc);
                text_doc.close();
            }

            System.out.println("END: Portfolio demo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Library.release();
    }
}
