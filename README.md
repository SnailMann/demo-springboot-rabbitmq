# RabbitMQ Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)

### RabbitMQ的使用

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