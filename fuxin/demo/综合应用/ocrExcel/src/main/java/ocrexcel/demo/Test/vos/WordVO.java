package ocrexcel.demo.Test.vos;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 下午1:58
 */
public class WordVO {

    //文本
    private String word;

    //X轴坐标
    private Float x;

    //y轴坐标
    private Float y;

    //页数
    private Integer pageNo;

    //字符在当前页 所有字符的位置
    private Integer index;

    //字宽
    private Float width;

    //字高
    private Float height;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "WordVO{" +
                "word='" + word + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", pageNo=" + pageNo +
                ", index=" + index +
                ", width=" + width +
                ", fontSize=" + height +
                '}';
    }
}
