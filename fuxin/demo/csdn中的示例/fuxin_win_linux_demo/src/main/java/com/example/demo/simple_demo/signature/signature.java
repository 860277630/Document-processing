package com.example.demo.simple_demo.signature;// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add, sign and
// verify signature in PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.FileReaderCallback;
import com.foxit.sdk.common.fxcrt.StreamCallback;
import com.foxit.sdk.common.fxcrt.PauseCallback;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.*;
import com.foxit.sdk.pdf.annots.Widget;

import java.io.File;
import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Enumeration;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.Signature.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

class CryptNoRestrict {

	public CryptNoRestrict() {
	}

	public CryptNoRestrict(String encoding) {
		this.encoding = encoding;
	}

	private String encoding = "GBK";

	protected byte lastSignMsg[];

	protected int lastSignedLength;
	
	public boolean SignMsg(final byte[] TobeSigned, final String KeyFile,
			final String PassWord) throws Exception {
		File f = new File(KeyFile);
		InputStream fiKeyFile = new FileInputStream(f);

		boolean result = false;

		KeyStore ks = KeyStore.getInstance("PKCS12");

		try {
			ks.load(fiKeyFile, PassWord.toCharArray());
		} catch (Exception ex) {
			if (fiKeyFile != null)
				fiKeyFile.close();
			throw ex;
		}
		Enumeration myEnum = ks.aliases();
		String keyAlias = null;
		RSAPrivateCrtKey prikey = null;

		while (myEnum.hasMoreElements()) {
			keyAlias = (String) myEnum.nextElement();
			if (ks.isKeyEntry(keyAlias)) {
				prikey = (RSAPrivateCrtKey) ks.getKey(keyAlias,
						PassWord.toCharArray());
				break;
			}
		}

		if (prikey == null) {
			result = false;
			throw new Exception("not found private key:" + KeyFile);
		} else {
			Signature sign = Signature.getInstance("SHA1withRSA");
			sign.initSign(prikey);
			byte[] bytebuf = TobeSigned;
			sign.update(bytebuf);
			this.lastSignMsg = sign.sign();
			this.lastSignedLength = lastSignMsg.length;
			result = true;
		}
		return result;
	}

	public boolean VerifyMsg(byte[] TobeVerified, byte[] PlainText,
			String CertFile) throws Exception {
		boolean result = false;
		File f = new File(CertFile);
		InputStream certfile_stream = new FileInputStream(f);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		X509Certificate x509cert = null;
		try {
			x509cert = (X509Certificate) cf
					.generateCertificate(certfile_stream);
		} catch (Exception ex) {
			if (certfile_stream != null)
				certfile_stream.close();
			throw ex;
		}

		RSAPublicKey pubkey = (RSAPublicKey) x509cert.getPublicKey();
		Signature verify = Signature.getInstance("SHA1withRSA");
		verify.initVerify(pubkey);
		byte[] bytebuf = PlainText;
		verify.update(bytebuf);
		
		byte[] realbuffer = new byte[this.lastSignedLength];
		
		for (int i = 0; i < lastSignedLength; i++)
			realbuffer[i] = TobeVerified[i];
		
		if (verify.verify(realbuffer)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public int getLastSignedLength() {
		return this.lastSignedLength;
	}
	
	private static void Hex2Ascii(int len, byte data_in[], byte data_out[]) {
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; i++) {
			temp1[0] = data_in[i];
			temp1[0] = (byte) (temp1[0] >>> 4);
			temp1[0] = (byte) (temp1[0] & 0x0f);
			temp2[0] = data_in[i];
			temp2[0] = (byte) (temp2[0] & 0x0f);
			if (temp1[0] >= 0x00 && temp1[0] <= 0x09) {
				(data_out[j]) = (byte) (temp1[0] + '0');
			} else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f) {
				(data_out[j]) = (byte) (temp1[0] + 0x57);
			}

			if (temp2[0] >= 0x00 && temp2[0] <= 0x09) {
				(data_out[j + 1]) = (byte) (temp2[0] + '0');
			} else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f) {
				(data_out[j + 1]) = (byte) (temp2[0] + 0x57);
			}
			j += 2;
		}
	}

	private static void Ascii2Hex(int len, byte data_in[], byte data_out[]) {
		byte temp1[] = new byte[1];
		byte temp2[] = new byte[1];
		for (int i = 0, j = 0; i < len; j++) {
			temp1[0] = data_in[i];
			temp2[0] = data_in[i + 1];
			if (temp1[0] >= '0' && temp1[0] <= '9') {
				temp1[0] -= '0';
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xf0);
			} else if (temp1[0] >= 'a' && temp1[0] <= 'f') {
				temp1[0] -= 0x57;
				temp1[0] = (byte) (temp1[0] << 4);
				temp1[0] = (byte) (temp1[0] & 0xf0);
			}

			if (temp2[0] >= '0' && temp2[0] <= '9') {
				temp2[0] -= '0';
				temp2[0] = (byte) (temp2[0] & 0x0f);
			} else if (temp2[0] >= 'a' && temp2[0] <= 'f') {
				temp2[0] -= 0x57;
				temp2[0] = (byte) (temp2[0] & 0x0f);
			}
			data_out[j] = (byte) (temp1[0] | temp2[0]);

			i += 2;
		}
	}

	protected String replaceAll(String strURL, String strAugs) {
		int start = 0;
		int end = 0;
		String temp = new String();
		while (start < strURL.length()) {
			end = strURL.indexOf(" ", start);
			if (end != -1) {
				temp = temp.concat(strURL.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strURL.length()) {
					strURL = temp;
					break;
				}
			} else if (end == -1) {
				if (start == 0)
					break;
				if (start < strURL.length()) {
					temp = temp
							.concat(strURL.substring(start, strURL.length()));
					strURL = temp;
					break;
				}
			}
		}

		temp = "";
		start = end = 0;

		while (start < strAugs.length()) {
			end = strAugs.indexOf(" ", start);
			if (end != -1) {
				temp = temp.concat(strAugs.substring(start, end).concat("%20"));
				if ((start = end + 1) >= strAugs.length()) {
					strAugs = temp;
					break;
				}
			} else if (end == -1) {
				if (start == 0)
					break;
				if (start < strAugs.length()) {
					temp = temp.concat(strAugs.substring(start,
							strAugs.length()));
					strAugs = temp;
					break;
				}
			}
		}
		return strAugs;
	}
}

