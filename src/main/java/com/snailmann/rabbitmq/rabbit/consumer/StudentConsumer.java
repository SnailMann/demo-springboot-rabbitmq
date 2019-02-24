package com.snailmann.rabbitmq.rabbit.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.entity.Student;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
@RabbitListener(queues = "student")
public class StudentConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @RabbitHandler
    public void receive(Student student) {
        System.out.println(student);
    }

    @RabbitHandler
    public void receiveMsg(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            Student student = objectMapper.readValue(json, Student.class);
            System.out.println("msg: " + student);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
