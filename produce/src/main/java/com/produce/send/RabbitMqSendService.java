package com.produce.send;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liu
 */
@Component
public class RabbitMqSendService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送数据
     *
     * @param routingKey 路由KEY
     * @param data       数据
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:44
     */
    public boolean send(String routingKey, Object data) {
        rabbitTemplate.convertAndSend(routingKey, data);
        return true;
    }

    /**
     * 发送数据
     *
     * @param routingKey 路由KEY
     * @param exchange   交换机
     * @param data       数据
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:44
     */
    public boolean send(String routingKey, String exchange, Object data) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data);
        return true;
    }

    /**
     * 发送等级消息
     *
     * @param routingKey 路由KEY
     * @param data       数据
     * @param priority   级别
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:47
     */
    public boolean sendPriority(String routingKey, Object data, int priority) {
        rabbitTemplate.convertAndSend(routingKey, data, message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        });
        return true;
    }

    /**
     * 发送等级消息
     *
     * @param routingKey 路由KEY
     * @param exchange   交换机
     * @param data       数据
     * @param priority   级别
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:47
     */
    public boolean sendPriority(String routingKey, String exchange, Object data, int priority) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data, message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        });
        return true;
    }

    /**
     * 发送延时消息
     *
     * @param routingKey 路由KEY
     * @param exchange   交换机
     * @param data       数据
     * @param delayTime  延时时间/毫秒
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:47
     */
    public boolean sendDelay(String routingKey, String exchange, Object data, long delayTime) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data, message -> {
            //注意这里时间可以使long，而且是设置header
            message.getMessageProperties().setHeader("x-delay", delayTime);
            return message;
        });
        return true;
    }

    /**
     * 发送延时等级消息
     *
     * @param routingKey 路由KEY
     * @param exchange   交换机
     * @param data       数据
     * @param delayTime  延时时间/毫秒
     * @param priority   级别
     * @return boolean
     * @author liu
     * @date 2021/11/28 10:47
     */
    public boolean sendDelayPriority(String routingKey, String exchange, Object data, long delayTime, int priority) {
        rabbitTemplate.convertAndSend(exchange, routingKey, data, message -> {
            //注意这里时间可以使long，而且是设置header
            message.getMessageProperties().setHeader("x-delay", delayTime);
            message.getMessageProperties().setPriority(priority);
            return message;
        });
        return true;
    }

}
