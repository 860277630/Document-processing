// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to render all PDF layers of a PDF,
// and add layers in a PDF document.

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.*;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.pdf.*;
import com.foxit.sdk.pdf.graphics.GraphicsObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.foxit.sdk.common.Bitmap.e_DIBArgb;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.foxit.sdk.pdf.LayerContext.e_UsageView;
import static com.foxit.sdk.pdf.LayerTree.*;
import static com.foxit.sdk.pdf.PDFDoc.e_SaveFlagNormal;
import static com.foxit.sdk.pdf.PDFPage.e_ParsePageNormal;

public class pdflayer {
    private static String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
    private static String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
    private static String output_path = "../output_files/pdflayer/";
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

    static void renderPageToImage(PDFPage page, LayerContext layer_content, String bitmap_file) throws PDFException {
        if (!page.isParsed()) {
            // Parse page.
            page.startParse(e_ParsePageNormal, null, false);
        }

        int width = (int) page.getWidth();
        int height = (int) page.getHeight();
        Matrix2D matrix = page.getDisplayMatrix(0, 0, width, height, page.getRotation());

        // Prepare a bitmap for rendering.
        Bitmap bitmap = new Bitmap(width, height, e_DIBArgb, null, 0);
        bitmap.fillRect(0xFFFFFFFF, null);

        // Render page.
        Renderer render = new Renderer(bitmap, false);

        if (!layer_content.isEmpty()) {
            render.setLayerContext(layer_content);
        }
        render.startRender(page, matrix, null);
        Image image = new Image();
        image.addFrame(bitmap);

        image.saveAs(bitmap_file);
    }

    static String usgaeCodeToString(int state) {
        switch (state) {
            case e_StateON:
                return "ON";
            case e_StateOFF:
                return "OFF";
            case e_StateUnchanged:
                return "Unchanged";
            case e_StateUndefined:
                return "Undefined";
        }
        return "Unknown";
    }

    static void write_prefix(int depth, String prefix, String formatbuff, FileWriter text_doc) throws IOException {
        for (int i = 0; i < depth; i++) {
            text_doc.write(String.format("%s", "\t"));
        }
        text_doc.write(formatbuff);
    }

    static void getAllLayerNodesInformation(LayerNode layer_node, int depth, FileWriter text_doc) throws PDFException, IOException {

        if (depth >= 0) {
            write_prefix(depth, "\t", String.format("%s", layer_node.getName()), text_doc);

            if (layer_node.hasLayer()) {
                int state = layer_node.getViewUsage();
                text_doc.write(String.format(" %s\r\n", state == e_StateON ? "[*]" : "[ ]"));
                write_prefix(depth, "\t", String.format("View usage state:\t%s\r\n", usgaeCodeToString(state)), text_doc);
                write_prefix(depth, "\t", String.format("Export usage state:\t%s\r\n", usgaeCodeToString(layer_node.getExportUsage())), text_doc);

                LayerPrintData print_data = layer_node.getPrintUsage();
                write_prefix(depth, "\t", String.format("Print usage state:\t%s, subtype: %s\r\n", usgaeCodeToString(print_data.getPrint_state()), print_data.getSubtype()), text_doc);
                LayerZoomData zoom_data = layer_node.getZoomUsage();
                write_prefix(depth, "\t", String.format("Zomm usage:\tmin_factor = %.4f max_factor = %.4f\r\n\r\n", zoom_data.getMin_factor(), zoom_data.getMax_factor()), text_doc);
            } else {
                text_doc.write("\r\n");
            }
        }

        depth++;
        int count = layer_node.getChildrenCount();
        for (int i = 0; i < count; i++) {
            LayerNode child = layer_node.getChild(i);
            getAllLayerNodesInformation(child, depth, text_doc);
        }
    }

    static void setAllLayerNodesInformation(LayerNode layer_node) throws PDFException {
        if (layer_node.hasLayer()) {
            layer_node.setDefaultVisible(true);
            layer_node.setExportUsage(e_StateUndefined);
            layer_node.setViewUsage(e_StateOFF);
            LayerPrintData print_data = new LayerPrintData("subtype_print", e_StateON);
            layer_node.setPrintUsage(print_data);
            LayerZoomData zoom_data = new LayerZoomData(1, 10);
            layer_node.setZoomUsage(zoom_data);
            String new_name = String.format("[View_OFF_Print_ON_Export_Undefined]") + layer_node.getName();
            layer_node.setName(new_name);
        }
        int count = layer_node.getChildrenCount();
        for (int i = 0; i < count; i++) {
            LayerNode child = layer_node.getChild(i);
            setAllLayerNodesInformation(child);
        }
    }

    static void setAllLayerNodesVisible(LayerContext context, LayerNode layer_node, boolean visible) throws PDFException {
        if (layer_node.hasLayer()) {
            context.setVisible(layer_node, false);
        }
        int count = layer_node.getChildrenCount();
        for (int i = 0; i < count; i++) {
            LayerNode child = layer_node.getChild(i);
            setAllLayerNodesVisible(context, child, visible);
        }
    }

