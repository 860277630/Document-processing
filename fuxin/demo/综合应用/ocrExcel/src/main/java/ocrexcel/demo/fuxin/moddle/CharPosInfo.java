package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: RestDemo
 * @description: 单个字符的位置信息
 * @author: wjl
 * @create: 2022-06-28 09:22
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharPosInfo {
    //距离左边距 距离
    private Double left;
    //距离 上边距 距离
    private Double top;
    //宽度
    private Double width;
    //高度
    private Double height;
    //可信度
    private Double angle;
    //单字符长度值
    private String charStr;
    //页数值  start with 0
    private Integer pageNum;
    //当前字符在本页中的位置 start with 0
    private Integer index;

}
