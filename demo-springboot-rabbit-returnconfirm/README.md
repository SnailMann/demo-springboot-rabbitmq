# demo-springboot-rabbit-returnconfirm

用来测试rabbitmq的confirm和return机制

## 注意

- `com.snailmann.rabbitmq.returnconfirm.simple`包下实现的是原生Rabbitmq API下的confirm模式


## Confirm机制


- Confirm机制模式下，当你发送消息的时候，你会设置一个该消息的唯一ID,当broker成功接收后，会给你返回回馈，并告知是哪个消息成功接收的，唯一ID是什么
- Confirm机制只保证，消息是否到达broker， 既只需要管消息是否投递到交换机即可，交换机之后的路由就不再管了


## Return机制

- Return机制则更为严格，你需要将消息投递到队列才算成功，既消息即使到达了交换机，但由于没有对应的routing key，路由到不存在的队列中，导致消息丢失，那么就会反馈，消息丢失
- 只有消息投递到队列，Return机制才会认为是成功

