// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to do LTV verification (using default callback) in PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.common.fxcrt.FileReaderCallback;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Enumeration;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.Signature.*;

import java.io.InputStream;

// Here, the implementation of TrustedCertStoreCallback is very simple : 
// trust all input certificate(s) when this callback function is triggered during LTV verification.
// User can improve the implementation of the callback class TrustedCertStoreCallback or choose not to use TrustedCertStoreCallback.
class MyTrustedCertStoreCallback extends TrustedCertStoreCallback {
	MyTrustedCertStoreCallback() {
	}

	@Override
	public boolean isCertTrusted(byte[] cert) {
		return true;
	}
	
	@Override
	public boolean isCertTrustedRoot(byte[] cert) {
		return true;
	}
}


public class ltv {
	public static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
	public static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";

	public static String output_path = "../output_files/";
	public static String input_path = "../input_files/";
	public static String output_directory = output_path + "ltv/";

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
		String state_str = "";
		if ((sig_state & e_StateUnknown) == e_StateUnknown)
			state_str += "Unknown";
		if ((sig_state & e_StateNoSignData) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "NoSignData";
		}
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
		if ((sig_state & e_StateVerifyIncredible) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyIncredible";
		}
		if ((sig_state & e_StateVerifyNoChange) > 0) {
			if (state_str.length() > 0)
				state_str += "|";
			state_str += "VerifyNoChange";
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

	static void PKCS7Signature(String input_pdf_path, String signed_pdf_path) throws Exception {
		
		PDFDoc pdf_doc = new PDFDoc(input_pdf_path);
		pdf_doc.startLoad(null, false, null);
								
		PDFPage pdf_page = pdf_doc.getPage(0);

		// Add a new signature to first page.
		float page_height = pdf_page.getHeight();
		float page_width = pdf_page.getWidth();
		RectF new_sig_rect = new RectF((page_width / 2 - 50.0f), (page_height / 2 - 50.0f),
									   (page_width / 2 + 50.0f), (page_height / 2 + 50.0f));
		// Add a new signature to page.
		com.foxit.sdk.pdf.Signature new_signature = pdf_page.addSignature(new_sig_rect);
		if (new_signature.isEmpty()) {
			throw new Exception("Add signature failed!");
		}
		String org_sub_filter = new_signature.getSubFilter();
		String filter = "Adobe.PPKLite";
		String sub_filter = "adbe.pkcs7.detached";
		new_signature.setFilter(filter);
		new_signature.setSubFilter(sub_filter);
		
		// Set values for the new signature.
		new_signature.setKeyValue(e_KeyNameSigner, "Foxit PDF SDK");
		new_signature.setKeyValue(e_KeyNameContactInfo, "support@foxitsoftware.com");
		new_signature.setKeyValue(e_KeyNameDN, "CN=CN,MAIL=MAIL@MAIL.COM");
		new_signature.setKeyValue(e_KeyNameLocation, "Fuzhou, China");
		String new_value = String.format(String.format(
				"As a sample for subfilter \"%s\"", sub_filter));
		new_signature.setKeyValue(e_KeyNameReason, String.format(new_value));
		new_signature.setKeyValue(e_KeyNameText, String.format(new_value));
		DateTime sign_time = DateTime.getLocalTime();
		new_signature.setSignTime(sign_time);
		
		// Set appearance flags to decide which content would be used in appearance.
		int ap_flags = e_APFlagLabel | e_APFlagSigner | e_APFlagReason
				| e_APFlagDN | e_APFlagLocation | e_APFlagText
				| e_APFlagSigningTime;
		new_signature.setAppearanceFlags(ap_flags);
		
		String cert_file_path = input_path + "foxit_all.pfx";
		String cert_file_password = "123456";

		new_signature.startSign(cert_file_path, cert_file_password.getBytes(),
								e_DigestSHA256, signed_pdf_path, null, null);
	}

	static void UseLTVVerifier(PDFDoc pdf_doc, boolean is_to_add_dss) throws PDFException {
		// Here use default RevocationCallback, so no need to call LTVVerifier::SetRevocationCallback
		LTVVerifier ltv_verifier = new LTVVerifier(pdf_doc, true, true, false, LTVVerifier.e_SignatureCreationTime);
    
    // Use implemented TrustedCertStoreCallback to trust some cerificates during LTV verification.
    // Here, the implementation of TrustedCertStoreCallback is very simple : 
    // trust all input certificate(s) when this callback function is triggered during LTV verification.
    // User can improve the implementation of the callback class TrustedCertStoreCallback or choose not to use TrustedCertStoreCallback.
    MyTrustedCertStoreCallback my_callback = new MyTrustedCertStoreCallback();
    ltv_verifier.setTrustedCertStoreCallback(my_callback);
    
		ltv_verifier.setVerifyMode(LTVVerifier.e_VerifyModeAcrobat);
		
		SignatureVerifyResultArray sig_verify_result_array = ltv_verifier.verify();
		for (long i = 0; i < sig_verify_result_array.getSize(); i++) {
			SignatureVerifyResult sig_verify_result = sig_verify_result_array.getAt(i);
		    String signature_name = sig_verify_result.getSignatureName();
		    int sig_state = sig_verify_result.getSignatureState();
		    int ltv_state = sig_verify_result.getLTVState();
		    String ltv_state_str = null;
		    switch (ltv_state) {
		      case SignatureVerifyResult.e_LTVStateInactive:
		        ltv_state_str = "inactive";
		        break;
		      case SignatureVerifyResult.e_LTVStateEnable:
		        ltv_state_str = "enabled";
		        break;
		      case SignatureVerifyResult.e_LTVStateNotEnable:
		        ltv_state_str = "not enabled";
		        break;
		    }
		    System.out.println(String.format("Signature name:%s, signature state: %s, LTV state: %s",
		    								 signature_name,
		    								 TransformSignatureStateToString(sig_state),
		    								 ltv_state_str));
		}

		if (is_to_add_dss) {
		    for (long i = 0; i < sig_verify_result_array.getSize(); i++) {
		    	if ((sig_verify_result_array.getAt(i).getSignatureState() & Signature.e_StateVerifyValid) == Signature.e_StateVerifyValid)
		    		ltv_verifier.addDSS(sig_verify_result_array.getAt(i));
		    }
		}
	}

	static void DoLTV(String signed_pdf_path, String saved_ltv_pdf_path) throws PDFException {
		// Use default SignatureCallback for signing a time stamp signature with filter "Adobe.PPKLite" and subfilter "ETSI.RFC3161",
		// so no need to register a custom signature callback.

		TimeStampServerMgr.initialize();
		String free_server_name = "FreeTSAServer";
		String free_server_url = "http://ca.signfiles.com/TSAServer.aspx";

		TimeStampServer timestamp_server = TimeStampServerMgr.addServer(free_server_name, free_server_url, "", "");
		TimeStampServerMgr.setDefaultServer(timestamp_server);

		PDFDoc pdf_doc = new PDFDoc(signed_pdf_path);
		pdf_doc.startLoad(null, false, null);
		// Add DSS
		System.out.println("== Before Add DSS ==");
		UseLTVVerifier(pdf_doc, true);

		// Add DTS
		PDFPage pdf_page = pdf_doc.getPage(0);
		// The new time stamp signature will have default filter name "Adobe.PPKLite" and default subfilter name "ETSI.RFC3161".
		Signature timestamp_signature = pdf_page.addSignature(new RectF(), "", Signature.e_SignatureTypeTimeStamp, true);
		String empty_str = "";
		Progressive sign_progressive = timestamp_signature.startSign("", empty_str.getBytes(), Signature.e_DigestSHA256, saved_ltv_pdf_path, null, null);
		if (sign_progressive.getRateOfProgress() != 100)
			sign_progressive.resume();

		// Check saved file.
		PDFDoc check_pdf_doc = new PDFDoc(saved_ltv_pdf_path);
		check_pdf_doc.startLoad(null, false, null);
		// Just LTV veify.
		System.out.println("== After Add DSS ==");
		UseLTVVerifier(check_pdf_doc, false);

		TimeStampServerMgr.release();
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

		String input_file_name = "AboutFoxit.pdf";
		String input_file_path = input_path + input_file_name;
		System.out.println(String.format("Input file path: %s\r\n", input_file_path));
		String signed_pdf_path = output_directory + input_file_name.substring(0, input_file_name.length()-4) + "_signed.pdf";
		PKCS7Signature(input_file_path, signed_pdf_path);

		// Do LTV
		String saved_ltv_pdf_path = signed_pdf_path.substring(0, signed_pdf_path.length()-4) + "_ltv.pdf";
		DoLTV(signed_pdf_path, saved_ltv_pdf_path);

		Library.release();
	}

}
