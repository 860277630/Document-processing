
// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add fillsign to PDF document.
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.FillSign;
import com.foxit.sdk.pdf.FillSign.*;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.FillSignObject;
import com.foxit.sdk.pdf.TextFillSignObjectDataArray;
import com.foxit.sdk.pdf.TextFillSignObjectData;
import com.foxit.sdk.pdf.SignatureFillSignObject;
import com.foxit.sdk.pdf.graphics.TextState;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Bitmap;

import java.io.File;
import static java.lang.Math.max;
import static java.lang.StrictMath.ceil;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_ErrInvalidLicense;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagRemoveRedundantObjects;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNoOriginal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.common.Constants.e_Rotation0;
import static com.foxit.sdk.common.Font.e_StdIDHelvetica;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeCrossMark;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeCheckMark;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeRoundRectangle;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeLine;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeDot;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeSignature;
import static com.foxit.sdk.pdf.FillSign.e_FillSignObjectTypeInitialsSignature;
import static com.foxit.sdk.common.Bitmap.e_DIBArgb;

public class fillsign {

  private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
  private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";

  private static String output_path = "../output_files/fillsign/";
  private static String input_path = "../input_files/";

  // You can also use System.load("filename") instead. The filename argument must
  // be an absolute path name.
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
      System.out.println("Library Initialize Error: " + error_code);
      return;
    }
    createResultFolder(output_path);
    String input_file = input_path + "blank.pdf";
    System.out.println("fillsign Start ");
    try {
      PDFDoc doc = new PDFDoc(input_file);
      error_code = doc.load(null);
      if (error_code != e_ErrSuccess) {
        System.out.println("The Doc [" + input_file + " Error: " + error_code);
        return;
      }

      PDFPage page = doc.getPage(0);

      page.startParse(e_ParsePageNormal, null, false);
      FillSign sign = new FillSign(page);
      FillSignObject fillsignobject = sign.addObject(e_FillSignObjectTypeCrossMark, new PointF(0, 0), 100, 100,e_Rotation0);
      fillsignobject = sign.addObject(e_FillSignObjectTypeCheckMark, new PointF(100, 100), 50, 100, e_Rotation0);
      fillsignobject = sign.addObject(e_FillSignObjectTypeRoundRectangle, new PointF(200, 200), 100, 50, e_Rotation0);
      fillsignobject = sign.addObject(e_FillSignObjectTypeLine, new PointF(300, 300), 100, 50, e_Rotation0);
      fillsignobject = sign.addObject(e_FillSignObjectTypeDot, new PointF(400, 400), 100, 100, e_Rotation0);

      TextFillSignObjectDataArray textdataarray = new TextFillSignObjectDataArray();
      TextFillSignObjectData textdata = new TextFillSignObjectData();
      TextState textState = new TextState();
      Font font = new Font(e_StdIDHelvetica);

      textState.setFont(font);
      textState.setFont_size(20);
      textState.setOrigin_position(new PointF(500, 0));
      textState.setCharspace(1.0f);
      textdata.setText("666777888");
      textdata.setText_state(textState);
      textdataarray.add(textdata);
      FillSignObject text = sign.addTextObject(textdataarray, new PointF(400, 100), 200, 200, e_Rotation0, false);
      text.generateContent();
      fillsignobject = sign.addObject(e_FillSignObjectTypeSignature, new PointF(300, 100), 100, 100, e_Rotation0);
      Bitmap bitmap = new Bitmap(100, 100, e_DIBArgb, null, 0);
      long background_color = 0xFFFF0000;
      bitmap.fillRect(background_color, null);
      SignatureFillSignObject signature = new SignatureFillSignObject(fillsignobject);
      signature.setBitmap(bitmap);

      fillsignobject.generateContent();
      page.generateContent();

      String pdf_new = output_path + "new_fillsign.pdf";
      doc.saveAs(pdf_new, e_SaveFlagNoOriginal);
      System.out.println("fillsign Finish : All fillsign generated successfully");

    } catch (PDFException e) {
      e.printStackTrace();
      return;
    }
    Library.release();
  }
}
