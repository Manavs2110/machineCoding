package org.Kafka.service;

import org.Kafka.model.Topic;
import org.Kafka.repository.TopicRepository;

public class ProducerService {
    private TopicRepository topicRepository;

    public ProducerService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void publish(String topicId, String payload){
        Topic topic = topicRepository.getTopic(topicId);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found");
        }
        topic.publish(payload);
    }
}
