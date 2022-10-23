// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to do encryption and decryption for a PDF file
// by using password security method, custom security method, Foxit DRM security method, certificate security method
// or Microsoft RMS security method.

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;

import com.foxit.sdk.common.Library;
import com.foxit.sdk.pdf.CertificateEncryptData;
import com.foxit.sdk.pdf.CertificateSecurityCallback;
import com.foxit.sdk.pdf.CertificateSecurityHandler;
import com.foxit.sdk.pdf.CustomEncryptData;
import com.foxit.sdk.pdf.CustomSecurityHandler;
import com.foxit.sdk.pdf.DRMEncryptData;
import com.foxit.sdk.pdf.DRMSecurityHandler;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.RMSEncryptData;
import com.foxit.sdk.pdf.RMSSecurityCallback;
import com.foxit.sdk.pdf.RMSSecurityHandler;
import com.foxit.sdk.pdf.SecurityHandler;
import com.foxit.sdk.pdf.StdEncryptData;
import com.foxit.sdk.pdf.StdSecurityHandler;

public class Security {
    static final String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    static final String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    static final String output_path = "../output_files/";
    static final String input_path = "../input_files/";

    static final String output_directory = output_path + "security/";

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

    static void certificateSecurity(String input_file) {
        Random rand = new Random(23);
        byte[] seed = new byte[24];
        rand.nextBytes(seed);
        for (int i = 20; i < 24; i++) {
            seed[i] = (byte) 0xFF;
        }

        try {
            PDFDoc doc = new PDFDoc(input_file);
            int error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Do encryption.
            String cert_file_path = input_path + "foxit.cer";
            ArrayList<byte[]> envelopes = new ArrayList<byte[]>();
            byte[] bytes=null;
            try {
                bytes = CryptUtil.encryptByKey(seed, CryptUtil.getPublicKey(cert_file_path));
                envelopes.add(bytes);
            } catch (Exception e) {
                System.out.println("[Failed] Cannot get certificate information from " + cert_file_path);
                return;
            }
            byte[] data=new byte[20+bytes.length];
            System.arraycopy(seed, 0, data, 0, 20);
            System.arraycopy(bytes, 0, data, 20, bytes.length);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");  
            messageDigest.update(data);  
            byte[] initial_key = new byte[16];
            System.arraycopy(messageDigest.digest(),0,initial_key,0,16);  
            CertificateSecurityHandler handler = new CertificateSecurityHandler();
            CertificateEncryptData encrypt_data = new CertificateEncryptData(true, SecurityHandler.e_CipherAES,
                    envelopes);
            handler.initialize(encrypt_data, initial_key);

            doc.setSecurityHandler(handler);
            String output_file = output_directory + "certificate_encrypt.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);

            // Do decryption.
            CertificateSecurityCallback callback = new CertificateSecurityEvent(input_path + "foxit_all.pfx", "123456");
            Library.registerSecurityCallback("Adobe.PubSec", callback);

            PDFDoc encrypted_doc = new PDFDoc(output_file);
            error_code = encrypted_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + output_file + " Error: " + error_code);
                return;
            }

            output_file = output_directory + "certificate_decrypt.pdf";
            encrypted_doc.removeSecurity();
            encrypted_doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
			handler.delete();
			doc.delete();
			encrypted_doc.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void foxitDRMDecrypt(String input_file) {
        try {
            PDFDoc doc = new PDFDoc(input_file);
            int error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Do encryption.
            DRMSecurityHandler handler = new DRMSecurityHandler();
            String file_id = "Simple-DRM-file-ID";
            String initialize_key = "Simple-DRM-initialize-key";
            DRMEncryptData encrypt_data = new DRMEncryptData(true, "Simple-DRM-filter", SecurityHandler.e_CipherAES, 16,
                    true, 0xfffffffc);
            handler.initialize(encrypt_data, file_id, initialize_key);
            doc.setSecurityHandler(handler);

            String output_file = output_directory + "foxit_drm_encrypt.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);

            // Do decryption.
            DRMSecurityEvent callback = new DRMSecurityEvent(file_id, toChars(initialize_key));
            Library.registerSecurityCallback("FoxitDRM", callback);
            PDFDoc encrypted_doc = new PDFDoc(output_file);
            error_code = encrypted_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + output_file + " Error: " + error_code);
                return;
            }

