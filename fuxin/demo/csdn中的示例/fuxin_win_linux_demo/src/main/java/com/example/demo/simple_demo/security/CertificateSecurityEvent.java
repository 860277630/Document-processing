// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains a simple example to demonstrate how to implement a callback class CertificateSecurityEvent which is used
// for certificate decryption.

import com.foxit.sdk.pdf.CertificateSecurityCallback;

public class CertificateSecurityEvent extends CertificateSecurityCallback {
    private String filePath;
    private String password;

    public CertificateSecurityEvent(String filePath, String password) {
        this.filePath = filePath;
        this.password = password;
    }
    
    @Override
    public void release() {}

    @Override
    public byte[] getDecryptionKey(byte[] arg0) {
        return CryptUtil.decryptByKey(arg0, CryptUtil.getPrivateKey(filePath, password));
    }
}
