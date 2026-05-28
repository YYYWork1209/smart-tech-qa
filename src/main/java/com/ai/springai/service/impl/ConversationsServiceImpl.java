package com.ai.springai.service.impl;

import com.ai.springai.entity.po.Conversations;
import com.ai.springai.mapper.ConversationsMapper;
import com.ai.springai.service.IConversationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与AI的会话表 服务实现类
 * </p>
 *
 * @author YYYWork1209
 * @since 2026-05-28
 */
@Service
public class ConversationsServiceImpl extends ServiceImpl<ConversationsMapper, Conversations> implements IConversationsService {

}
