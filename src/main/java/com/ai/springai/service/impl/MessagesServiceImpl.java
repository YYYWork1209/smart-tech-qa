package com.ai.springai.service.impl;

import com.ai.springai.entity.po.Messages;
import com.ai.springai.mapper.MessagesMapper;
import com.ai.springai.service.IMessagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会话消息记录表 服务实现类
 * </p>
 *
 * @author YYYWork1209
 * @since 2026-05-28
 */
@Service
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements IMessagesService {

}
