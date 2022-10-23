package ocrexcel.demo.Test;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class Test01 {
    /**
     * @param templateFilePath PDF模版文件路径
     * @param data             表单数据
     * @param imageData        图片文件路径
     * @param disableEditing   生成后的PDF文件 true不可编辑  false可编辑
     * @param pdfFilePath      生成后PDF文件路径
     * @throws Exception
     */
    @Test
    void test(String templateFilePath, HashMap<String, String> data, HashMap<String, String> imageData, boolean disableEditing, String pdfFilePath) throws Exception {
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        PdfStamper pdfStamper = null;
        FileOutputStream fos = null;
        try {
            // 读取PDF模版文件
            reader = new PdfReader(templateFilePath);
            // 输出流
            bos = new ByteArrayOutputStream();
            // 构建PDF对象
            pdfStamper = new PdfStamper(reader, bos);

            // 获取表单数据
            AcroFields form = pdfStamper.getAcroFields();

            // 使用中文字体 使用 AcroFields填充值的不需要在程序中设置字体，在模板文件中设置字体为中文字体 Adobe 宋体 std L
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bfChinese);
            if (data != null && data.size() > 0) {
                // 表单赋值
                for (String key : data.keySet()) {
                    form.setField(key, data.get(key));
                    // 也可以指定字体
                    form.setFieldProperty(key, "textfont", bfChinese, null);
                }
            }
            // 添加图片
            if (null != imageData && imageData.size() > 0) {
                for (String key : imageData.keySet()) {
                    int pageNo = form.getFieldPositions(key).get(0).page;
                    Rectangle signRect = form.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    // 读图片
                    Image image = Image.getInstance(imageData.get(key));
                    // 获取操作的页面
                    PdfContentByte under = pdfStamper.getOverContent(pageNo);
                    // 根据域的大小缩放图片
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    // 添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);
                }
            }
            pdfStamper.setFormFlattening(disableEditing);
            pdfStamper.close();

            // 保存文件
            fos = new FileOutputStream(pdfFilePath);
            fos.write(bos.toByteArray());
            fos.flush();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null != bos) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null != reader) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void test(){
        System.out.println("sssssss");
    }
}
