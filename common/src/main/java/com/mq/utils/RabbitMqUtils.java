package com.mq.utils;

import com.mq.compon.DefaultMessageListenerHandler;
import com.mq.compon.RedisPubSub;
import com.mq.entity.RabbitMqListenerEntity;
import com.mq.entity.RabbitMqQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * RabbitMq 工具类
 *
 * @author liu
 */
@Slf4j
@Component
public class RabbitMqUtils {

    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    private RedisPubSub redisPubSub;
    @Resource
    private CachingConnectionFactory connectionFactory;
    @Resource
    private DefaultMessageListenerHandler defaultMessageListenerHandler;

    /**
     * 获取RabbitAdmin
     * <p>
     * 有什么没写操作队列管理的操作，用这个这个自己写
     * </p>
     *
     * @return org.springframework.amqp.rabbit.core.RabbitAdmin
     * @author liu
     * @date 2021/11/26 17:10
     */
    public RabbitAdmin getRabbitAdmin() {
        return rabbitAdmin;
    }

    /**
     * 队列信息
     *
     * @param queueName 队列名
     * @return org.springframework.amqp.core.QueueInformation
     * @author liu
     * @date 2021/11/26 17:11
     */
    public QueueInformation getQueueInfo(String queueName) {
        return rabbitAdmin.getQueueInfo(queueName);
    }

    /**
     * 队列是否存在
     *
     * @param queueName 队列名
     * @return boolean
     * @author liu
     * @date 2021/11/26 17:11
     */
    public boolean existQueue(String queueName) {
        return Objects.nonNull(getQueueInfo(queueName));
    }

