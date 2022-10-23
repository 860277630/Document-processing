// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to do bitmap transformation,
// such as flipping, stretching, and so on.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.RectI;

import java.io.File;

import static com.foxit.sdk.common.Bitmap.e_Downsample;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class bitmap_transform {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/bitmap_transform/";
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

    private static void SaveBitmap(Bitmap bitmap, String file_path) throws PDFException {
        Image image = new Image();
        image.addFrame(bitmap);
        image.saveAs(file_path);
    }

    public static void main(String[] args) throws PDFException {
        String input_file = input_path + "Foxit.bmp";
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        try {
            Image image = new Image(input_file);
            Bitmap bitmap = image.getFrameBitmap(0);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // Stretch bitmap.
            Bitmap stretch_bitmap = bitmap.stretchTo(width / 2, height * 2, e_Downsample, null);
            String output_file = output_path + "stetch_bitmap_Foxit.bmp";
            SaveBitmap(stretch_bitmap, output_file);

            // Transform bitmap.
            int left = 0;
            int top = 0;
            Matrix2D matrix = new Matrix2D(width * 2.0f, 0.f, 0.f, height / 2.0f, 0.f, 0.f);
            Bitmap transform_bitmap = bitmap.transformTo(matrix, e_Downsample, left, top, null);
            output_file = output_path + "transform_bitmap_Foxit.bmp";
            SaveBitmap(transform_bitmap, output_file);

            // Vertical and horizontal flip bitmap.
            Bitmap flip_bitmap = bitmap.flip(true, true);
            output_file = output_path + "flip_horz_vert_Foxit.bmp";
            SaveBitmap(flip_bitmap, output_file);

            // Swap the X and Y axis, and then vertical and horizontal flip bitmap.
            Bitmap swap_xy_bitmap = bitmap.swapXY(true, true, null);
            output_file = output_path + "swap_xy_flip_horz_vert_Foxit.bmp";
            SaveBitmap(swap_xy_bitmap, output_file);

            // Calculate the bounding box according to the given background color.
            RectI rect = bitmap.calculateBBoxByColor(0xFFFDFDFD);
            Bitmap clone_bitmap;
            if (rect.height() > 0 && rect.width() > 0) {
                clone_bitmap = bitmap.clone(rect);
                output_file = output_path + "calculate_bbox_by_color_0xFFFDFDFD_foxit.bmp";
                SaveBitmap(clone_bitmap, output_file);
            } else {
                System.out.println("[Warning] The result of bounding box (according to the given background color) is an empty rectangle.\r\n");
            }

            // Detect the bounding box of content according to the given color difference between content and margin.
            rect = bitmap.detectBBoxByColorDiffer(20, 64);
            if (rect.height() > 0 && rect.width() > 0) {
                clone_bitmap = bitmap.clone(rect);
                output_file = output_path + "detect_bbox_by color_differ_20_64_Foxit.bmp";
                SaveBitmap(clone_bitmap, output_file);
            } else {
                System.out.println("[Warning] The result of bounding box (according to the given color difference between content and margin)");
                System.out.println(" is an empty rectangle.\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        Library.release();
        return;
    }
}
