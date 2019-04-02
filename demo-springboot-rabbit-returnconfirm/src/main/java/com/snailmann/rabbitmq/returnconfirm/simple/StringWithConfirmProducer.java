package com.snailmann.rabbitmq.returnconfirm.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 生产者开启消息Confirm模式
 */
@Component
@Slf4j
public class StringWithConfirmProducer {


    public static void main(String[] args) throws IOException {
        String exchangeName = "test_confirm_exchange";
        String routingkey = "confirm.save";
        topicSender(routingkey, exchangeName, "jerry");
    }


    public static void topicSender(String routingKey, String exchange, String body) throws IOException {
        //1. 获得连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("127.0.0.1");
        //2. 创建一个连接
        Connection connection = connectionFactory.createConnection();
        //3. 通过连接创建一个信道,  fasle不开启事务，事务和comfirm机制是会冲突的
        Channel channel = connection.createChannel(false);
        //4. 指定我们的消息投递模式： 消息的确认模式
        channel.confirmSelect();
        //5. 声明交换机和队列
        String exchangeName = "test_confirm_exchange";
        String routingkey = "confirm.save";
        String queueName = "test_confirm_queue";
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingkey);
        //6. 发送一条消息
        channel.basicPublish(exchange, routingKey, null, body.getBytes());
        //7. 添加一个确认监听,监听Broker给我们回复的响应
        channel.addConfirmListener(new ConfirmListener() {

            /**
             * 返回的是失败的时候会进入这个方法
             * @param deliveryTag 我们发送的消息的唯一标签，即标识某条消息
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.warn("{} success ack!", deliveryTag);

            }

            /**
             * 返回的是成功的时候会进入这个方法
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.error("{} not ack!", deliveryTag);
            }
        });

        //8. 这里就不做channel的close，不然就监听不到了

    }


}



