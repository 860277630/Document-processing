// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to operate PDF objects directly.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.objects.PDFArray;
import com.foxit.sdk.pdf.objects.PDFDictionary;
import com.foxit.sdk.pdf.objects.PDFObject;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;

public class pdfobjects {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfobjects/";
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

    // This function is to do some operation to PDF object.
    static void ObjectOperation(PDFDoc document) throws PDFException {
        PDFObject boolean_object = PDFObject.createFromBoolean(true);
        int boolean_object_number = document.addIndirectObject(boolean_object);

        System.out.println(String.format("Object number of new boolean object: %d", boolean_object_number));
        boolean boolean_object_value = boolean_object.getBoolean();
        System.out.println(String.format("\tValue of new boolean object: %s", boolean_object_value ? "true" : "false"));

        PDFObject float_object = PDFObject.createFromFloat(0.1f);
        int float_object_number = document.addIndirectObject(float_object);
        System.out.println(String.format("Object number of new number object (as float): %d", float_object_number));
        float float_object_value = float_object.getFloat();
        System.out.println(String.format("\tValue of new number object (as float): %f", float_object_value));

        PDFObject integer_object = PDFObject.createFromInteger(1);
        int integer_object_number = document.addIndirectObject(integer_object);
        System.out.println(String.format("Object number of new number object (as integer): %d ", integer_object_number));
        int integer_object_value = integer_object.getInteger();
        System.out.println(String.format("\tValue of new number object (as integer): %d", integer_object_value));

        PDFObject string_object = PDFObject.createFromString("foxit");
        int string_object_number = document.addIndirectObject(string_object);
        System.out.println(String.format("Object number of new string object: %d", string_object_number));
        String string_object_value = string_object.getWideString();
        System.out.println(String.format("\tValue of new string object: %s", string_object_value));

        PDFObject name_object = PDFObject.createFromName("sdk");
        int name_object_number = document.addIndirectObject(name_object);
        System.out.println(String.format("Object number of new name object: %d ", name_object_number));
        String name_object_value = name_object.getName();
        System.out.println(String.format("\tValue of new name object: %s", name_object_value));

        DateTime date_time = new DateTime(2017, 9, 27, 19, 36, 6, 0, (short) 8, 0);
        PDFObject datetime_object = PDFObject.createFromDateTime(date_time);
        int datetime_object_number = document.addIndirectObject(datetime_object);
        System.out.println(String.format("Object number of new string object (as date-time):  %d", datetime_object_number));
        String datetime_object_value = datetime_object.getWideString();
        System.out.println(String.format("\tValue of new string object (as date-time): %s", datetime_object_value));

        PDFArray array = PDFArray.create();
        PDFObject boolean_object_reference = PDFObject.createReference(document, boolean_object_number);
        int boolean_object_reference_number = boolean_object_reference.getObjNum();
        System.out.println(String.format("Object number of a new reference object to a boolean object: %d", boolean_object_reference_number));
        PDFObject float_object_reference = PDFObject.createReference(document, float_object_number);
        PDFObject integer_object_reference = PDFObject.createReference(document, integer_object_number);
        PDFObject integer_object_direct = integer_object_reference.getDirectObject();
        System.out.println(String.format("Object number of the direct number object: %d", integer_object_direct.getObjNum()));
        PDFObject string_object_reference = PDFObject.createReference(document, string_object_number);
        PDFObject name_object_reference = PDFObject.createReference(document, name_object_number);
        PDFObject datetime_object_reference = PDFObject.createReference(document, datetime_object_number);

        array.addElement(boolean_object_reference);
        array.addElement(float_object_reference);
        array.addElement(integer_object_reference);
        array.addElement(string_object_reference);
        array.addElement(name_object_reference);
        array.addElement(datetime_object_reference);
        array.addElement(boolean_object.cloneObject());

        int array_object_number = document.addIndirectObject(array);
        System.out.println(String.format("Object number of array object: %d", array_object_number));
    }

    // This function is to remove some properties from catalog dictionary.
    static void RemoveCatalogKey(PDFDoc document) throws PDFException {
        if (document.isEmpty()) {
            return;
        }

        PDFDictionary catalog = document.getCatalog();
        if (null == catalog) {
            return;
        }

        String[] key_strings = {"Type", "Boolean", "Name", "String", "Array", "Dict"};
        int count = key_strings.length;
        for (int i = 0; i < count; i++) {
            if (catalog.hasKey(key_strings[i])) {
                catalog.removeAt(key_strings[i]);
            }
        }
    }

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        String input_file = input_path + "pdfobjects.pdf";
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            PDFDoc doc = new PDFDoc(input_file);
            doc.startLoad(null, false, null);

            // Do some operation about PDF object.
            ObjectOperation(doc);
            String save_pdf_patth = output_path + "pdfobjects_addnewobjects.pdf";
            doc.startSaveAs(save_pdf_patth, e_SaveFlagNoOriginal, null);

            // To remove some properties from catalog dictionary.
            RemoveCatalogKey(doc);
            save_pdf_patth = output_path + "pdfobjects_removekeyfromcatalog.pdf";
            doc.startSaveAs(save_pdf_patth, e_SaveFlagNoOriginal, null);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Library.release();
    }
}
