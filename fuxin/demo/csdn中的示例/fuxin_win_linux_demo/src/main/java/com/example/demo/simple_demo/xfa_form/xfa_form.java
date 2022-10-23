// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to export data, import data and reset form for
// XFA document.

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.WStringArray;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.addon.xfa.*;
import com.foxit.sdk.addon.xfa.XFADoc.*;
import com.foxit.sdk.common.fxcrt.*;
import com.foxit.sdk.common.WStringArray;
import com.foxit.sdk.PDFException;
import java.io.File;

public class xfa_form {

    private static String output_path = "../output_files/xfa_form/";
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
		String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
		String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
		createResultFolder(output_path);
		// Initialize library
		int error_code = Library.initialize(sn, key);
		
		if (error_code != Constants.e_ErrSuccess) {
			System.out.printf("Library Initialize Error: %d\n", error_code);
			return;
		}

		try {
			XFAAppHandler xfa_app = new XFAAppHandler();
			Library.registerXFAAppProviderCallback(xfa_app);
			String input_file = input_path + "xfa_dynamic.pdf";
			PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
			if (error_code != e_ErrSuccess) {
				System.out.println("The Doc " + input_file + " Error: " + error_code);
				return;
			}
            XFADocHandler xfa_dochandler = new XFADocHandler();
			XFADoc xfa_doc = new XFADoc(doc, xfa_dochandler);
			xfa_doc.startLoad(null);
			String output_xml = output_path + "xfa_form.xml";
			xfa_doc.exportData(output_xml, 0);

			xfa_doc.resetForm();
			doc.saveAs(output_path + "xfa_dynamic_resetform.pdf", PDFDoc.e_SaveFlagNormal);

			xfa_doc.importData(output_xml);
			doc.saveAs(output_path + "xfa_dynamic_importdata.pdf", PDFDoc.e_SaveFlagNormal);
			
			System.out.println("Xfa test.");
		} catch (PDFException e) {
			System.out.println(e.getMessage());
			return;
		}
        Library.release();
	}

}

class XFAAppHandler extends AppProviderCallback
{
  	@Override
	public void release(){ }
	
	@Override 
	public String  getAppInfo(int  app_info){ return "Foxit SDK"; }
	
	@Override 
	public void beep(int type){}
	
	@Override 
	public int msgBox(String message, String title, int icon_type, int button_type) { return 0;}
	
	@Override 
	public String  response(String question, String title, String default_answer, boolean is_mask){	return "answer"; }
	
	@Override 
	public FileReaderCallback  downLoadUrl (String url){	return null;}
		
	@Override 
	public String postRequestURL(String url, String data, String content_type, String encode, String header) { 	return "PostRequestUrl"; }
	
	@Override 
	public boolean putRequestURL(String url, String data, String encode){ 	return true; }
	
	@Override 
	public String  loadString(int string_id){ 	return "LoadString"; }
	
	@Override 
	public WStringArray showFileDialog(String string_title, String string_filter, boolean is_openfile_dialog){ return new WStringArray(); }
	
};

class XFADocHandler extends  DocProviderCallback
{
	@Override
	public void release(){ };

	@Override
	public void invalidateRect(int page_index, RectF rect, int flag) {};

	@Override
	public void displayCaret(int page_index, boolean is_visible, RectF rect){};
	
	@Override	
	public boolean getPopupPos (int page_index, float min_popup, float max_popup, RectF rect_widget, RectF inout_rect_popup){return false;};
	
	@Override	
	public boolean popupMenu(int page_index, PointF rect_popup){return false;};
	
	@Override	
	public int getCurrentPage(XFADoc doc){return 0;};
	
	@Override	
	public void setCurrentPage(XFADoc doc, int current_page_index){};
	
	@Override	
	public String getTitle(XFADoc doc){return "";};
	
	@Override	
	public void exportData(XFADoc doc, String file_path){};

	@Override	
	public void importData(XFADoc doc, String file_path){};

	@Override	
	public  void gotoURL(XFADoc doc, String url){};
	
	@Override	
	public  void print(XFADoc doc, int start_page_index, int end_page_index, int options){}

	@Override	
	public  long getHighlightColor(XFADoc doc){return 0;}
	
	@Override	
	public boolean submitData (XFADoc doc, String target, int format, int text_encoding, String content){return false;}
	
	@Override	
	public  void setFocus(XFAWidget xfa_widget){};
	
	@Override	
	public  void pageViewEvent (int page_index, int page_view_event_type) {};

	@Override
	public void setChangeMark(XFADoc doc) {};

	@Override
	public void widgetEvent(XFAWidget xfa_widget, int widget_event_type) {};
};
