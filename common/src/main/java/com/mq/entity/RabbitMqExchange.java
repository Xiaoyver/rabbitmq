package com.mq.entity;

import com.mq.enums.RabbitMqExchangeTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 交换机
 *
 * @author liu
 */
@Data
@Builder
public class RabbitMqExchange {

    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 队列默认交换机类型
     */
    @Builder.Default
    private RabbitMqExchangeTypeEnum exchangeType = RabbitMqExchangeTypeEnum.WORK;
    /**
     * 交换机的额外参数
     * <p>
     * 例如延时交换机,需要指明延时的类型
     * argsMap.put("x-delayed-type", "direct");
     * </p>
     */
    private Map<String, Object> argsMap;
    //下面的参数如果不知道具体意义请使用默认的，不要修改
    /**
     * 如果我们声明一个独占，则为真（该交换机将仅被声明者的连接使用）
     */
    @Builder.Default
    private Boolean durable = true;
    /**
     * 如果服务器在不再使用时删除，则为真
     */
    @Builder.Default
    private Boolean autoDelete = false;
}
