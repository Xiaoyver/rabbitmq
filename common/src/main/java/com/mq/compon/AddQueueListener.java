package com.mq.compon;

import com.mq.entity.RabbitMqListenerEntity;
import com.mq.utils.JsonUtil;
import com.mq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author gd liu
 * @version 1.0
 * @date 2021/11/24
 */
@Slf4j
@Component
public class AddQueueListener implements MessageListener {

    @Resource
    private RabbitMqUtils rabbitMqUtils;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("{}", message);
        RabbitMqListenerEntity rabbitMqListenerEntity;
        String json = JsonUtil.jsonToBean(message.toString(), String.class);
        if (Objects.isNull(json)) {
            return;
        }
        rabbitMqListenerEntity = JsonUtil.jsonToBean(json, RabbitMqListenerEntity.class);
        if (Objects.isNull(rabbitMqListenerEntity)) {
            return;
        }
        rabbitMqUtils.addListener(rabbitMqListenerEntity);
    }
}