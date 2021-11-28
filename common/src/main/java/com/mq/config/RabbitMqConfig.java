package com.mq.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liu
 */
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

}
