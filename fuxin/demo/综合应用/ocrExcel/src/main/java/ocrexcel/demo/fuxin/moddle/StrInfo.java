package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrInfo {
    private String val;
    private Float xStart;
    private Float xEnd;
    private Float xMiddle;
    private Float width;
    //Y坐标这里记录的是 行中的Y坐标
    private Float yAxis;
    private Float[] rowHeight;
    private Integer pageNum;

}
