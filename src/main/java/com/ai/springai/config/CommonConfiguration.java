package com.ai.springai.config;

import com.ai.springai.enums.SystemPromptEnums;
import com.ai.springai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().build();
    }

    /**
     * <p>创建 ChatClient，聊天客户端，通过他与大模型发送请求</p>
     * <p>默认系统提示：你叫小云</p>
     * <p>参数：openAiChatModel，大模型配置，包含模型名称、温度等参数，是从application.yaml中读取</p>
     * <P>读取不需要手动配置，是Spring自动注入的</P>
     * @return  ChatClient，聊天客户端，用于与大模型交互，发送请求，接收响应等
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel){
            return ChatClient.builder(openAiChatModel) // 创建聊天客户端，包含大模型配置
                    .defaultAdvisors(
                            new SimpleLoggerAdvisor(), // 添加日志记录器，用于记录请求和响应
                            MessageChatMemoryAdvisor.builder(chatMemory()).build()  // 添加消息内存顾问，用于管理聊天历史
                    )
                    .defaultSystem("你叫小云")  // 设置系统提示，用于与大模型交互
                    .build();
    }

    /**
     * <p>创建 ChatClient，聊天客户端，通过他与大模型发送请求</p>
     * <p>默认系统提示：你是一名客服机器人</p>
     * <p>参数：openAiChatModel，大模型配置，包含模型名称、温度等参数，是从application.yaml中读取</p>
     * <P>读取不需要手动配置，是Spring自动注入的</P>
     * @return  ChatClient，聊天客户端，用于与大模型交互，发送请求，接收响应等
     */
    @Bean
    public ChatClient serviceClient(OpenAiChatModel openAiChatModel, CourseTools courseTools){
        return ChatClient.builder(openAiChatModel) // 创建聊天客户端，包含大模型配置
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加日志记录器，用于记录请求和响应
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()  // 添加消息内存顾问，用于管理聊天历史
                )
                .defaultSystem(SystemPromptEnums.SERVICE_PROMPT.getSystemPrompt())  // 设置系统提示，用于与大模型交互
                .defaultTools(courseTools)
                .build();
    }



}
