// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add various annotations to PDF document.

import static com.foxit.sdk.common.Constants.e_ErrSuccess;

import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.util.Random;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Constants;
import com.foxit.sdk.common.DateTime;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Path;
import com.foxit.sdk.common.fxcrt.FloatArray;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.PointFArray;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.pdf.FileSpec;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.Rendition;
import com.foxit.sdk.pdf.actions.Action;
import com.foxit.sdk.pdf.actions.URIAction;
import com.foxit.sdk.pdf.actions.RenditionAction;
import com.foxit.sdk.pdf.annots.Annot;
import com.foxit.sdk.pdf.annots.BorderInfo;
import com.foxit.sdk.pdf.annots.Caret;
import com.foxit.sdk.pdf.annots.Circle;
import com.foxit.sdk.pdf.annots.DefaultAppearance;
import com.foxit.sdk.pdf.annots.FileAttachment;
import com.foxit.sdk.pdf.annots.FreeText;
import com.foxit.sdk.pdf.annots.Highlight;
import com.foxit.sdk.pdf.annots.Ink;
import com.foxit.sdk.pdf.annots.Line;
import com.foxit.sdk.pdf.annots.Link;
import com.foxit.sdk.pdf.annots.Markup;
import com.foxit.sdk.pdf.annots.Note;
import com.foxit.sdk.pdf.annots.PolyLine;
import com.foxit.sdk.pdf.annots.Polygon;
import com.foxit.sdk.pdf.annots.Popup;
import com.foxit.sdk.pdf.annots.QuadPoints;
import com.foxit.sdk.pdf.annots.QuadPointsArray;
import com.foxit.sdk.pdf.annots.Square;
import com.foxit.sdk.pdf.annots.Squiggly;
import com.foxit.sdk.pdf.annots.StrikeOut;
import com.foxit.sdk.pdf.annots.Underline;
import com.foxit.sdk.pdf.annots.Stamp;
import com.foxit.sdk.pdf.annots.RichTextStyle;
import com.foxit.sdk.pdf.annots.Screen;
import com.foxit.sdk.pdf.annots.Sound;
import com.foxit.sdk.pdf.objects.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class annotation {
    static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    static String output_path = "../output_files/annotation/";
    static String input_path = "../input_files/";
    static String input_file = input_path + "annotation_input.pdf";
    static String output_file = output_path + "annotation.pdf";

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

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        {
            // Load a document
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Get first page with index 0
            PDFPage page = doc.getPage(0);
            AddAnnotations(page);
            // Save PDF file
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
        }

        {
            output_file = output_path + "annotation_lower_level_API.pdf";
            // Load a document
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);
            if (error_code != e_ErrSuccess) {
                System.out.println("The Doc " + input_file + " Error: " + error_code);
                return;
            }

            // Get first page with index 0
            PDFPage page = doc.getPage(0);
            AddAnnotsWithLowLevelAPI(page);
            // Save PDF file
            doc.saveAs(output_file, PDFDoc.e_SaveFlagNoOriginal);
        }
		
		{
			String output_sound_file = output_path + "sound.wav";
            String input_sound_pdf_file = input_path + "sound.pdf";
            Sound sound = GetSoundAnnot(input_sound_pdf_file, 0);
			if (SaveSoundToFile(sound, output_sound_file))
			{
			  System.out.println("Save sound annotation to file, saved file path:" + output_sound_file);
			}
		}
        
        Library.release();
    }
	
    public static  boolean SaveSoundToFile(Sound sound, String path)  throws PDFException {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(path, "rw");
            PDFStream soundStream = sound.getSoundStream();
            int streamSize = soundStream.getDataSize(false).intValue();
            byte[] data = new byte[streamSize];
            soundStream.getData(false, streamSize, data);

            short bit = (short) sound.getBits();

            int riffSize = streamSize + 36;
            randomAccessFile.write("RIFF".getBytes());
            randomAccessFile.write(intToByteArray(riffSize));
            randomAccessFile.write("WAVEfmt ".getBytes());

            int chunkSize = 16;
            randomAccessFile.write(intToByteArray(chunkSize));

            short format = 1;
            randomAccessFile.write(shortToByteArray(format));

            int channelCount = sound.getChannelCount();
            randomAccessFile.write(shortToByteArray((short) channelCount));

            int rate = (int) sound.getSamplingRate();
            randomAccessFile.write(intToByteArray(rate));

            int bytePerSec = rate * channelCount * bit / 8;
            randomAccessFile.write(intToByteArray(bytePerSec));

            short blockAlign = (short) (bit * channelCount / 8);
            randomAccessFile.write(shortToByteArray(blockAlign));
            randomAccessFile.write(shortToByteArray(bit));
            randomAccessFile.write("data".getBytes());
            randomAccessFile.write(intToByteArray(streamSize));

            boolean ret = false;
            int encodingFormat = sound.getSampleEncodingFormat();
            switch (encodingFormat) {
                case Sound.e_SampleEncodingFormatALaw:
                    break;
                case Sound.e_SampleEncodingFormatMuLaw:
                    break;
                case Sound.e_SampleEncodingFormatSigned:
                    byte[] buffer = new byte[streamSize + 1];
                    int j = 0, k = 0;
                    for (int i = 0; i < streamSize; i += 2) {
                        byte low = data[j++];
                        byte high;
                        if (j == streamSize) {
                            high = 0;
                        } else {
                            high = data[j++];
                        }

                        buffer[k++] = high;
                        buffer[k++] = low;
                    }
                    randomAccessFile.write(buffer, 0, streamSize);
                    ret = true;
                    break;
                case Sound.e_SampleEncodingFormatRaw:
                default:
                    randomAccessFile.write(data, 0, streamSize);
                    ret = true;
                    break;
            }

            randomAccessFile.close();
            return ret;
        } catch (PDFException | IOException e) {
            e.printStackTrace();
        }finally {
        }
        return false;
    }

    private static byte[] intToByteArray(int value)  throws PDFException {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }

    private static byte[] shortToByteArray(short value)  throws PDFException {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF)
        };
    }	
	
    public static Annot GetAnnotWithType(PDFPage pdf_page, int annot_type, int annot_index_with_type)  throws PDFException 
    {
        Annot ret_annot = null;
        if (pdf_page.isEmpty() || (int)annot_type < -1) return ret_annot;
        int annot_count = pdf_page.getAnnotCount();
        int spec_annot_index = -1;
        for (int i = 0; i < annot_count; i++)
        {
            Annot annot_i = pdf_page.getAnnot(i);
            int cmp_index = -1;

            if (-1 == (int)annot_type)
            {
                cmp_index = i;
            }
            else if (annot_i.getType() == annot_type)
            {
                spec_annot_index++;
                cmp_index = spec_annot_index;
            }
            if (cmp_index == annot_index_with_type)
            {
                ret_annot = annot_i;
                break;
            }
        }
        return ret_annot;
    }

    public static Sound GetSoundAnnot(String input_pdf_path, int page_index)  throws PDFException 
    {
        Sound sound = null;
        PDFDoc doc = new PDFDoc(input_pdf_path);
		doc.load(null);
        PDFPage pdf_page = doc.getPage(page_index);
        if (pdf_page.isEmpty())
        {
            return sound;
        }

        Annot annot = GetAnnotWithType(pdf_page, Annot.e_Sound, 0);
        if (annot.isEmpty())
        {
            return sound;
        }
        if (annot.getType() != Annot.e_Sound)
        {
            return sound;
        }
        sound = new Sound(annot);

        return sound;
    }	
	
    public static void AddAnnotsWithLowLevelAPI(PDFPage page) throws PDFException {
        {
            // Add line annotation
            PDFDictionary annot_dict = PDFDictionary.create();
            annot_dict.setAtName("Type", "Annot");
            annot_dict.setAtName("Subtype", "Line");
            annot_dict.setAtName("IT", "LineArrow");
            annot_dict.setAtInteger("F", 4);
            annot_dict.setAtString("Contents", "A line arrow annotation.");
            annot_dict.setAtString("T", "Foxit SDK");
            annot_dict.setAtString("Subj", "Arrow");
            annot_dict.setAtString("NM", RandomUID());
            annot_dict.setAtDateTime("CreationDate", GetLocalDateTime());
            annot_dict.setAtDateTime("M", GetLocalDateTime());
            annot_dict.setAtRect("Rect", new RectF(0, 650, 100, 750));
            PDFArray coords_array = PDFArray.create();
            coords_array.addFloat(20);
            coords_array.addFloat(670);
            coords_array.addFloat(80);
            coords_array.addFloat(730);
            annot_dict.setAt("L", coords_array);

            PDFArray line_style = PDFArray.create();
            line_style.addName("None");
            line_style.addName("OpenArrow");
            annot_dict.setAt("LE", line_style);
            PDFDictionary border_dict = PDFDictionary.create();
            border_dict.setAtFloat("W", 2.0f);
            border_dict.setAtName("S", "S");
            border_dict.setAtName("Type", "Border");
            annot_dict.setAt("BS", border_dict);
            PDFArray color_array = PDFArray.create();
            color_array.addFloat(1);
            color_array.addFloat(1);
            color_array.addFloat(0);
            annot_dict.setAt("C", color_array);
            Annot annot = page.addAnnot(annot_dict);
            annot.resetAppearanceStream();
            System.out.println("Add a line annotation by lower level API.");
        }
		
        {
            // Add line annotation with caption
            PDFDictionary annot_dict = PDFDictionary.create();
            annot_dict.setAtName("Type", "Annot");
            annot_dict.setAtName("Subtype", "Line");
            annot_dict.setAtName("IT", "LineArrow");
            annot_dict.setAtInteger("F", 4);
            annot_dict.setAtString("Contents", "A common line.");
            annot_dict.setAtString("T", "Foxit SDK");
            annot_dict.setAtString("Subj", "Line");
            annot_dict.setAtString("NM", RandomUID());
            annot_dict.setAtDateTime("CreationDate", GetLocalDateTime());
            annot_dict.setAtDateTime("M", GetLocalDateTime());
            annot_dict.setAtRect("Rect", new RectF(0, 650, 100, 750));
            annot_dict.setAtBoolean("Cap", true);
            PDFArray caption_offset = PDFArray.create();
            caption_offset.addFloat(0);
            caption_offset.addFloat(5);
            annot_dict.setAt("CO", caption_offset);
            PDFArray coords_array = PDFArray.create();
            coords_array.addFloat(40);
            coords_array.addFloat(670);
            coords_array.addFloat(100);
            coords_array.addFloat(730);
            annot_dict.setAt("L", coords_array);

            PDFArray line_style = PDFArray.create();
            line_style.addName("Square");
            line_style.addName("OpenArrow");
            annot_dict.setAt("LE", line_style);
            PDFDictionary border_dict = PDFDictionary.create();
            border_dict.setAtFloat("W", 2.0f);
            border_dict.setAtName("S", "S");
            border_dict.setAtName("Type", "Border");
            annot_dict.setAt("BS", border_dict);
            PDFArray color_array = PDFArray.create();
            color_array.addFloat(0);
            color_array.addFloat(1);
            color_array.addFloat(0);
            annot_dict.setAt("C", color_array);
            Annot annot = page.addAnnot(annot_dict);
            annot.resetAppearanceStream();
            System.out.println("Add a line annotation with caption by lower level API.");
        }
		
        {
            // Add circle annotation
            PDFDictionary annot_dict = PDFDictionary.create();
            annot_dict.setAtName("Type", "Annot");
            annot_dict.setAtName("Subtype", "Circle");
            annot_dict.setAtInteger("F", 4);
            annot_dict.setAtString("T", "Foxit SDK");
            annot_dict.setAtString("Subj", "Circle");
            annot_dict.setAtString("NM", RandomUID());
            annot_dict.setAtDateTime("CreationDate", GetLocalDateTime());
            annot_dict.setAtDateTime("M", GetLocalDateTime());
            annot_dict.setAtRect("Rect", new RectF(120, 650, 180, 750));

            PDFDictionary border_dict = PDFDictionary.create();
            border_dict.setAtFloat("W", 2.0f);
            border_dict.setAtName("S", "S");
            border_dict.setAtName("Type", "Border");
            annot_dict.setAt("BS", border_dict);
            PDFArray color_array = PDFArray.create();
            color_array.addFloat(0);
            color_array.addFloat(1);
            color_array.addFloat(0);
            annot_dict.setAt("C", color_array);
            Annot annot = page.addAnnot(annot_dict);
            annot.resetAppearanceStream();
            System.out.println("Add a circle annotation by lower level API.");
        }
		
        {
            // Add square annotation
            PDFDictionary annot_dict = PDFDictionary.create();
            annot_dict.setAtName("Type", "Annot");
            annot_dict.setAtName("Subtype", "Square");
            annot_dict.setAtInteger("F", 4);
            annot_dict.setAtString("T", "Foxit SDK");
            annot_dict.setAtString("Subj", "Square");
            annot_dict.setAtString("NM", RandomUID());
            annot_dict.setAtDateTime("CreationDate", GetLocalDateTime());
            annot_dict.setAtDateTime("M", GetLocalDateTime());
            annot_dict.setAtRect("Rect", new RectF(200, 650, 300, 750));

            PDFDictionary border_dict = PDFDictionary.create();
            border_dict.setAtFloat("W", 2.0f);
            border_dict.setAtName("S", "S");
            border_dict.setAtName("Type", "Border");
            annot_dict.setAt("BS", border_dict);
            PDFArray interior_color_array = PDFArray.create();
            interior_color_array.addFloat(1);
            interior_color_array.addFloat(1);
            interior_color_array.addFloat(0);
            annot_dict.setAt("IC", interior_color_array);
            PDFArray color_array = PDFArray.create();
            color_array.addFloat(0);
            color_array.addFloat(1);
            color_array.addFloat(0);
            annot_dict.setAt("C", color_array);
            Annot annot = page.addAnnot(annot_dict);
            annot.resetAppearanceStream();
            System.out.println("Add a square annotation by lower level API.");
        }
    }

    public static void AddAnnotations(PDFPage page) throws PDFException {
        // Add line annotation 
        // No special intent, as a common line.
        Annot annot = page.addAnnot(Annot.e_Line, new RectF(0, 650, 100, 750));
        Line line = new Line(annot);
        //This flag is used for printing annotations.
        line.setFlags(4);
        line.setStartPoint(new PointF(20, 650));
        line.setEndPoint(new PointF(100, 740));
        // Intent, as line arrow.
        line.setContent("A line arrow annotation");
        line.setIntent("LineArrow");
        line.setSubject("Arrow");
        line.setTitle("Foxit SDK");
        line.setCreationDateTime(GetLocalDateTime());
        line.setModifiedDateTime(GetLocalDateTime());
        line.setUniqueID(RandomUID());
        // Appearance should be reset.
        line.resetAppearanceStream();

        line = new Line(page.addAnnot(Annot.e_Line, new RectF(0, 650, 100, 760)));
        //This flag is used for printing annotations.
        line.setFlags(4);
        // Set foxit RGB color
        line.setBorderColor(0x00FF00);
        line.setStartPoint(new PointF(10, 650));
        line.setEndPoint(new PointF(100, 750));
        line.setContent("A common line.");
        line.setLineStartStyle(Markup.e_EndingStyleSquare);
        line.setLineEndStyle(Markup.e_EndingStyleOpenArrow);
        // Show text in line
        line.enableCaption(true);
        line.setCaptionOffset(new PointF(0, 5));

        line.setSubject("Line");
        line.setTitle("Foxit SDK");
        line.setCreationDateTime(GetLocalDateTime());
        line.setModifiedDateTime(GetLocalDateTime());
        line.setUniqueID(RandomUID());
        // Appearance should be reset.
        line.resetAppearanceStream();
        System.out.println("Add a line annotation.");

        // Add circle annotation
        annot = page.addAnnot(Annot.e_Circle, new RectF(100, 650, 200, 750));
        Circle circle = new Circle(annot);
        //This flag is used for printing annotations.
        circle.setFlags(4);
        circle.setInnerRect(new RectF(120, 660, 160, 740));
        circle.setSubject("Circle");
        circle.setTitle("Foxit SDK");
        circle.setCreationDateTime(GetLocalDateTime());
        circle.setModifiedDateTime(GetLocalDateTime());
        circle.setUniqueID(RandomUID());
        // Appearance should be reset.
        circle.resetAppearanceStream();
        System.out.println("Add a circle annotation.");

        // Add square annotation
        annot = page.addAnnot(Annot.e_Square, new RectF(200, 650, 300, 750));
        Square square = new Square(annot);
        //This flag is used for printing annotations.
        square.setFlags(4);
        square.setFillColor(0x00FF00);
        square.setInnerRect(new RectF(220, 660, 260, 740));
        square.setSubject("Square");
        square.setTitle("Foxit SDK");
        square.setCreationDateTime(GetLocalDateTime());
        square.setModifiedDateTime(GetLocalDateTime());
        square.setUniqueID(RandomUID());
        // Appearance should be reset.
        square.resetAppearanceStream();
        System.out.println("Add a square annotation.");

        // Add polygon annotation, as cloud.
        annot = page.addAnnot(Annot.e_Polygon, new RectF(300, 650, 500, 750));
        Polygon polygon = new Polygon(annot);
        //This flag is used for printing annotations.
        polygon.setFlags(4);
        polygon.setIntent("PolygonCloud");
        polygon.setFillColor(0x0000FF);
        PointFArray vertexe_array = new PointFArray();
        vertexe_array.add(new PointF(335, 665));
        vertexe_array.add(new PointF(365, 665));
        vertexe_array.add(new PointF(385, 705));
        vertexe_array.add(new PointF(365, 740));
        vertexe_array.add(new PointF(335, 740));
        vertexe_array.add(new PointF(315, 705));
        polygon.setVertexes(vertexe_array);
        polygon.setSubject("Cloud");
        polygon.setTitle("Foxit SDK");
        polygon.setCreationDateTime(GetLocalDateTime());
        polygon.setModifiedDateTime(GetLocalDateTime());
        polygon.setUniqueID(RandomUID());
        // Appearance should be reset.
        polygon.resetAppearanceStream();
        System.out.println("Add a polygon annotation.");

        // Add polygon annotation, with dashed border.
        BorderInfo borderinfo = new BorderInfo();
        borderinfo.setCloud_intensity(2.0f);
        borderinfo.setWidth(2.0f);
        borderinfo.setStyle(BorderInfo.e_Dashed);
        borderinfo.setDash_phase(3.0f);

        FloatArray floatArray = new FloatArray();
        floatArray.add(2.0f);
        floatArray.add(2.0f);
        borderinfo.setDashes(floatArray);

        annot = page.addAnnot(Annot.e_Polygon, new RectF(400, 650, 500, 750));
        polygon = new Polygon(annot);
        //This flag is used for printing annotations.
        polygon.setFlags(4);
        polygon.setFillColor(0x0000FF);
        polygon.setBorderInfo(borderinfo);
        vertexe_array = new PointFArray();
        vertexe_array.add(new PointF(435, 665));
        vertexe_array.add(new PointF(465, 665));
        vertexe_array.add(new PointF(485, 705));
        vertexe_array.add(new PointF(465, 740));
        vertexe_array.add(new PointF(435, 740));
        vertexe_array.add(new PointF(415, 705));

        polygon.setVertexes(vertexe_array);
        polygon.setSubject("Polygon");
        polygon.setTitle("Foxit SDK");
        polygon.setCreationDateTime(GetLocalDateTime());
        polygon.setModifiedDateTime(GetLocalDateTime());
        polygon.setUniqueID(RandomUID());
        // Appearance should be reset.
        polygon.resetAppearanceStream();
        System.out.println("Add a polygon cloud annotation.");

        // Add polyline annotation 
        annot = page.addAnnot(Annot.e_PolyLine, new RectF(500, 650, 600, 700));
        PolyLine polyline = new PolyLine(annot);
        //This flag is used for printing annotations.
        polyline.setFlags(4);
        vertexe_array = new PointFArray();
        vertexe_array.add(new PointF(515, 705));
        vertexe_array.add(new PointF(535, 740));
        vertexe_array.add(new PointF(565, 740));
        vertexe_array.add(new PointF(585, 705));
        vertexe_array.add(new PointF(565, 665));
        vertexe_array.add(new PointF(535, 665));
        polyline.setVertexes(vertexe_array);
        polyline.setSubject("PolyLine");
        polyline.setTitle("Foxit SDK");
        polyline.setCreationDateTime(GetLocalDateTime());
        polyline.setModifiedDateTime(GetLocalDateTime());
        polyline.setUniqueID(RandomUID());
        // Appearance should be reset.
        polyline.resetAppearanceStream();
        System.out.println("Add a polyline annotation.");

        {
            // Add freetext annotation, as type writer
            annot = page.addAnnot(Annot.e_FreeText, new RectF(10, 550, 200, 600));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelvetica));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentLeft);
            freetext.setIntent("FreeTextTypewriter");
            freetext.setContent("A typewriter annotation");
            freetext.setSubject("FreeTextTypewriter");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a typewriter freetext annotation.");
        }
        {
            // Add freetext annotation, as call-out
            annot = page.addAnnot(Annot.e_FreeText, new RectF(300, 550, 400, 600));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelveticaB));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentCenter);
            freetext.setIntent("FreeTextCallout");
            PointFArray callout_points = new PointFArray();
            callout_points.add(new PointF(250, 540));
            callout_points.add(new PointF(280, 570));
            callout_points.add(new PointF(300, 570));

            freetext.setCalloutLinePoints(callout_points);
            freetext.setCalloutLineEndingStyle(Markup.e_EndingStyleOpenArrow);
            freetext.setContent("A callout annotation.");
            freetext.setSubject("FreeTextCallout");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a callout freetext annotation.");
        }
        {
            // Add freetext annotation, as text box
            annot = page.addAnnot(Annot.e_FreeText, new RectF(450, 550, 550, 600));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelveticaI));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentCenter);
            freetext.setContent("A text box annotation.");
            freetext.setSubject("Textbox");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a text box freetext annotation.");
        }
        // Add highlight annotation
        Highlight highlight = new Highlight(page.addAnnot(Annot.e_Highlight, new RectF(10, 450, 100, 550)));
        //This flag is used for printing annotations.
        highlight.setFlags(4);
        highlight.setContent("Highlight");
        QuadPoints quad_points = new QuadPoints();
        quad_points.setFirst(new PointF(10, 500));
        quad_points.setSecond(new PointF(90, 500));
        quad_points.setThird(new PointF(10, 480));
        quad_points.setFourth(new PointF(90, 480));
        QuadPointsArray quad_points_array = new QuadPointsArray();
        quad_points_array.add(quad_points);
        highlight.setQuadPoints(quad_points_array);
        highlight.setSubject("Highlight");
        highlight.setTitle("Foxit SDK");
        highlight.setCreationDateTime(GetLocalDateTime());
        highlight.setModifiedDateTime(GetLocalDateTime());
        highlight.setUniqueID(RandomUID());
        // Appearance should be reset.
        highlight.resetAppearanceStream();
        System.out.println("Add a highlight annotation.");

        // Add underline annotation
        Underline underline = new Underline(page.addAnnot(Annot.e_Underline, new RectF(100, 450, 200, 550)));
        //This flag is used for printing annotations.
        underline.setFlags(4);
        quad_points = new QuadPoints();
        quad_points.setFirst(new PointF(110, 500));
        quad_points.setSecond(new PointF(190, 500));
        quad_points.setThird(new PointF(110, 480));
        quad_points.setFourth(new PointF(190, 480));
        quad_points_array = new QuadPointsArray();
        quad_points_array.add(quad_points);
        underline.setQuadPoints(quad_points_array);
        underline.setSubject("Underline");
        underline.setTitle("Foxit SDK");
        underline.setCreationDateTime(GetLocalDateTime());
        underline.setModifiedDateTime(GetLocalDateTime());
        underline.setUniqueID(RandomUID());
        // Appearance should be reset.
        underline.resetAppearanceStream();
        System.out.println("Add a underline annotation.");

        // Add squiggly annotation
        Squiggly squiggly = new Squiggly(page.addAnnot(Annot.e_Squiggly, new RectF(200, 450, 300, 550)));
        squiggly.setIntent("Squiggly");
        //This flag is used for printing annotations.
        squiggly.setFlags(4);
        quad_points = new QuadPoints();
        quad_points.setFirst(new PointF(210, 500));
        quad_points.setSecond(new PointF(290, 500));
        quad_points.setThird(new PointF(210, 480));
        quad_points.setFourth(new PointF(290, 480));
        quad_points_array = new QuadPointsArray();
        quad_points_array.add(quad_points);
        squiggly.setQuadPoints(quad_points_array);
        squiggly.setSubject("Squiggly");
        squiggly.setTitle("Foxit SDK");
        squiggly.setCreationDateTime(GetLocalDateTime());
        squiggly.setModifiedDateTime(GetLocalDateTime());
        squiggly.setUniqueID(RandomUID());
        // Appearance should be reset.
        squiggly.resetAppearanceStream();
        System.out.println("Add a squiggly annotation.");

        // Add strikeout annotation
        StrikeOut strikeout = new StrikeOut(page.addAnnot(Annot.e_StrikeOut, new RectF(300, 450, 400, 550)));
        //This flag is used for printing annotations.
        strikeout.setFlags(4);
        quad_points = new QuadPoints();
        quad_points.setFirst(new PointF(310, 500));
        quad_points.setSecond(new PointF(390, 500));
        quad_points.setThird(new PointF(310, 480));
        quad_points.setFourth(new PointF(390, 480));
        quad_points_array = new QuadPointsArray();
        quad_points_array.add(quad_points);
        strikeout.setQuadPoints(quad_points_array);
        strikeout.setSubject("StrikeOut");
        strikeout.setTitle("Foxit SDK");
        strikeout.setCreationDateTime(GetLocalDateTime());
        strikeout.setModifiedDateTime(GetLocalDateTime());
        strikeout.setUniqueID(RandomUID());
        // Appearance should be reset.
        strikeout.resetAppearanceStream();
        System.out.println("Add a strikeout annotation.");

        // Add caret annotation
        Caret caret = new Caret(page.addAnnot(Annot.e_Caret, new RectF(400, 450, 420, 470)));
        //This flag is used for printing annotations.
        caret.setFlags(4);
        caret.setInnerRect(new RectF(410, 450, 430, 470));
        caret.setContent("Caret annotation");
        caret.setSubject("Caret");
        caret.setTitle("Foxit SDK");
        caret.setCreationDateTime(GetLocalDateTime());
        caret.setModifiedDateTime(GetLocalDateTime());
        caret.setUniqueID(RandomUID());
        // Appearance should be reset.
        caret.resetAppearanceStream();
        System.out.println("Add a caret annotation.");

        // Add note annotation
        Note note = new Note(page.addAnnot(Annot.e_Note, new RectF(10, 350, 50, 400)));
        //This flag is used for printing annotations.
        note.setFlags(4);
        note.setIconName("Comment");
        note.setSubject("Note");
        note.setTitle("Foxit SDK");
        note.setContent("Note annotation.");
        note.setCreationDateTime(GetLocalDateTime());
        note.setModifiedDateTime(GetLocalDateTime());
        note.setUniqueID(RandomUID());
        // Add popup to note annotation
        Popup popup = new Popup(page.addAnnot(Annot.e_Popup, new RectF(300, 450, 500, 550)));
        popup.setBorderColor(0x00FF00);
        popup.setOpenStatus(false);
        popup.setModifiedDateTime(GetLocalDateTime());
        note.setPopup(popup);

        // Add reply annotation to note annotation
        Note reply = note.addReply();
        reply.setContent("reply");
        reply.setModifiedDateTime(GetLocalDateTime());
        reply.setTitle("Foxit SDK");
        reply.setUniqueID(RandomUID());

        // Add state annotation to note annotation
        Note state = new Note(note.addStateAnnot("Foxit SDK", Markup.e_StateModelReview, Markup.e_StateAccepted));
        state.setContent("Accepted set by Foxit SDK");
        state.setUniqueID(RandomUID());
        // Appearance should be reset.
        note.resetAppearanceStream();
        System.out.println("Add a note annotation.");

        // Add ink annotation
        Ink ink = new Ink(page.addAnnot(Annot.e_Ink, new RectF(100, 350, 200, 450)));
        //This flag is used for printing annotations.
        ink.setFlags(4);
        Path inklist = new Path();
        float width = 100;
        float height = 100;
        float out_width = Math.min(width, height) * 2 / 3.f;
        float inner_width = (float) (out_width * (Math.sin(18.f / 180.f * 3.14f)) / (Math.sin(36.f / 180.f * 3.14f)));
        PointF center = new PointF(150, 400);
        float x = out_width;
        float y = 0;
        inklist.moveTo(new PointF(center.getX() + x, center.getY() + y));
        for (int i = 0; i < 5; i++) {
            x = (float) (out_width * Math.cos(72.f * i / 180.f * 3.14f));
            y = (float) (out_width * Math.sin(72.f * i / 180.f * 3.14f));
            inklist.lineTo(new PointF(center.getX() + x, center.getY() + y));

            x = (float) (inner_width * Math.cos((72.f * i + 36) / 180.f * 3.14f));
            y = (float) (inner_width * Math.sin((72.f * i + 36) / 180.f * 3.14f));
            inklist.lineTo(new PointF(center.getX() + x, center.getY() + y));
        }
        inklist.lineTo(new PointF(center.getX() + out_width, center.getY() + 0));
        inklist.closeFigure();
        ink.setInkList(inklist);
        ink.setSubject("Ink");
        ink.setTitle("Foxit SDK");
        ink.setContent("Note annotation.");
        ink.setCreationDateTime(GetLocalDateTime());
        ink.setModifiedDateTime(GetLocalDateTime());
        ink.setUniqueID(RandomUID());
        // Appearance should be reset.
        ink.resetAppearanceStream();
        System.out.println("Add an ink annotation.");

        // Add file attachment annotation
        String pdf_file = input_path + "AboutFoxit.pdf";
        FileAttachment file_attachment = new FileAttachment(
                page.addAnnot(Annot.e_FileAttachment, new RectF(280, 350, 300, 380)));
        //This flag is used for printing annotations.
        file_attachment.setFlags(4);
        file_attachment.setIconName("Graph");

        FileSpec file_spec = new FileSpec(page.getDocument());
        file_spec.setFileName("attachment.pdf");
        file_spec.setCreationDateTime(GetLocalDateTime());
        file_spec.setDescription("The original file");
        file_spec.setModifiedDateTime(GetLocalDateTime());
        file_spec.embed(pdf_file);
        file_attachment.setFileSpec(file_spec);
        file_attachment.setSubject("File Attachment");
        file_attachment.setTitle("Foxit SDK");
        // Appearance should be reset.
        file_attachment.resetAppearanceStream();
        System.out.println("Add an attachment  annotation.");

        // Add link annotation
        Link link = new Link(page.addAnnot(Annot.e_Link, new RectF(350, 350, 380, 400)));
        link.setHighlightingMode(Annot.e_HighlightingPush);
        //This flag is used for printing annotations.
        link.setFlags(4);
        // Add action for link annotation
        Action action = Action.create(page.getDocument(), Action.e_TypeURI);
        URIAction uriAction = new URIAction(action);
        uriAction.setTrackPositionFlag(true);
        uriAction.setURI("www.foxitsoftware.com");
        link.setAction(uriAction);
        // Appearance should be reset.
        link.resetAppearanceStream();
        System.out.println("Add a link  annotation.");


        // Set icon provider for annotation to Foxit PDF SDK.
        MyIconProvider icon_provider = new MyIconProvider(input_path+"Stamps/");
        Library.setAnnotIconProviderCallback(icon_provider);
        
        // Add common stamp annotation.
        Library.setActionCallback(null);
        icon_provider.setUseDynamicStamp(false);
        annot = page.addAnnot(Annot.e_Stamp, new RectF(110, 150, 200, 250));
        Stamp static_stamp = new Stamp(annot);
        //This flag is used for printing annotations.
        static_stamp.setFlags(4);
        static_stamp.setIconName("Approved");    
        // Appearance should be reset.
        static_stamp.resetAppearanceStream();
        
        // Add dynamic stamp annotation.
        MyActionCallback action_callback = new MyActionCallback(page.getDocument());
        Library.setActionCallback(action_callback);
        icon_provider.setUseDynamicStamp(true);
        annot = page.addAnnot(Annot.e_Stamp, new RectF(10,150,100,250));
        Stamp dynamic_stamp = new Stamp(annot);
        //This flag is used for printing annotations.
        dynamic_stamp.setFlags(4);
        dynamic_stamp.setIconName("Approved");
        // Appearance should be reset.
        dynamic_stamp.resetAppearanceStream();
        System.out.println("Add stamp annotation.");
        
        {
            // Add freetext annotation with richtext, as type writer
            annot = page.addAnnot(Annot.e_FreeText, new RectF(10, 50, 200, 100));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelvetica));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentLeft);
            freetext.setIntent("FreeTextTypewriter");
            freetext.setContent("A typewriter annotation");
            freetext.setSubject("FreeTextTypewriter");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());

            RichTextStyle richtext_style = new RichTextStyle();
            String os = System.getProperty("os.name");  
            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Times New Roman", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSerif", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0xFF0000);
            richtext_style.setText_size(10);
            freetext.addRichText("Typewriter annotation ", richtext_style);

            richtext_style.setText_color(0x00FF00);
            richtext_style.setIs_underline(true);
            freetext.addRichText("1-underline ", richtext_style);

            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Calibri", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSans", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0x0000FF);
            richtext_style.setIs_underline(false);
            richtext_style.setIs_strikethrough(true);
            int richtext_count = freetext.getRichTextCount();
            freetext.insertRichText(richtext_count-1, "2_strikethrough ", richtext_style);    
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a typewriter freetext annotation with richtext.");
        }
        {
            // Add freetext annotation with richtext, as call-out
            annot = page.addAnnot(Annot.e_FreeText, new RectF(300, 50, 400, 100));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelveticaB));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentCenter);
            freetext.setIntent("FreeTextCallout");
            PointFArray callout_points = new PointFArray();
            callout_points.add(new PointF(250, 60));
            callout_points.add(new PointF(280, 80));
            callout_points.add(new PointF(300, 80));

            freetext.setCalloutLinePoints(callout_points);
            freetext.setCalloutLineEndingStyle(Markup.e_EndingStyleOpenArrow);
            freetext.setContent("A callout annotation.");
            freetext.setSubject("FreeTextCallout");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());
            
            RichTextStyle richtext_style = new RichTextStyle();
            String os = System.getProperty("os.name");  
            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Times New Roman", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSerif", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0xFF0000);
            richtext_style.setText_size(10);
            freetext.addRichText("Callout annotation ", richtext_style);

            richtext_style.setText_color(0x00FF00);
            richtext_style.setIs_underline(true);
            freetext.addRichText("1-underline ", richtext_style);

            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Calibri", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSans", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0x0000FF);
            richtext_style.setIs_underline(false);
            richtext_style.setIs_strikethrough(true);
            int richtext_count = freetext.getRichTextCount();
            freetext.insertRichText(richtext_count-1, "2_strikethrough ", richtext_style);
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a callout freetext annotation with richtext.");
        }
        {
            // Add freetext annotation with richtext, as text box
            annot = page.addAnnot(Annot.e_FreeText, new RectF(450, 50, 550, 100));
            FreeText freetext = new FreeText(annot);
            //This flag is used for printing annotations.
            freetext.setFlags(4);
            // Set default appearance
            DefaultAppearance default_ap = new DefaultAppearance();
            default_ap.setFlags(
                    DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize | DefaultAppearance.e_FlagTextColor);
            default_ap.setFont(new Font(Font.e_StdIDHelveticaI));
            default_ap.setText_size(12.0f);
            default_ap.setText_color(0x000000);
            // Set default appearance for form.
            freetext.setDefaultAppearance(default_ap);
            freetext.setAlignment(Constants.e_AlignmentCenter);
            freetext.setContent("A text box annotation.");
            freetext.setSubject("Textbox");
            freetext.setTitle("Foxit SDK");
            freetext.setCreationDateTime(GetLocalDateTime());
            freetext.setModifiedDateTime(GetLocalDateTime());
            freetext.setUniqueID(RandomUID());
            RichTextStyle richtext_style = new RichTextStyle();
            String os = System.getProperty("os.name");  
            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Times New Roman", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSerif", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0xFF0000);
            richtext_style.setText_size(10);
            freetext.addRichText("Textbox annotation ", richtext_style);

            richtext_style.setText_color(0x00FF00);
            richtext_style.setIs_underline(true);
            freetext.addRichText("1-underline ", richtext_style);

            if(os.toLowerCase().startsWith("win")){  
                richtext_style.setFont(new Font("Calibri", 0, Font.e_CharsetANSI, 0));
            }else{ // linux
                richtext_style.setFont(new Font("FreeSans", 0, Font.e_CharsetANSI, 0));
            } 
            richtext_style.setText_color(0x0000FF);
            richtext_style.setIs_underline(false);
            richtext_style.setIs_strikethrough(true);
            int richtext_count = freetext.getRichTextCount();
            freetext.insertRichText(richtext_count-1, "2_strikethrough ", richtext_style);
            // Appearance should be reset.
            freetext.resetAppearanceStream();
            System.out.println("Add a text box freetext annotation with richtext.");
        }
        {
            //Add screen annotation.
            annot = page.addAnnot(Annot.e_Screen, new RectF(300, 150, 400, 200));
            Screen screen = new Screen(annot);
            //This flag is used for printing annotations.
            screen.setFlags(4);
            Image image = new Image(input_path + "FoxitLogo.jpg");
            screen.setTitle("Foxit SDK");
            screen.setBorderColor(0x0000FF);
            screen.setUniqueID(RandomUID());
            screen.setBorderInfo(new BorderInfo(2,BorderInfo.e_Solid,0,0,new FloatArray()));
            screen.setImage(image, 0, 1);
            
            //Prepare rendition action
            RenditionAction rendition_action = new RenditionAction(Action.create(page.getDocument(), Action.e_TypeRendition));
            rendition_action.setOperationType(RenditionAction.e_OpTypeAssociate);
            rendition_action.setScreenAnnot(screen);
            
            //Prepare rendition
            Rendition rendition = new Rendition(page.getDocument(), null);
            rendition.setRenditionName("screen for rendition");
            rendition.setMediaClipName("ytb210-Mopa.mp4");
            rendition.setPermission(Rendition.e_MediaPermTempAccess);
            FileSpec video_filespec = new FileSpec(page.getDocument());
            video_filespec.setFileName("ytb210-Mopa.mp4");
            video_filespec.embed(input_path +"ytb210-Mopa.mp4");
            rendition.setMediaClipFile(video_filespec);
            rendition.setMediaClipContentType("video/mp4");
            rendition_action.insertRendition(rendition, 0);
            screen.setAction(rendition_action);
            
            //Appearance should be reset.
            screen.resetAppearanceStream();
            System.out.println("Add a screen annotation.");
        }
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
    
    private static void createResultFolder(String output_path) {
        File myPath = new File(output_path);
        if (!myPath.exists()) {
            myPath.mkdir();
        }
    }
}
