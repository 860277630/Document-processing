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

import com.foxit.sdk.addon.compliance.ProgressCallback;

import com.foxit.sdk.common.Constants;

import java.io.FileWriter;
import java.io.IOException;

public class MyComplianceProgressCallback extends ProgressCallback {

	private FileWriter text_doc_ = null;
	
	public MyComplianceProgressCallback(String output_txt_file_path) {
		try {
			text_doc_ = new FileWriter(output_txt_file_path, false);
		} catch(IOException e) {
			System.out.println(String.format("[FAILED] Failed to create a txt file. TXT path:%s\r\n", output_txt_file_path));
			e.printStackTrace();
		}
	}

	@Override
	public void release() {
		try {
			if (null != text_doc_)
				text_doc_.close();
		} catch(IOException e) {
		}
	}

	@Override
	public void updateCurrentStateData(int current_rate, String current_state_string) {
		System.out.println(String.format("Current rate:%d", current_rate));
		try {
			text_doc_.write(String.format("Current rate:%d, state str:%s", current_rate, current_state_string));
			text_doc_.write("\r\n");
		} catch(IOException e) {
			
		}
	}
}
