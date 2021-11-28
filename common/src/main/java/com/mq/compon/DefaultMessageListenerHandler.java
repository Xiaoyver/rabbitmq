package com.mq.compon;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author gd liu
 * @version 1.0
 * @date 2021/11/23
 */
@Slf4j
@Component
public class DefaultMessageListenerHandler implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        log.info("{}", message);
        String ss = new String(message.getBody());
        log.info("data:{},queue:{}", ss, message.getMessageProperties().getConsumerQueue());
        // 设置手动确认
        Thread.sleep(1000L);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

}