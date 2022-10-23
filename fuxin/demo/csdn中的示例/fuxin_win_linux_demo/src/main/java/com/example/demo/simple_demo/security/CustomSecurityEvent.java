// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains a simple example to demonstrate how to implement a callback class CustomSecurityCallback which is used
// for custom encryption and decryption.

import com.foxit.sdk.pdf.CustomSecurityCallback;
import com.foxit.sdk.pdf.SecurityHandler;

public class CustomSecurityEvent extends CustomSecurityCallback {

	@Override
	public void release() {}
	
    @Override
    public Object createContext(String arg0, String arg1, String arg2) {
        return null;
    }

    @Override
    public byte[] decryptData(Object arg0, byte[] arg1) {
    	if (arg1 == null) return null;
        byte[] arg2 = new byte[arg1.length];
        for (int i = 0; i < arg1.length; i++) {
            arg2[i] = (byte) (arg1[i] + 1);
        }
        return arg2;
    }

    @Override
    public boolean encryptData(Object arg0, int arg1, int arg2, byte[] arg3, byte[] arg4) {
        for (int i = 0; i < arg3.length; i++) {
            arg4[i] = (byte) (arg3[i] - 1);
        }
        return true;
    }

    @Override
    public byte[] finishDecryptor(Object arg0) {
        return new byte[] {0 };
    }

    @Override
    public int getCipher(Object arg0) {
        return SecurityHandler.e_CipherAES;
    }

    @Override
    public int getDecryptedSize(Object arg0, int arg1) {
        return arg1;
    }

    @Override
    public byte[] getEncryptKey(Object arg0) {
        return new byte[] {'k', 'e', 'y', '\0' };
    }

    @Override
    public int getEncryptedSize(Object arg0, int arg1, int arg2, byte[] arg3) {
        return arg3.length;
    }

    @Override
    public int getUserPermissions(Object arg0, int arg1) {
        return 0xffffffc;
    }

    @Override
    public boolean isOwner(Object arg0) {
        return true;
    }

    @Override
    public boolean releaseContext(Object arg0) {
        return true;
    }

    @Override
    public Object startDecryptor(Object arg0, int arg1, int arg2) {
        return null;
    }

}
