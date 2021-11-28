package com.mq.compon;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gd liu
 * @version 1.0
 * @date 2021/7/25 17:03
 */
@Component
public class RedisPubSub {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 队列消费监听TOPIC
     */
    public static final ChannelTopic MQ_LISTENER_TOPIC = new ChannelTopic("/redis/listener");

    /**
     * 推送消息
     */
    public void publish(String content, ChannelTopic channelTopic) {
        System.out.println(content);
        redisTemplate.convertAndSend(channelTopic.getTopic(), content);
    }

}
