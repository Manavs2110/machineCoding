package org.Kafka.repository;

import org.Kafka.model.Topic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicRepository {
    Map<String, Topic> broker = new ConcurrentHashMap<>();

    public void createTopic(String topicId){
        if(broker.putIfAbsent(topicId, new Topic(topicId)) != null){
            throw new IllegalArgumentException("Topic " + topicId + " already exists");
        }
    }

    public Topic getTopic(String topicId){
        Topic topic = broker.get(topicId);
        if(topic == null){
            throw new IllegalArgumentException("Topic " + topicId + " does not exist");
        }
        return topic;
    }
}
