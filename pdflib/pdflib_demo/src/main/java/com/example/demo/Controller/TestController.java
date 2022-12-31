package com.example.demo.Controller;

import com.example.demo.Service.PdfLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: pdflib_demo
 * @description:
 * @author: wjl
 * @create: 2022-12-12 23:48
 **/
@RestController
public class TestController {

    @Autowired
    private PdfLibService  pdfLibService;


    @RequestMapping("/pickData")
    public String pickData(@RequestParam("file") MultipartFile file){
        pdfLibService.PickPdfDatas(file);
        return "sucess";
    }


}

