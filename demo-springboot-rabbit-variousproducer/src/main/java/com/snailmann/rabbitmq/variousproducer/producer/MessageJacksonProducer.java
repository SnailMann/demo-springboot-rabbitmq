package com.snailmann.rabbitmq.variousproducer.producer;

import com.snailmann.rabbitmq.variousproducer.entity.Student;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageJacksonProducer {

    @Autowired
    RabbitTemplate rabbitJacksonTemplate;

    /**
     * 使用JacksonMessageConverter来做序列化
     *
     * @param student
     */
    public void send(Student student) {
        rabbitJacksonTemplate.convertAndSend("student", student);
    }

}
