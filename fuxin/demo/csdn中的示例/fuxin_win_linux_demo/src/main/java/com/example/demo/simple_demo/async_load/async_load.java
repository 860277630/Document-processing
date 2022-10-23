// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to loading PDF document asynchronously.


import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Bitmap;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Renderer;
import com.foxit.sdk.common.file.AsyncReaderCallback;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;

import java.io.*;
import java.util.ArrayList;

import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrDataNotReady;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class async_load {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/async_load/";
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
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d", error_code));
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";
        String output_file = output_path + "async-load page0.bmp";

        try {

            AsyncFileRead file_read = new AsyncFileRead();
            file_read.loadFile(input_file, false);
            PDFDoc doc = new PDFDoc(file_read, true);

            // Actually, here, application should download needed data which specified by AsyncFileRead::AddDownloadHint().
            // But here, for simple example, we just "download" these data inside AsyncFileRead::AddDownloadHint().
            // So, just continue to check the ready state here, which will trigger AsyncFileRead::AddDownloadHint() to
            // "download" data.
            int code = e_ErrDataNotReady;
            while (code == e_ErrDataNotReady) {
                code = doc.load(null);
            }

            if (code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d", input_file, error_code));
                return;
            }

            // Actually, here, application should download needed data which specified by AsyncFileRead::AddDownloadHint().
            // But here, for simple example, we just "download" these data inside AsyncFileRead::AddDownloadHint().
            // So, just continue to check the ready state here, which will trigger AsyncFileRead::AddDownloadHint() to
            // "download" data.
            PDFPage page = null;
            code = e_ErrDataNotReady;
            while (code == e_ErrDataNotReady) {
                page = doc.getPage(0);
                if (!page.isEmpty()) {
                    code = e_ErrSuccess;
                }
            }
            // Parse page.
            page.startParse(e_ParsePageNormal, null, false);

            int width = (int) page.getWidth();
            int height = (int) page.getHeight();
            Matrix2D matrix = page.getDisplayMatrix(0, 0, width, height, page.getRotation());

            // Prepare a bitmap for rendering.
            Bitmap bitmap = new Bitmap(width, height, e_DIBArgb, null, 0);
            bitmap.fillRect(0xFFFFFFFF, null);
            // Render page
            Renderer render = new Renderer(bitmap, false);
            render.startRender(page, matrix, null);

            Image image = new Image();
            image.addFrame(bitmap);
            image.saveAs(output_file);

            System.out.println("async-load demo finished.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Library.release();
    }
}

// Data of asynchronous loader callback object.
class DownloadHintDataInfo {
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private int offset;
    private long size;
    private boolean is_downloaded;

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isIs_downloaded() {
        return is_downloaded;
    }

    public void setIs_downloaded(boolean is_downloaded) {
        this.is_downloaded = is_downloaded;
    }

    DownloadHintDataInfo() {
        this.offset = 0;
        this.size = 0;
        this.is_downloaded = false;
    }

    DownloadHintDataInfo(int offset, long size, boolean is_downloaded) {
        this.offset = offset;
        this.size = size;
        this.is_downloaded = is_downloaded;
    }
}

// This callback just simulates the downloading progress by DownloadHintDataInfo::is_downloaded,
// not really to download data.
class AsyncFileRead extends AsyncReaderCallback {
    private RandomAccessFile file_ = null;
    private boolean is_large_file_ = false;
    // Used to record added hint range and if the range is downloaded.
    private ArrayList<DownloadHintDataInfo> hint_data_record_;

    AsyncFileRead() {

    }

    public boolean loadFile(String file_path, boolean is_large_file) throws FileNotFoundException {
        this.is_large_file_ = is_large_file;
        hint_data_record_ = new ArrayList<DownloadHintDataInfo>();
        file_ = new RandomAccessFile(file_path, "r");
        return true;

    }

    @Override
    public long getSize() {
        try {
            return this.file_.length();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return 0;
    }

    @Override
    public boolean readBlock(byte[] buffer, long offset, long size) {
      try {
            file_.seek(offset);
            int read = file_.read(buffer, 0, (int) size);
            return read == size ? true : false;
        } catch (IOException e) {
            e.printStackTrace();
        }
      return false;
    }

    @Override
    public void release() {
        try {
             this.file_.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDataAvail(int offset, long size) {
        boolean ret = checkRecordDownloaded(offset, size, true);
        return ret;
    }

    @Override
    public boolean addDownloadHint(int offset, long size) {
        // Record the range and downloaded data.
        boolean ret = checkRecordDownloaded(offset, size, true);
        return ret;
    }

    boolean checkRecordDownloaded(int offset, long size, boolean to_download) {
        int record_count = hint_data_record_.size();
        for (int i = 0; i < record_count; i++) {
            DownloadHintDataInfo data_info = (DownloadHintDataInfo) hint_data_record_.get(i);

            // If (offset+size) is out of current data_info, just continue to check other record in hint_data_record_.
            if (offset > (data_info.getOffset() + data_info.getSize())) {
                continue;
            }
            if (offset + size < data_info.getOffset()) {
                continue;
            }

            // If data defined by <offset, size> has been in/within current data info, just download the data.
            if (offset >= data_info.getOffset() && (offset + size) <= (data_info.getOffset() + data_info.getSize())) {
                if (to_download)
                    data_info.setIs_downloaded(true);
                return data_info.isIs_downloaded();
            }

            // If only part of data defined by <offset, size> is in current data_info, download current data_info and
            // also check and download rest data.
            if (offset >= data_info.getOffset() && offset < (data_info.getOffset() + data_info.getSize()) &&
                    (offset + size) > (data_info.getOffset() + data_info.getSize())) {
                if (to_download)
                    data_info.setIs_downloaded(true);
                if (!data_info.isIs_downloaded())
                    return data_info.isIs_downloaded();
                int new_offset = data_info.getOffset() + (int)data_info.getSize() + 1;
                long new_size = size - 1 - (data_info.getOffset() + data_info.getSize() - offset);
                return checkRecordDownloaded(new_offset, new_size, to_download);
            }

            if (offset < data_info.getOffset() && (offset + size) >= data_info.getOffset() &&
                    (offset + size) <= (data_info.getOffset() + data_info.getSize())) {
                if (to_download)
                    data_info.setIs_downloaded(true);
                if (!data_info.isIs_downloaded())
                    return data_info.isIs_downloaded();
                int new_offset = offset;
                int new_size = data_info.getOffset() - 1 - offset;
                return checkRecordDownloaded(new_offset, new_size, to_download);
            }
            if (offset < data_info.getOffset() && (offset + size) > (data_info.getOffset() + data_info.getSize())) {
                if (to_download)
                    data_info.setIs_downloaded(true);
                if (!data_info.isIs_downloaded())
                    return data_info.isIs_downloaded();
                int new_offset = offset;
                long new_size = data_info.getOffset() - 1 - offset;
                if (checkRecordDownloaded(new_offset, new_size, to_download)) {
                    new_offset = data_info.getOffset() + (int)data_info.getSize() + 1;
                    new_size = size - 1 - (data_info.getOffset() - offset + data_info.getSize());
                    return checkRecordDownloaded(new_offset, new_size, to_download);
                } else
                    return false;
            }
        }
        if (to_download) {
            return downloadData(offset, size);
        }
        return false;
    }

    boolean downloadData(int offset, long size) {
        DownloadHintDataInfo new_info = new DownloadHintDataInfo(offset, size, true);
        hint_data_record_.add(new_info);
        return true;
    }
}
