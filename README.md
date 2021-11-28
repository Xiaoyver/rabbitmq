# RabbitMQ DEMO

### 功能点

1. 动态创建队列
2. 动态监听

## 创建队列

[createQueue()](./common/src/main/java/com/mq/utils/RabbitMqUtils.java)

```
//创建队列
createQueue(RabbitMqQueue rabbitMqQueue)
/*
 * 创建队列并且自动监听
 * ChannelAwareMessageListener作为监听数据后的处理类,有默认的处理
 * redisPubSub.publish()作为创建队列后发布一个事件,由消费者监听后处理
 */
createQueue(RabbitMqListenerEntity rabbitMqListenerEntity)
```

[RabbitMqProduceFactoryInit](./produce/src/main/java/com/produce/factory/RabbitMqProduceFactoryInit.java)

```
//生产者启动自动创建队列
 run(String... args)
```

## 监听

[addListener()](./common/src/main/java/com/mq/utils/RabbitMqUtils.java)

```
//动态添加监听
addListener(RabbitMqListenerEntity rabbitMqListenerEntity) 
```

[RabbitMqListenerFactoryInit](./consumer/src/main/java/com/consumer/factory/RabbitMqListenerFactoryInit.java)

```
//消费者者启动自动监听队列
 run(String... args)
```
