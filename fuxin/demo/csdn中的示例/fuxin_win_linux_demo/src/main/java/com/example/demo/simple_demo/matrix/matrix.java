// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file is a demo to demonstrate how to use matrix to translate, scale... objects.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Path;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.graphics.*;

import java.io.File;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_FillModeAlternate;
import static com.foxit.sdk.common.Font.*;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.*;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFill;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFillStrokeClip;

public class matrix {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/matrix/";
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
    	String input_file=input_path + "SamplePDF.pdf";
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
                if (error_code != e_ErrSuccess)
                {
                    System.out.println("The PDFDoc " + input_file + " Error: " + error_code);
                    Library.release();
                    return;
                }
                	PDFPage page = doc.getPage(0);
                    page.startParse((int)PDFPage.e_ParsePageNormal, null, false);
                    long pos = page.getFirstGraphicsObjectPosition(com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeAll);
                    int flag = 0;
                    while (pos > 0)
                    {
                        if (flag == (2 | 4))
                            break;
                        com.foxit.sdk.pdf.graphics.GraphicsObject obj = page.getGraphicsObject(pos);
                        pos = page.getNextGraphicsObjectPosition(pos, com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeAll);
                        int type = obj.getType();
                        float start_x = 0;
                        if (type == com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeText && ((flag & 2) <= 0))
                        {
                            com.foxit.sdk.pdf.graphics.TextObject text_obj = obj.getTextObject();
                            String text = text_obj.getText();
                            if (text.compareTo("Foxit Software Incorporated") != 0)
                            {
                                continue;
                            }
                            start_x = 400.0f;
                            flag |= 2;
                        }
                        else if (type == com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeImage && ((flag & 4) <= 0))
                        {
                            start_x = 150.0f;
                            flag |= 4;
                        }
                        else
                        {
                            continue;
                        }
                        // Now we have found the two page objects, transform them.
                        com.foxit.sdk.pdf.graphics.GraphicsObject clone_obj1 = obj.clone();
                        Matrix2D matrix = clone_obj1.getMatrix();
                        
                        // Translate the matrix in vertical direction.
                        matrix.translate(0, 150, false);
                        clone_obj1.setMatrix(matrix);
                        page.insertGraphicsObject(0, clone_obj1);

                        com.foxit.sdk.pdf.graphics.GraphicsObject clone_obj2 = obj.clone();
                        matrix = clone_obj2.getMatrix();
                        float d = matrix.getD();
                        float e = matrix.getE();
                        float f = matrix.getF();

                        // Translate it to the original point(0,0) first
                        matrix.translate(-e, -f, false);
                        // Rotate the matrix 90 degree in anticlockwise direction.
                        matrix.rotate(1.57f, false);
                        // Page rotation should be considered, assume that the current page rotation is 0.
                        float distance = d;
                        if (type == com.foxit.sdk.pdf.graphics.GraphicsObject.e_TypeText)
                        {
                            RectF obj_rect = obj.getRect();
                            distance = obj_rect.getTop() - obj_rect.getBottom();
                        }
                        // Translate it to the specific coordinate(fStartX,400)
                        matrix.translate(distance + start_x, 400, false);
                        clone_obj2.setMatrix(matrix);
                        page.insertGraphicsObject(0, clone_obj2);

                        com.foxit.sdk.pdf.graphics.GraphicsObject clone_obj3 = obj.clone();
                        matrix = clone_obj3.getMatrix();
                        // Translate it to the original point(0,0) first
                        matrix.translate(-e, -f, false);
                        // Magnify the matrix 1.5 times in both horizontal and vertical directions.
                        matrix.scale(1.5f, 1.5f, false);
                        // Translate it to the specific coordinate(fStartX,600)
                        matrix.translate(start_x, 600.0f, false);
                        clone_obj3.setMatrix(matrix);
                        page.insertGraphicsObject(0, clone_obj3);

                        com.foxit.sdk.pdf.graphics.GraphicsObject clone_obj4 = obj.clone();
                        matrix = clone_obj4.getMatrix();
                        // Translate it to the original point(0,0) first
                        matrix.translate(-e, -f, false);
                        // Skews the x axis by an angle 0.5 and the y axis by an angle 0.5.
                        matrix.shear(0.5f, 0.5f, false);
                        // Translate it to the specific coordinate(fStartX,800)
                        matrix.translate(start_x, 800.0f, false);
                        clone_obj4.setMatrix(matrix);
                        page.insertGraphicsObject(0, clone_obj4);
                        // Generate the page content
                        page.generateContent();
                }
                // Save the pdf document
                doc.saveAs("../output_files/matrix/MatrixTransformResult.pdf",0);
                System.out.println("Matrix demo.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Library.release();
        return;
    }
}
