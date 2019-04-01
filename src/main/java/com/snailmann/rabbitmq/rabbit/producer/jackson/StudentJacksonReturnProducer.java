package com.snailmann.rabbitmq.rabbit.producer.jackson;


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
public class StudentJacksonReturnProducer {

    @Autowired
    @Qualifier("rabbitJacksonReturnTemplate")
    RabbitTemplate rabbitJacksonReturnTemplate;

    /**
     * 点对点，直接模式 | 使用默认的交换机，和默认的routing key(既队列的名称)
     *
     * @param student
     */
    public void directDefaultSend(String queue, Student student) {
        log.info("direct default exchange with default routing key send : {}", student);
        log.warn("{}", rabbitJacksonReturnTemplate.isReturnListener());

        rabbitJacksonReturnTemplate.convertAndSend(queue, student);

    }


    /**
     * 点对点，直接模式 | 使用自定义的交换机和自定义的队列
     * 测试失败
     *
     * @param student
     */
    public void directCustomSend(String exchange, String routingKey, Student student) throws InterruptedException {
        log.info("direct custom exchange with custom routing key send : {}", student);

        rabbitJacksonReturnTemplate.convertAndSend("不存在的队列", routingKey, student);


    }


    /**
     * 广播模式
     *
     *
     * @param student
     */
    public void fanoutSend(String exchange, Student student) throws InterruptedException {
        log.info("fanout send : {}", student);
        rabbitJacksonReturnTemplate.convertAndSend(exchange, null, student);


    }


}
