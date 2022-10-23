package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: RestDemo
 * @description: 多行字符串 每行的信息
 * @author: wjl
 * @create: 2022-06-28 16:44
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerRowInfo {
    //左边距
    private Double left;
    //上边距
    private Double top;
    //宽度
    private Double width;
    //高度
    private Double height;
    //字符串内容
    private String strText;
}
