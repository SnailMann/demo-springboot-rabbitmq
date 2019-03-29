package com.snailmann.rabbitmq.rabbit.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.rabbitmq.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 测试生产者的Confirm模式
 */
@Slf4j
@Component
public class StudentConfirmSimpleProducer {

    @Autowired
    RabbitTemplate rabbitConfirmTemplate;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 点对点，直接模式 | 使用默认的交换机，和默认的routing key(既队列的名称)
     * 1. 如果要测试Confirm失败的情况，只需要让消息路由到一个不存在的exchange即可
     * 2. CorrelationData对象只能set一个String格式的ID, 为了做到可靠投递，我们可以设置其为消息的唯一标识，比如主键ID
     * 3. 如果Comfirm失败，会返回生产者CorrelationData对象
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) throws InterruptedException {
        log.info("direct default exchange with default routing key send : {}", student);
        for (int i = 0; i < 1; i++) {
            Thread.sleep(400);
            rabbitConfirmTemplate.convertAndSend(queue, student, new CorrelationData(student.getName()));
        }

    }


    /**
     * 点对点，直接模式 | 使用自定义的交换机和自定义的队列
     * 1. 如果要测试Confirm失败的情况，只需要让消息路由到一个不存在的exchange即可
     * 2. CorrelationData对象只能set一个String格式的ID, 为了做到可靠投递，我们可以设置其为消息的唯一标识，比如主键ID
     * 3. 如果Comfirm失败，会返回生产者CorrelationData对象
     *
     * @param student
     */
    public void directCustomSend(String exchange, String routingKey, Student student) throws InterruptedException {
        log.info("direct custom exchange with custom routing key send : {}", student);
        for (int i = 0; i < 1; i++) {
            Thread.sleep(400);
            rabbitConfirmTemplate.convertAndSend(exchange, routingKey, student, new CorrelationData(student.getName()));
        }
    }



}
