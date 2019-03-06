package com.snailmann.rabbitmq.rabbit.producer;


import com.snailmann.rabbitmq.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * 测试生产者的Return模式
 */
@Slf4j
@Component
public class StudentReturnProducer {

    @Autowired
    @Qualifier("rabbitReturnTemplate")
    RabbitTemplate rabbitReturnTemplate;

    /**
     * 点对点，直接模式 | 使用默认的交换机，和默认的routing key(既队列的名称)
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) {
        log.info("direct default exchange with default routing key send : {}", student);
        log.warn("{}", rabbitReturnTemplate.isReturnListener());

        rabbitReturnTemplate.convertAndSend(queue, student);

    }


}
