package mq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class App {

    //=========================================================

    /**
     * 简单的调用过程处理消息
     */

    @Test
    public void send() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("hello", false, false, false, null);

        channel.basicPublish("", "hello", null, "Hello world2".getBytes());

        channel.close();
        connection.close();
    }

    @Test
    public void receive() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("hello", false, false, false, null);

        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
            }

        });

        synchronized (this) {
            wait();
        }
    }

    //===============================================================================================

    /**
     *
     * 一条消息只会被一个消费者接收；
     消息是平均分配给消费者的；
     消费者只有在处理完某条消息后，才会收到下一条消息。
     事实上，RabbitMQ会循环地（一个接一个地）发送消息给消费者，这种分配消息的方式被称为round-robin（轮询）。
     */

    @Test
    public void send2() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 将第二个参数设为true，表示声明一个需要持久化的队列。
        // 需要注意的是，若你已经定义了一个非持久的，同名字的队列，要么将其先删除（不然会报错），要么换一个名字。
        //channel.queueDeclare("hello", true, false, false, null);


        channel.queueDeclare("hello", false, false, false, null);

        for (int i = 0; i < 9; i++) {

            // 修改了第三个参数，这是表明消息需要持久化
            // MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", "hello", null, String.valueOf(i).getBytes());
        }

        channel.close();
        connection.close();
    }

    @Test
    public void receive2() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", false, false, false, null);

        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
                try {
                      Thread.sleep(10000);
                    //Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        synchronized (this) {
            wait();
        }
    }

    //=============================消息确认=====================================

    @Test
    public void receive2Ack() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //channel.basicQos(1);  // 影响消费者是否公平的处理消息

        channel.queueDeclare("hello", false, false, false, null);

        channel.basicConsume("hello", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
                try {
                    Thread.sleep(10000);
                    //Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
        synchronized (this) {
            wait();
        }
    }




}
