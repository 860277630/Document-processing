// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to retrieve public key from certificate for CertificateSecurityEvent.

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;

final class CryptUtil {
    
    private CryptUtil() {
    }

    public static Key getPublicKey(String cerPath) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            FileInputStream stream = new FileInputStream(cerPath);
            java.security.cert.Certificate certificate = certificateFactory.generateCertificate(stream);
            stream.close();
            return certificate.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Key getPrivateKey(String pfxPath, String password) {
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            FileInputStream stream = new FileInputStream(pfxPath);
            keyStore.load(stream, null);
            stream.close();
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                return keyStore.getKey(alias, password.toCharArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptByKey(byte[] plainData, Key key) {
        return cryptByKey(plainData, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decryptByKey(byte[] encryptedData, Key key) {
        return cryptByKey(encryptedData, key, Cipher.DECRYPT_MODE);
    }

    private static byte[] cryptByKey(byte[] inputData, Key key, int opmode) {
        if (inputData == null) return null;
        // The max length of decrypted byte array: 128
        final int max_crypt_block = 128;
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(opmode, key);
            int len = inputData.length;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] data;
            // Decrypt data segment by segment
            while (len > offSet) {
                data = cipher.doFinal(inputData, offSet,
                        (len - offSet > max_crypt_block) ? max_crypt_block : (len - offSet));
                stream.write(data, 0, data.length);
                offSet += max_crypt_block;
            }
            byte[] outputData = stream.toByteArray();
            stream.close();
            return outputData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
