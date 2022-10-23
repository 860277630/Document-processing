package ocrexcel.demo.fuxin.utils;


import ocrexcel.demo.fuxin.config.PaddleOcr;
import ocrexcel.demo.fuxin.moddle.CharPosInfo;
import ocrexcel.demo.fuxin.moddle.TrCellInfo;
import ocrexcel.demo.fuxin.moddle.TrStrInfo;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: RestDemo
 * @description: PDF中字符、字符串的坐标信息
 * @create: 2022-06-23 14:17
 **/
public abstract class PdfStrAxis {

    //字符信息的存储
    protected List<List<CharPosInfo>> subjectList = new ArrayList<>();

    //加载信息
    protected abstract void loadDatas(String filePath, RestTemplate restTemplate, PaddleOcr paddleOcr) throws IOException, Exception;


    /**
     * 获取字符串列表中  每个字符串的信息
     *
     * @param clearColOrder  是否清空顺序
     * @param strs
     */
    protected abstract void getStrsInfos(List<TrCellInfo> strs, Boolean clearColOrder);

    //获取 单个字符的信息
    protected abstract List<TrStrInfo> getStrInfos(String str);


    //进行单个文字到字符串位置信息的转换
    protected abstract TrStrInfo totalPos(List<CharPosInfo> list);


}
