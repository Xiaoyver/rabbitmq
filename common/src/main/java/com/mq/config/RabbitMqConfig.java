package com.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 指定序列化
     *
     * @return org.springframework.amqp.support.converter.MessageConverter
     * @author liu
     * @date 2021/11/26 15:45
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 指定RabbitMQ
     *
     * @param connectionFactory 连接
     * @return org.springframework.amqp.rabbit.core.RabbitAdmin
     * @author liu
     * @date 2021/11/26 15:45
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 服务启动时候开启自动启动
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    /**
     * Rabbit侦听器端点注册表
     * <p>
     * 用于管理标注了@RabbitListener(id需有值)的方法
     *
     * @return Rabbit侦听器端点注册表
     */
    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }

    /**
     * 配置RabbitMQ 生产消息的Confirm模式
     *
     * @param connectionFactory 连接
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     */
    @Bean
    public RabbitTemplate getTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 消息发送到交换器Exchange后触发回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            // 可以进行消息入库操作，这里有个坑，如果消息进入队列失败，那么这里的ack还是true
            log.info("消息唯一标识 correlationData = {}", correlationData);
            log.info("确认结果 ack = {}", ack);
            log.info("失败原因 cause = {}", cause);
        });

        // 配置这个，下面的ReturnCallback 才会起作用
        template.setMandatory(true);
        // 如果消息从交换器发送到对应队列失败时触发（比如 根据发送消息时指定的routingKey找不到队列时会触发）
        template.setReturnsCallback(returnedMessage -> {
            log.info("消息主体 message= {}", returnedMessage.getMessage());
            log.info("回复码 replyCode= {}", returnedMessage.getReplyCode());
            log.info("回复描述 replyText= {}", returnedMessage.getReplyText());
            log.info("交换机名字 exchange= {}", returnedMessage.getExchange());
            log.info("路由键 routingKey= {}", returnedMessage.getRoutingKey());
            log.info("消息 {}", returnedMessage);
        });

        return template;
    }

}
