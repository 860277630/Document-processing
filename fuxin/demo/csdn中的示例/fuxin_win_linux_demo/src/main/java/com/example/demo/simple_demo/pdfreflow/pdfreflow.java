// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to reflow PDF pages.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.RectI;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.ReflowPage;

import java.io.File;

import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_Rotation0;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.ReflowPage.e_Normal;
import static com.foxit.sdk.pdf.ReflowPage.e_WithImage;
import static java.lang.Math.max;
import static java.lang.StrictMath.ceil;

public class pdfreflow {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfreflow/";
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

    public static void SaveBitmap(Bitmap bitmap, int index, String file_name) throws PDFException {
        RectF margin = new RectF(50, 30, 30, 30);
        PointF size = new PointF(480, 800);
        RectI rect = new RectI(0, 0, (int) size.getX(), (int) margin.getTop());
        bitmap.fillRect(0xFFFFFFFF, rect);
        rect = new RectI(0, (int) size.getY() - (int) margin.getBottom(), (int) size.getX(), (int) size.getY());
        bitmap.fillRect(0xFFFFFFFF, rect);
        Image image = new Image();
        image.addFrame(bitmap);

        String save_path;
        String sIndex;
        sIndex = "" + index;
        save_path = output_path + "reflow" + file_name + sIndex + ".bmp";
        image.saveAs(save_path);
    }

    // The change for the size of the picture depends on the size of the content of reflow page.
    public static void ReflowSingle(PDFDoc doc) throws PDFException {
        RectF margin = new RectF(50, 30, 30, 30);
        PointF size = new PointF(480, 800);
        int nCount = doc.getPageCount();
        for (int i = 0; i < nCount; i++) {
            PDFPage page = doc.getPage(i);
            // Parse PDF page.
            page.startParse(e_ParsePageNormal, null, false);

            ReflowPage reflow_page = new ReflowPage(page);
            // Set some arguments used for parsing the relfow page.
            reflow_page.setLineSpace(0);
            reflow_page.setScreenMargin((int) margin.getLeft(), (int) margin.getTop(), (int) margin.getRight(), (int) margin.getBottom());
            reflow_page.setScreenSize(size.getX(), size.getY());
            reflow_page.setZoom(100);
            reflow_page.setParseFlags(e_Normal);

            // Parse reflow page.
            reflow_page.startParse(null);

            // Get actual size of content of reflow page. The content size does not contain the margin.
            float content_width = reflow_page.getContentWidth();
            float content_height = reflow_page.getContentHeight();

            // Create a bitmap for rendering the reflow page. The bitmap size contains the margin.
            Bitmap bitmap = new Bitmap((int) (content_width + margin.getLeft() + margin.getRight()),
                    (int) (content_height + margin.getTop() + margin.getBottom()), e_DIBArgb, null, 0);
            bitmap.fillRect(0xFFFFFFFF, null);

            // Render reflow page.
            Renderer renderer = new Renderer(bitmap, false);
            Matrix2D matrix = reflow_page.getDisplayMatrix(0, 0,(int)content_width,(int)content_height,e_Rotation0);
            renderer.startRenderReflowPage(reflow_page, matrix, null);
            String file_name = "_single_";
            SaveBitmap(bitmap, i, file_name);
        }

    }

    // Fixed bitmap size, just to simulate split screen situation.
    public static void ReflowContinuous(PDFDoc doc) throws PDFException {
        RectF margin = new RectF(50, 30, 30, 30);
        PointF size = new PointF(480, 800);
        float display_height = size.getY() - margin.getTop() - margin.getBottom();
        Bitmap bitmap = new Bitmap((int) (size.getX()), (int) (size.getY()), e_DIBArgb, null, 0);
        float offset_y = 0;
        int nCount = doc.getPageCount();

        int bitmap_index = 0;

        for (int i = 0; i < nCount; i++) {
            PDFPage page = doc.getPage(i);

            // Parse PDF page.
            page.startParse(e_ParsePageNormal, null, false);

            ReflowPage reflow_page = new ReflowPage(page);
            // Set some arguments used for parsing the relfow page.
            reflow_page.setLineSpace(0);
            reflow_page.setScreenMargin((int) (margin.getLeft()), (int) (margin.getTop()),
                    (int) (margin.getRight()), (int) (margin.getBottom()));
            reflow_page.setScreenSize(size.getX(), size.getY());
            reflow_page.setZoom(100);
            reflow_page.setParseFlags(e_WithImage);

            reflow_page.setTopSpace(offset_y);

            // Parse reflow page.
            reflow_page.startParse(null);

            // Get actual size of content of reflow page.
            // The content size does not contain the margin but contains the top space.
            float content_height = reflow_page.getContentHeight();
            float content_width = reflow_page.getContentWidth();
            // Render reflow page.
            Renderer renderer = new Renderer(bitmap, false);
            Matrix2D matrix = reflow_page.getDisplayMatrix(0, 0,(int)content_width,(int)content_height,e_Rotation0);
            renderer.startRenderReflowPage(reflow_page, matrix, null);

            int rate_need_screen_count = (int) (ceil(max(content_height - display_height, 0.0f) / display_height));
            if (rate_need_screen_count > 0) {
                // Before do next rendering, save current bitmap first.
                String file_name = "_continuous_";
                SaveBitmap(bitmap, bitmap_index++, file_name);

                float has_display_height = display_height;
                for (int j = 0; j < rate_need_screen_count; j++) {
                    // Clear the bitmap and used it to do next rendering.
                    bitmap.fillRect(0xFFFFFFFF, null);
                    // Render reflow page.
                    renderer = new Renderer(bitmap, false);
                    matrix = reflow_page.getDisplayMatrix(0, -has_display_height,(int)content_width,(int)content_height,e_Rotation0);
                    renderer.startRenderReflowPage(reflow_page, matrix, null);
                    if (j != rate_need_screen_count - 1) {
                        has_display_height += display_height;
                        SaveBitmap(bitmap, bitmap_index++, file_name);
                    } else {
                        offset_y = content_height - rate_need_screen_count * display_height;
                    }
                }
            } else {
                offset_y = content_height;
            }
        }
        String file_name = "_continuous_";
        SaveBitmap(bitmap, bitmap_index++, file_name);

    }

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }
            ReflowContinuous(doc);
            ReflowSingle(doc);
            System.out.println("Reflow test.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Library.release();
    }
}
