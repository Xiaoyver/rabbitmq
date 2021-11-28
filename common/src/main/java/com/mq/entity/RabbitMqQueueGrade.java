package com.mq.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 队列等级
 *
 * @author liu
 */
@Data
@Builder
public class RabbitMqQueueGrade {

    /**
     * 队列等级默认参数，不需修改
     */
    @Builder.Default
    private String args = "x-max-priority";
    /**
     * 队列等级,默认10级
     * <p>
     * priority越大，优先级别越高
     * </p>
     * <p>
     * 经过测试，如果放入的数据级别不正确，数据消费的优先级别如下（priority=10）
     * 10>11>12>13>9>8>1
     * </p>
     */
    @Builder.Default
    private Integer priority = 10;
}
