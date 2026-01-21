package org.Kafka.service;

import org.Kafka.model.Consumer;
import org.Kafka.model.Message;
import org.Kafka.model.Topic;
import org.Kafka.repository.TopicRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerService {
    private TopicRepository topicRepository;
    ExecutorService executor = Executors.newCachedThreadPool();
    public ConsumerService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void consume(String topicId, Consumer consumer){
        Topic topic = topicRepository.getTopic(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("Topic does not exist");
        }
        executor.submit(new ConsumerWorker(topic, consumer));
    }
}
