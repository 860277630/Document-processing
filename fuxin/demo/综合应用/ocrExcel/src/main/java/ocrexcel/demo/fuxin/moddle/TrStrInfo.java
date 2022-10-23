package ocrexcel.demo.fuxin.moddle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RestDemo
 * @description: 表格中 字符串的位置信息
 * @author: wjl
 * @create: 2022-06-28 14:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrStrInfo {
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
    //页数
    private Integer page;
    //如果是多层的  需要记录每层的信息  一层也算多层
    private List<PerRowInfo> rowInfos;


}
