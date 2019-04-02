package com.snailmann.rabbitmq.returnconfirm.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


/**
 * 测试Confirm机制的消费者
 */
@Slf4j
public class StringWithConfirmConsumer {


    public static void main(String[] args) throws IOException, TimeoutException {

        //1. 创建连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("127.0.0.1");
        //2. 创建连接
        Connection connection = connectionFactory.createConnection();
        //3. 创建信道
        Channel channel = connection.createChannel(false);
        //4. 消费者声明交换机和队列
        String exchangeName = "test_confirm_exchange";
        String routingkey = "confirm.save";
        String queueName = "test_confirm_queue";
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingkey);

        //5. 创建消费者
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        System.out.println("consumer:");
        channel.close();
        connection.close();
    }

}
