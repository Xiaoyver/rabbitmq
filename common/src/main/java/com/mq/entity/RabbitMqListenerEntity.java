package com.mq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * 队列监听数据
 *
 * @author liu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMqListenerEntity {

    /**
     * 队列数据
     */
    private RabbitMqQueue rabbitMqQueue;

    /**
     * 处理监听数据的Listener
     */
    private ChannelAwareMessageListener messageListener;

}
