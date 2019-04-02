package com.snailmann.rabbitmq.variousproducer.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.variousproducer.entity.Student;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDefaultProducer {


    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 使用SimpleMessageConverter来序列化Java对象
     *
     * @param student
     */
    public void sendObject(Student student) {
        rabbitTemplate.convertAndSend("student", student);
    }

    /**
     * 使用SimpleMessageConverter来序列化字符串
     *
     * @param string
     */
    public void sendString(String string) {
        rabbitTemplate.convertAndSend("student", string);
    }

    /**
     * 使用SimpleMessageConverter来发送字节数组 | 与Serializable无关
     *
     * @param bytes
     */
    public void sendBytes(byte[] bytes) {
        rabbitTemplate.convertAndSend("student", bytes);
    }
}
