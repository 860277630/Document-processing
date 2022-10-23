// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to save a PDF document as a wrapper file
// and then open the wrapper file.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.fxcrt.FileReaderCallback;
import com.foxit.sdk.common.fxcrt.FileWriterCallback;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.WrapperData;

import java.io.*;

import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagIncremental;
import static com.foxit.sdk.pdf.PDFDoc.e_WrapperPDFV2;

public class pdfwrapper {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfwrapper/";
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

    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);

        String input_file = input_path + "wrapper.pdf";
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            // Load PDF document.
            PDFDoc doc_wrapper = new PDFDoc(input_file);
            error_code = doc_wrapper.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("[Failed] Cannot load PDF document: %s.\r\nError Code: %d\r\n", input_file, error_code));
            }
            // Save the PDF document as a wrapper file.
            WrapperData wrapperData = new WrapperData(10, "Foxit", "Foxit", "www.foxitsoftware.com", "foxit");

            String output_file = output_path + "AboutFoxit_wrapper.pdf";

            PDFDoc doc_real = new PDFDoc(input_path + "AboutFoxit.pdf");
            error_code = doc_real.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("[Failed] Cannot load PDF document: %s.\r\nError Code: %d\r\n", (input_path + "SamplePDF.pdf"), error_code));

            }
            doc_real.saveAs(output_file, PDFDoc.e_SaveFlagNormal);

            // "0xFFFFFFFC" can be changed to other values defined in enumeration FSUserPermissions.
            doc_wrapper.saveAsWrapperFile(output_file, wrapperData, 0xFFFFFFFC, "");

            // Open the wrapper file.
            OpenWrapperFile(output_file);

            System.out.println("Add wrapper to PDF file.");
            
            output_file = output_path + "AboutFoxit_payloadfile.pdf";
            doc_wrapper.startSaveAsPayloadFile(output_file, input_path + "AboutFoxit.pdf", "Unknown", "no_description", 1.0f, PDFDoc.e_SaveFlagIncremental, null);

            System.out.println("Save as payload file.");

            input_file = input_path + "Microsoft IRM protection 2.pdf";
            PDFDoc doc_rmsv2 = new PDFDoc(input_file);
            error_code = doc_rmsv2.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("[Failed] Cannot load PDF document: %s.\r\nError Code: %d\r\n", input_file, error_code));
            }
            
            if (e_WrapperPDFV2 == doc_rmsv2.getWrapperType()) {
              FoxitFileWriter payloadfile = new FoxitFileWriter();
              payloadfile.LoadFile(output_path + "MicrosoftIRMServices Protected PDF.pdf");
              doc_rmsv2.startGetPayloadFile(payloadfile, null);
              System.out.println("Get RMS V2 payload file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
        return;
    }

    static void OpenWrapperFile(String file_name) throws IOException, PDFException {
        String file_info = output_path + "Wrapperinfo.txt";
        FileWriter text_doc = new FileWriter(file_info, false);

        PDFDoc doc = new PDFDoc(file_name);
        int code = doc.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("[Failed] Cannot load PDF document: %s\r\nError Message: %d\r\n", file_name, code));
            text_doc.close();
            return;
        }
        if (!doc.isWrapper()) {
            System.out.println(String.format("[Failed] %s is not a wrapper file.\r\n", file_name));
            text_doc.close();
            return;
        }
        int offset = doc.getWrapperOffset();
        text_doc.write(String.format("offset: %d\r\n", offset));
        WrapperData wrapper_data = doc.getWrapperData();
        text_doc.write(String.format("version: %d\r\n", wrapper_data.getVersion()));
        text_doc.write(String.format("type: %s\r\n", wrapper_data.getType()));
        text_doc.write(String.format("app_id: %s\r\n", wrapper_data.getApp_id()));
        text_doc.write(String.format("uri: %s\r\n", wrapper_data.getUri()));
        text_doc.write(String.format("description: %s\r\n", wrapper_data.getDescription()));
        text_doc.close();
        
        //"offset" can also indicate the end position of the document or the document size
        FileReader file_reader = new FileReader(offset);
        file_reader.LoadFile(file_name);

        PDFDoc doc_real = new PDFDoc(file_reader, false);
        code = doc_real.load(null);
        if (code != e_ErrSuccess) {
            System.out.println(String.format("[Failed] Cannot load real PDF document.\r\nError Message: %d\r\n", code));
            return;
        }

        RenderPDF2Img(doc_real);
    }


    static void RenderPDF2Img(PDFDoc doc) throws PDFException {
        int page_count = doc.getPageCount();

        for (int i = 0; i < page_count; i++) {
            PDFPage page = doc.getPage(i);

            // Parse page.
            page.startParse(e_ParsePageNormal, null, false);

            int width = (int) page.getWidth();
            int height = (int) page.getHeight();
            Matrix2D matrix = page.getDisplayMatrix(0, 0, width, height, page.getRotation());

            // Prepare a bitmap for rendering.
            Bitmap bitmap = new Bitmap(width, height, e_DIBArgb, null, 0);
            bitmap.fillRect(0xFFFFFFFF, null);

            // Render page.
            Renderer render = new Renderer(bitmap, false);
            render.startRender(page, matrix, null);
            // Add the bitmap to an image and save the image.
            Image image = new Image();
            image.addFrame(bitmap);
            String image_name = String.format("AboutFoxit_%d", i);
            image_name = output_path + image_name + "_wrapper.jpg";

            if (!image.saveAs(image_name)) {
                System.out.println(String.format("[Failed] Cannot save image file.\r\nPage index: %d", i));
            }
        }

    }

}


class FileReader extends FileReaderCallback {
    private RandomAccessFile file_ = null;
    private int filesize_ = 0;

    FileReader(int offset) {
        this.filesize_ = offset;
    }

    boolean LoadFile(String file_path) throws FileNotFoundException {
        file_ = new RandomAccessFile(file_path, "r");
        return true;
    }

    @Override
    public long getSize() {
        return this.filesize_;
    }

    @Override
    public boolean readBlock(byte[] buffer, long offset, long size) {

        try {
            file_.seek(offset);
            int read = file_.read(buffer, 0, (int) size);
            return read == size ? true : false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void release() {
        try {
            this.file_.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FoxitFileWriter extends FileWriterCallback {
    private RandomAccessFile file_ = null;

    boolean LoadFile(String file_path) throws FileNotFoundException {
        File path_file = new File(file_path);
        if(path_file.exists() == true) path_file.delete();
        file_ = new RandomAccessFile(file_path, "rw");
        return true;
    }

    @Override
    public long getSize() {
        try {
            return this.file_.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean writeBlock(byte[] buffer, long offset, long size) {
        try {
            file_.seek(offset);
            file_.write(buffer, 0, (int) size);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeBlock(byte[] buffer, long size) {
        return writeBlock(buffer, getSize(), size);
    }

    @Override
    public boolean flush() {
        return true;
    }
	
    public void release() {
        try {
            this.file_.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

