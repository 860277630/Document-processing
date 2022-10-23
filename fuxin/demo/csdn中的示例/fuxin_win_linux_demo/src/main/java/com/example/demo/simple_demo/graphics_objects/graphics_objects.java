// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to create a PDF document,
// insert the text path and image objects into the created PDF document, copy shading objects,
// and save the file with inserted graphics objects.

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
import com.foxit.sdk.pdf.GraphicsObjects;

import java.io.File;
import java.io.FileWriter;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_FillModeAlternate;
import static com.foxit.sdk.common.Font.*;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.*;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFill;
import static com.foxit.sdk.pdf.graphics.TextState.e_ModeFillStrokeClip;

public class graphics_objects {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/graphics_objects/";
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

    static void AddTextObjects(PDFPage page) throws PDFException {
        long position = page.getLastGraphicsObjectPosition(e_TypeText);
        TextObject text_object = TextObject.create();

        text_object.setFillColor(0xFFFF7F00);

        // Prepare text state
        TextState state = new TextState();
        state.setFont_size(80.0f);
        state.setFont(new Font("Simsun", e_StylesSmallCap, e_CharsetGB2312, 0));
        state.setTextmode(e_ModeFill);
        text_object.setTextState(page, state, false, 750);

        // Set text.
        text_object.setText("Foxit Software");
        long last_position = page.insertGraphicsObject(position, text_object);

        RectF rect = text_object.getRect();
        float offset_x = (page.getWidth() - (rect.getRight() - rect.getLeft())) / 2;
        float offset_y = page.getHeight() * 0.8f - (rect.getTop() - rect.getBottom()) / 2;
        text_object.transform(new Matrix2D(1, 0, 0, 1, offset_x, offset_y), false);

        // Generate content
        page.generateContent();

        // Clone a text object from the old text object.
        text_object = text_object.clone().getTextObject();
        state.setFont(new Font(e_StdIDTimes));
        state.setFont_size(48.0f);
        state.setTextmode(e_ModeFillStrokeClip);

        text_object.setTextState(page, state, true, 750);
        text_object.setText("www.foxitsoftware.com");

        text_object.setFillColor(0xFFAAAAAA);
        text_object.setStrokeColor(0xFFF68C21);
        page.insertGraphicsObject(last_position, text_object);

        rect = text_object.getRect();
        offset_x = (page.getWidth() - (rect.getRight() - rect.getLeft())) / 2;
        offset_y = page.getHeight() * 0.618f - (rect.getTop() - rect.getBottom()) / 2;
        text_object.transform(new Matrix2D(1, 0, 0, 1, offset_x, offset_y), false);

        // Generate content again after transformation.
        page.generateContent();

        //Show how to get the characters' information of text object.
        try {
          int text_object_charcount = text_object.getCharCount();
          FileWriter file_writer = new FileWriter(output_path + "text_objects_info.txt", false);
          file_writer.write("The new text object has " + text_object_charcount + "characters.\r\n");

          for (int i = 0; i < text_object_charcount; i++) {
            //The character's position.
            PointF char_pos = text_object.getCharPos(i);
            //The character's width.
            float width = text_object.getCharWidthByIndex(i);
            //The character's height.
            float height = text_object.getCharHeightByIndex(i);

            file_writer.write("The position of the " + i + " characters is (" + char_pos.getX() + "," + char_pos.getY() + ").The width and height of the characters is (" + width + "," + height + ").\r\n");
          }
          file_writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void AddPieces(PDFPage page, GraphicsObject orignal_pieces, RectF dst_rect) throws PDFException {
        long position = page.getFirstGraphicsObjectPosition(e_TypeAll);
        GraphicsObject pieces = orignal_pieces.clone();

        RectF piece_rect = pieces.getRect();

        // Calculates the transformation matrix between dst_rect and  piece_rect.
        float a = (dst_rect.getRight() - dst_rect.getLeft()) / (piece_rect.getRight() - piece_rect.getLeft());
        float d = (dst_rect.getTop() - dst_rect.getBottom()) / (piece_rect.getTop() - piece_rect.getBottom());
        float e = dst_rect.getLeft() - piece_rect.getLeft() * a;
        float f = dst_rect.getTop() - piece_rect.getTop() * d;

        // Transform rect.
        pieces.transform(new Matrix2D(a, 0, 0, d, e, f), false);
        page.insertGraphicsObject(position, pieces);

        page.generateContent();
    }

    public static void AddPathObjects(PDFPage page, GraphicsObject black_pieces, GraphicsObject white_pieces) throws PDFException {
        long position = page.getLastGraphicsObjectPosition(e_TypePath);
        PathObject path_object = PathObject.create();
        Path path = new Path();
        float page_width = page.getWidth();
        float page_height = page.getHeight();

        float width = Math.min(page_width, page_height) / 20.0f;
        float start_x = (page_width - width * 18.0f) / 2.0f;
        float start_y = (page_height - width * 18.0f) / 2.0f;

        // Draw a chess board
        for (int i = 0; i < 19; i++) {
            float x1 = start_x;
            float y1 = i * width + start_y;

            float x2 = start_x + 18 * width;
            path.moveTo(new PointF(x1, y1));
            path.lineTo(new PointF(x2, y1));

            x1 = i * width + start_x;
            y1 = start_y;

            float y2 = 18 * width + start_y;
            path.moveTo(new PointF(x1, y1));
            path.lineTo(new PointF(x1, y2));
        }

        int[] star = {3, 9, 15};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RectF rect = new RectF(start_x + star[i] * width - width / 12, start_y + star[j] * width - width / 12,
                        start_x + star[i] * width + width / 12, start_y + star[j] * width + width / 12);
                path.appendEllipse(rect);
            }
        }
        
        path_object.setPathData(path);
        path_object.setFillColor(0xFF000000);
        path_object.setFillMode(e_FillModeAlternate);
        path_object.setStrokeState(true);
        path_object.setStrokeColor(0xFF000000);

        page.insertGraphicsObject(position, path_object);
        page.generateContent();

        // Draw pieces
        PointF[][] pieces_vector = {{new PointF(3, 3), new PointF(3, 7), new PointF(3, 15), new PointF(13, 2),
                new PointF(13, 16), new PointF(13, 17), new PointF(15, 16), new PointF(16, 16)},
                {new PointF(11, 16), new PointF(12, 14), new PointF(14, 4), new PointF(14, 15),
                        new PointF(15, 3), new PointF(15, 9), new PointF(15, 15), new PointF(16, 15)}};
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < pieces_vector[k].length; i++) {
                int x = (int) pieces_vector[k][i].getX();
                int y = (int) pieces_vector[k][i].getY();
                AddPieces(page, (k % 2 != 0) ? white_pieces : black_pieces,
                        new RectF(start_x + x * width - width / 2.f, start_y + y * width - width / 2.f,
                                start_x + x * width + width / 2.f, start_y + y * width + width / 2.f));
            }
        }
    }

    public static void AddImageObjects(PDFPage page, String image_file) throws PDFException {
        long position = page.getLastGraphicsObjectPosition(e_TypeImage);
        Image image = new Image(image_file);
        ImageObject image_object = ImageObject.create(page.getDocument());
        image_object.setImage(image, 0);

        float width = image.getWidth();
        float height = image.getHeight();

        float page_width = page.getWidth();
        float page_height = page.getHeight();

        // Please notice the matrix value.
        image_object.setMatrix(new Matrix2D(width, 0, 0, height, (page_width - width) / 2.0f, (page_height - height) / 2.0f));

        page.insertGraphicsObject(position, image_object);
        page.generateContent();
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
            String input_file = input_path + "graphics_objects.pdf";
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);

            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                return;
            }
            // Get original shading objects from PDF page.
            PDFPage original_page = doc.getPage(0);
            original_page.startParse(e_ParsePageNormal, null, false);
            long position = original_page.getFirstGraphicsObjectPosition(e_TypeShading);
            if (position == 0) {
                return;
            }
            GraphicsObject black_pieces = original_page.getGraphicsObject(position);

            position = original_page.getNextGraphicsObjectPosition(position, e_TypeShading);
            GraphicsObject white_pieces = original_page.getGraphicsObject(position);

            // Add a new PDF page and insert text objects.
            PDFPage page = doc.insertPage(0, PDFPage.e_SizeLetter);
            AddTextObjects(page);

            // Add a new PDF page and insert image objects.
            page = doc.insertPage(1, PDFPage.e_SizeLetter);
            String image_file = input_path + "sdk.png";
            AddImageObjects(page, image_file);

            // Add a new PDF page and insert path objects, and copy shading objects.
            page = doc.insertPage(2, PDFPage.e_SizeLetter);
            AddPathObjects(page, black_pieces, white_pieces);

            String output_file = output_path + "graphics_objects.pdf";
            doc.saveAs(output_file, e_SaveFlagNormal);
            System.out.println("Add graphics objects to PDF file.");
         
            // Remove text objects from PDF page.
            page = doc.getPage(0);
            page.startParse(PDFPage.e_ParsePageNormal, null, false);
            position = page.getFirstGraphicsObjectPosition(GraphicsObject.e_TypeAll);
            while(position > 0) {
        	    GraphicsObject obj = page.getGraphicsObject(position);
        	    if(obj.getType() == GraphicsObject.e_TypeText) {
        		    TextObject textobj = obj.getTextObject();
        		    String s = textobj.getText();
        		    if(s.length() > 0) {
        			    page.removeGraphicsObject(textobj);
        			    position = page.getFirstGraphicsObjectPosition(GraphicsObject.e_TypeAll);
        			    continue;
        		    }
        	    }
        	    else if(obj.getType() == GraphicsObject.e_TypeFormXObject) {
        		    FormXObject formxobj = obj.getFormXObject();
        		    GraphicsObjects graphicsObjects_form = formxobj.getGraphicsObjects();
        		    long pos = graphicsObjects_form.getFirstGraphicsObjectPosition(GraphicsObject.e_TypeText);
        		    if(pos > 0) {
        			    TextObject textobj = page.getGraphicsObject(pos).getTextObject();
        			    if(textobj.getType() == GraphicsObject.e_TypeText) {
        				    String s = textobj.getText();
        				    if(s.length() > 0) {
        					    page.removeGraphicsObject(textobj);
        					    position = page.getFirstGraphicsObjectPosition(GraphicsObject.e_TypeAll);
        					    continue;
        				    }
        			    }
        		    }
        	    }
        	    position = page.getFirstGraphicsObjectPosition(GraphicsObject.e_TypeAll);
            }
            page.generateContent();
         
            output_file = output_path + "graphics_objects_remove_text_objects.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNormal);
            System.out.println("Remove text objects form PDF file.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
    }
}
