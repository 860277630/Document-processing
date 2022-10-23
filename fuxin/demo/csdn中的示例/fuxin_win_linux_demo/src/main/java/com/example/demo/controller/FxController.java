package com.example.demo.controller;

import com.example.demo.simple_demo.pdf2text.pdf2text;
import com.foxit.sdk.PDFException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: fuxin_win_linux_demo
 * @description:
 * @author: wjl
 * @create: 2022-10-23 00:22
 **/
@RestController
@Slf4j
public class FxController {
    @RequestMapping("test")
    public String test(){
        log.info("==come in==");
        pdf2text text = new pdf2text();
        try {
            text.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sucess";
    }
}
