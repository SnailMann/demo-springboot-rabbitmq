# RabbitMQ Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)


### RabbitMQ的使用

- 生产者不管消息投递的队列是什么，只需要关系要投递的交换机是什么，路由键是什么？具体路由到那个队列，是交换局根据路由键去做的
- 消息者不需要管我监听那个交换机，路由键是什么，只需要关心我需要从哪个队列消费消息

### RabbitMQ的配置

- 配置 | 可以使用Jackson2JsonMessageConverter来代替默认的序列化，不替代则无法自动转换对象为String
```java

@Configuration
public class RabbitConfig {


    private static final MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}


```


### RabbitMQ的四种模式

- direct
- fanout
- topic
- headers

只有direct有默认的交换机，默认绑定所有的队列，并且routing key是队列的名称
其他的几种方式没有默认的交换机，没有默认绑定；对于默认的direct交换机，从默认的交换机中，不允许显示的绑定队列,删除队列


### 生产端的Confirm机制

意义：
- 作用就是可以实现生产端的消息是否投递成功的一个确认机制；是否投递成功的判断是该消息是否到底指定的exchange;
- Confirm机制只管消息是否投递到交换机，不管是否路由到队列
- 即Confirm机制只管是否投递到交换机

实现：
1. `cachingConnectionFactory.setPublisherConfirms(true);`将连接广场设置为开启Confirm模式
2. `rabbitTemplate.setConnectionFactory(cachingConnectionFactory);`将该连接工厂植入rabbitTemplate
3. `rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {...});`rabbitTemplate设置Confirm回调函数


代码：
```java
        @Bean
        public RabbitTemplate rabbitConfirmTemplate() {
           CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
           //1. 声明这是一个支持confirm机制的connectionFactory
           cachingConnectionFactory.setPublisherConfirms(true);
           cachingConnectionFactory.setHost(connectionFactory.getHost());
           cachingConnectionFactory.setPort(connectionFactory.getPort());
           RabbitTemplate rabbitTemplate = new RabbitTemplate();
           //2. 声明confirmCallback
           rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
               if (ack) {
                   log.warn("blocker success receive!! cause :{} and data: {}", cause, correlationData);
               } else {
                   log.error("blocker failed receive!! cause :{} and data: {}", cause, correlationData);
               }
           });
           rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
           rabbitTemplate.setMessageConverter(messageConverter);
           return rabbitTemplate;
       }
```

测试：

- 只要生产端发送的消息到达指定交换机exchange，回调函数的ack就是true
- 只要生产端发送的消息没有到达指定交换机exchange，回调函数的ack就是false;我们可以把消息发送到一个不存在的exchange去测试







### 生产端的Return机制


意义：
- 当消息投递到指定的交换机，但是没有路由到指定的队列时，就会触发Return机制，将没有成功投递到队列的消息返回
- 但是如果在投递交换机时，就没有投递成功，而导致没有投递到队列，是不会触发Return机制的
- 即Return只管是否有投递到对应的队列

mandatory的作用:
- 当mandatory标志位设置为true时，如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，那么broker会调用basic.return方法将消息返还给生产者;当mandatory设置为false时，出现上述情况broker会直接将消息丢弃;通俗的讲，mandatory标志告诉broker代理服务器至少将消息route到一个队列中，否则就将消息return给发送者


实现：
1. `cachingConnectionFactory.setPublisherReturns(true); `连接工厂启动Return机制
2. `rabbitTemplate.setMandatory(true);`rabbitTemplate要启动强制性返回机制
3. `rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->{})` rabbitTempalte要设置Return回调

代码：
```java
    @Bean
    public RabbitTemplate rabbitReturnTemplate() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        //1. 声明这是一个支持confirm机制的connectionFactory
        cachingConnectionFactory.setPublisherReturns(true);
        cachingConnectionFactory.setHost(connectionFactory.getHost());
        cachingConnectionFactory.setPort(connectionFactory.getPort());
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        //2. 声明强制性
        rabbitTemplate.setMandatory(true);
        //3. 声明returnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.warn("message: {} , replyCode: {} , replyText: {} , exchange: {} , routingKey:{}", message, replyCode, replyText, exchange, routingKey));
        rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

```

测试：

- 只要生产端发送的消息有到达队列，就代表成功
- 只要生产端发送的消息没有到达队列，就代表不成功(应该不存在到达一个队列，没有到达另一个队列的情况)




