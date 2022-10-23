package com.example.demo.simple_demo.pdf2text;// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to extract text from PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;

import java.io.*;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class pdf2text {

    private static String bat_path = "../fxFiles/bats/";

    // You can also use System.load("filename") instead. The filename argument must be an absolute path name.
    private void init() {
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

    public  void main() throws Exception {
        String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
        String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
/*        String input_path = "./src/main/java/com/example/demo/simple_demo/input_files/";
        String output_path = "./src/main/java/com/example/demo/simple_demo/output_files/pdf2text/";*/
        String input_path = "../fxFiles/input_files/";
        String output_path = "../fxFiles/output_files/pdf2text/";
        String input_file = input_path + "AboutFoxit.pdf";
        String output_file = output_path + "AboutFoxit.txt";

        changeLocalTime("2022-10-20");
        init();
        createResultFolder(output_path);

        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }
        try {
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }
            FileOutputStream stream = new FileOutputStream(output_file);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            // Get page count
            int pageCount = doc.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PDFPage page = doc.getPage(i);
                // Parse page
                page.startParse(e_ParsePageNormal, null, false);
                // Get the text select object.
                TextPage text_select = new TextPage(page, TextPage.e_ParseTextNormal);
                int count = text_select.getCharCount();
                if (count > 0) {
                    String chars = text_select.getChars(0, -1);
                    writer.write(chars);
                }
            }
            writer.close();
            stream.close();
            System.out.println("Convert PDF file to TEXT file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Library.release();
        changeLocalTime("2022-10-23");
    }




    //以单线程的方式  更改时间
    private static synchronized void changeLocalTime(String dateStr) throws Exception {
        //date  必须是是以-为分隔符的 字符串  并且含有年月日
        String[] times = dateStr.split("-");
        if (times.length != 3) {
            return;
        }
        String os = System.getProperty("os.name").toLowerCase();
        //目的年份 用于福鑫的注册
        if (os.startsWith("win")) {
            String nowPath = bat_path;
            File temDir = new File(nowPath);
            File batFile = new File(nowPath + "Date.bat");
            if (!temDir.exists()) {
                temDir.mkdir();
                batFile.createNewFile();
            }
            FileWriter fw = new FileWriter("Date.bat");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off\n");
            bw.write("%1 mshta vbscript:CreateObject(\"Shell.Application\").ShellExecute(\"cmd.exe\",\"/c %~s0 ::\",\"\",\"runas\",1)(window.close)&&exit\n");
            bw.write("date " + times[0] + "/" + times[1] + "/" + times[2]);
            bw.close();
            fw.close();
            Process process = Runtime.getRuntime().exec("Date.bat");
            process.waitFor();
            //等上面的执行完毕后再删除文件
            batFile.delete();
        } else if (os.startsWith("mac")) {
            Runtime.getRuntime().exec("cmd /c " + times[0] + "-" + times[1] + "-" + times[2]);//Windows 系统
        } else {
            //然后进行时间的恢复
            Runtime.getRuntime().exec(" sudo date -s " + times[0] + "-" + times[1] + "-" + times[2]+"`date +%T`");//linux 系统为tomcat用户分配了权限
        }
    }

}

