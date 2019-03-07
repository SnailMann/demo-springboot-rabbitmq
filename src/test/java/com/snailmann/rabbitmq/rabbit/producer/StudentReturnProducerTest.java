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

/**
 * 生产者开启Return测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentReturnProducerTest {
    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentReturnProducer studentReturnProducer;

    /**
     * ok
     */
    @Test
    public void directDefaultSend() {
        studentReturnProducer.directDefaultSend("student", student);
    }

    /**
     * 测试失败
     */
    public void directCustomSend() throws InterruptedException {
        studentReturnProducer.directCustomSend("不存在的交换机", "不存在的路由键", student);
    }


    public void fanoutSend() throws InterruptedException {
        studentReturnProducer.fanoutSend("amq.fanout",student);
    }
}