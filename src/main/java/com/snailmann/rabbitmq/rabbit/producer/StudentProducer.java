package com.snailmann.rabbitmq.rabbit.producer;

import com.snailmann.rabbitmq.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 点对点，直接模式 | 使用默认的交换机，和默认的routing key(既队列的名称)
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) {
        log.info("direct default exchange with default routing key send : {}", student);
        rabbitTemplate.convertAndSend(queue, student);
    }


    /**
     * 点对点，直接模式 | 使用默认的交换机，自定义的routing key | 这是错误的写法
     * 错误示范，默认的交换机会绑定所有的队列，但是只会使用默认的队列名称作为routing key;
     * 所以不能显示的把自定义的routing key绑定到默认交换机中
     *
     * @param routingKey
     * @param student
     */
    public void directDefaultWithCustomKeySend(String routingKey, Student student) {
        log.info("direct default exchange with custom routing key send : {}", student);
        rabbitTemplate.convertAndSend(routingKey, student);
    }

    /**
     * 点对点，直接模式 | 使用自定义的交换机和routing key
     *
     * @param exchange
     * @param routingKey
     * @param student
     */
    public void directCustomWithCustomKeySend(String exchange, String routingKey, Student student) {
        log.info("direct custom exchage with custom routing key send: {}", student);
        rabbitTemplate.convertAndSend(exchange, routingKey, student);
    }


    /**
     * 广播模式 | 使用自定义的交换机
     * (广播模式不需要routing key，只要绑定了广播模式的exchange的队列，肯定都会收到消息)
     *
     * @param exchange
     * @param student
     */
    public void fanoutSend(String exchange, Student student) {
        log.info("fault default exchage send: {}", student);
        rabbitTemplate.convertAndSend(exchange, null, student);
    }


    /**
     * 主题模式 | 使用自定义的交换机和routing key
     *
     * @param student
     */
    public void topicSend(String exchange, String routingKey, Student student) {
        log.info("topic custom exchage with custom routing key send: {}", student);
        rabbitTemplate.convertAndSend(exchange, routingKey, student);
    }

}
