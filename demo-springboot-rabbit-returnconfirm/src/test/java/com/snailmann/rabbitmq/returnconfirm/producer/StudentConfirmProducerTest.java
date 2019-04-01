package com.snailmann.rabbitmq.returnconfirm.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.snailmann.rabbitmq.returnconfirm.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * Jackson消息转换器模式
 * 生产者开启Confirm机制测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentConfirmProducerTest {


    Student student = new Student();

    {
        student.setName("Jerry");
        student.setAge(new Random(1).nextInt(60));
        student.setBirstday(new Date());
    }

    @Autowired
    StudentConfirmProducer studentConfirmProducer;

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
    public void directCustomSend() throws InterruptedException {
        studentConfirmProducer.directCustomSend("不存的交换机", "student", student);
    }



}