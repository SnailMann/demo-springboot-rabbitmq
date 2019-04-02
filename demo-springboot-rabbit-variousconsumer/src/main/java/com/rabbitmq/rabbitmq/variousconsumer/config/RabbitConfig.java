package com.rabbitmq.rabbitmq.variousconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ的配置文件
 * 1. 使用默认的SimpleMessageConverter
 * 2. 使用Jackson2JsonMessageConverter
 */
@Slf4j
@Configuration
public class RabbitConfig {


    /**
     * Jaskson消息转换器
     */
    private static final MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * RabbitListenerContainerFactory默认连接工厂
     * 使用默认的SimpleMessageConverter消息转换器 | 不指定就是默认
     *
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }


    /**
     * RabbitTempalte的默认版本
     * 使用默认的SimpleMessageConverter消息转换器 | 不指定就是默认
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        return rabbitTemplate;
    }


    /**
     * RabbitListenerContainerFactory的Jackson工厂
     * 使用Jackson2JsonMessageConverter
     *
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerJacksonContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    /**
     * RabbitTempalte的Jackson版本
     * 消息转换器使用Jackson2JsonMessageConverter
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitJacksonTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}
