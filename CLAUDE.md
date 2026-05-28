# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在该仓库中工作时提供指导。

## 构建与运行

```bash
# 编译（跳过测试）
./mvnw clean install -DskipTests

# 启动应用
./mvnw spring-boot:run

# 运行全部测试
./mvnw test

# 运行单个测试
./mvnw test -Dtest=SpringAiApplicationTests
```

Windows PowerShell 下使用 `.\mvnw.cmd clean install -DskipTests`

## 项目概述

**smart-tech-qa** — 基于 Spring AI 的智能技术问答平台，通过阿里云百炼 DashScope 大模型提供课程咨询和试听预约服务。

## 技术栈

- **Spring Boot 3.4.3** + **Spring AI 1.0.0**（通过 `spring-ai-bom`）
- **Java 17**，Maven 构建
- **AI 模型**：阿里云百炼 DashScope（OpenAI 兼容接口），对话 `qwen3-max`，嵌入 `text-embedding-v3`；可选 Ollama 本地部署
- **数据库**：MySQL + **MyBatis-Plus 3.5.10.1**
- **向量存储**：Redis（`spring-ai-starter-vector-store-redis`），索引 `java-index`
- **Lombok**

## 架构分层

```
config/
  CommonConfiguration.java   → ChatClient × 3（basic / service / rag）、ChatMemory、VectorStore
  CrosConfiguration.java     → 全局 CORS
controller/
  ChatController.java        → POST /ai/chat, GET /ai/service（流式 Flux<String>）
  ChatHistoryController.java → GET /ai/history/{type}[/{chatId}]
  PDFController.java         → 骨架占位
tools/
  CourseTools.java           → @Tool: findCourse, queryAllSchools, generateCourseReservation
repository/
  ChatHistoryRepository      → 会话 ID 列表持久化接口
  ChatHistory.java           → 内存 Map 实现（TODO: 迁移到数据库）
reader/
  MarkDownReader.java        → 读取 text.md 转为 List<Document>，用于向量库导入
service/                     → MyBatis-Plus IService 层（Course、School、CourseReservation）
mapper/                      → MyBatis-Plus BaseMapper 接口 + 空 XML
entity/
  po/                        → 数据库实体：Course、School、CourseReservation
  query/                     → AI 工具参数：CourseQuery、Sort
  vo/                        → 响应 DTO：MessageVO
enums/
  SessionType.java           → CHAT / SERVICE
  SystemPromptEnums.java     → 系统提示词：RAG_PROMPT、SERVICE_PROMPT
```

## 关键设计

### 三个 ChatClient Bean（CommonConfiguration.java）
- **`chatClient`** — 基础客户端，提示词"你叫小云"，通用对话
- **`serviceClient`** — 客服客户端，使用 `SERVICE_PROMPT`（"小黑"角色），绑定 `CourseTools` 用于数据库查询
- **`ragClient`** — RAG 客户端，使用 `RAG_PROMPT`，绑定 `QuestionAnswerAdvisor`（基于 `SimpleVectorStore`）

### AI 配置
- 通过 OpenAiChatModel 连接 DashScope OpenAI 兼容接口：`https://dashscope.aliyuncs.com/compatible-mode/v1`
- API 密钥来自环境变量 `${ALIYUN_API_KEY}`
- ChatClient 装配了 `MessageChatMemoryAdvisor`（会话窗口）和 `SimpleLoggerAdvisor`

### 函数调用（CourseTools.java）
- 三个 `@Tool` 方法供大模型调用：`findCourse`、`queryAllSchools`、`generateCourseReservation`
- `CourseQuery` 支持可选的 `type`、`edu` 过滤条件和排序参数
- `generateCourseReservation` 创建 `course_reservation` 记录并返回生成的 ID

### 会话记忆
- **会话列表**：`ChatHistory.java` — 内存 `Map<String, List<String>>`，按会话类型分类
- **消息内容**：Spring AI 的 `MessageWindowChatMemory` — 内存窗口存储
- 两者均有 TODO 标记，后续需迁移到数据库持久化

### RAG 管道（部分实现）
- `MarkDownReader` 将 `static/text.md` 导入向量库（排除代码块和引用块）
- 已引入 PDF 读取依赖（`spring-ai-pdf-document-reader`），但 `PDFController` 为骨架
- Redis 向量库配置：索引 `java-index`，前缀 `notes`

### API 接口
| 方法 | 路径 | 说明 |
|--------|------|------|
| POST | `/ai/chat?prompt=...&chatId=...` | 通用流式对话 |
| GET | `/ai/service?prompt=...&chatId=...` | 客服流式对话 |
| GET | `/ai/history/{type}` | 按类型获取会话 ID 列表 |
| GET | `/ai/history/{type}/{chatId}` | 获取会话消息历史 |

### 流式响应
- 两个聊天接口均返回 `Flux<String>`，使用 `text/html` 类型（前端未处理 SSE）

## 配置说明

- **`application.yaml`**：DashScope API 地址 + 模型，MySQL `localhost:3306/itheima`，Redis 向量库
- **环境变量**：`ALIYUN_API_KEY` — 阿里云百炼 API 密钥
- **MySQL 表**：`course`、`school`、`course_reservation`

## 测试

单个测试类：`SpringAiApplicationTests.java` — 基础上下文加载验证。
