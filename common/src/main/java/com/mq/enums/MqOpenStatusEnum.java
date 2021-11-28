package com.mq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 队列状态
 *
 * @author liu
 */
@Getter
public enum MqOpenStatusEnum {
    /**
     * 开启
     * 关闭
     */
    OPEN,
    STOP;
}
