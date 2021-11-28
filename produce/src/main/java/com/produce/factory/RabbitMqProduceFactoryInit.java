package com.produce.factory;

import com.mq.entity.RabbitMqQueue;
import com.mq.entity.RabbitMqQueueGrade;
import com.mq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动项目创建队列
 *
 * @author gd liu
 * @version 1.0
 * @date 2021/11/23
 */
@Slf4j
@Component
public class RabbitMqProduceFactoryInit implements CommandLineRunner {

    @Resource
    private RabbitMqUtils rabbitMqUtils;
    private static List<RabbitMqQueue> list = new ArrayList<>();

    static {
        list.add(RabbitMqQueue
                .builder()
                .queue("work_msg_queue")
                .routingKey("work_msg_queue")
                .description("Work工作模式队列")
                .exchange(new DirectExchange("amq.direct"))
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("work_msg_queue1")
                .routingKey("work_msg_queue")
                .description("Work工作模式队列")
                .exchange(new DirectExchange("amq.direct"))
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("work_msg_grade_queue")
                .routingKey("work_msg_grade_queue")
                .description("Work工作模式消息级别队列")
                .queueGrade(RabbitMqQueueGrade.builder().build())
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("fanout_queue")
                .description("扇形队列")
                .exchange(new FanoutExchange("amq.fanout", true, false))
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("topic_queue")
                .description("Topic队列")
                .routingKey("topic*")
                .exchange(new TopicExchange("amq.topic", true, false))
                .build());
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        list.add(RabbitMqQueue
                .builder()
                .queue("delay_queue")
                .description("延时队列")
                .routingKey("delay_queue")
                .map(args)
                .exchange(new CustomExchange("delay.exchange", "x-delayed-message", true, false, args))
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("delay_queue1")
                .description("延时队列")
                .routingKey("delay_queue")
                .map(args)
                .exchange(new CustomExchange("delay.exchange", "x-delayed-message", true, false, args))
                .build());
        list.add(RabbitMqQueue
                .builder()
                .queue("delay_grade_queue")
                .description("延时等级队列")
                .routingKey("delay_grade_queue")
                .map(args)
                .queueGrade(RabbitMqQueueGrade.builder().priority(5).build())
                .exchange(new CustomExchange("delay.exchange", "x-delayed-message", true, false, args))
                .build());
    }

    @Override
    public void run(String... args) throws Exception {
        /*
         * 这里可以注入一个Service 查询数据库的配置
         * 这里为了方便直接写了静态的List
         */
        list.forEach(rabbitMqQueue -> rabbitMqUtils.createQueue(rabbitMqQueue));
    }

}
