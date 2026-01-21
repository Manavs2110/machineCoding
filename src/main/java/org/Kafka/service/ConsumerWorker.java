package org.Kafka.service;

import org.Kafka.model.Consumer;
import org.Kafka.model.Message;
import org.Kafka.model.Topic;

public class ConsumerWorker implements Runnable{
    Topic topic;
    Consumer consumer;

    public ConsumerWorker(Topic topic, Consumer consumer) {
        this.topic = topic;
        this.consumer = consumer;
    }

    @Override
    public void run(){
        while(true){
                int consumerOffset = consumer.getOffset(topic.getTopicId());
                Message msg = topic.getMessage(consumerOffset);
            if (msg == null) {
                continue;
            }
                System.out.println(
                        "Consumer " + consumer.getConsumerId() +
                                " received: " + msg.getMessage() +
                                " (offset " + msg.getOffSet() + ")"
                );
                synchronized (consumer){
                    if(consumer.getOffset(topic.getTopicId()) == consumerOffset){
                        consumer.advanceOffset(topic.getTopicId());
                    }
                }
        }
    }
}
