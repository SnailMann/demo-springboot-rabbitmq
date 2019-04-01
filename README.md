# RabbitMQ Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

### 测试注意

- 跑测试用例的时候，要注意，测试用例也是会启动Spring容器的，所以如果有@RabbitListener，也是会开启消费的，不要给这个情况影响了测试结果

### RabbitMQ的使用

- 生产者不管消息投递的队列是什么，只需要关系要投递的交换机是什么，路由键是什么？具体路由到那个队列，是交换局根据路由键去做的
- 消息者不需要管我监听那个交换机，路由键是什么，只需要关心我需要从哪个队列消费消息

### RabbitMQ常要解决的问题

- 消息重复消费问题
- 消息投递是否成功问题
- 消息消费失败问题

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

- 如果项目有消费者，则需要配置rabbitListenerContainerFactory
- 如果项目有生成者，则需要配置rabbitTemplate

### MessageConvertor
常用的MessageConvertor有

- `SimpleMessageConvertor`
byte[]类型则不做转换，String类型转换成byte[], 其他Object通过Java序列化方式转换成byte[]
- `Jackson2JsonMessageConvertor` 
Json与对象之间的序列化与反序列化
- `自定义MessageConvertor`





### @RabbitListener和@RabbitHandler

- 通常情况下@RabbitListener可以放在类上，也可以放在方法上，@RabbitHandler可以放在方法上
- 使用不同的MessageConvertor，对@RabbitListener是放在类上还是方法上，以及是否需要@RabbitHandler是有影响的
- 在使用默认的SimpleMessageConvertor的情况下

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

该配置可以代替cachingConnectionFactory.setPublisherConfirms(true);
```yaml
spring:
  rabbitmq:
    publisher-confirms: true 
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
 

该配置可以代替cachingConnectionFactory.setPublisherReturns(true);
```yaml
spring:
  rabbitmq:
    publisher-returns: true 
```

测试：

- 只要生产端发送的消息有到达队列，就代表成功
- 只要生产端发送的消息没有到达队列，就代表不成功(应该不存在到达一个队列，没有到达另一个队列的情况)


### 自动ACK

默认的情况就是自动ACK,消费者在拿到消息后，消费阶段没有抛异常，则会自动ack; 如果抛出了异常则会自定nack
如果抛出异常后，被我们捕获了，会被认为消费成功，自动ack


### 手动ACK

消息确认模式有：
- AcknowledgeMode.NONE：自动确认
- AcknowledgeMode.AUTO：根据情况确认
- AcknowledgeMode.MANUAL：手动确认


意义：

- 可以让开发人员自行决定是否要手动ack，自行ack或nack可以增加程序的灵活性，实现一个些逻辑，但也增加了开发成本


场景：

- 比如判断消息是否消费成功，如果消费失败抛异常，则手动nack, 告诉队列该消息消费失败; 可以选择重试，重试到一定的次数，进入死信或则丢弃;Rabbit本身不支持重试次数，所以该功能可以在客户端代码实现


代码：

```java
@Bean
public RabbitListenerContainerFactory<?> rabbitListenerManualAckContainerFactory(ConnectionFactory connectionFactory){
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(new Jackson2JsonMessageConverter());
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);             //开启手动 ack
    return factory;
}
```

该配置可以代替factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: manual
```

消费者
```java
@Component
@RabbitListener(queues = "student", containerFactory = "rabbitListenerManualAckJacksonContainerFactory")
public class StudentManualAckConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @RabbitHandler
    public void receive(Student student, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("manualAck : " + tag + "body:" + student);
        try {
            //手动ack
            channel.basicAck(tag, false);
            //消费失败,手动nack
            //require为true，则消息会无限重试，如果为false，消息会进入死信或丢弃
            channel.basicNack(tag, false, false);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }


}
```
