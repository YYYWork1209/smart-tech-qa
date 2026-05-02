package com.ai.springai.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.print.DocFlavor;
import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    // produces = "text/html;charset=UTF-8" 用于指定控制器方法返回的响应内容类型和字符编码。
    // 其实就是指定返回的数据中，添加响应头内容Content-type来指定响应数据的格式，非流式输出自带的就有不需额外指定
    @PostMapping(value = "/chat",produces = "text/html;charset=UTF-8")
    public Flux<String> chatAI(@RequestParam(value = "prompt") String userMessage,
                                @RequestParam String chatId){
        log.info("请求接收成功：等待大模型返回结果...");
        return chatClient.prompt()
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID,chatId);
                })
                .user(userMessage)
                .stream()
                .content();
    }

}
