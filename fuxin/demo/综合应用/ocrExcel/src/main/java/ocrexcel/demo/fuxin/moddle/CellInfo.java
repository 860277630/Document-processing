package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: RestDemo
 * @description: 单元格信息
 * @author: wjl
 * @create: 2022-06-24 15:16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellInfo {
    private String val;
    private Float colOrder;
    private Float rowOrder;
    private StrInfo strInfo;
    //这里需要添加  列序号的确认 和 行序号的确认 以防止 反复被确认  序号
    private Boolean colOrderConf;

}
