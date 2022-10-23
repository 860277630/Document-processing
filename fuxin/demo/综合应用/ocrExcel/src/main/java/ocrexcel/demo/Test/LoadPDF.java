package ocrexcel.demo.Test;



import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroupAttributes;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class LoadPDF {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //打开PDF文件
        PDDocument doc = PDDocument.load(new FileInputStream("D://PDF//pdf//4.pdf"));
        //获取第一页的数据
        PDPage pageOne = doc.getPage(0);
        //获取页面resources
        PDResources resources = pageOne.getResources();

        //获取COS对象的名字集合（如果不了解什么是COS对象，请参考这个链接
        //https://www.pdftron.com/documentation/cli/guides/pdf-cosedit/faq/#what-is-cos
        Iterable<COSName> xObjectNames = resources.getXObjectNames();

        //遍历COS对象
        xObjectNames.forEach(item->{
            Integer Count  =0;
            try {
                PDXObject xObject = resources.getXObject(item);
                System.out.println(item.getName());
                //如果为图片类型就调Java的图片处理接口，将其保存下来
                if(xObject instanceof PDFormXObject){
                    System.out.println("===============come in form ================");
                    PDFormXObject fmobject = (PDFormXObject)xObject;
                    //获取区域范围
                    PDRectangle bBox = fmobject.getBBox();
                    //长宽以及左下右上
                    System.out.println("长-宽-左下x-左下y-右上x-右上y:"+bBox.getWidth()+"-"+bBox.getHeight()+
                            "-"+bBox.getLowerLeftX()+":"+bBox.getLowerLeftY()+":"+bBox.getUpperRightX()+":"+bBox.getUpperRightY());
                    //返回流
                    InputStream contents = fmobject.getContents();
                    //写入D盘
                    FileOutputStream fos = new FileOutputStream("D://b.txt");
                    byte[] b = new byte[1024];
                    while ((contents.read(b)) != -1) {
                        fos.write(b);// 写入数据
                    }
                    contents.close();
                    fos.close();// 保存数据


                    //返回表单类型
                    int formType = fmobject.getFormType();
                    System.out.println("表单类型："+formType);


                    //组属性字典
                    PDTransparencyGroupAttributes group = fmobject.getGroup();
                    //获取可选矩阵
                    Matrix matrix = fmobject.getMatrix();
                    //获取资源
                    PDResources pdResources = fmobject.getResources();
                    for (COSBase value : pdResources.getCOSObject().getValues()) {
                        System.out.println("+++++"+value.toString());
                    }
                    //父树中获取键
                    int structParents = fmobject.getStructParents();
                }
                if(xObject instanceof PDImageXObject) {
                    PDImageXObject imgobject = (PDImageXObject)xObject;
                    BufferedImage image = imgobject.getImage();

                    ImageIO.write(image, "png", new File("第"+Count+"张图片.png"));
                    System.out.println("ImageSaved");
                    Count++;
                }
            } catch (IOException e) {
                System.out.println("图片保存出错");
                e.printStackTrace();
            }

        });

    }

}


