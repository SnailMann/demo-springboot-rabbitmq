package com.snailmann.rabbitmq.rabbit.producer.defaults;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snailmann.rabbitmq.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;


/**
 * RabbitTempalte生产端普通测试 | Jackson消息转换器模式
 * 三种发生方式 direct,topic,fanout
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentProducerTest {

    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentProducer studentProducer;

    /**
     * ok
     */
    @Test
    public void directDefaultSend() throws JsonProcessingException {
        studentProducer.directDefaultSend("student", student);
    }

    /**
     * 不ok，没有这种写法
     */
    @Test
    public void directDefaultWithCustomKeySend() throws JsonProcessingException {
        studentProducer.directDefaultWithCustomKeySend("custom-student", student);
    }

    /**
     * ok
     */
    @Test
    public void directCustomWithCustomKeySend() throws JsonProcessingException {
        studentProducer.directCustomWithCustomKeySend("direct-rabbit-test", "custom-student", student);
    }


    /**
     * ok
     */
    @Test
    public void fanoutSend() throws JsonProcessingException {
        studentProducer.fanoutSend("amq.fanout", student);
    }

    /**
     * ok
     */
    @Test
    public void topicSend() throws JsonProcessingException {
        studentProducer.topicSend("amq.topic", "test.student", student);
    }
}