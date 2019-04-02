package com.rabbitmq.rabbitmq.variousconsumer.consumer;

import com.rabbitmq.rabbitmq.variousconsumer.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j

public class StudentDefaultConsumer {


    @RabbitListener(queues = "student", containerFactory = "rabbitListenerContainerFactory")
    public void receive(Student student) {
        log.warn("defalut Object :{}", student);

    }


    public void receive(String string) {
        log.warn("default string :{}", string);

    }


    public void receive(byte[] bytes) {
        log.warn("default bytes :{}", bytes);

    }


}
