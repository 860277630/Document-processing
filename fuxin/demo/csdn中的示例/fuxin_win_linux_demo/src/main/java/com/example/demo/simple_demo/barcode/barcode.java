// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to generate barcode.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Barcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class barcode {
	static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
	static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
	static String output_path = "../output_files/barcode/";

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

	private static void Save2Image(Bitmap bitmap, String filepath) throws PDFException {
		// Add the bitmap to image and save the image.
		Image image = new Image();
		image.addFrame(bitmap);
		String save_name = output_path + filepath;
		image.saveAs(save_name);
	}
	
	// Generate barcode in different type and save each of them as an image file.
	public static void Barcode_GenerateEachType(String codeStr, int codeFormat, int unitWidth, int unitHeight, int qrLevel, String bmpName) {
		try {
			Barcode barcode = new Barcode();
			Bitmap bitmap = barcode.generateBitmap(codeStr, codeFormat, unitWidth, unitHeight, qrLevel);
			Save2Image(bitmap, bmpName);
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	// Generate all types of barcode and save each of them as an image file.
	public static void Barcode_GenerateAll() {
		// Strings used as barcode content.
		String[] codeStr = {"TEST-SHEET", "102030405060708090", "80674313", "890444000335", "9780804816632", "070429", "Unknown - change me!", "TestForBarcodeQrCode"};
		// Barcode format types
		int[] codeFormat = {Barcode.e_FormatCode39, Barcode.e_FormatCode128,
			Barcode.e_FormatEAN8, Barcode.e_FormatUPCA, 
			Barcode.e_FormatEAN13, Barcode.e_FormatITF, 
			Barcode.e_FormatPDF417, Barcode.e_FormatQRCode};

		// Image names for the saved image files.(except QR code)
		String[] bmpName = {"/code39_TEST-SHEET.bmp", "/CODE_128_102030405060708090.bmp",
			"/EAN_8_80674313.bmp", "/UPC_A_890444000335.bmp", "/EAN_13_9780804816632.bmp",
			"/ITF_070429.bmp", "/PDF_417_Unknown.bmp"};

		//Format error correction level of QR code.
		int[] qrLevel = {Barcode.e_QRCorrectionLevelLow, Barcode.e_QRCorrectionLevelMedium, 
			Barcode.e_QRCorrectionLevelQuater, Barcode.e_QRCorrectionLevelHigh};
		//Image names for the saved image files for QR code.
		String[] bmpQrName = {"/QR_CODE_TestForBarcodeQrCode_L.bmp", "/QR_CODE_TestForBarcodeQrCode_M.bmp",
			"/QR_CODE_TestForBarcodeQrCode_Q.bmp", "/QR_CODE_TestForBarcodeQrCode_H.bmp"};

		//Unit width for barcode in pixels, preferred value is 1-5 pixels.
		int unitWidth = 2;
		//Unit height for barcode in pixels, preferred value is >= 20 pixels.
		int unitHeight = 120;

		// Generate barcode for different types.
		for (int i = 0; i < 8; i++) {
			if (codeFormat[i] != Barcode.e_FormatQRCode)  // Not QR code
				Barcode_GenerateEachType(codeStr[i], codeFormat[i], unitWidth, unitHeight, 0, bmpName[i]);
			else {
				// QR code
				//Generate for each format error correction level.
				for (int j = 0; j < 4; j++)
					Barcode_GenerateEachType(codeStr[i], codeFormat[i], unitWidth, unitHeight, qrLevel[j], bmpQrName[j]);
			}
		}
	}
		
	public static void main(String[] args) throws PDFException, IOException {
		createResultFolder(output_path);
		// Initialize library.
		int error_code = Library.initialize(sn, key);
		if (error_code != e_ErrSuccess) {
			System.out.println(String.format("Library Initialize Error: %d\n", error_code));
			return;
		}

		try {
			// Generate all types of barcode
			Barcode_GenerateAll();
			System.out.println("Barcode demo.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Library.release();
		return;
	}

}
