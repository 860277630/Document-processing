// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains a simple example to demonstrate how to implement a callback class ActionCallback which is used
// for dynamic stamp annotations.

import com.foxit.sdk.common.Range;
import com.foxit.sdk.common.WStringArray;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.ActionCallback;
import com.foxit.sdk.MenuItemConfig;
import com.foxit.sdk.ButtonItem;
import com.foxit.sdk.DialogDescriptionConfig;
import com.foxit.sdk.PlayerArgs;
import com.foxit.sdk.PrintParams;
import com.foxit.sdk.MediaPlayerCallback;
import com.foxit.sdk.IdentityProperties;
import com.foxit.sdk.MenuListArray;
import com.foxit.sdk.MenuItemExArray;
import com.foxit.sdk.MenuItemEx;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.pdf.Signature;
import com.foxit.sdk.pdf.actions.Destination;
import com.foxit.sdk.common.Constants;

public class MyActionCallback extends ActionCallback {

	private PDFDoc pDoc;
	public MyActionCallback(PDFDoc doc) {
		pDoc = doc;
	}

	@Override
	public void release() {

	}

	@Override
	public boolean invalidateRect(PDFDoc document, int page_index, RectF pdf_rect) {
		return false;
	}

	@Override
	public int getCurrentPage(PDFDoc document) {
		return -1;
	}
	@Override
	public void setCurrentPage(PDFDoc document, int page_index) {
		
	}

	@Override
	public int getPageRotation(PDFDoc document, int page_index) {
		return Constants.e_Rotation0;
	}

	@Override
	public boolean setPageRotation(PDFDoc document, int page_index, int rotation) {
		return false;
	}

	@Override
	public boolean executeNamedAction(PDFDoc document, String named_action){
		return false;
	}

	@Override
	public boolean setDocChangeMark(PDFDoc document, boolean change_mark) {
		return false;
	}

	@Override
	public boolean getDocChangeMark(PDFDoc document) {
		return false;
	}

	@Override
	public int getOpenedDocCount() {
		return -1;
	}
	
	@Override
	public PDFDoc getOpenedDoc(int index) {
		return pDoc;
	}
	
	@Override
	public PDFDoc getCurrentDoc() {
		return pDoc;
	}

    @Override
	public PDFDoc createBlankDoc() {
		return null;
	}

	@Override
	public boolean openDoc(String file_path, String password) {
		return false;
	}
  
	@Override
	public boolean beep(int type) {
		return false;
	}

	@Override
	public String response(String question, String title, String default_value, String label, boolean is_password) {
		return null;
	}
	
	@Override
	public String getFilePath(PDFDoc document) {
		return null;
	}
	
	@Override
	public boolean print(PDFDoc document, boolean is_ui, Range page_range, boolean is_silent, boolean is_shrunk_to_fit,
						 boolean is_printed_as_image, boolean is_reversed, boolean is_to_print_annots) {
		return false;
	}
	
	@Override
	public boolean submitForm(PDFDoc document, byte[] form_data, String url, int file_format_type) {
		return false;
	}
	
	@Override
	public boolean launchURL(String url) {
		return false;
	}
	
	@Override
	public String browseFile() {
		return null;
	}
	
	@Override
	public String browseFile(boolean is_open_dialog, String file_format, String file_filter) {
		return null;
	}
	
	@Override
	public int getLanguage() {
		return e_LanguageCHS;
	}
	
	@Override
	public int alert(String msg, String title, int type, int icon) {
		return 0;
	}
	
	@Override
	public IdentityProperties getIdentityProperties() {
		return new IdentityProperties("foxitsoftware", "simple_demo@foxitsoftware.cn", "simple demo", "Simple", "simple", "demo", "developer", "gsdk");
	}

  @Override
  public boolean setIdentityProperties(IdentityProperties identity_properties) {
    return false;
  }
	
	@Override
	public String popupMenu(MenuListArray menus, java.lang.Boolean is_selected_item) {
		return null;
	}
	
	@Override
	public MenuItemEx popupMenuEx(MenuItemExArray menus, java.lang.Boolean is_selected_item) {
		return new MenuItemEx();
	}
	
	@Override
	public String getAppInfo(int type) {
		return null;
	}
	
	@Override
	public boolean mailData(java.lang.Object data, boolean is_ui, String to, String subject, String cc, String bcc, String message) {
		return false;
	}
  
    @Override
	public int verifySignature(PDFDoc document, Signature pdf_signature) {
		return com.foxit.sdk.pdf.Signature.e_StateUnknown;
	}
	
	@Override
	public String getUntitledBookmarkName() {
		return "Untitled";
	}
	
	@Override
	public WStringArray getPrinterNameList() {
      return new WStringArray();
    }

	@Override
    public boolean addToolButton(ButtonItem button_item) {
		return false;
	}
	
	@Override
    public boolean removeToolButtom(String button_name) {
		return false;
	}
	
	@Override
    public MenuListArray getMenuItemNameList() {
		return new MenuListArray();
	}

	@Override
    public boolean addSubMenu(MenuItemConfig menu_item_config) {
		return false;
	}
	
	@Override
    public boolean addMenuItem(MenuItemConfig menu_item_config, boolean is_prepend) {
		return false;
	}
	
	@Override
    public boolean showDialog(DialogDescriptionConfig dlg_config) {
		return true;
	}
	
	@Override
    public boolean getFullScreen() {
		return false;
	}
	
	@Override
    public void setFullScreen(boolean is_full_screen) {
		
	}

    @Override
    public void onFieldValueChanged(String field_name, int type, WStringArray value_before_changed, WStringArray value_after_changed) {

    }
	
	@Override
    public MediaPlayerCallback openMediaPlayer(PlayerArgs player_args) {
		return null;
	}
	
	@Override
	public void setCurrentPage(PDFDoc pdf_doc, Destination dest) {
		
	}
	
	@Override
	public void closeDoc(PDFDoc pdf_doc, boolean is_prompt_to_save) {
		
	}
	
	@Override
	public String getAttachmentsFilePath(PDFDoc pdf_doc, String name) {
		return "";
	}
	
	@Override
	public String getExtractedEmbeddedFilePath(PDFDoc pdf_doc, String name) {
		return "";
	}
	
	@Override
	public int getLayoutMode() {
		return 0;
	}
	
	@Override
	public PointF getMousePosition() {
		return new PointF();
	}
	
	@Override
	public float getPageScale() {
		return 0;
	}
	
	@Override
	public RectF getPageWindowRect() {
		return new RectF();
	}
	
	@Override
	public int getPageZoomMode() {
		return 0;
	}
	
	@Override
	public String getTemporaryDirectory() {
		return "";
	}
	
	@Override
	public String getTemporaryFileName(PDFDoc pdf_doc, String file_name) {
		return "";
	}
	
	@Override
	public boolean isLocalFile(PDFDoc pdf_doc) {
		return false;
	}
	
	@Override
	public int mailDoc(PDFDoc pdf_doc, String to_address, String cc_address, String bcc_address, String subject, String message, boolean is_ui) {
		return 0;
	}
	
	@Override
	public boolean print(PDFDoc pdf_doc, PrintParams print_params) {
		return false;
	}
	
	@Override
	public void scroll(PointF point) {
	}
	
	@Override
	public void selectPageNthWord(int page_index, int word_index, boolean is_show_selection) {
	}
	
	@Override
	public void setPageScale(int zoom_mode, Destination dest) {
	}
	
	@Override
	public void setLayoutMode(int layout_mode, boolean is_cover_mode) {
	}
	
	@Override
	public void updateLogicalLabel() {
	}
}
