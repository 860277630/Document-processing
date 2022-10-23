// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to do full text search in PDF documents under a folder.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.fxcrt.*;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.fts.*;
import com.foxit.sdk.common.*;
	
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Date; 
	
import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class fulltextsearch {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/fulltextsearch/";
    private static String input_path = "../input_files/fulltextsearch";

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
        //  createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }
        String directory = input_path;
        FullTextSearch search = new FullTextSearch();
        try {
              createResultFolder(output_path);
            String dbPath = output_path + "search.db";
            search.setDataBasePath(dbPath);
            // Get document source information.
            DocumentsSource source = new DocumentsSource(directory);
         
            // Create a Pause callback object implemented by users to pause the updating process.
            PauseUtil pause = new PauseUtil(30);
         
            // Start to update the index of PDF files which receive from the source.
            Progressive progressive = search.startUpdateIndex(source, pause, false);
            int state = Progressive.e_ToBeContinued;
            while (state == Progressive.e_ToBeContinued) {
                state = progressive.resume();
            }
         
            // Create a callback object which will be invoked when a matched one is found.
            MySearchCallback searchCallback = new MySearchCallback(output_path + "result.txt");
         
            // Search the specified keyword from the indexed data source.
            search.searchOf("Foxit", FullTextSearch.e_RankHitCountASC, searchCallback);
			System.out.println("FullTextSearch demo.");
        } catch (PDFException e) {
            e.printStackTrace();
        }
        
        Library.release();
    }

}

class MySearchCallback extends SearchCallback {
    private static final String TAG = MySearchCallback.class.getCanonicalName();
	private FileWriter text_doc_ = null;

	public MySearchCallback(String output_txt_file_path) {
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
    public int retrieveSearchResult(String filePath, int pageIndex, String matchResult, int matchStartTextIndex, int matchEndTextIndex) {
		try {
			String result_str = String.format("RetrieveSearchResult:\nFound file is: %s\nPage index is: %d\nStart text index: %d\nEnd text index: %d\nMatch is: %s\n", filePath, pageIndex, matchStartTextIndex, matchEndTextIndex, matchResult);
			text_doc_.write(result_str);
			text_doc_.write("\r\n");
		} catch(IOException e) {
			
		}
        return 0;
    }
}

class PauseUtil extends PauseCallback{
    private long m_milliseconds = 0;
    private long m_startTime = 0;
 
    public PauseUtil(long milliSeconds) {
        Date date = new Date();
        m_milliseconds = milliSeconds;
        m_startTime = date.getTime();
    }
 
    @Override
    public boolean needToPauseNow() {
        // TODO Auto-generated method stub
        if (this.m_milliseconds < 1)
            return false;
        Date date = new Date();
        long diff = date.getTime() - m_startTime;
        if (diff > this.m_milliseconds) {
            m_startTime = date.getTime();
            return true;
        } else
            return false;
    }
}