class CertUtil {
	CryptNoRestrict cryptNoRestrict = new CryptNoRestrict();
	
	public byte[] SignMsg(final byte[] TobeSigned, final String KeyFile,
			final String PassWord) throws Exception {
		cryptNoRestrict.SignMsg(TobeSigned, KeyFile, PassWord);
		return cryptNoRestrict.lastSignMsg;
	}

	public boolean VerifyMsg(byte[] TobeVerified, byte[] PlainText,
			String CertFile) throws Exception {
		return cryptNoRestrict.VerifyMsg(TobeVerified, PlainText, CertFile);
	}

}

// Used for implementing SignatureCallback.
class DigestContext {
	public FileReaderCallback file_read_callback_;
	public int[] byte_range_array_;
	public int byte_range_array_size_;

	DigestContext(FileReaderCallback file_read_callback,
			int[] byte_range_array, int byte_range_array_size) {
		this.file_read_callback_ = file_read_callback;
		this.byte_range_array_ = byte_range_array;
		this.byte_range_array_size_ = byte_range_array_size;
	}
};

// Implementation of SignatureCallback
class SignatureCallbackImpl extends SignatureCallback {
	private String sub_filter_;
	private DigestContext digest_context_ = null;
	byte[] arrall = null;
	private CertUtil cerUtil = new CertUtil();
	
	SignatureCallbackImpl(String subfilter) {
		sub_filter_ = subfilter;
	}

	@Override
	public void release() {
		
	}

	@Override
	public boolean startCalcDigest(FileReaderCallback var1, int[] var2,
			com.foxit.sdk.pdf.Signature var3, Object var4) {
		digest_context_ = new DigestContext(var1, var2, var2.length);
		return true;
	}

	@Override
	public int continueCalcDigest(Object var1, PauseCallback var2) {
		return com.foxit.sdk.common.Progressive.e_Finished;
	}

	@Override
	public byte[] getDigest(Object var1) {
		return arrall;
	}
	
