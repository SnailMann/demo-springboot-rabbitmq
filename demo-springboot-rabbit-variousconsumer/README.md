# demo-springboot-rabbit-variousconsumer

测试rabbitmq各式各样的消息接收方式

## 注意

1. @RabbitLisenter单独放在方法上
2. @RabbitLisenter放在类上以及@RabbitHandler放在方法上的形式去配合

以上两种方法会对消息的序列化造成不同的反应，所以要特别注意，但网上的资料也不是很多
## 测试

- simple格式监听者消费json格式的消息
- json格式的监听者消费simple格式的消息

## SimpleMessageConverter

- 如果是Java序列化对象，序列化的字节数组中是带有包名的，所以肯定会要保证包名一致，你才能使用Java的序列化方式，同时实体类还要满足实现了Serializable的条件
- 如果是json格式的消息体被SimpleMessageConverter格式的消费者监听了，那么只能当做byte[]字节数组来接收，不然会抛异常； @RabbitListener在类上，@RabbitHandler在方法上回抛出


## Jackson2JsonMessageConverter

- json格式的消息被json格式的监听者消费时，必须保证Type_id是一致的，不然是会消费失败的，抛出异常（@RabbitLisenter在类上，@RabbitHandler在方法上的情况） | @RabbitLisenter单独放在方法上，即使包名不一致，也是可以解析出数据的

## @RabbitListener与@RabbitHandler

- @RabbitLisenter可以放在类上，也可以放在方法上
- @RabbitLisenter单独放在方法上的时候，根据MessageConvertor的特性，会直接转换成参数类型
- @RabbitLisenter放在类上上，要搭配@RabbitHandler使用，Lisenter在类上，Handler在方法上。Lisenter声明队列，Handler代表处理的消息类型
