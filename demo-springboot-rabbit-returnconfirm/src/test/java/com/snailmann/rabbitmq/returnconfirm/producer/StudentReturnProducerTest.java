package com.snailmann.rabbitmq.returnconfirm.producer;

import com.snailmann.rabbitmq.returnconfirm.entity.Student;
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
     * 测试成功
     */
    @Test
    public void directDefaultSend() {
        studentReturnProducer.directDefaultSend("student", student);
    }

    /**
     * 测试失败
     */
    @Test
    public void directCustomSend() throws InterruptedException {
        studentReturnProducer.directCustomSend(null, "不存在的路由键", student);
    }

    @Test
    public void fanoutSend() throws InterruptedException {
        studentReturnProducer.fanoutSend("amq.fanout", student);
    }
}