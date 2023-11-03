package com.example.demo.Service;

import com.pdflib.TET;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfAnnotationCollection;
import com.spire.pdf.annotations.PdfTextWebLinkAnnotationWidget;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @program: pdflib_demo
 * @description:  利用pdflib  将pdf转化成xml  并提取最基本的元素
 * @author: wjl
 * @create: 2022-12-26 17:26
 **/
@Service
@Slf4j
public class PdfLibService {

    private static final String PAGE_OPTLIST = "granularity=word";

    public void PickPdfDatas(MultipartFile file){
        //首先进行初始化
        initSdk();
        System.out.println("pdflib的环境位置:"+System.getProperty("java.library.path"));
        //然后进行  xml的转换
        try (PDDocument doc = PDDocument.load(file.getInputStream())){
            int pages = doc.getNumberOfPages();
            //因为免费版的   PDFlib 对于超过  超过10页 或1M  的PDF  是不予处理的
            //所以暂时将  PDF  的每页   都单独截取出来
            //当然也可以  截取几页
            for (int page = 1; page <= pages; page++) {
                //首先将 PDF  每页都截取下来进行保存  保存在和 该项目  同目录的 文件夹中

                // 获取路径  换成 同目录下的
                String projectPath = System.getProperty("user.dir");
                String targetPath = projectPath.substring(0,projectPath.lastIndexOf("\\"))+"/pdflib_files";

                //然后进行保存
                PageExtractor pageExtractor = new PageExtractor(doc, page, (page+0)<pages?(page+0):pages);
                PDDocument extract = pageExtractor.extract();
                extract.save(targetPath+"/pdfs/"+page+".pdf");

                //然后进行转换
                parseXml(targetPath,String.valueOf(page));

                //然后进行xml  解析
                pickDataFromXml(targetPath+"/xmls/"+page+".xml");
                extract.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //进行xml  解析
    //获取到word  标签  并且  输出 他的位置信息
    private void pickDataFromXml(String path){
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document =  saxReader.read(new File(path));
            Element rootElement = document.getRootElement();
            List<Element> list   = new ArrayList<>();
            toTrTd(list,rootElement,"Word");
            //然后   每一个 Word    都包含 Text  和  Box标签    顺次  获取 即可
            for (Element ele : list) {
                System.out.println("**************************************************");
                StringBuilder  str = new StringBuilder("");
                for (Element element : ele.elements()) {
                    str.append("=====");
                    if(Objects.equals("Text",element.getName())){
                        str.append("文本内容："+element.getStringValue());
                    }else if(Objects.equals("Box",element.getName())){
                        str.append("坐标1："+element.attributeValue("llx")+"=====");
                        str.append("坐标2："+element.attributeValue("urx")+"=====");
                        str.append("坐标3："+element.attributeValue("lly")+"=====");
                        str.append("坐标4："+element.attributeValue("ury")+"=====");
                    }
                }
                System.out.println(str);
                System.out.println("**************************************************");
            }

        }catch (Exception e){
            log.info(e.getMessage());
        }

    }


    private  void toTrTd(List<Element> list, Element element, String eleNames) {

        String elementName = element.getName();
        List<Element> elementList = element.elements();
        if (elementName.equals(eleNames)) {
            list.add(element);
        } else {
            if (elementList.size() != 0) {
                for (Element ele : elementList) {
                    toTrTd(list, ele, eleNames);
                }
            }
        }
    }

    /*
    * 当文件超过1M后 可以去掉 里面的   图片
    * 实在不行就用 OCR方式进行解析
    * */

    //  将PDF  转为 xml    并对相关信息进行输出
    private void parseXml(String path,String fileName){
        //然后加载
        TET tet = null;
        try {
            tet = new TET();
            tet.set_option("searchpath={" + path + "/pdfs" + "}");
            //  密钥 0就是无效key
            tet.set_option("license={0}");
            int doc = tet.open_document(fileName + ".pdf", "tetml={}");
            if (doc == -1) {
                System.err.println("Error " + tet.get_errnum() + " in " + tet.get_apiname() + "(): " + tet.get_errmsg());
                tet.delete();
                return;
            }
            //  获取页数
            final int n_pages = (int) tet.pcos_get_number(doc, "length:pages");
            /*
             * Loop over pages in the document;
             */
            for (int pageno = 0; pageno <= n_pages; ++pageno) {
                tet.process_page(doc, pageno, PAGE_OPTLIST);
            }

            /*
             * This could be combined with the last page-related call.
             */
            tet.process_page(doc, 0, "tetml={trailer}");

            /*
             * Get the TETML document as a byte array.
             */
            final byte[] tetml = tet.get_tetml(doc, "");

            {
                //保存在本地
                File file  = new File(path+"/xmls/"+fileName+".xml");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(tetml,0,tetml.length);
                fos.flush();
                fos.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (tet != null) {
                tet.delete();
            }
        }

    }

    //对SDK  进行初始化 操作
    private void initSdk(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {
            System.loadLibrary("pdflib_java");
            //System.loadLibrary("tet_java");
        } else {
            //linux  下要填写成绝对路径
            System.load("/usr/local/ocrFiles/outlib/libtet_java.so");
            //System.loadLibrary("libtet_java");
        }
    }
}
