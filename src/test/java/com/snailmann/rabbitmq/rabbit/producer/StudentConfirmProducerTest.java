package com.snailmann.rabbitmq.rabbit.producer;

import com.snailmann.rabbitmq.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

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
     * ok
     */
    @Test
    public void directDefaultSend() {
        studentConfirmProducer.directDefaultSend("student", student);
    }

}