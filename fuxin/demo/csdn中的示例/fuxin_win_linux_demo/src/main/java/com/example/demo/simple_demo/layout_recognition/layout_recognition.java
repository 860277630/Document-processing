// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to to call layout recognition related classes
// for PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Image;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.Path;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.common.Progressive;
import com.foxit.sdk.common.Range;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.graphics.*;
import com.foxit.sdk.pdf.objects.PDFDictionary;
import com.foxit.sdk.addon.layoutrecognition.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.common.Constants.e_FillModeAlternate;
import static com.foxit.sdk.common.Font.*;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;
import static com.foxit.sdk.pdf.graphics.GraphicsObject.*;
import static com.foxit.sdk.addon.layoutrecognition.LRElement.*;
import static com.foxit.sdk.addon.layoutrecognition.LRStructureElement.*;


public class layout_recognition {

    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/layout_recognition/";
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

    private static void getChildFromElement(LRStructureElement pElement, ArrayList<LRElement> elemArray, int nType) throws PDFException {
        int nElemListSize = pElement.getChildCount();
        for (int i=0; i<nElemListSize; i++) {
            LRElement item = pElement.getChild(i);
            int item_type = item.getElementType();
            if (item_type == nType) 
                elemArray.add(item);
            if (item.isStructureElement()) {
                LRStructureElement srt_item = new LRStructureElement(item);
                getChildFromElement(srt_item, elemArray, nType);
            }
        }
    }

    public static void writeTextWithPrefix(OutputStreamWriter doc, int depth, String prefix, String content) throws IOException {
        for (int i = 0; i < depth; i++) {
            doc.write(prefix);
        }
        doc.write(content);
    } 

    private static String lr_Format(boolean val) {
        return val ? "True" : "False";
    }

    private static String lr_Format(int val) {
        return String.format("%d", val);
    }

    private static String lr_Format(float val) {
        return String.format("%.1f", val);
    }

    private static String lr_Format(long val) {
        return String.format("#%02X%02X%02X", (byte)(val >> 16), (byte)(val >> 8), (byte)(val));
    }

    private static String lr_FormatAttributeValueEnum(int val) {
        return LRStructureElement.stringifyEnumVariant(val);
    }

    private static void OutputLRStructureElement(LRStructureElement element, OutputStreamWriter doc, int depth) throws PDFException, IOException {
        if (element.isEmpty())
            return;

        LRStructureElement parentElement = element.getParentElement();
        boolean bIsEmpty = parentElement.isEmpty();
        
        int elemType = element.getElementType();
        String elementTypeStr = LRStructureElement.stringifyElementType(elemType);
        String outputStr = "< StructureElement: " + elementTypeStr + " >\r\n";
        writeTextWithPrefix(doc, depth, "\t", outputStr);
        
        int nSize = element.getSupportedAttributeCount();
        for (int i=0; i<nSize; i++) {
            int attrType = element.getSupportedAttribute(i);
            
            int attrValueType = element.getAttributeValueType(attrType);
            int nLength = 0;
            boolean bIsArray = LRStructureElement.isArrayAttributeValueType(attrValueType);
            nLength = element.getAttributeValueCount(attrType);
            if (attrValueType == LRStructureElement.e_AttributeValueTypeEmpty)
                continue;
        
            String szKey = LRStructureElement.stringifyAttributeType(attrType);
            String szVal = "";
            if (bIsArray) szVal += "[";
        
            for (int idx = 0; idx < nLength; idx++)
            {
                if (idx > 0) szVal += ", ";
        
                switch (attrValueType)
                {
                case LRStructureElement.e_AttributeValueTypeEnum:
                case LRStructureElement.e_AttributeValueTypeEnumArray:
                    szVal += lr_FormatAttributeValueEnum(element.getAttributeValueEnum(attrType, idx));
                    break;
                case LRStructureElement.e_AttributeValueTypeInt32:
                case LRStructureElement.e_AttributeValueTypeInt32Array:
                    szVal += lr_Format(element.getAttributeValueInt32(attrType, idx));
                    break;
                case LRStructureElement.e_AttributeValueTypeFloat:
                case LRStructureElement.e_AttributeValueTypeFloatArray:
                    szVal += lr_Format(element.getAttributeValueFloat(attrType, idx));
                    break;
                case LRStructureElement.e_AttributeValueTypeARGB:
                case LRStructureElement.e_AttributeValueTypeARGBArray:
                    szVal += lr_Format(element.getAttributeValueARGB(attrType, idx));
                    break;
                case LRStructureElement.e_AttributeValueTypeWStr:
                case LRStructureElement.e_AttributeValueTypeWStrArray:
                    szVal += element.getAttributeValueString(attrType, idx);
                    break;
                }
            }
            if (bIsArray) szVal += "]";
        
            outputStr = szKey + ": " + szVal + "\r\n";
            writeTextWithPrefix(doc, depth, "\t", outputStr);
        }
    }

