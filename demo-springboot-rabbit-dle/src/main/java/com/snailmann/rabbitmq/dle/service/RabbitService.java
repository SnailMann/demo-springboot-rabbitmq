package com.snailmann.rabbitmq.dle.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Action;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitAdmin rabbitAdmin;

    public void dleTest() {

        /**
         * 死信交换机和死信队列
         */
        TopicExchange dleExchange = new TopicExchange("snailmann.dle.exchange");
        Queue dleQueue = new Queue("snailmann.dle.queue");
        rabbitAdmin.declareExchange(dleExchange);
        rabbitAdmin.declareQueue(dleQueue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(dleQueue)
                .to(dleExchange)
                .with("#"));

        /**
         * 设置了死信队列模式和队列TTL过期实际的普通队列
         */
        Map<String, Object> arguments = new HashMap<>(10);
        arguments.put("x-dead-letter-exchange", dleExchange.getName());
        arguments.put("x-dead-letter-routing-key", "#");
        arguments.put("x-message-ttl", 10000); //队列的过期实际为10s
        Queue ttlQueue = new Queue("snailmann.ttl.queue", true, false, false, arguments);
        rabbitAdmin.declareQueue(ttlQueue);

        /**
         * 我们往有过期时间的ttl队列，发送一个消息，而且并没有消费者去消费
         * 最后我们发现本应该在snailmann.ttl.queue队列的消息，过了10s后，被转发到snailmann.dle.queue队列中
         */
        rabbitTemplate.convertAndSend(ttlQueue.getName(), "hello world");

    }
}
