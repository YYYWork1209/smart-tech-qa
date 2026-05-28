package com.ai.springai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会话消息记录表
 * </p>
 *
 * @author YYYWork1209
 * @since 2026-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("messages")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属会话ID（外键）
     */
    @TableField("conversation_id")
    private String conversationId;

    /**
     * 消息角色
     */
    @TableField("role")
    private String role;

    /**
     * 消息内容（支持长文本）
     */
    @TableField("content")
    private String content;

    /**
     * 该消息消耗的token数量
     */
    @TableField("token_count")
    private Integer tokenCount;

    /**
     * 消息时间（毫秒精度）
     */
    @TableField("created_at")
    private LocalDateTime createdAt;


}
