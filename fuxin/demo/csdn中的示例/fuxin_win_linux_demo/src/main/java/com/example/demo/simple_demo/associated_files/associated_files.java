// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to associate files with PDF.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.AssociatedFiles;
import com.foxit.sdk.pdf.FileSpec;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.annots.Annot;
import com.foxit.sdk.pdf.graphics.FormXObject;
import com.foxit.sdk.pdf.graphics.GraphicsObject;
import com.foxit.sdk.pdf.graphics.ImageObject;
import com.foxit.sdk.pdf.graphics.MarkedContent;
import com.foxit.sdk.pdf.objects.PDFDictionary;
import com.foxit.sdk.pdf.objects.PDFObject;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.AssociatedFiles.e_RelationshipSource;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.pdf.annots.Annot.e_Note;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeFormXObject;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeImage;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeText;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;


public class associated_files {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/associated_files/";
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
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        createResultFolder(output_path);
        
        // Get information about associated files from PDF documents.
        String input_file = input_path + "AF_Catalog_Page_Annot.pdf";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc [" +  input_file + " Error: " +  error_code);
                return;
            }

            AssociatedFiles associated_files = new AssociatedFiles(doc);
            PDFDictionary doc_catalog_dict = doc.getCatalog();
            int count = associated_files.getAssociatedFilesCount(doc_catalog_dict);
            System.out.println("The catalog dictionary of \"AF_Catalog_Page_Annot.pdf\" has" + count + " associated files.");
            FileSpec filespec = associated_files.getAssociatedFile(doc_catalog_dict, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "af_1.txt");
            PDFPage page = doc.getPage(0);
            PDFDictionary page_dict = page.getDict();
            count = associated_files.getAssociatedFilesCount(page_dict);
            System.out.println("The page dictionary of \"AF_Catalog_Page_Annot.pdf\" has " + count + " associated files.");
            filespec = associated_files.getAssociatedFile(page_dict, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "af_2.txt");
                            
            Annot annot = page.getAnnot(0);
            PDFDictionary annot_dict = annot.getDict();
            count = associated_files.getAssociatedFilesCount(annot_dict);
            System.out.println("The annot dictionary of \"AF_Catalog_Page_Annot.pdf\" has " + count + " associated files.");
            filespec = associated_files.getAssociatedFile(annot_dict, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "af_4.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
  
        input_file = input_path + "AF_ImageXObject_FormXObject.pdf";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc [" +  input_file + " Error: " +  error_code);
                return;
            }

            AssociatedFiles associated_files = new AssociatedFiles(doc);
            PDFPage page = doc.getPage(0);
            page.startParse(e_ParsePageNormal, null, false);

            long pos = page.getFirstGraphicsObjectPosition(e_TypeImage);
            GraphicsObject image_x_object = page.getGraphicsObject(pos);
            PDFDictionary image_x_object_dict = image_x_object.getImageObject().getStream().getDictionary();
            int count = associated_files.getAssociatedFilesCount(image_x_object_dict);
            System.out.println("The image x object of \"AF_ImageXObject_FormXObject.pdf\" has " + count + " associated files.");
            FileSpec filespec = associated_files.getAssociatedFile(image_x_object_dict, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "af_5.txt");

            pos = page.getFirstGraphicsObjectPosition(e_TypeFormXObject);
            GraphicsObject form_x_object = page.getGraphicsObject(pos);
            PDFDictionary form_x_object_dict = form_x_object.getFormXObject().getStream().getDictionary();
            count = associated_files.getAssociatedFilesCount(form_x_object_dict);
            System.out.println("The form x object of \"AF_ImageXObject_FormXObject.pdf\" has "+ count + "associated files.");
            filespec = associated_files.getAssociatedFile(form_x_object_dict, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "af_6.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        input_file = input_path + "AF_MarkedContent.pdf";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc [" + input_file + " Error: " + error_code);
                return;
            }

            AssociatedFiles associated_files = new AssociatedFiles(doc);
            PDFPage page = doc.getPage(0);
            page.startParse(e_ParsePageNormal, null, false);
            long pos = page.getFirstGraphicsObjectPosition(e_TypeText);
            GraphicsObject text_object = page.getGraphicsObject(pos);