            output_file = output_directory + "foxit_drm_decrypt.pdf";

            encrypted_doc.removeSecurity();
            encrypted_doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
			handler.delete();
			doc.delete();
			encrypted_doc.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void customSecurity(String input_file) {
        try {
            PDFDoc doc = new PDFDoc(input_file);
            int error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Do encryption.
            CustomSecurityEvent callback = new CustomSecurityEvent();
            CustomSecurityHandler handler = new CustomSecurityHandler();
            final String encrypt_info = "Foxit simple demo custom security";
            CustomEncryptData encrypt_data = new CustomEncryptData(true, "Custom", "foxit-simple-demo");
            handler.initialize(encrypt_data, callback, encrypt_info);
            doc.setSecurityHandler(handler);
            String output_file = output_directory + "custom_encrypt.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);

            // Do decryption.
            Library.registerSecurityCallback("Custom", callback);
            PDFDoc encrypted_doc = new PDFDoc(output_file);

            error_code = encrypted_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + output_file + " Error: " + error_code);
                return;
            }

            output_file = output_directory + "custom_decrypt.pdf";
            encrypted_doc.removeSecurity();
            encrypted_doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
			handler.delete();
			doc.delete();
			encrypted_doc.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void rmsSecurity(String input_file) {
        try {
            PDFDoc doc = new PDFDoc(input_file);
            int error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Do encryption.
            RMSSecurityCallback callback = new RMSSecurityEvent(toChars("Simple-RMS-encrpty-key"));
            RMSSecurityHandler handler = new RMSSecurityHandler();
            ArrayList<byte[]> server_eul_list = new  ArrayList<byte[]>();
            server_eul_list.add(toChars("WM-1"));
            server_eul_list.add(toChars("This document has been encrypted by RMS encryption."));
            server_eul_list.add(toChars("WM-2"));
            server_eul_list.add(toChars("Just for simple demo rms security."));
            RMSEncryptData encrypt_data = new RMSEncryptData(true, "PubishLicense_0123", server_eul_list, 1);
            handler.initialize(encrypt_data, callback);
            doc.setSecurityHandler(handler);
            String output_file = output_directory + "rms_encrypt.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);

            // Do decryption.
            Library.registerSecurityCallback("MicrosoftIRMServices", callback);
            PDFDoc encrypted_doc = new PDFDoc(output_file);
            error_code = encrypted_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + output_file + " Error: " + error_code);
                return;
            }

            output_file = output_directory + "rms_decrypt.pdf";
            encrypted_doc.removeSecurity();
            encrypted_doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
			handler.delete();
			doc.delete();
			encrypted_doc.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void stdSecurity(String input_file) {
        try {
            PDFDoc doc = new PDFDoc(input_file);
            int error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Do encryption.
            StdSecurityHandler handler = new StdSecurityHandler();
            StdEncryptData encrypt_data = new StdEncryptData(true, 0xfffffffc, SecurityHandler.e_CipherAES, 16);
            handler.initialize(encrypt_data, toChars("user"), toChars("owner"));
            doc.setSecurityHandler(handler);

            String output_file = output_directory + "std_encrypt_userpwd[user]_ownerpwd[owner].pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);

            // Do decryption.
            PDFDoc encrypted_doc = new PDFDoc(output_file);
            error_code = encrypted_doc.load(toChars("owner"));
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + output_file + " Error: " + error_code);
                return;
            }

            encrypted_doc.removeSecurity();
            output_file = output_directory + "std_decrypt.pdf";
            encrypted_doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
			handler.delete();
			doc.delete();
			encrypted_doc.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void securityImpl(String pdf_file) {
         certificateSecurity(pdf_file);
         foxitDRMDecrypt(pdf_file);
         customSecurity(pdf_file);
         rmsSecurity(pdf_file);
         stdSecurity(pdf_file);
    }

    public static void main(String[] args) {

        createResultFolder(output_directory);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        String input_file = input_path + "AboutFoxit.pdf";
        securityImpl(input_file);
        Library.release();

        System.out.println("Security demo test.");
    }

    private static byte[] toChars(String value) throws UnsupportedEncodingException {
        return value.getBytes("ASCII");
    }

    private static void createResultFolder(String output_path) {
        File directory = new File(output_path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
