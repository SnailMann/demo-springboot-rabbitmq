package com.snailmann.rabbitmq.returnconfirm.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.returnconfirm.entity.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 普通消费者
 */

@Component
@RabbitListener(queues = "student")
public class StudentConsumer {


    @RabbitHandler
    public void receive(Student student) {
        System.out.println(student);
    }


}
