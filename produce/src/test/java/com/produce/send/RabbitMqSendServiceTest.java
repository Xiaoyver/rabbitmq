package com.produce.send;

import com.mq.entity.RabbitMqListenerEntity;
import com.mq.entity.RabbitMqQueue;
import com.mq.utils.JsonUtil;
import com.mq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class RabbitMqSendServiceTest {

    @Resource
    private RabbitMqSendService rabbitMqSendService;
    @Resource
    private RabbitMqUtils rabbitMqUtils;

    @Test
    void send() {
        for (int i = 0; i < 10; i++) {
            log.info("{}", rabbitMqSendService.send("work_msg_queue", i));
        }
    }

    @Test
    void sendDelay() {
        for (int i = 0; i < 10; i++) {
            log.info("{}", rabbitMqSendService.sendDelay("delay_queue", "delay.exchange", i, i * 2000));
        }
    }

    @Test
    void createQueue() {
        RabbitMqListenerEntity   rabbitMqListenerEntity=    RabbitMqListenerEntity
                .builder()
                .rabbitMqQueue(RabbitMqQueue
                        .builder()
                        .queue("work_msg_queue2")
                        .description("Work工作模式队列")
                        .build())
                .build();

        log.warn("{}", JsonUtil.beanToJson(rabbitMqListenerEntity));
        rabbitMqUtils.createQueue(rabbitMqListenerEntity);
    }
}