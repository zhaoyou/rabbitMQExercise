package mq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishSubscribe {

    @Test
    public void send() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("notice", "fanout");
        channel.basicPublish( "notice", "", null, "Hello China".getBytes());
        channel.close();
        connection.close();
    }


    @Test
    public void receive() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("notice", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "notice", "");

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
            }

        });
        synchronized (this) {
            wait();
        }
    }


}
