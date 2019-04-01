package com.snailmann.rabbitmq.rabbit.consumer.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.entity.Student;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 普通消费者
 */

@RabbitListener(queues = "student")
public class StudentJacksonConsumer {

    @Autowired
    ObjectMapper objectMapper;


    public void receive(Student student) {
        System.out.println(student);
    }

    /**
     * 测试获取MessageProperties的信息
     *
     * @param message
     */
    public void receiveMsg(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            Student student = objectMapper.readValue(json, Student.class);
            System.out.println("msg: " + student);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(message.getMessageProperties().toString());
    }

}
