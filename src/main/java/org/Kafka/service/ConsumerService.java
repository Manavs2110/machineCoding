package org.Kafka.service;

import org.Kafka.model.Consumer;
import org.Kafka.model.ConsumerGroup;
import org.Kafka.model.Topic;
import org.Kafka.repository.TopicRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerService {
    private TopicRepository topicRepository;
    private Map<String, ConsumerGroup> groups = new ConcurrentHashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();

    public ConsumerService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    // Starts a background worker that keeps reading the topic for this consumer.
    public void subscribe(String topicId, Consumer consumer){
        Topic topic = topicRepository.getTopic(topicId);
        ConsumerGroup group = groups.computeIfAbsent(consumer.getGroupId(), ConsumerGroup::new);
        executor.submit(new ConsumerWorker(topic, group, consumer));
    }

    public void resetOffset(String groupId, String topicId, int offset){
        ConsumerGroup group = groups.get(groupId);
        if(group == null){
            throw new IllegalArgumentException("Group " + groupId + " does not exist");
        }
        topicRepository.getTopic(topicId).resetOffset(group, offset);
    }

    // Interrupts every worker so they stop waiting and exit.
    public void shutdown(){
        executor.shutdownNow();
    }
}
