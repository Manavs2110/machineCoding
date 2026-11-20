package org.Kafka.service;

import org.Kafka.repository.TopicRepository;

public class ProducerService {
    private TopicRepository topicRepository;

    public ProducerService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void publish(String topicId, String payload){
        topicRepository.getTopic(topicId).publish(payload);
    }
}
