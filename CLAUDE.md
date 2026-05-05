# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建与运行

```bash
# 编译项目（跳过测试）
./mvnw clean install -DskipTests

# 启动应用
./mvnw spring-boot:run

# 运行全部测试
./mvnw test

# 运行单个测试
./mvnw test -Dtest=SpringAiApplicationTests
```

## 项目概述

**smart-tech-qa** — 基于 Spring AI 的智能技术问答平台。核心是一个 AI 客服系统，通过大模型（通义千问 qwen-turbo）为用户提供课程咨询和试听预约服务。

## 技术栈

- **Spring Boot 3.4.3** + **Spring AI 1.0.0**（通过 `spring-ai-bom`）
- **Java 17**，Maven 构建
- **AI 模型**：阿里云百炼 DashScope（OpenAI 兼容接口），`qwen-turbo` 模型；可选 Ollama 作为本地后端
- **数据库**：MySQL + **MyBatis-Plus 3.5.10.1**
- **Lombok** 简化代码

## 架构分层

```
config/       → 配置类：ChatClient、ChatMemory、CORS
controller/   → REST 接口：聊天、历史记录
tools/        → Spring AI @Tool 函数（查询课程、查询校区）
repository/   → 会话记录仓储（当前内存实现）
service/      → MyBatis-Plus 业务层（Course、School、CourseReservation）
mapper/       → MyBatis-Plus Mapper 接口
entity/
  po/         → 数据库实体（Course、School、CourseReservation）
  query/      → 工具函数参数对象（CourseQuery、Sort）
  vo/         → 响应 DTO（MessageVO）
enums/        → 系统提示词模板（SystemPromptEnums）
```

## 关键设计

### AI 配置（`CommonConfiguration.java`）
- 使用 `OpenAiChatModel` 连接阿里云百炼（DashScope），通过 OpenAI 兼容接口调用通义千问
- `ChatClient` 装配了 `MessageChatMemoryAdvisor`（会话记忆窗口）和 `SimpleLoggerAdvisor`（日志记录）
- 默认系统提示词："你叫小云"
- `SystemPromptEnums.SERVICE_PROMPT` 已定义但未接入 ChatClient（定义了完整的"小黑"客服角色提示词，包含课程咨询和预约流程）

### 函数调用（`CourseTools.java`）
- `@Tool` 注解方法暴露给大模型作为可调用工具
- `findCourse(CourseQuery)` — 按类型、学历查询课程，支持排序
- `queryAllSchools()` — 查询所有校区列表
- **注意**：预约功能尚未实现 `@Tool` 方法（仅有 Service 层）

### 会话记忆
- **会话列表**：`ChatHistory.java` 中基于内存 `Map<String, List<String>>` 存储，按 type 分类
- **消息内容**：Spring AI 的 `MessageWindowChatMemory`（内存窗口存储）
- 两处均有 TODO，后续需迁移到数据库持久化

### 流式聊天（`ChatController.java`）
- `POST /ai/chat?prompt=...&chatId=...` 返回 `Flux<String>` 流式响应
- 使用 `text/html` 而非 `text/event-stream`，因为前端未处理 SSE 格式

### 系统提示词（`SystemPromptEnums.java`）
- 定义了"黑马程序员"AI 客服"小黑"的完整角色设定（中文）
- 结构化流程：打招呼 → 收集用户信息 → 查询课程 → 推荐 → 收集预约信息 → 确认
- 包含 prompt 注入防护、价格保密规则
- 推荐课程和校区时要求使用表格展示

## 配置说明

- **`application.yaml`**：API Key 通过环境变量 `${ALIYUN_API_KEY}` 注入，MySQL 连接 `localhost:3306/itheima`
- **环境变量**：`ALIYUN_API_KEY`（阿里云百炼 API 密钥）
- **数据库**：MySQL，数据库名 `itheima`，表 `course`、`school`、`course_reservation`

## API 接口

