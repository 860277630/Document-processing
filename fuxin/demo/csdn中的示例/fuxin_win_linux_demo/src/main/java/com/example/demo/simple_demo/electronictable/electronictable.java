// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add electronic table to PDF document.

import java.util.Calendar;
import java.io.File;
import java.io.IOException;

import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.fxcrt.FloatArray;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Image;

import com.foxit.sdk.pdf.annots.RichTextStyle;
import com.foxit.sdk.addon.TableBorderInfo;
import com.foxit.sdk.addon.TableData;
import com.foxit.sdk.addon.TableCellData;
import com.foxit.sdk.addon.TableCellDataArray;
import com.foxit.sdk.addon.TableCellDataColArray;
import com.foxit.sdk.addon.TableCellIndexArray;
import com.foxit.sdk.addon.TableGenerator;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.PDFException;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_AlignmentLeft;
import static com.foxit.sdk.common.Constants.e_AlignmentCenter;
import static com.foxit.sdk.common.Constants.e_AlignmentRight;
import static com.foxit.sdk.common.Font.e_StdIDHelvetica;
import static com.foxit.sdk.common.Font.e_CharsetANSI;
import static com.foxit.sdk.addon.TableBorderInfo.e_TableBorderStyleSolid;
import static com.foxit.sdk.pdf.annots.RichTextStyle.e_CornerMarkNone;
import static com.foxit.sdk.pdf.annots.RichTextStyle.e_CornerMarkSubscript;
import static com.foxit.sdk.pdf.annots.RichTextStyle.e_CornerMarkSuperscript;

public class electronictable {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/electronictable/";

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

    private static DateTime GetLocalDateTime() {
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
        int gmt = offset / (3600 * 1000);

        datetime.setUtc_hour_offset((short) gmt);
        datetime.setUtc_minute_offset(offset % (3600 * 1000) / 60);
        return datetime;
    }

    private static String DateTimeToString(DateTime datetime) {

        return String.format("%d/%d/%d-%d:%d:%d %s%d:%d", datetime.getYear(), datetime.getMonth(), datetime.getDay(),
                datetime.getHour(),
                datetime.getMinute(), datetime.getSecond(), datetime.getUtc_hour_offset() > 0 ? "+" : "-",
                datetime.getUtc_hour_offset(),
                datetime.getUtc_minute_offset());
    }

    private static RichTextStyle GetTableTextStyle(int index) throws PDFException {
        RichTextStyle style = new RichTextStyle();
        style.setFont(new Font(e_StdIDHelvetica));
        style.setText_size(10);
        style.setText_alignment(e_AlignmentLeft);
        style.setText_color(0x000000);
        style.setIs_bold(false);
        style.setIs_italic(false);
        style.setIs_underline(false);
        style.setIs_strikethrough(false);
        style.setMark_style(e_CornerMarkNone);

        switch (index) {
            case 1:
                style.setText_alignment(e_AlignmentCenter);
                break;
            case 2: {
                style.setText_alignment(e_AlignmentRight);
                style.setText_color(0x00FF00);
                break;
            }
            case 3:
                style.setText_size(15);
                break;
            case 4: {
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win"))
                    style.setFont(new Font("Times New Roman", 0, e_CharsetANSI, 0));
                else
                    style.setFont(new Font("FreeSerif", 0, e_CharsetANSI, 0));
                style.setText_color(0xFF0000);
                style.setText_alignment(e_AlignmentRight);
                break;
            }
            case 5: {
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win"))
                    style.setFont(new Font("Times New Roman", 0, e_CharsetANSI, 0));
                else
                    style.setFont(new Font("FreeSerif", 0, e_CharsetANSI, 0));
                style.setIs_bold(true);
                style.setText_alignment(e_AlignmentRight);
                break;
            }
            case 6: {
                style.setIs_bold(true);
                style.setIs_italic(true);
            }
            case 7: {
                style.setIs_bold(true);
                style.setIs_italic(true);
                style.setText_alignment(e_AlignmentCenter);
                break;
            }
            case 8: {
                style.setIs_underline(true);
                style.setText_alignment(e_AlignmentRight);
                break;
            }
            case 9:
                style.setIs_strikethrough(true);
                break;
            case 10:
                style.setMark_style(e_CornerMarkSubscript);
                break;
            case 11:
                style.setMark_style(e_CornerMarkSuperscript);
                break;
            default:
                break;
        }
        return style;
    }

    public static String GetTableCellText(int index) throws PDFException {
        String cell_text = "";
        switch (index) {
            case 0:
                cell_text = "Reference style";
                break;
            case 1:
                cell_text = "Alignment center";
                break;
            case 2:
                cell_text = "Green text color and alignment right";
                break;
            case 3:
                cell_text = "Text font size 15";
                break;
            case 4: {
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win"))
                    cell_text = "Red text color, Times New Roman font and alignment right";
                else
                    cell_text = "Red text color, FreeSerif font and alignment right";
                break;
            }
            case 5: {
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win"))
                    cell_text = "Bold, Times New Roman font and alignment right";
                else
                    cell_text = "Bold, FreeSerif font and alignment right";
                break;
            }
            case 6:
                cell_text = "Bold and italic";
                break;
            case 7:
                cell_text = "Bold, italic and alignment center";
                break;
            case 8:
                cell_text = "Underline and alignment right";
                break;
            case 9:
                cell_text = "Strikethrough";
                break;
            case 10:
                cell_text = "CornerMarkSubscript";
                break;
            case 11:
                cell_text = "CornerMarkSuperscript";
                break;
            default:
                cell_text = " ";
                break;
        }
        return cell_text;
    }

