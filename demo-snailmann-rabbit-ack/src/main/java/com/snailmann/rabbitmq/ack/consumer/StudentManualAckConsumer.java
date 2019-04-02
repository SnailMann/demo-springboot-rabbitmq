package com.snailmann.rabbitmq.ack.consumer;

import com.rabbitmq.client.Channel;
import com.snailmann.rabbitmq.ack.entity.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "student", containerFactory = "rabbitListenerManualAckContainerFactory")
public class StudentManualAckConsumer {


    @RabbitHandler
    public void receive(Student student, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println("manualAck : " + tag + "body:" + student);
        try {
            //手动ack
            channel.basicAck(tag, false);
            //消费失败，手动nack
            //require为true，则消息会无限重试，如果为false，消息会进入死信或丢弃
            channel.basicNack(tag, false, false);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

}
