package org.Kafka.service;

import org.Kafka.model.Consumer;
import org.Kafka.model.ConsumerGroup;
import org.Kafka.model.Message;
import org.Kafka.model.Topic;

public class ConsumerWorker implements Runnable{
    Topic topic;
    ConsumerGroup group;
    Consumer consumer;

    public ConsumerWorker(Topic topic, ConsumerGroup group, Consumer consumer) {
        this.topic = topic;
        this.group = group;
        this.consumer = consumer;
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            Message msg = topic.poll(group); // waits for the group's next message
            if(msg == null) return;          // interrupted -> shutdown
            try {
                consumer.consume(msg);
            } catch (RuntimeException e) {
                // one bad message must not kill the worker
                System.out.println("Consumer " + consumer.getConsumerId() + " failed on offset " + msg.getOffset() + ": " + e.getMessage());
            }
        }
    }
}
