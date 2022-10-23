package ocrexcel.demo.Test.cn.isuyu.demo.keyword.port.utils;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import ocrexcel.demo.Test.vos.WordVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerRenderListener implements RenderListener {

    //线程安全的list
    private List<WordVO> wordVOS = Collections.synchronizedList(new ArrayList(1000));


    private Integer pages;

    public CustomerRenderListener(Integer pages) {
        this.pages = pages;
    }

    public List<WordVO> getWordVOS() {
        return wordVOS;
    }

    public void beginTextBlock() {

    }


    /**
     * TextRenderInfo.getDescentLine  行底部
     * TextRenderInfo.getAscentLine   行上面
     * TextRenderInfo.getBaseline   行中间
     * @param textRenderInfo
     */
    @Override
    public void renderText(TextRenderInfo textRenderInfo) {
        String text = textRenderInfo.getText();
        //关键字的起始坐标
        Vector startPoint = textRenderInfo.getBaseline().getStartPoint();
        Vector endPoint = textRenderInfo.getBaseline().getEndPoint();

        //行高度  即  字高度
        float fontHeight = textRenderInfo.getAscentLine().getStartPoint().get(1)
                - textRenderInfo.getDescentLine().getStartPoint().get(1);

        //如果读到的是多个字符，我们将其分割为多个字符添加到集合中
        String [] keys = text.split("");
        //字体大小
        float fontWidth = (endPoint.get(0) - startPoint.get(0)) / text.length();

        for (int i = 0; i < keys.length; i++) {
            //如果是空格我们排除掉 因为做匹配的时候空格全部都被换掉了
            if (keys[i].equals("")) {
                continue;
            }
            int index = wordVOS.size();
            WordVO wordVO = new WordVO();
            wordVO.setWord(keys[i].replace(" "," "));
            wordVO.setX(startPoint.get(0) + fontWidth * i);
            wordVO.setY(startPoint.get(1));
            //获取传过来的合同页数
            wordVO.setPageNo(this.pages);
            //设置当前字符在当前页字符集的索引下标
            wordVO.setIndex(index);
            wordVO.setWidth(fontWidth);
            wordVO.setHeight(fontHeight);
            wordVOS.add(wordVO);
        }
    }

    public void endTextBlock() {

    }

    public void renderImage(ImageRenderInfo imageRenderInfo) {

    }

}
