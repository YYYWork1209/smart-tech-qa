package com.ai.springai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 对 PDF文件进行解析，存入向量数据库，实现基于PDF文件的向量数据库的问答
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat-pdf")
public class PDFController {

//    @GetMapping(produces = "text/html;charset=UTF-8")
//    public Flux<String> pdfChat(){
//
//    }

}
