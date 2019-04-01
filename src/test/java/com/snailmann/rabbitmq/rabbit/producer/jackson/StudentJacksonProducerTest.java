package com.snailmann.rabbitmq.rabbit.producer.jackson;

import com.snailmann.rabbitmq.entity.Student;
import com.snailmann.rabbitmq.rabbit.producer.jackson.StudentJacksonProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;


/**
 *
 * RabbitTempalte生产端普通测试 | Jackson消息转换器模式
 * 三种发生方式 direct,topic,fanout
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentJacksonProducerTest {

    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentJacksonProducer studentJacksonProducer;

    /**
     * ok
     */
    @Test
    public void directDefaultSend() {
        studentJacksonProducer.directDefaultSend("student", student);
    }

    /**
     * 不ok，没有这种写法
     */
    @Test
    public void directDefaultWithCustomKeySend() {
        studentJacksonProducer.directDefaultWithCustomKeySend("custom-student", student);
    }

    /**
     * ok
     */
    @Test
    public void directCustomWithCustomKeySend() {
        studentJacksonProducer.directCustomWithCustomKeySend("direct-rabbit-test", "custom-student", student);
    }


    /**
     * ok
     */
    @Test
    public void fanoutSend() {
        studentJacksonProducer.fanoutSend("amq.fanout", student);
    }

    /**
     * ok
     */
    @Test
    public void topicSend() {
        studentJacksonProducer.topicSend("amq.topic", "test.student", student);
    }
}