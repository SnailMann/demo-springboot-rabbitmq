# demo-snailmann-rabbit-ack

主要是用来写测试手动ack和自动ack的例子


## 注意

- `AcknowledgeMode.NONE`如果是自动ack模式下,消费者消费期间出现异常，而程序没有显式捕获，交给虚拟机的话，就会出现rabbit不断重试消费该消息的过程；如果想要不重试，则需要显式捕获，做一些操作，比如下沉处理状态到数据库中；当你显式捕获后，Rabbitmq就会认为你已经有针对性处理了，自动ack
- `AcknowledgeMode.MANUAL` 如果是手动ack下，那么不管消费是否被消费成功，你只有手动的对该channel.basicAck，才会返回成功ack;如果手动的channel.basicNack, 那么就会手动的返回不成功ack(即nack)


