package org.Kafka.repository;

import org.Kafka.model.Topic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicRepository {
    Map<String, Topic> broker = new ConcurrentHashMap<>();
    public void createTopic(String topicId){
        broker.putIfAbsent(topicId, new Topic(topicId));
    }
    public void publish(String topicId, String message){
        broker.get(topicId).publish(message);
    }
    public Topic getTopic(String topicId){
        return broker.get(topicId);
    }
}
