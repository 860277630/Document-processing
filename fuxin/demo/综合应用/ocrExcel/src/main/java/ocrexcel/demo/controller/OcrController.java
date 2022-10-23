package ocrexcel.demo.controller;

import lombok.extern.slf4j.Slf4j;
import ocrexcel.demo.service.test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ocrExcel
 * @description:
 * @author: wjl
 * @create: 2022-10-20 10:14
 **/
@RestController
@Slf4j
public class OcrController {

    @RequestMapping("/test")
    public String test(){
        log.info("test");
        return "sucess";
    }



    @RequestMapping("/fuxin/toExcel")
    public String fxToExcel(String foreDate,String fileName){
/*        PdfExcelPaddle pdfExcelPaddle = new PdfExcelPaddle();
        try {
            pdfExcelPaddle.PdfToExcel(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        test test = new test();
        try {
            test.main(foreDate,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "处理完毕";
    }



}
