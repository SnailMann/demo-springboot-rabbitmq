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
     * 如果要测试Confirm失败的情况，只需要让消息路由到一个不存在的exchange即可
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) throws InterruptedException {
        log.info("direct default exchange with default routing key send : {}", student);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(400);
            rabbitConfirmTemplate.convertAndSend(queue, student);
        }

    }


    /**
     * 点对点，直接模式 | 使用自定义的交换机和自定义的队列
     * 如果要测试Confirm失败的情况，只需要让消息路由到一个不存在的exchange即可
     *
     * @param student
     */
    public void directCustomSend(String exchange, String routingKey, Student student) throws InterruptedException {
        log.info("direct custom exchange with custom routing key send : {}", student);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(400);
            rabbitConfirmTemplate.convertAndSend(exchange, routingKey, student);
        }

    }


}
