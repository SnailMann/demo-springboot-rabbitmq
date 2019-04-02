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
     * 使用JacksonMessageConverter来做序列化Java对象
     *
     * @param student
     */
    public void send(Student student) {
        rabbitJacksonTemplate.convertAndSend("student", student);
    }


    /**
     * 使用JacksonMessageConverter来序列化字符串
     *
     * @param string
     */
    public void sendString(String string) {
        rabbitJacksonTemplate.convertAndSend("student", string);
    }

    /**
     * 使用JacksonMessageConverter来发送字节数组
     *
     * @param bytes
     */
    public void sendBytes(byte[] bytes) {
        rabbitJacksonTemplate.convertAndSend("student", bytes);
    }

}
