package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: RestDemo
 * @description: 表格单元格信息
 * @author: wjl
 * @create: 2022-06-28 15:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrCellInfo {
    private String val;
    private Float colOrder;
    private TrStrInfo strInfo;


}