    static void renderAllLayerNodes(PDFDoc doc, LayerContext context, LayerNode layer_node, String pdf_name) throws PDFException {
        if (layer_node.hasLayer()) {
            context.setVisible(layer_node, true);
            int nCount = doc.getPageCount();
            for (int i = 0; i < nCount; i++) {
                PDFPage page = doc.getPage(i);
                String s = String.format("%d", i);
                String layer_name = layer_node.getName();
                layer_name = layer_name.replace(":", "");
                layer_name = layer_name.replace(">", "");
                layer_name = layer_name.replace("<", "");
                layer_name = layer_name.replace("=", "");
                String file_name = output_path + "page_" + s + "_layer_node_" + layer_name + ".bmp";
                renderPageToImage(page, context, file_name);
            }
        }

        int count = layer_node.getChildrenCount();
        for (int i = 0; i < count; i++) {
            LayerNode child = layer_node.getChild(i);
            renderAllLayerNodes(doc, context, child, pdf_name);
        }
        if (layer_node.hasLayer()) {
            // The visibility of parent layer will affect the children layers,
            // so reset the visibility of parent layer after children have been rendered.
            context.setVisible(layer_node, false);
        }
    }

    public static void main(String[] args) throws PDFException, IOException {
        createResultFolder(output_path);
        // Initialize library
        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            System.out.println(String.format("Library Initialize Error: %d\n", error_code));
            return;
        }

        String input_file = input_path + "OCTest_src.pdf";
   
        try {
            {
              PDFDoc doc = new PDFDoc(input_file);
              error_code = doc.load(null);
  
              if (error_code != e_ErrSuccess) {
                  System.out.println(String.format("The Doc [%s] Error: %d\n", input_file, error_code));
                  return;
              }
              LayerTree layertree = new LayerTree(doc);
              LayerNode root = layertree.getRootNode();
              if (root.isEmpty()) {
                  System.out.println("No layer information!");
                  return;
              }
  
              // Get original information of all layer nodes.
              String file_info = output_path + "original_layer_informations.txt";
              FileWriter original_doc = new FileWriter(file_info, false);
              getAllLayerNodesInformation(root, -1, original_doc);
              // Set new information.
              setAllLayerNodesInformation(root);
  
              // Get new information.
              file_info = output_path + "new_layer_informations.txt";
              FileWriter new_info = new FileWriter(file_info, false);
              getAllLayerNodesInformation(root, -1, new_info);
  
              String output_file = output_path + "new_layers.pdf";
              doc.saveAs(output_file, e_SaveFlagNormal);
              // Render layer node.
              LayerContext context = new LayerContext(doc, e_UsageView);
              setAllLayerNodesVisible(context, root, false);
  
              renderAllLayerNodes(doc, context, root, output_file);
              original_doc.close();
              new_info.close();
            }
              
            {
              // Edit layer tree
              PDFDoc doc = new PDFDoc(input_file);
              error_code = doc.load(null);
              LayerTree layertree = new LayerTree(doc);
              LayerNode root = layertree.getRootNode();
              int children_count = root.getChildrenCount();
              root.removeChild(children_count -1);
              LayerNode child = root.getChild(children_count - 2);
              LayerNode child0 = root.getChild(0);
              child.moveTo(child0, 0);
              child.addChild(0, "AddedLayerNode", true);
              child.addChild(0, "AddedNode", false);
              String file_info = output_path + "edit_layer_informations.txt";
              FileWriter edit_info = new FileWriter(file_info, false);
              getAllLayerNodesInformation(root, -1, edit_info);
              edit_info.close();

              String output_file = output_path + "edit_layer_tree.pdf";
              doc.saveAs(output_file, e_SaveFlagNormal);
            }

            {
              // Create layer tree
              PDFDoc doc = new PDFDoc(input_path + "AboutFoxit.pdf");
              error_code = doc.load(null);
              boolean has_layer = doc.hasLayer();
              LayerTree layertree = new LayerTree(doc);
              has_layer = doc.hasLayer();
              LayerNode root = layertree.getRootNode();
              int children_count = root.getChildrenCount();
              root.addChild(0, "AddedLayerNode", true);
              root.addChild(0, "AddedNode", false);
              String file_info = output_path + "create_layer_informations.txt";
              FileWriter create_info = new FileWriter(file_info, false);
              getAllLayerNodesInformation(root, -1, create_info);
              create_info.close();

              String output_file = output_path + "create_layer_tree.pdf";
              doc.saveAs(output_file, e_SaveFlagNormal);
            }

            {
              //Copy graphics objects from A PDF to B PDF
              PDFDoc doc = new PDFDoc(input_file);
              error_code = doc.load(null);
              PDFPage page = doc.getPage(0);
              page.startParse(e_ParsePageNormal, null, false);
              
              //Create layer tree.
              PDFDoc newdoc = new PDFDoc(input_path + "pdfobjects.pdf");
              error_code = newdoc.load(null);
              PDFPage newpage = newdoc.getPage(0);
              newpage.startParse(e_ParsePageNormal, null, false);
              boolean haslayer = newdoc.hasLayer();
              LayerTree layertree = new LayerTree(newdoc);
              haslayer = newdoc.hasLayer();
              LayerNode root = layertree.getRootNode();
              LayerNode node = root.addChild(0, "AddedLayerNode", true);
              
              for(int i = 0;i < 5;i++) {
                GraphicsObject graphics_object = page.getGraphicsObject(i);
                newpage.insertGraphicsObject(0, graphics_object);
                node.addGraphicsObject(newpage, graphics_object);
              }
              newpage.generateContent();
              String output_file = output_path + "copy_graphics_objects.pdf";
              newdoc.saveAs(output_file, e_SaveFlagNormal);
            }

            System.out.println("PDFLayer test.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Library.release();
        return;
    }
}
