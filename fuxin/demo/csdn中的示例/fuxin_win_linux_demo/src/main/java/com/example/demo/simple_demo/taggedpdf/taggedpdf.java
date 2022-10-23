//Copyright (C) 2003-2022, Foxit Software Inc..
//All Rights Reserved.
//
//http://www.foxitsoftware.com
//
//The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
//You cannot distribute any part of Foxit PDF SDK to any third party or general public,
//unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
//This file contains an example to demonstrate how to use Foxit PDF SDK to tag a PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.SignatureCallback;
import com.foxit.sdk.addon.accessibility.*;
import com.foxit.sdk.common.fxcrt.RectF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class taggedpdf {

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
     String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
	 String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
     String input_path = "../input_files/";
     String output_path = "../output_files/taggedpdf/";
     String input_file = input_path + "AboutFoxit.pdf";
     createResultFolder(output_path);

     // Initialize library
     int error_code = Library.initialize(sn, key);
     if (error_code != e_ErrSuccess) {
         System.out.println("Library Initialize Error: " + error_code);
         return;
     }
     
     {
         String output_file_path = output_path + "TaggedPdf_StartAutoTagged.pdf";

         PDFDoc pdfDoc= new PDFDoc(input_file);
         pdfDoc.load(null);
         TaggedPDF taggedpdf = new TaggedPDF(pdfDoc);
         Progressive progressive = taggedpdf.startTagDocument(null);
         int progressState = Progressive.e_ToBeContinued;
         while (Progressive.e_ToBeContinued == progressState)
           progressState = progressive.resume();

         pdfDoc.saveAs(output_file_path, 0);
     }
     {
         String output_file_path = output_path + "TaggedPdf_StartAutoTagged_SetCallback.pdf";

         PDFDoc pdfDoc= new PDFDoc(input_file);
         pdfDoc.load(null);
         TaggedPDF taggedpdf = new TaggedPDF(pdfDoc);
         TaggedPDFCallbackImpl callback = new TaggedPDFCallbackImpl();
         taggedpdf.setCallback(callback);
         Progressive progressive = taggedpdf.startTagDocument(null);
         int progressState = Progressive.e_ToBeContinued;
         while (Progressive.e_ToBeContinued == progressState)
           progressState = progressive.resume();

         pdfDoc.saveAs(output_file_path, 0);
     }
		 
     Library.release();
 }
}

class AutoTag_ReportElemRectCon {
	  public RectF rcRect;
	  public int eConfidence;
}
class ReportElemMap extends HashMap<Integer,ArrayList<AutoTag_ReportElemRectCon>> {}
class ReportResultPagesMap extends HashMap<Integer,ReportElemMap> {}
//Implementation of TaggedPDFCallback
class TaggedPDFCallbackImpl extends TaggedPDFCallback{
	private ReportResultPagesMap result_map_ = new ReportResultPagesMap();

	@Override
	public void release() {
		System.out.println("TaggedPDFCallbackImpl::Release()");
		ResetResult();
	}
	
	String GetReportCategoryString(int type) {
		String sFormat = "";
		switch (type) {
		case TaggedPDFCallback.e_ReportCategoryRegion:
		    sFormat = "Region";
		    break;
		case TaggedPDFCallback.e_ReportCategoryArtifact:
		    sFormat = "Artifact";
		    break;
		case TaggedPDFCallback.e_ReportCategoryParagraph:
		    sFormat = "Paragraph";
		    break;
		case TaggedPDFCallback.e_ReportCategoryListItem:
		    sFormat = "List Item";
		    break;
		case TaggedPDFCallback.e_ReportCategoryFigure:
		    sFormat = "Figure";
		    break;
		case TaggedPDFCallback.e_ReportCategoryTable:
		    sFormat = "Table";
		    break;
		case TaggedPDFCallback.e_ReportCategoryTableRow:
		    sFormat = "Table Row";
		    break;
		case TaggedPDFCallback.e_ReportCategoryTableHeader:
		    sFormat = "Table Header";
		    break;
		case TaggedPDFCallback.e_ReportCategoryTocItem:
		    sFormat = "Toc Item";
		    break;
		default:
		    break;
		}
		return sFormat;
	}

	String GetReportConfidenceString(int type) {
		String sFormat = "";
		switch (type) {
		case TaggedPDFCallback.e_ReportConfidenceHigh:
		    sFormat = "High";
		    break;
		case TaggedPDFCallback.e_ReportConfidenceMediumHigh:
		    sFormat = "Medium High";
		    break;
		case TaggedPDFCallback.e_ReportConfidenceMedium:
		    sFormat = "Medium";
		    break;
		case TaggedPDFCallback.e_ReportConfidenceMediumLow:
		    sFormat = "Medium Low";
		    break;
		case TaggedPDFCallback.e_ReportConfidenceLow:
		    sFormat = "Low";
		    break;
		default:
		    break;
		}
		return sFormat;
	}

	@Override
	public void report(int category, int confidence, int page_index, RectF rect) {
		System.out.println("Page Index: " + page_index + ", ReportCategory: " + GetReportCategoryString(category)
		      + ", ReportConfidence: " + GetReportConfidenceString(confidence) + ", Rect: [" + rect.getLeft()
		      + ", " +rect.getTop() + ", " + rect.getRight() + ", " + rect.getBottom() + "]");

		AutoTag_ReportElemRectCon pRectCon = new AutoTag_ReportElemRectCon();
		pRectCon.rcRect = rect;
		pRectCon.eConfidence = confidence;

		ReportElemMap pElemMap = null;
		ArrayList<AutoTag_ReportElemRectCon> pArray = null;

		if (!result_map_.containsKey(page_index)) {
		    pArray = new ArrayList<AutoTag_ReportElemRectCon>();
		    pArray.add(pRectCon);
		    pElemMap = new ReportElemMap();
		    pElemMap.put(category, pArray);
		    result_map_.put(page_index, pElemMap);
		}
		else {
		    pElemMap = result_map_.get(page_index);
		    if (pElemMap.containsKey(category)) {
		        pArray = pElemMap.get(category);
		        pArray.add(pRectCon);
		    }
		    else {
		        pArray = new ArrayList<AutoTag_ReportElemRectCon>();
		        pArray.add(pRectCon);
		        pElemMap.put(category, pArray);
		    }
		}
	}

	ReportResultPagesMap GetResult() {
		return result_map_;
	}

	void ResetResult() {
		result_map_.clear();
	}
}