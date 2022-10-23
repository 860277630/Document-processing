package ocrexcel.demo.fuxin.utils;


import ocrexcel.demo.Test.vos.WordVO;
import ocrexcel.demo.fuxin.enums.MergeEnum;
import ocrexcel.demo.fuxin.moddle.CellInfo;
import ocrexcel.demo.fuxin.moddle.StrInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: RestDemo
 * @description: PDF中字符、字符串的坐标信息
 * @create: 2022-06-23 14:17
 **/
public abstract class PdfStrAxisUtil {

    //字符信息的存储
    protected List<List<WordVO>> subjectList = new ArrayList<>();


    //加载信息
    protected abstract  void loadDatas(String filePath) throws IOException;


    /**
     * 获取字符串列表中  每个字符串的信息
     *
     * @param clearColOrder  是否清空顺序
     * @param strs
     * @param isXaxis  是否是X轴横向的统计
     */
    protected abstract void getStrsInfos(List<CellInfo> strs, Boolean clearColOrder, MergeEnum isXaxis);

    //获取 单个字符的信息
    protected abstract List<StrInfo> getStrInfos(String str);


    //进行单个文字到字符串位置信息的转换
    protected abstract StrInfo totalPos(List<WordVO> list);


}
