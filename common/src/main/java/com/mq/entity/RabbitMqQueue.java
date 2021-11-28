package com.mq.entity;

import com.mq.enums.MqOpenStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Exchange;

import java.util.Map;

/**
 * 队列创建数据
 *
 * @author liu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMqQueue {

    /**
     * 队列
     */
    private String queue;
    /**
     * 队列描述(中文名)
     */
    private String description;
    /**
     * 交换机,Work工作模式这个可以为空
     */
    private Exchange exchange;
    /**
     * 路由KEY
     * <p>
     * fanout交换机不需要
     * </p>
     * <p>
     * 直连交换机和默认的交换机不需要,默认是使用队列名
     * </p>
     */
    private String routingKey;

    //下面的参数如果不知道具体意义请使用默认的，不要修改
    /**
     * 队列优先升级
     */
    private RabbitMqQueueGrade queueGrade;
    /**
     * 队列状态
     */
    @Builder.Default
    private MqOpenStatusEnum openStatusEnum = MqOpenStatusEnum.OPEN;
    /**
     * 如果我们声明一个持久队列，则为真（该队列将在服务器重启后继续存在）
     */
    @Builder.Default
    private Boolean durable = true;
    /**
     * 如果我们声明一个独占队列，则为真（该队列将仅被声明者的连接使用）
     */
    @Builder.Default
    private Boolean exclusive = false;
    /**
     * 如果服务器在不再使用时删除队列，则为真
     */
    @Builder.Default
    private Boolean autoDelete = false;
    /**
     * 队列存在时,是否删除,然后再创建
     */
    @Builder.Default
    private Boolean existDelete = false;
    /**
     * 创建队列的额外参数
     */
    private Map<String, Object> map;
}
