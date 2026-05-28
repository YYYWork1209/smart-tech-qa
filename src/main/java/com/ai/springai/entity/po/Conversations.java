package com.ai.springai.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与AI的会话表
 * </p>
 *
 * @author YYYWork1209
 * @since 2026-05-28
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("conversations")
public class Conversations implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID，由前端生成传入（如UUID）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 会话标题
     */
    @TableField("title")
    private String title;

    /**
     * 会话类型：chat-闲聊，service-客服
     */
    @TableField("type")
    private String type;

    /**
     * 状态：1-正常，0-已归档/删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间（毫秒精度）
     */
    @TableField("created_at")
    private LocalDateTime createdAt;


}
