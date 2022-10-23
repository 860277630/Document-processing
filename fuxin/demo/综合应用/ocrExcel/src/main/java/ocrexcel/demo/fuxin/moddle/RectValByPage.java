package ocrexcel.demo.fuxin.moddle;

public class RectValByPage {
    private Rect rect;
    private String word;
    private Integer pageNum;
    private double reliability;

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public double getReliability() {
        return reliability;
    }

    public void setReliability(double reliability) {
        this.reliability = reliability;
    }

    @Override
    public String toString() {
        return "RectValByPage{" +
                "rect=" + rect +
                ", word='" + word + '\'' +
                ", pageNum=" + pageNum +
                ", reliability=" + reliability +
                '}';
    }
}
