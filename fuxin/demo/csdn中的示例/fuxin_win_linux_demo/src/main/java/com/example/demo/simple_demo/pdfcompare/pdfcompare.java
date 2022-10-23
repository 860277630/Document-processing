// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to compare pdf page with the other.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectFArray;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.addon.comparison.CompareResultInfo;
import com.foxit.sdk.addon.comparison.CompareResultInfoArray;
import com.foxit.sdk.addon.comparison.CompareResults;
import com.foxit.sdk.addon.comparison.Comparison;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.pdf.annots.Annot;
import com.foxit.sdk.pdf.annots.Squiggly;
import com.foxit.sdk.pdf.annots.StrikeOut;
import com.foxit.sdk.pdf.annots.Stamp;
import com.foxit.sdk.pdf.annots.QuadPoints;
import com.foxit.sdk.pdf.annots.QuadPointsArray;

import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.util.Random;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

public class pdfcompare {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdfcompare/";
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
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        String input_base_file = input_path + "pdfcompare/test_base.pdf";
        String input_compared_file = input_path + "pdfcompare/test_compared.pdf";
        try {
            PDFDoc base_doc = new PDFDoc(input_base_file);
            error_code = base_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_base_file + " Error: " + error_code);
                return;
            }

            PDFDoc compared_doc = new PDFDoc(input_compared_file);
            error_code = compared_doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_compared_file + " Error: " + error_code);
                return;
            }
            
            Comparison comparison = new Comparison(base_doc, compared_doc);
            CompareResults result = comparison.doCompare(0, 0, Comparison.e_CompareTypeText);
            CompareResultInfoArray oldInfo = result.getBase_doc_results();
            CompareResultInfoArray newInfo = result.getCompared_doc_results();
            long oldInfoSize = oldInfo.getSize();
            long newInfoSize = newInfo.getSize();
            PDFPage page = compared_doc.getPage(0);
            PDFPage page1 = base_doc.getPage(0);
            for (int i=0; i<oldInfoSize; i++)
            {
                CompareResultInfo item = oldInfo.getAt(i);
                int type = item.getType();
                if (type == CompareResultInfo.e_CompareResultTypeDeleteText)
                {
                    String res_string;
                    res_string = String.format("\"%s\"", item.getDiff_contents());
                    CreateDeleteText(page1, item.getRect_array(), 0xff0000, res_string, "Compare : Delete", "Text");
                }
                else if (type == CompareResultInfo.e_CompareResultTypeInsertText)
                {
                    String res_string;
                    res_string = String.format("\"%s\"", item.getDiff_contents());
                    CreateInsertTextStamp(page1, item.getRect_array(), 0x0000ff, res_string, "Compare : Insert", "Text");
                }
                else if (type == CompareResultInfo.e_CompareResultTypeReplaceText)
                {
                    String res_string;
                    res_string = String.format("[Old]: \"%s\"\r\n[New]: \"%s\"", newInfo.getAt(i).getDiff_contents(), item.getDiff_contents());
                    CreateSquigglyRect(page1, item.getRect_array(), 0xe7651a, res_string, "Compare : Replace", "Text");
                }

            }
            for (int i=0; i<newInfoSize; i++)
            {
                CompareResultInfo item = newInfo.getAt(i);
                int type = item.getType();
                if (type == CompareResultInfo.e_CompareResultTypeDeleteText)
                {
                    String res_string;
                    res_string = String.format("\"%s\"", item.getDiff_contents());
                    CreateDeleteTextStamp(page, item.getRect_array(), 0xff0000, res_string, "Compare : Delete", "Text");
                }
                else if (type == CompareResultInfo.e_CompareResultTypeInsertText)
                {
                    String res_string;
                    res_string = String.format("\"%s\"", item.getDiff_contents());
                    CreateDeleteText(page, item.getRect_array(), 0x0000ff, res_string, "Compare : Insert", "Text");
                }
                else if (type == CompareResultInfo.e_CompareResultTypeReplaceText)
                {
                    String res_string;
                    res_string = String.format("[Old]: \"%s\"\r\n[New]: \"%s\"", oldInfo.getAt(i).getDiff_contents(), item.getDiff_contents());
                    CreateSquigglyRect(page, item.getRect_array(), 0xe7651a, res_string, "Compare : Replace", "Text");
                }

            }
            
            compared_doc.saveAs(output_path + "result.pdf", PDFDoc.e_SaveFlagNormal);
            base_doc.saveAs(output_path + "old.pdf", PDFDoc.e_SaveFlagNormal);

            PDFDoc new_doc = comparison.generateComparedDoc(Comparison.e_CompareTypeAll);
            new_doc.saveAs(output_path + "generate_result.pdf", PDFDoc.e_SaveFlagNormal);
            System.out.println("Pdf compare test.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Library.release();
    }

    private static String RandomUID() {
		String uuid = "";
		String temp = "0123456789qwertyuiopasdfghjklzxcvbnm";

		for (int n = 0; n < 16; n++) {
			String uuid_temp;
			Random random = new Random();
			int b = random.nextInt() % 255;

			switch (n) {
			case 6:
				uuid_temp = String.format("4%x", b % 15);
				break;
			case 8:
				int index = random.nextInt() % temp.length();
				if (index < 0)
					index = (-1) * index;
				uuid_temp = String.format("%c%x", temp.charAt(index), b % 15);
				break;
			default:
				uuid_temp = String.format("%02x", b);
				break;
			}
			uuid += uuid_temp;

			switch (n) {
			case 3:
			case 5:
			case 7:
			case 9:
				uuid += '-';
				break;
			}
		}
		return uuid;
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
    
    static void CreateSquigglyRect(PDFPage page, RectFArray rects, int color, String csContents, String csType, String csObjType) throws PDFException
    {
		int rectSize = rects.getSize();
		if (rectSize == 0) return;
    	Squiggly squiggly = new Squiggly(page.addAnnot(Annot.e_Squiggly, new RectF()));
    	squiggly.setContent(csContents);
    
        QuadPointsArray quad_points_array = new QuadPointsArray();
        for (int i=0; i<rectSize; i++)
        {
            RectF item = rects.getAt(i);
            QuadPoints quad_points = new QuadPoints();
            quad_points.setFirst(new PointF(item.getLeft(), item.getTop()));
            quad_points.setSecond(new PointF(item.getRight(), item.getTop()));
            quad_points.setThird(new PointF(item.getLeft(), item.getBottom()));
            quad_points.setFourth(new PointF(item.getRight(), item.getBottom()));
            quad_points_array.add(quad_points);
        }
        squiggly.setQuadPoints(quad_points_array);
    
        squiggly.setBorderColor(color);
        squiggly.setSubject(csObjType);
        squiggly.setTitle(csType);
        squiggly.setCreationDateTime(GetLocalDateTime());
        squiggly.setModifiedDateTime(GetLocalDateTime());
        squiggly.setUniqueID(RandomUID());
    
        squiggly.resetAppearanceStream();
    }
    static void CreateDeleteText(PDFPage page, RectFArray rects, int color, String csContents, String csType, String csObjType) throws PDFException
    {
		int rectSize = rects.getSize();
		if (rectSize == 0) return;		
    	StrikeOut strikeout = new StrikeOut(page.addAnnot(Annot.e_StrikeOut, new RectF()));
    	strikeout.setContent(csContents);
    
        QuadPointsArray quad_points_array = new QuadPointsArray();

        for (int i=0; i<rectSize; i++)
        {
            RectF item = rects.getAt(i);
            QuadPoints quad_points = new QuadPoints();
            quad_points.setFirst(new PointF(item.getLeft(), item.getTop()));
            quad_points.setSecond(new PointF(item.getRight(), item.getTop()));
            quad_points.setThird(new PointF(item.getLeft(), item.getBottom()));
            quad_points.setFourth(new PointF(item.getRight(), item.getBottom()));
            quad_points_array.add(quad_points);
        }
        strikeout.setQuadPoints(quad_points_array);
    
        strikeout.setBorderColor(color);
        strikeout.setSubject(csObjType);
        strikeout.setTitle(csType);
        strikeout.setCreationDateTime(GetLocalDateTime());
        strikeout.setModifiedDateTime(GetLocalDateTime());
        strikeout.setUniqueID(RandomUID());
    
        strikeout.resetAppearanceStream();
    }

    static void CreateDeleteTextStamp(PDFPage page, RectFArray rects, int color, String csContents, String csType, String csObjType) throws PDFException
    {
        RectF rcStamp = new RectF();
        int rectSize = rects.getSize();
        if (rectSize > 0)
        {
            RectF item = rects.getAt(0);
            rcStamp.setLeft(item.getLeft());
            rcStamp.setTop(item.getTop() + 12);
            rcStamp.setRight(rcStamp.getLeft() + 9);
            rcStamp.setBottom(rcStamp.getTop() - 12);
        }

        Image pImage = new Image(input_path + "pdfcompare/delete_stamp.png");      
         
        Stamp stamp = new Stamp(page.addAnnot(Annot.e_Stamp, rcStamp));
        stamp.setContent(csContents);
        stamp.setBorderColor(color);
        stamp.setSubject(csObjType);
        stamp.setTitle(csType);
        stamp.setCreationDateTime(GetLocalDateTime());
        stamp.setModifiedDateTime(GetLocalDateTime());
        stamp.setUniqueID(RandomUID());
        stamp.setImage(pImage, 0, 0);
    
        stamp.resetAppearanceStream();
    }
    static void CreateInsertTextStamp(PDFPage page, RectFArray rects, int color, String csContents, String csType, String csObjType) throws PDFException
    {
        RectF rcStamp = new RectF();
        int rectSize = rects.getSize();
        if (rectSize > 0)
        {
            RectF item = rects.getAt(0);
            rcStamp.setLeft(item.getLeft());
            rcStamp.setTop(item.getTop() - 4);
            rcStamp.setRight(rcStamp.getLeft() + 4);
            rcStamp.setBottom(rcStamp.getTop() - 8);
        }
    
        Image pImage = new Image(input_path + "pdfcompare/insert_stamp.png");
    
        Stamp stamp = new Stamp(page.addAnnot(Annot.e_Stamp, rcStamp));
        stamp.setContent(csContents);
        stamp.setBorderColor(color);
        stamp.setSubject(csObjType);
        stamp.setTitle(csType);
        stamp.setCreationDateTime(GetLocalDateTime());
        stamp.setModifiedDateTime(GetLocalDateTime());
        stamp.setUniqueID(RandomUID());
        stamp.setImage(pImage, 0, 0);
    
        stamp.resetAppearanceStream();
        
    }
}
