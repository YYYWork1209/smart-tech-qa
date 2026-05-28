package com.ai.springai.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>聊天记录仓库实现类作用如下：
 * <li>保存聊天记录，根据 type 来保存</li>
 * <li>获取聊天记录，根据 type 来获取</li>
 * </p>
 * <p> 会话历史内容保存在SoringAI提供的MessageWindowChatMemory中 </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatHistory implements ChatHistoryRepository{

    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;


    // 改用Mysql进行存储，会话表存储会话id标题，消息表存储会话对应的id
//    private final Map<String, List<String>> chatTypeAndId = new HashMap<>();

    @Override
    public void saveChatHistory(String type,String chatId) {
        //保存之前先判断是否已经存储有了当前id的聊天记录
        // 如果不存在，把当前type以及聊天记录id列表存入数据库
        // 如果存在，返回当前type对应的id值列表（一种类型会有多个会话，同时还需对应用户id）
        // 返回值为当前类型下的所有会话id值






    }

    @Override
    public List<String> getChatHistory(String type) {
        //依据传入的type类型查询对应的会话id列表

        return List.of();
    }
}