    public static void AddElectronicTable(PDFPage page) throws PDFException {
        // Add a spreadsheet with 4 rows and 3 columns
        {
            int index = 0;
            TableCellDataArray cell_array = new TableCellDataArray();

            for (int row = 0; row < 4; row++) {
                TableCellDataColArray col_array = new TableCellDataColArray();
                for (int col = 0; col < 3; col++) {
                    String cell_text = GetTableCellText(index);
                    RichTextStyle style = GetTableTextStyle(index++);
                    TableCellData cell_data = new TableCellData(style, cell_text, new Image(), new RectF());
                    col_array.add(cell_data);
                }
                    cell_array.add(col_array);
            }
            float page_width = page.getWidth();
            float page_height = page.getHeight();
            RectF rect = new RectF(100, 550, page_width - 100, page_height - 100);
			TableBorderInfo outsideborderinfoleft = new TableBorderInfo();
		    outsideborderinfoleft.setLine_width(1);
			TableBorderInfo outsideborderinforight = new TableBorderInfo();
		    outsideborderinforight.setLine_width(1);
			TableBorderInfo outsideborderinfotop = new TableBorderInfo();
		    outsideborderinfotop.setLine_width(1);
			TableBorderInfo outsideborderinfobottom = new TableBorderInfo();
		    outsideborderinfobottom.setLine_width(1);
			
			TableBorderInfo insideborderinfo_row = new TableBorderInfo();
			insideborderinfo_row.setLine_width(1);
		    TableBorderInfo insideborderinfo_col = new TableBorderInfo();
			insideborderinfo_col.setLine_width(1);
            TableData data = new TableData(rect, 4, 3, outsideborderinfoleft, outsideborderinforight, outsideborderinfotop,outsideborderinfobottom,insideborderinfo_row, insideborderinfo_col, new TableCellIndexArray(),new FloatArray(), new FloatArray());
          
            TableGenerator.addTableToPage(page, data, cell_array);
        }
      
        //Add a spreadsheet with 5 rows and 6 columns
        {
            String cell_text = " ";
            String[] show_text = { "Foxit Software Incorporated", "Foxit Reader", "Foxit MobilePDF", "Foxit PhantomPDF", "Foxit PDF SDKs" };
            TableCellDataArray cell_array = new TableCellDataArray();
        
            for (int row = 0; row < 5; row++) {
                TableCellDataColArray col_array = new TableCellDataColArray();
            
                for (int col = 0; col < 6; col++) {
                    if (col == 5)
                        cell_text = DateTimeToString(GetLocalDateTime());
                    else
                        cell_text = show_text[col];
                    RichTextStyle style = GetTableTextStyle(row);
                    TableCellData cell_data = new TableCellData(style, cell_text, new Image(), new RectF());
                
                  col_array.add(cell_data);
                }
              cell_array.add(col_array);
            }
            float page_width = page.getWidth();
            float page_height = page.getHeight();
            RectF rect = new RectF(10, 200, page_width - 10, page_height - 350);
			TableBorderInfo outsideborderinfoleft = new TableBorderInfo();
		    outsideborderinfoleft.setLine_width(2);
			TableBorderInfo outsideborderinforight = new TableBorderInfo();
		    outsideborderinforight.setLine_width(2);
			TableBorderInfo outsideborderinfotop = new TableBorderInfo();
		    outsideborderinfotop.setLine_width(2);
			TableBorderInfo outsideborderinfobottom = new TableBorderInfo();
		    outsideborderinfobottom.setLine_width(2);
			TableBorderInfo insideborderinfo_row = new TableBorderInfo();
			insideborderinfo_row.setLine_width(2);
		    TableBorderInfo insideborderinfo_col = new TableBorderInfo();
			insideborderinfo_col.setLine_width(2);
            TableData data = new TableData(rect, 5, 6, outsideborderinfoleft, outsideborderinforight, outsideborderinfotop,outsideborderinfobottom, insideborderinfo_row, insideborderinfo_col, new TableCellIndexArray(), new FloatArray(), new FloatArray());
            TableGenerator.addTableToPage(page, data, cell_array);
        }
      
    }

    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            myPath.mkdir();
        }
    }

    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);
        // Initialize Library.
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            PDFDoc doc = new PDFDoc();
            PDFPage page = doc.insertPage(0, PDFPage.e_SizeLetter);
            AddElectronicTable(page);
            String output_file = output_path + "electronictable_result.pdf";
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("electronictable demo");
        Library.release();
        return;
    }
}