| 方法 | 路径 | 说明 |
|--------|------|------|
| POST | `/ai/chat?prompt=...&chatId=...` | 流式聊天 |
| GET | `/ai/history/{type}` | 获取某类型下的会话列表 |
| GET | `/ai/history/{type}/{chatId}` | 获取某会话的聊天记录 |

## 可拓展功能（适合实习项目方向）

以下是基于当前项目架构可以进一步实现的功能，每个都有一定深度但难度适中，适合作为实习项目经验：

### 1. 会话记忆 MySQL 持久化

**现状**：`ChatHistory` 使用内存 Map 存储会话列表，`MessageWindowChatMemory` 基于内存窗口存储消息内容，应用重启后数据全部丢失。

**实现思路**：
- 设计会话表（chat_session）和消息表（chat_message）的数据库 schema
- 实现 `ChatHistoryRepository` 的 MySQL 版本，替代内存 Map
- 自定义 `ChatMemory` 实现类，将消息历史持久化到数据库，而非使用 `MessageWindowChatMemory`
- 添加定时任务（`@Scheduled`）或拦截器清理过期会话

**涉及的知识点**：MyBatis-Plus 高级用法、数据库设计、Spring 定时任务、数据分层架构

### 2. 课程预约完整闭环 + 校验流程

**现状**：`CourseReservation` 实体和 `ICourseReservationService` 已存在，但没有暴露给 AI 模型的 `@Tool` 方法，预约流程不完整。

**实现思路**：
- 在 `CourseTools` 中添加 `createReservation()` 工具方法，包含参数校验注解
- 添加重名校验：同一学生同一课程不允许重复预约
- 预约完成后通过 AI 回复模板生成格式化确认信息
- 添加管理端 REST API：查询/取消/确认预约，支持分页

**涉及的知识点**：Spring AI Function Calling 深度使用、参数校验、业务规则编排、RESTful API 设计

### 3. RAG 知识库问答（热门方向）

**现状**：当前仅靠大模型自身知识回答问题，无法获取最新的课程资料或官方文档。

**实现思路**：
- 添加 `spring-ai-starter-vector-store-mysql`（或 pgvector）依赖，搭建向量数据库
- 将课程大纲、常见问题、官方文档等文本切片（split）并向量化（embedding）存入向量库
- 在 `CommonConfiguration` 中配置 `VectorStore` 和 `RetrievalAugmentationAdvisor`
- 用户提问时先检索相关文档片段，拼入 prompt 上下文后再调用大模型回答
- 可选：使用阿里云 DashScope 的 embedding 模型进行向量化，减少额外依赖

**涉及的知识点**：RAG 架构、向量数据库、文本切片策略、Embedding 模型选择、Spring AI Adapter 机制。这是当前 AI 岗位面试的高频考点。

### 4. 多模型动态切换（策略模式）

**现状**：`OpenAiChatModel` 在代码中硬编码，切换模型需要修改配置重启应用。

**实现思路**：
- 定义模型配置实体表（model_config），存储模型名称、base-url、api-key、temperature 等
- 用策略模式封装不同模型供应商（DashScope 通义千问、DeepSeek、本地 Ollama、OpenAI）
- 实现 `ChatClient` 的动态创建与缓存，根据 chatId 或用户级别选择模型
- 添加切换接口 `POST /ai/model/switch?chatId=...&model=...`
- 同一个会话中可以中途切换模型，对比不同模型的回答效果

**涉及的知识点**：策略模式、工厂模式、Spring Bean 生命周期、缓存策略。体现了良好的软件设计能力。

### 5. 实时通知与消息推送

**现状**：前端只能通过主动刷新查看会话列表和历史，无法实时感知新消息或预约状态变更。

**实现思路**：
- 引入 WebSocket（Spring WebSocket 或 Netty），建立长连接
- 预约状态变更时（如管理员确认预约），通过 WebSocket 推送通知到对应用户
- 大模型流式输出的同时，将消息片段同步广播到同会话的其他客户端（如管理员监看）
- 心跳检测和断线重连机制

**涉及的知识点**：WebSocket 协议、Spring WebSocket 配置、消息广播、前后端实时通信。这是一个全栈功能，前后端都能体现能力。
