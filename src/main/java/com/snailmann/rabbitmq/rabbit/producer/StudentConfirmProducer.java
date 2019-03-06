package com.snailmann.rabbitmq.rabbit.producer;


import com.snailmann.rabbitmq.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 测试生产者的Confirm模式
 */
@Slf4j
@Component
public class StudentConfirmProducer {

    @Autowired
    RabbitTemplate rabbitConfirmTemplate;

    /**
     * 点对点，直接模式 | 使用默认的交换机，和默认的routing key(既队列的名称)
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) {
        log.info("direct default exchange with default routing key send : {}", student);
        rabbitConfirmTemplate.convertAndSend(queue, student);
    }


}
