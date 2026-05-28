CREATE TABLE `conversations` (
                                 `id`          VARCHAR(64)  NOT NULL COMMENT '会话ID，由前端生成传入（如UUID）',
                                 `user_id`     VARCHAR(64)  NOT NULL COMMENT '用户ID',
                                 `title`       VARCHAR(255) NOT NULL DEFAULT '' COMMENT '会话标题',
                                 `type`        ENUM('chat','service') NOT NULL DEFAULT 'chat' COMMENT '会话类型：chat-闲聊，service-客服',
                                 `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-已归档/删除',
                                 `created_at`  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间（毫秒精度）',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户与AI的会话表';

CREATE TABLE `messages` (
                            `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '消息自增主键',
                            `conversation_id` VARCHAR(64)  NOT NULL COMMENT '所属会话ID（外键）',
                            `role`            ENUM('user','assistant','system') NOT NULL COMMENT '消息角色',
                            `content`         LONGTEXT     NOT NULL COMMENT '消息内容（支持长文本）',
                            `token_count`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '该消息消耗的token数量',
                            `created_at`      DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '消息时间（毫秒精度）',
                            PRIMARY KEY (`id`),
                            CONSTRAINT `fk_message_conversation`
                                FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`)
                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话消息记录表';