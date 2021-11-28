package com.mq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 交换机类型
 *
 * @author liu
 */
@Getter
public enum RabbitMqExchangeTypeEnum {
    /**
     * 队列类型
     */
    WORK,
    DIRECT,
    FANOUT,
    HEADERS,
    MATCH,
    TRACE,
    TOPIC,
    /**
     * 延时队列，需要插件支持(rabbitmq_delayed_message_exchange)
     * <p>
     * URL:https://www.rabbitmq.com/community-plugins.html
     */
    DELAY;
}
