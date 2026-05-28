package com.ai.springai.repository;

import java.util.List;

public interface ChatHistoryRepository {

    /**
     * 用来保存聊天记录，前段会有不同类型的聊天记录，比如普通聊天、客服聊天等，所以需要根据type来保存
     * @param type 聊天记录类型
     * @param chatId 聊天记录id，用于唯一标识一条聊天记录
     */
    void saveChatHistory(String type,String chatId,String userMessage);

    /**
     * 用来获取聊天记录，根据 type来获取
     * @param type 聊天记录类型
     */
    List<String> getChatHistory(String type);
}
