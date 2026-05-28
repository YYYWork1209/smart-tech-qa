package com.ai.springai.controller;


import com.ai.springai.enums.SessionType;
import com.ai.springai.enums.SystemPromptEnums;
import com.ai.springai.repository.ChatHistoryRepository;
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

    private final ChatClient serviceClient;

    private final ChatHistoryRepository chatHistoryRepository;

    // 这里前端并不会处理sse格式的数据所以只能先使用text/html
    // produces = "text/event-stream;charset=UTF-8" 用于指定控制器方法返回的响应内容类型和字符编码。
    // 其实就是指定返回的数据中，添加响应头内容Content-type来指定响应数据的格式，非流式输出自带的就有不需额外指定
    @PostMapping(value = "/chat",produces = "text/html;charset=UTF-8")
    public Flux<String> chatAI(@RequestParam(value = "prompt") String userMessage,
                                @RequestParam String chatId){
        log.info("请求接收成功：等待大模型返回结果...");

        //把会话id保存起来，下次页面左边会展示出来之前的会话记录这里保存的是 会话类型 该类型下的会话id列表
        chatHistoryRepository.saveChatHistory(String.valueOf(SessionType.CHAT),chatId);

        return chatClient.prompt()
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID,chatId); // 向大模型传递会话id，用于保持会话状态
                })  // 这里传递的参数是向advisor传递的参数，所有的advisor都能看到这里传入的参数
                .user(userMessage)
                .stream()
                .content();
    }

    @GetMapping(value = "/service",produces = "text/html;charset=UTF-8")
    public Flux<String> serviceChat(@RequestParam("prompt") String userPrompt,
                                    @RequestParam("chatId") String chatId){

        //把会话id保存起来，下次页面左边会展示出来之前的会话记录
        chatHistoryRepository.saveChatHistory(String.valueOf(SessionType.SERVICE),chatId);

        return serviceClient.prompt()
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID,chatId);
                })
                .user(userPrompt)
                .stream()
                .content();

    }

}
