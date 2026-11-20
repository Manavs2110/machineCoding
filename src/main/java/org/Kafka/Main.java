package org.Kafka;

import org.Kafka.model.Consumer;
import org.Kafka.repository.TopicRepository;
import org.Kafka.service.ConsumerService;
import org.Kafka.service.ProducerService;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TopicRepository repository = new TopicRepository();
        repository.createTopic("orders");
        ProducerService producerService = new ProducerService(repository);
        ConsumerService consumerService = new ConsumerService(repository);

        // "billing" has 2 consumers: they SHARE the orders (each order goes to one of them)
        consumerService.subscribe("orders", new Consumer("B1", "billing"));
        consumerService.subscribe("orders", new Consumer("B2", "billing"));
        // "shipping" has 1 consumer: it gets EVERY order
        consumerService.subscribe("orders", new Consumer("S1", "shipping"));

        for (int i = 1; i <= 4; i++) {
            producerService.publish("orders", "Order-" + i);
        }
        Thread.sleep(500);

        System.out.println("\n--- Replay: reset shipping to offset 0 ---\n");
        consumerService.resetOffset("shipping", "orders", 0);
        Thread.sleep(500);

        consumerService.shutdown(); // stop all workers so the program exits
    }
}
