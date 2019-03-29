package com.snailmann.rabbitmq.rabbit.producer;

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
 * 生产者开启Confirm机制测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentConfirmSimpleProducerTest {


    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentConfirmSimpleProducer studentConfirmProducer;

    /**
     * 测试成功案例
     */
    @Test
    public void directDefaultSend() throws InterruptedException {
        studentConfirmProducer.directDefaultSend("student", student);
    }

    /**
     * 测试失败案例
     *
     * @throws InterruptedException
     */
    @Test
    public void directCustomSendTest() throws InterruptedException {
        studentConfirmProducer.directCustomSend("不存的交换机", "student", student);
    }

    /**
     * 测试失败案例
     *
     * @throws InterruptedException
     */
    @Test
    public void directCustomSendWithMessage() throws InterruptedException, JsonProcessingException {
        studentConfirmProducer.directCustomSendWithMessage(null, "student", student);
    }


}