            int count = associated_files.getAssociatedFilesCount(text_object);
            System.out.println("The text object of \"AF_MarkedContent.pdf\" has " + count + " associated files.");
            FileSpec filespec = associated_files.getAssociatedFile(text_object, 0);
            System.out.println("The file name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "bitmap.bmp");

            filespec = associated_files.getAssociatedFile(text_object, 1);
            System.out.println("The second name is \"" + filespec.getFileName() + "\".");
            filespec.exportToFile(output_path + "text.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Associate files with objects in PDF documents.
        input_file = input_path + "AssociateTestFile.pdf";
        try {
            // Create a document
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                return;
            }

            AssociatedFiles associated_files = new AssociatedFiles(doc);

            // Create filespec with 1.txt
            FileSpec filespec_txt = new FileSpec(doc);
            filespec_txt.setAssociteFileRelationship(e_RelationshipSource);
            filespec_txt.setFileName("1.txt");
            DateTime dateTime = new DateTime(2017, 9, 15, 17, 20, 20, 0, (short) 8, 0);
            filespec_txt.setDescription("text");
            filespec_txt.embed(input_path + "1.txt");
            filespec_txt.setCreationDateTime(dateTime);
            filespec_txt.setModifiedDateTime(dateTime);
            filespec_txt.setSubtype("application/octet-stream");

            // Associate 1.txt with catalog dictionary.
            {
                PDFObject catalog_dict = doc.getCatalog();
                associated_files.associateFile(catalog_dict, filespec_txt);
                System.out.println(String.format("Associate a text file with catalog dictionary."));
                FileSpec filespec_catalog = associated_files.getAssociatedFile(catalog_dict, 0);
                filespec_catalog.exportToFile(output_path + "catalog.txt");
            }

            PDFPage page = doc.getPage(0);
            page.startParse(PDFPage.e_ParsePageNormal, null, false);

            // Associate 1.txt with page dictionary.
            {
                associated_files.associateFile(page, filespec_txt);
                System.out.println(String.format("Associate a text file with page."));

                FileSpec filespec_page = associated_files.getAssociatedFile(page.getDict(), 0);
                filespec_page.exportToFile(output_path + "page.txt");
            }

            // Associate 1.txt with annotation dictionary.
            {
                RectF rect = new RectF(100, 50, 220, 100);
                Annot annot = page.addAnnot(e_Note, rect);
                annot.resetAppearanceStream();
                associated_files.associateFile(annot, filespec_txt);
                System.out.println(String.format("Associate a text file with annotation."));
                FileSpec filespec_annot = associated_files.getAssociatedFile(annot.getDict(), 0);
                filespec_annot.exportToFile(output_path + "annotation.txt");
            }

            // Create filespec with 2.jpg image.
            FileSpec filespec_jpg = new FileSpec(doc);
            filespec_jpg.setAssociteFileRelationship(e_RelationshipSource);
            filespec_jpg.setFileName("2.jpg");
            filespec_jpg.setDescription("jpeg");
            filespec_jpg.embed(input_path + "2.jpg");
            filespec_jpg.setCreationDateTime(dateTime);
            filespec_jpg.setModifiedDateTime(dateTime);
            filespec_jpg.setSubtype("application/octet-stream");

            // Associate 2.jpg with marked content.
            {
                PDFDictionary page_dict = page.getDict();
                PDFObject resource_dict = page_dict.getElement("Resources");
                if (null == resource_dict) {
                    return;
                }

                long position = page.getFirstGraphicsObjectPosition(e_TypeText);
                GraphicsObject text_object = page.getGraphicsObject(position);
                if (null != text_object) {
                    MarkedContent markcontent = text_object.getMarkedContent();
                    if (null == markcontent) {
                        return;
                    }

                    if (markcontent.getItemCount() == 0) {
                        markcontent.addItem("Associated", null);
                    }
                    associated_files.associateFile(text_object, 0, resource_dict, "textobject", filespec_jpg);
                    page.generateContent();

                    System.out.println(String.format("Associate a jpeg file with markcontent."));

                    FileSpec filespec_text_object = associated_files.getAssociatedFile(text_object, 0);
                    filespec_text_object.exportToFile(output_path + "textobject.jpg");
                }
            }

            // Associate 2.jpg with image object.
            {
                long position = page.getFirstGraphicsObjectPosition(e_TypeImage);
                ImageObject image_oject = page.getGraphicsObject(position).getImageObject();
                if (null != image_oject) {
                    associated_files.associateFile(image_oject, filespec_jpg);
                    System.out.println(String.format("Associate a jpeg file with image xobject."));
                    FileSpec filespec_image_oject = associated_files.getAssociatedFile(image_oject.getStream().getDictionary(), 0);
                    filespec_image_oject.exportToFile(output_path + "image_x_object.jpg");
                }
            }

            // Associate 2.jpg with form XObject.
            {
                long position = page.getFirstGraphicsObjectPosition(e_TypeFormXObject);

                FormXObject form_x_object = page.getGraphicsObject(position).getFormXObject();
                if (null != form_x_object) {
                    associated_files.associateFile(form_x_object, filespec_jpg);
                    System.out.println(String.format("Associate a jpeg file with form xobject."));

                    FileSpec filespec_form_x_object = associated_files.getAssociatedFile(form_x_object.getStream().getDictionary(), 0);
                    filespec_form_x_object.exportToFile(output_path + "form_x_object.jpg");
                }
            }

            // Save PDF file
            String newPdf = output_path + "associated_files.pdf";
            doc.saveAs(newPdf, e_SaveFlagNoOriginal);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Library.release();
    }
}