	@Override
	public byte[] sign(byte[] digest, String cert_path, byte[] cert_password, int digest_algorithm, java.lang.Object client_data){
		try {
			try {
				FileReaderCallback filehandler = digest_context_.file_read_callback_;
				{
					long size = filehandler.getSize();
					byte[] arr1 = new byte[digest_context_.byte_range_array_[1]];
					filehandler.readBlock(arr1,
							digest_context_.byte_range_array_[0],
							digest_context_.byte_range_array_[1]);
					byte[] arr2 = new byte[digest_context_.byte_range_array_[3]];
					filehandler.readBlock(arr2,
							digest_context_.byte_range_array_[2],
							digest_context_.byte_range_array_[3]);
					size = 0;
					arrall = new byte[(int) digest_context_.byte_range_array_[1]
							+ (int) digest_context_.byte_range_array_[3]];
					System.arraycopy(arr1, 0, arrall, 0, arr1.length);
					System.arraycopy(arr2, 0, arrall, arr1.length, arr2.length);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] encryptStr = cerUtil.SignMsg(arrall, signature.input_path
					+ "foxit_all.pfx", "123456");

			return encryptStr;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] sign(byte[] digest, StreamCallback cert_path, byte[] cert_password, int digest_algorithm, java.lang.Object client_data){
		return null;
	}
	
	@Override
	public int verifySigState(byte[] var1, byte[] var2, Object var3) {
		boolean verify_state = false;
		try {
			verify_state = cerUtil.VerifyMsg(var2, arrall,
					signature.input_path + "foxit.cer");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verify_state ? com.foxit.sdk.pdf.Signature.e_StateVerifyNoChange : com.foxit.sdk.pdf.Signature.e_StateVerifyChange ;
	}
	
	@Override
	public boolean isNeedPadData() {return false;}
  
	@Override
	public int checkCertificateValidity(String cert_path, byte[] cert_password, java.lang.Object client_data) {
    // User can check the validity of input certificate here.
    // If no need to check, just return e_CertValid.
    return com.foxit.sdk.pdf.SignatureCallback.e_CertValid;
  }
}

public class signature {
	public static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
	public static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";

	public static String output_path = "../output_files/";
	public static String input_path = "../input_files/";
	public static String input_file = input_path + "AboutFoxit.pdf";
	public static String output_directory = output_path + "signature/";

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

	static String TransformSignatureStateToString(int sig_state) {
		if (0 == sig_state)
			return "Unknown";
		String state_str = "";
		if ((sig_state & e_StateNoSignData) > 0)
			state_str += "NoSignData";
		if ((sig_state & e_StateUnsigned) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "Unsigned";
		}
		if ((sig_state & e_StateSigned) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "Signed";
		}
		if ((sig_state & e_StateVerifyValid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerfiyValid";
		}
		if ((sig_state & e_StateVerifyInvalid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyInvalid";
		}
		if ((sig_state & e_StateVerifyErrorData) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyErrorData";
		}
		if ((sig_state & e_StateVerifyNoSupportWay) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyNoSupportWay";
		}
		if ((sig_state & e_StateVerifyErrorByteRange) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyErrorByteRange";
		}
		if ((sig_state & e_StateVerifyChange) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyChange";
		}
		if ((sig_state & e_StateVerifyNoChange) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyNoChange";
		}
		if ((sig_state & e_StateVerifyIncredible) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIncredible";
		}
		if ((sig_state & e_StateVerifyIssueValid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueValid";
		}
		if ((sig_state & e_StateVerifyIssueUnknown) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueUnknown";
		}
		if ((sig_state & e_StateVerifyIssueRevoke) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueRevoke";
		}
		if ((sig_state & e_StateVerifyIssueExpire) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueExpire";
		}
		if ((sig_state & e_StateVerifyIssueUncheck) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueUncheck";
		}
		if ((sig_state & e_StateVerifyIssueCurrent) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIssueCurrent";
		}
		if ((sig_state & e_StateVerifyTimestampNone) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampNone";
		}
		if ((sig_state & e_StateVerifyTimestampDoc) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampDoc";
		}
		if ((sig_state & e_StateVerifyTimestampValid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampValid";
		}
		if ((sig_state & e_StateVerifyTimestampInvalid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampInvalid";
		}
		if ((sig_state & e_StateVerifyTimestampExpire) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampExpire";
		}
		if ((sig_state & e_StateVerifyTimestampIssueUnknown) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampIssueUnknown";
		}
		if ((sig_state & e_StateVerifyTimestampIssueValid) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampIssueValid";
		}
		if ((sig_state & e_StateVerifyTimestampTimeBefore) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyTimestampTimeBefore";
		}
		return state_str;
	}

	static String DateTimeToString(DateTime datetime) {
		return String.format("%d/%d/%d-%d:%d:%d %s%d:%d", datetime.getYear(),
				datetime.getMonth(), datetime.getDay(), datetime.getHour(),
				datetime.getMinute(), datetime.getSecond(),
				datetime.getUtc_hour_offset() > 0 ? "+" : "-",
				datetime.getUtc_hour_offset(), datetime.getUtc_minute_offset());
	}

	static DateTime GetLocalDateTime() {
		Calendar c = Calendar.getInstance();

		DateTime datetime = new DateTime();
		datetime.setYear(c.get(Calendar.YEAR));
		datetime.setMonth(c.get(Calendar.MONTH) + 1);
		datetime.setDay(c.get(Calendar.DATE));
		datetime.setHour(c.get(Calendar.HOUR));
		datetime.setMinute(c.get(Calendar.MINUTE));
		datetime.setSecond(c.get(Calendar.SECOND));
		
		java.util.TimeZone timeZone = c.getTimeZone();
		int offset = timeZone.getRawOffset();
		int gmt = offset/(3600*1000);
		
		datetime.setUtc_hour_offset((short)gmt);
		datetime.setUtc_minute_offset(offset%(3600*1000) / 60);
		return datetime;
	}

	static com.foxit.sdk.pdf.Signature AddSiganture(PDFPage pdf_page,
			String sub_filter) throws Exception {
		float page_height = pdf_page.getHeight();
		float page_width = pdf_page.getWidth();
		RectF new_sig_rect = new RectF(0, (float) (page_height * 0.9),
				(float) (page_width * 0.4), page_height);
		// Add a new signature to page.
		com.foxit.sdk.pdf.Signature new_sig = pdf_page
				.addSignature(new_sig_rect);
		if (new_sig.isEmpty()) {
			throw new Exception("Add signature failed!");
		}

		// Set values for the new signature.
		new_sig.setKeyValue(e_KeyNameSigner, "Foxit PDF SDK");
		String new_value = String.format(String.format(
				"As a sample for subfilter \"%s\"", sub_filter));
		new_sig.setKeyValue(e_KeyNameReason, String.format(new_value));
		new_sig.setKeyValue(e_KeyNameContactInfo, "support@foxitsoftware.com");
		new_sig.setKeyValue(e_KeyNameDN, "CN=CN,MAIL=MAIL@MAIL.COM");
		new_sig.setKeyValue(e_KeyNameLocation, "Fuzhou, China");
		new_value = String.format(String.format(
				"As a sample for subfilter \"%s\"", sub_filter));
		new_sig.setKeyValue(e_KeyNameText, String.format(new_value));
		DateTime sign_time = GetLocalDateTime();
		new_sig.setSignTime(sign_time);
		String image_file_path = input_path + "FoxitLogo.jpg";
		Image image = new Image(image_file_path);
		new_sig.setImage(image, 0);
		// Set appearance flags to decide which content would be used in appearance.
		int ap_flags = e_APFlagLabel | e_APFlagSigner | e_APFlagReason
				| e_APFlagDN | e_APFlagLocation | e_APFlagText
				| e_APFlagSigningTime | e_APFlagBitmap;
		new_sig.setAppearanceFlags(ap_flags);

		return new_sig;
	}

	static void AdobePPKLiteSignature(PDFDoc pdf_doc, boolean use_default) throws Exception {
		String filter = "Adobe.PPKLite";
		String sub_filter = "adbe.pkcs7.detached";

		if (!use_default) {
			sub_filter = "adbe.pkcs7.sha1";
			SignatureCallbackImpl sig_callback = new SignatureCallbackImpl(
				sub_filter);
			Library.registerSignatureCallback(filter, sub_filter, sig_callback);
		}
		
		System.out.println(String.format("Use signature callback object for filter \"%s\" and sub-filter \"%s\"",filter, sub_filter));	
								
		PDFPage pdf_page = pdf_doc.getPage(0);
		// Add a new signature to first page.
		com.foxit.sdk.pdf.Signature new_signature = AddSiganture(pdf_page,
				sub_filter);
		// Set filter and subfilter for the new signature.
		new_signature.setFilter(filter);
		new_signature.setSubFilter(sub_filter);
		boolean is_signed = new_signature.isSigned();
		int sig_state = new_signature.getState();
		System.out.println(String.format(
				"[Before signing] Signed?:%s\t State:%s", is_signed ? "true"
						: "false", TransformSignatureStateToString(sig_state)));

		// Sign the new signature.
		String signed_pdf_path = output_directory + "signed_newsignature.pdf";
		if (use_default)
			signed_pdf_path = output_directory + "signed_newsignature_default_handle.pdf";
		String cert_file_path = input_path + "foxit_all.pfx";
		String cert_file_password = "123456";

		// Cert file path will be passed back to application through callback function SignatureCallback::Sign().
		// In this demo, the cert file path will be used for signing in callback function SignatureCallback::Sign().
		new_signature.startSign(cert_file_path, cert_file_password.getBytes(),
				e_DigestSHA1, signed_pdf_path, null, null);

		System.out.println("[Sign] Finished!");
		is_signed = new_signature.isSigned();
		sig_state = new_signature.getState();
		System.out.println(String.format(
				"[After signing] Signed?:%s\tState:%s", is_signed ? "true"
						: "false", TransformSignatureStateToString(sig_state)));

		// Open the signed document and verify the newly added signature (which is the last one).
		System.out.println(String.format("Signed PDF file: %s",
				signed_pdf_path));
		PDFDoc signed_pdf_doc = new PDFDoc(signed_pdf_path);
		int error_code = signed_pdf_doc.load(null);
		if (e_ErrSuccess != error_code) {
			System.out.println("Fail to open the signed PDF file.");
			return;
		}
		// Get the last signature which is just added and signed.
		int sig_count = signed_pdf_doc.getSignatureCount();
		com.foxit.sdk.pdf.Signature signed_signature = signed_pdf_doc
				.getSignature(sig_count - 1);
		// Verify the intergirity of signature.
		signed_signature.startVerify(null, null);
		System.out.println("[Verify] Finished!");
		is_signed = signed_signature.isSigned();
		sig_state = signed_signature.getState();
		System.out.println(String.format(
				"[After verifying] Signed?:%s\tState:%s",
				is_signed ? "true" : "false",
				TransformSignatureStateToString(sig_state)));
	}

	static void CheckSignatureInfo(PDFDoc pdf_doc) throws PDFException {
		int sig_count = pdf_doc.getSignatureCount();
		if (sig_count < 1) {
			System.out.println("No signature in current PDF file.");
			return;
		}
		for (int i = 0; i < sig_count; i++) {
			System.out.println(String.format("Signature index: %d", i));
			com.foxit.sdk.pdf.Signature signature = pdf_doc.getSignature(i);
			if (signature.isEmpty())
				continue;
			boolean is_signed = signature.isSigned();
			System.out.println(String.format("Signed?:%s", is_signed ? "true"
					: "false"));
			int sig_state = signature.getState();
			System.out.println(String.format("State:%s",
					TransformSignatureStateToString(sig_state)));

			Widget widget_annot = signature.getControl(0).getWidget();
			System.out
					.println(String
							.format("Object number %d of related widget annotation's dictionary",
									widget_annot.getDict().getObjNum()));

			String filter = signature.getFilter();
			System.out.println(String.format("Filter:%s",
					filter));
			String sub_filter = signature.getSubFilter();
			System.out.println(String.format("Sub-filter:%s",
					sub_filter));
			if (is_signed) {
				DateTime sign_time = signature.getSignTime();
				String time_str = DateTimeToString(sign_time);
				System.out.println(String.format("Sign Time:%s",
						time_str));
			}
			String key_value;
			key_value = signature.getKeyValue(e_KeyNameSigner);
			System.out.println(String.format("Signer:%s", key_value));
			key_value = signature.getKeyValue(e_KeyNameLocation);
			System.out
					.println(String.format("Location:%s", key_value));
			key_value = signature.getKeyValue(e_KeyNameReason);
			System.out.println(String.format("Reason:%s", key_value));
			key_value = signature.getKeyValue(e_KeyNameContactInfo);
			System.out.println(String.format("Contact Info:%s",
					key_value));
			key_value = signature.getKeyValue(e_KeyNameDN);
			System.out.println(String.format("DN:%s", key_value));
			key_value = signature.getKeyValue(e_KeyNameText);
			System.out.println(String.format("Text:%s", key_value));
		}
	}

	private static void createResultFolder(String output_path) {
		File myPath = new File(output_path);
		if (!myPath.exists()) {
			myPath.mkdir();
		}
	}

	public static void main(String[] args) throws Exception {
		createResultFolder(output_directory);
		// Initialize library.
		int error_code = Library.initialize(sn, key);
		if (error_code != e_ErrSuccess) {
			System.out.println(String.format("Library Initialize Error: %d\n",
					error_code));
			return;
		}
		System.out.println(String.format("Input file path: %s\r\n", input_file));
				
		for (int i = 0; i < 2; i++) {		
			PDFDoc pdf_doc = new PDFDoc(input_file);
			pdf_doc.startLoad(null, false, null);

			// Check information of existed signature(s) in PDF file if there's any signature in the PDF file.
			CheckSignatureInfo(pdf_doc);
			// Add new signature, sign it and verify it with filter "Adobe.PPKLite" and different subfilter.
	    
			AdobePPKLiteSignature(pdf_doc, i > 0 ? true:false);
			CheckSignatureInfo(pdf_doc);
		}

		Library.release();
	}

}