    /**
     * 删除队列
     *
     * @param queueName 队列名
     * @return boolean
     * @author liu
     * @date 2021/11/26 17:30
     */
    public boolean deleteQueue(String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 创建队列-并且绑定
     *
     * @param rabbitMqListenerEntity 队列数据
     * @return boolean
     * @author liu
     * @date 2021/11/26 17:27
     */
    public boolean createQueue(RabbitMqListenerEntity rabbitMqListenerEntity) {
        //队列如果存在,检查是否需要删除队列再创建
        if (createQueue(rabbitMqListenerEntity.getRabbitMqQueue())) {
            //推送队列绑定消息到Redis，消费者收到监听，开始监听
            log.warn("{}", JsonUtil.beanToJson(rabbitMqListenerEntity));
            redisPubSub.publish(JsonUtil.beanToJson(rabbitMqListenerEntity), RedisPubSub.MQ_LISTENER_TOPIC);
        }
        return true;
    }

    /**
     * 创建队列
     *
     * @param rabbitMqQueue 队列数据
     * @return boolean
     * @author liu
     * @date 2021/11/26 17:27
     */
    public boolean createQueue(RabbitMqQueue rabbitMqQueue) {
        //队列如果存在,检查是否需要删除队列再创建
        if (existQueue(rabbitMqQueue.getQueue())) {
            if (Boolean.TRUE.equals(rabbitMqQueue.getExistDelete())) {
                deleteQueue(rabbitMqQueue.getQueue());
            } else {
                return true;
            }
        }
        //创建队列
        Queue queue = getQueue(rabbitMqQueue);
        String res = rabbitAdmin.declareQueue(queue);
        log.info("创建队列{}成功", res);
        //绑定队列
        bind(rabbitMqQueue, queue);
        return true;
    }

    /**
     * 绑定队列
     * TOPIC的队列可以绑定多个KEY
     *
     * @param rabbitMqQueue 队列
     * @return boolean
     * @author liu
     * @date 2021/11/27 16:55
     */
    public boolean bindQueue(RabbitMqQueue rabbitMqQueue) {
        //处理额外参数
        Queue queue = getQueue(rabbitMqQueue);
        //绑定队列,如果没有交换机,默认是work工作模式
        return bind(rabbitMqQueue, queue);
    }

    /**
     * 绑定
     *
     * @param rabbitMqQueue 队列
     * @param queue         队列
     * @return boolean
     * @author liu
     * @date 2021/11/27 16:54
     */
    private boolean bind(RabbitMqQueue rabbitMqQueue, Queue queue) {
        if (Objects.isNull(rabbitMqQueue.getExchange())) {
            return true;
        }
        rabbitAdmin.declareExchange(rabbitMqQueue.getExchange());
        //发布订阅型,不需要指定routingKey
        if (rabbitMqQueue.getExchange() instanceof FanoutExchange) {
            FanoutExchange exchange = (FanoutExchange) rabbitMqQueue.getExchange();
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
            log.info("绑定队列{}到交换机{}成功", rabbitMqQueue.getQueue(), exchange.getName());
            return true;
        }
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(rabbitMqQueue.getExchange())
                .with(rabbitMqQueue.getRoutingKey()).noargs());
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(rabbitMqQueue.getExchange())
                .with("a" + rabbitMqQueue.getRoutingKey()).noargs());
        log.info("绑定队列{}到交换机{}成功", rabbitMqQueue.getQueue(), rabbitMqQueue.getExchange().getName());
        return true;
    }

    /**
     * 构建队列
     *
     * @param rabbitMqQueue 队列数据
     * @return org.springframework.amqp.core.Queue
     * @author liu
     * @date 2021/11/27 16:52
     */
    private Queue getQueue(RabbitMqQueue rabbitMqQueue) {

        Map<String, Object> argsMap = new HashMap<>(16);
        if (Objects.nonNull(rabbitMqQueue.getMap())) {
            argsMap = rabbitMqQueue.getMap();
        }
        //如果是优先级队列,增加优先级的参数
        if (Objects.nonNull(rabbitMqQueue.getQueueGrade())) {
            argsMap.put(rabbitMqQueue.getQueueGrade().getArgs(), rabbitMqQueue.getQueueGrade().getPriority());
        }
        return new Queue(rabbitMqQueue.getQueue(), rabbitMqQueue.getDurable(), rabbitMqQueue.getExclusive(),
                rabbitMqQueue.getAutoDelete(), argsMap);
    }

    /**
     * 添加队列监听
     *
     * @param rabbitMqListenerEntity 队列
     * @author gd liu
     * @date 2021/11/27 18:04
     */
    public void addListener(RabbitMqListenerEntity rabbitMqListenerEntity) {
        RabbitMqQueue rabbitMqQueue = rabbitMqListenerEntity.getRabbitMqQueue();
        //默认的监听处理处理
        MessageListener listenerInstant = rabbitMqListenerEntity.getMessageListener() == null
                ? defaultMessageListenerHandler : rabbitMqListenerEntity.getMessageListener();
        if (existQueue(rabbitMqQueue.getQueue())) {
            SimpleMessageListenerContainer listenerContainer = createListenerContainer(listenerInstant, rabbitMqQueue.getQueue());
            Assert.notNull(listenerContainer, rabbitMqQueue.getQueue() + "获取SimpleMessageListenerContainer为空!");
            if (listenerContainer.isRunning()) {
                log.info("{}正在运行", listenerContainer);
            } else {
                listenerContainer.start();
                log.info("添加队列{}({})监听成功!", rabbitMqQueue.getQueue(), rabbitMqQueue.getDescription());
            }
        } else {
            log.info("队列:{}({})不存在", rabbitMqQueue.getDescription(), rabbitMqQueue.getQueue());
        }
    }

    /**
     * 添加监听
     *
     * @param listenerInstant 消息的异步传递的侦听器接口
     * @param queueNames      队列名
     * @return org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
     * @author gd liu
     * @date 2021/11/27 17:57
     */
    private SimpleMessageListenerContainer createListenerContainer(MessageListener listenerInstant, String... queueNames) {
        SimpleMessageListenerContainer container = null;
        try {
            container = new SimpleMessageListenerContainer(connectionFactory);
            container.addQueueNames(queueNames);
            container.setMessageListener(listenerInstant);
            //下面的一些参数,有兴趣可以在上层入参封装一下,我的业务场景允许这样统一设置
            container.setAutoStartup(true);
            container.setBatchSize(1);
            container.setPrefetchCount(1);
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
            long consumerCount = container.getActiveConsumerCount();
            log.info("修改成功:现有队列监听者[" + consumerCount + "]个");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return container;
    }
}
