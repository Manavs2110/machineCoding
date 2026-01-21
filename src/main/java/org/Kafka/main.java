package org.Kafka;

import org.Kafka.model.Consumer;
import org.Kafka.repository.TopicRepository;
import org.Kafka.service.ConsumerService;
import org.Kafka.service.ProducerService;

public class main {
    static void main(String args[]) throws InterruptedException {
        TopicRepository repository = new TopicRepository();
        repository.createTopic("orders");
        ProducerService producerService = new ProducerService(repository);
        ConsumerService consumerService = new ConsumerService(repository);
        Consumer consumer1 = new Consumer("C1");
        Consumer consumer2 = new Consumer("C2");
        consumerService.consume("orders", consumer1);
        consumerService.consume("orders", consumer2);
        producerService.publish("orders", "Order-1");
        producerService.publish("orders", "Order-2");
        Thread.sleep(1000);
        System.out.println("\n--- Resetting offset for C1 ---\n");
        consumer1.pause();
        consumer1.resetOffset("orders", 0);
        consumer1.resume();
        producerService.publish("orders", "Order-3");
    }
}
