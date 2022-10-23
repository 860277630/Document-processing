// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains a simple example to demonstrate how to implement a callback class DRMSecurityCallback which is used
// for DRM decryption.

import com.foxit.sdk.pdf.DRMSecurityCallback;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.SecurityHandler;

public class DRMSecurityEvent extends DRMSecurityCallback {
    private String fileID;
    private byte[] initialKey;

    public DRMSecurityEvent(String fileID, byte[] initialKey) {
        this.fileID = fileID;
        this.initialKey = initialKey;
    }

    @Override
    public void release() {}
    
    @Override
    public int getCipherType(PDFDoc arg0, String arg1) {
        return SecurityHandler.e_CipherAES;
    }

    @Override
    public String getFileID(PDFDoc arg0, String arg1) {
        return fileID;
    }

    @Override
    public byte[] getInitialKey(PDFDoc arg0, String arg1) {
        return initialKey;
    }

    @Override
    public int getKeyLength(PDFDoc arg0, String arg1) {
        return 16;
    }

    @Override
    public int getUserPermissions(PDFDoc arg0, String arg1) {
        return 0xFFFFFFFC;
    }

    @Override
    public boolean isOwner(PDFDoc arg0, String arg1) {
        return true;
    }

}
