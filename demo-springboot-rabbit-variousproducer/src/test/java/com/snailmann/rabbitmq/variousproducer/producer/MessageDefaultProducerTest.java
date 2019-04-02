package com.snailmann.rabbitmq.variousproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.variousproducer.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageDefaultProducerTest {

    @Autowired
    MessageDefaultProducer messageDefaultProducer;
    @Autowired
    ObjectMapper objectMapper;

    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }


    /**
     * 测试使用SimpleMessageConverter发送Java对象
     */
    @Test
    public void send() {
        messageDefaultProducer.sendObject(student);
    }

    /**
     * 测试使用SimpleMessageConverter发送String
     *
     * @throws JsonProcessingException
     */
    @Test
    public void sendString() throws JsonProcessingException {
        messageDefaultProducer.sendString(objectMapper.writeValueAsString(student));
    }

    /**
     * 测试使用SimpleMessageConverter发送bytes
     *
     * @throws JsonProcessingException
     */
    @Test
    public void sendBytes() throws JsonProcessingException {
        messageDefaultProducer.sendBytes(objectMapper.writeValueAsBytes(student));
    }
}