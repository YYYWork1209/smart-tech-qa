package com.ai.springai.repository;


import com.ai.springai.entity.po.Conversations;
import com.ai.springai.entity.po.CourseReservation;
import com.ai.springai.entity.po.Messages;
import com.ai.springai.enums.ConversationStatus;
import com.ai.springai.enums.MessagesType;
import com.ai.springai.enums.SessionType;
import com.ai.springai.service.IConversationsService;
import com.ai.springai.service.ICourseReservationService;
import com.ai.springai.service.IMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
public class ChatHistory implements ChatHistoryRepository{

    private IConversationsService iConversationsService;
    private IMessagesService iMessagesService;

    // 改用Mysql进行存储，会话表存储会话id标题，消息表存储会话对应的id
//    private final Map<String, List<String>> chatTypeAndId = new HashMap<>();

    @Override
    public void saveChatHistory(String type,String chatId,String userMessage) {
        //保存之前先判断是否已经存储有了当前id的聊天记录
        // 如果不存在，把当前type以及聊天记录id列表存入数据库
        // 如果存在，返回当前type对应的id值列表（一种类型会有多个会话，同时还需对应用户id）
        // 返回值为当前类型下的所有会话id值

        Conversations conversationById = iConversationsService.getById(chatId);
        if(conversationById != null){
            // 已经存在当前会话，直接向会话对应的消息列表中添加消息即可
            iMessagesService.save(Messages.builder()
                            .role(MessagesType.USER.getMessagesType())
                            .conversationId(chatId)
                            .createdAt(LocalDateTime.now())
                            .content(userMessage)
                    .build());
        }

        // 否则就是没有获取到，即这是一个新的会话，同时更新会话表和消息表
        // 会话信息存入数据库
        iConversationsService.save(Conversations.builder()
                        .id(chatId)
                        .title(userMessage)  //TODO 标题暂时使用用户第一次提问的问题来定
                        .status(ConversationStatus.NORMAL.getStatus())
                        .createdAt(LocalDateTime.now())
                        .type(SessionType.CHAT.getSessionType())
                .build());

        // 消息信息存入数据库
        iMessagesService.save(Messages.builder()
                .role(MessagesType.USER.getMessagesType())
                .conversationId(chatId)
                .createdAt(LocalDateTime.now())
                .content(userMessage)
                .build());


    }

    @Override
    public List<String> getChatHistory(String type) {
        //依据传入的type类型查询对应的会话id列表

        return List.of();
    }
}
