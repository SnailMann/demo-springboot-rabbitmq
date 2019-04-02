package com.rabbitmq.rabbitmq.variousconsumer.consumer;

import com.rabbitmq.rabbitmq.variousconsumer.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Slf4j
@RabbitListener(queues = "student", containerFactory = "rabbitListenerJacksonContainerFactory")
public class StudentJacksonConsumer {

    @RabbitHandler
    public void receive(Student student) {
        log.error("jack Object :{}", student);

    }

    @RabbitHandler
    public void receive(String string) {
        log.error("jack string :{}", string);

    }

    @RabbitHandler
    public void receive(byte[] bytes) {
        log.error("jack bytes :{}", bytes);

    }

}
