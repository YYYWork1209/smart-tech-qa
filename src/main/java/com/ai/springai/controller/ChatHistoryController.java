package com.ai.springai.controller;


import com.ai.springai.repository.ChatHistoryRepository;
import com.ai.springai.entity.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory messageWindowChatMemory;

    /**
     * 依据传递来的 type值，来查询对应的会话 id列表，目的是在聊天框左侧展示聊天历史栏信息
     */
    @GetMapping("/{type}")
    public List<String> typeChatHistoryList(@PathVariable String type){
       return  chatHistoryRepository.getChatHistory(type);
    }

    /**
     * 依据传递来的会话type以及会话Id查询对应的会话历史，
     * TODO 后续结合用户Id，查询对应用户下的会话历史
     */
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> chatHistory(@PathVariable String type, @PathVariable String chatId){
        //依据会话id去MessageWindowChatMemory获取会话记录
        List<Message> messageList = messageWindowChatMemory.get(chatId);
        //把返回的绘画历史转换为MessageVO返回给前端，目的是明确每条会话内容对应的角色，方便前端渲染展示
        List<MessageVO> messageVOList = messageList.stream().map(message -> new MessageVO(message)).toList();
        return messageVOList;

    }


}
