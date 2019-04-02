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
public class MessageJacksonProducerTest {

    @Autowired
    private MessageJacksonProducer messageJacksonProducer;
    @Autowired
    private ObjectMapper objectMapper;

    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    /**
     * 使用Jackson2JsonMessageConverter发送Java对象
     */
    @Test
    public void send() {
        messageJacksonProducer.send(student);
    }

    /**
     * 使用Jackson2JsonMessageConverter发送字符串对象
     *
     * @throws JsonProcessingException
     */
    @Test
    public void sendString() throws JsonProcessingException {
        messageJacksonProducer.sendString(objectMapper.writeValueAsString(student));
    }

    /**
     * 使用Jackson2JsonMessageConverter发送bytep[]数组
     *
     * @throws JsonProcessingException
     */
    @Test
    public void sendBytes() throws JsonProcessingException {
        messageJacksonProducer.sendBytes(objectMapper.writeValueAsBytes(student));
    }
}