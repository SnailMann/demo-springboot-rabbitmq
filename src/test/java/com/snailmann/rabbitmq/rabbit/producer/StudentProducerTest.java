package com.snailmann.rabbitmq.rabbit.producer;

import com.snailmann.rabbitmq.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

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
    public void directDefaultSend() {
        studentProducer.directDefaultSend("student", student);
    }

    /**
     * 不ok，没有这种写法
     */
    @Test
    public void directDefaultWithCustomKeySend() {
        studentProducer.directDefaultWithCustomKeySend("custom-student", student);
    }

    /**
     * ok
     */
    @Test
    public void directCustomWithCustomKeySend() {
        studentProducer.directCustomWithCustomKeySend("direct-rabbit-test", "custom-student", student);
    }


    /**
     * ok
     */
    @Test
    public void fanoutSend() {
        studentProducer.fanoutSend("amq.fanout", student);
    }

    /**
     * ok
     */
    @Test
    public void topicSend() {
        studentProducer.topicSend("amq.topic", "test.student", student);
    }
}