    private static void OutputLRStructureElement(LRGraphicsObjectElement element, OutputStreamWriter doc, int depth) throws PDFException, IOException {
        if (element.isEmpty())
            return;
        
        String outputStr = "< LRGraphicsObjectElement: >\r\n";
        LRGraphicsObjectElement parentPageObj = element.getParentGraphicsObjectElement();
        boolean bIsEmpty = parentPageObj.isEmpty();
        writeTextWithPrefix(doc, depth, "\t", outputStr);
        GraphicsObject pageObj = element.getGraphicsObject();
        int type = 0;
		if (pageObj != null)
		  type = pageObj.getType();
        RectF rcBox = element.getBBox();
        PDFDictionary pDict = element.getDict();
        int objIndex = element.getGraphicsObjectIndex();
        Matrix2D matrix = element.getMatrix();
        String szVal = "";
        szVal = String.format("BBox: [%.1f,%.1f,%.1f,%.1f]\r\n", rcBox.getLeft(), rcBox.getTop(), rcBox.getRight(), rcBox.getBottom());
        writeTextWithPrefix(doc, depth, "\t", szVal);
        szVal = String.format("Matrix: [%.1f,%.1f,%.1f,%.1f,%.1f,%.1f]\r\n", matrix.getA(), matrix.getB(), matrix.getC(), matrix.getD(), matrix.getE(), matrix.getF());
        writeTextWithPrefix(doc, depth, "\t", szVal);
        szVal = String.format("PageObjectIndex: %d\r\n", objIndex);
        writeTextWithPrefix(doc, depth, "\t", szVal);

        String text = (type == (int)GraphicsObject.e_TypeText) ? pageObj.getTextObject().getText() : "";
        if (text.length() > 0) {
            szVal = "Text: ";
            szVal += text;
            szVal += "\r\n";
            writeTextWithPrefix(doc, depth, "\t", szVal);
        }
    }

    private static void OutputLRStructureElement(LRContentElement element, OutputStreamWriter doc, int depth) throws PDFException, IOException {
        if (element.isEmpty())
            return;
        
        LRGraphicsObjectElement pageObj = element.getGraphicsObjectElement();
        boolean bIsEmpty = pageObj.isEmpty();
        LRStructureElement parentPageObj = element.getParentElement();
        bIsEmpty = parentPageObj.isEmpty();
        
        int elemType = element.getElementType();
        String elementTypeStr = LRElement.stringifyElementType(elemType);
        String outputStr = "< LRContentElement: " + elementTypeStr + " >\r\n";
        writeTextWithPrefix(doc, depth, "\t", outputStr);
        
        RectF rcBox = element.getBBox();
        int nStartPos = 0, nLength = 0;
        Range range = element.getGraphicsObjectRange();
        if (!range.isEmpty()) {
          nStartPos = range.getSegmentStart(0);
          nLength = range.getSegmentEnd(0) - nStartPos + 1;
        }
        Matrix2D matrix = element.getMatrix();
        
        String szVal = "";
        szVal = String.format("BBox: [%.1f,%.1f,%.1f,%.1f]\r\n", rcBox.getLeft(), rcBox.getTop(), rcBox.getRight(), rcBox.getBottom());
        writeTextWithPrefix(doc, depth, "\t", szVal);
        szVal = String.format("Matrix: [%.1f,%.1f,%.1f,%.1f,%.1f,%.1f]\r\n", matrix.getA(), matrix.getB(), matrix.getC(), matrix.getD(), matrix.getE(), matrix.getF());
        writeTextWithPrefix(doc, depth, "\t", szVal);
        szVal = String.format("StartPos: %d\r\n", nStartPos);
        writeTextWithPrefix(doc, depth, "\t", szVal);
        szVal = String.format("Length: %d\r\n", nLength);
        writeTextWithPrefix(doc, depth, "\t", szVal);

        if (!bIsEmpty)
            OutputLRStructureElement(pageObj, doc, depth + 1);
    }

    private static void showLRElementInfo(LRStructureElement element, OutputStreamWriter doc, int depth) throws PDFException, IOException {
        int nElemListSize = element.getChildCount();
        OutputLRStructureElement(element, doc, depth);
        for (int i=0; i<nElemListSize; i++)
        {
            LRElement item = element.getChild(i);
            int item_type = item.getElementType();
            if (item.isStructureElement())
            {
                LRStructureElement srt_item = new LRStructureElement(item);
                showLRElementInfo(srt_item, doc, depth + 1);
            } else if (item.isContentElement())
            {
                LRContentElement srt_item = new LRContentElement(item);
                OutputLRStructureElement(srt_item, doc, depth);
            } else if (item.isGraphicsObjectElement())
            {
                LRGraphicsObjectElement srt_item = new LRGraphicsObjectElement(item);
                OutputLRStructureElement(srt_item, doc, depth);
            }
        }
    }

    private static void outputAllLRElement(LRStructureElement element, String info_path) throws PDFException, IOException {
        FileOutputStream fos = new FileOutputStream(info_path);
        OutputStreamWriter text_doc = new OutputStreamWriter(fos, "UTF-8");
        if (!element.isEmpty()) {
            showLRElementInfo(element, text_doc, 0);
        } else {
            text_doc.write("No layout recognition information!\r\n");
        }
        text_doc.flush();
        text_doc.close();
        fos.close();
    }

    public static void main(String[] args) throws PDFException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        try {
            String input_file = input_path + "AboutFoxit.pdf";
            String info_file = output_path + "layout_recognition_info.txt";
            PDFDoc doc = new PDFDoc(input_file);
            error_code = doc.load(null);

            if (error_code != e_ErrSuccess) {
                System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                return;
            }

            PDFPage original_page = doc.getPage(0);
            original_page.startParse(e_ParsePageNormal, null, false);
            LRContext context = new LRContext(original_page);

            Progressive progressive = context.startParse(null);

            LRStructureElement root = context.getRootElement();
            int type = root.getElementType();
            int nElemListSize = root.getChildCount();
            System.out.println(String.format("RootElement getChildCount: %d", nElemListSize));
            outputAllLRElement(root, info_file);

            ArrayList<LRElement> element_array = new ArrayList<LRElement>();
            getChildFromElement(root, element_array, (int)LRElement.e_ElementTypeSpan);
            System.out.println(String.format("LRElement::e_ElementTypeSpan count: %d\n", element_array.size()));

            System.out.println("Layout recognition test.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
    }
}
