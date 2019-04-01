package com.snailmann.rabbitmq.rabbit.producer.jackson;

import com.snailmann.rabbitmq.entity.Student;
import com.snailmann.rabbitmq.rabbit.producer.jackson.StudentJacksonReturnProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * 生产者开启Return测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentJacksonReturnProducerTest {
    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentJacksonReturnProducer studentJacksonReturnProducer;

    /**
     * 测试成功
     */
    @Test
    public void directDefaultSend() {
        studentJacksonReturnProducer.directDefaultSend("student", student);
    }

    /**
     * 测试失败
     */
    @Test
    public void directCustomSend() throws InterruptedException {
        studentJacksonReturnProducer.directCustomSend("不存在的交换机", "不存在的路由键", student);
    }

    @Test
    public void fanoutSend() throws InterruptedException {
        studentJacksonReturnProducer.fanoutSend("amq.fanout", student);
    }
}