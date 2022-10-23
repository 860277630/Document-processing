// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to implement a callback class IconProviderCallback which is used
// for the appearance of stamp annotations.

import java.util.HashMap;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.annots.IconProviderCallback;
import com.foxit.sdk.pdf.annots.ShadingColor;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class MyIconProvider extends IconProviderCallback {
	private HashMap<String, PDFDoc> pdf_doc_map_;
	private String file_folder_;
	private boolean use_dynamic_stamp_;

	public MyIconProvider(String file_folder) {
		file_folder_ = file_folder;
		pdf_doc_map_ = new HashMap<String, PDFDoc>();
		use_dynamic_stamp_ = false;
	}

	@Override
	public void release() {

	}

	// If one icon provider offers different style icon for one icon name of a kind of annotaion,
    // please use different provider ID or version in order to distinguish different style for Foxit PDF SDK.
    // Otherwise, only the first style icon for the same icon name of same kind of annotation will have effect.
	@Override
	public String getProviderID() {
		if (use_dynamic_stamp_) {
			return "Simple Demo Dynamic IconProvider";
		}  else {
			return "Simple Demo IconProvider";
		}
	}

	@Override
	public String getProviderVersion() {
		return "1.0.0";
	}
	@Override
	public boolean hasIcon(int annot_type, String icon_name) {
		String path;
		if (use_dynamic_stamp_) {
			path = file_folder_ + "/DynamicStamps/" + icon_name + ".pdf";
		} else {
			path = file_folder_ + "/StaticStamps/" + icon_name + ".pdf";
		}
		PDFDoc doc = (PDFDoc)pdf_doc_map_.get(path);
		if (doc != null && !doc.isEmpty()) {
			try {
				if (doc.getPageCount()<1)
					return false;
			} catch (PDFException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		try {
			doc = new PDFDoc(path);
			int error_code = doc.load(null);
			if (e_ErrSuccess != error_code) {
				doc = null;
			} else {
				pdf_doc_map_.put(path, doc);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return !doc.isEmpty();
	}

	@Override
	public boolean canChangeColor(int annot_type, String icon_name) {
		return false;
	}

	@Override
	public PDFPage getIcon(int annot_type, String icon_name, long color) {
		String path;
		if (use_dynamic_stamp_) {
			path = file_folder_ + "/DynamicStamps/" + icon_name + ".pdf";
		} else {
			path = file_folder_ + "/StaticStamps/" + icon_name + ".pdf";
		}
		PDFDoc doc = (PDFDoc)pdf_doc_map_.get(path);
		try {
			if (doc == null || doc.isEmpty() || doc.getPageCount() < 1)
				return null;
		} catch (PDFException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return doc.getPage(0);
		} catch (PDFException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean getShadingColor(int annot_type, String icon_name, long referenced_color, int shading_index,
			ShadingColor shading_color) {
		return false;
	}

	@Override
	public float getDisplayWidth(int annot_type, String icon_name) {
		return 0.0f;
	}

	@Override
	public float getDisplayHeight(int annot_type, String icon_name) {
		return 0.0f;
	}

	public void setUseDynamicStamp(boolean use_dynamic_stamp) {
		use_dynamic_stamp_ = use_dynamic_stamp;
	}
}
