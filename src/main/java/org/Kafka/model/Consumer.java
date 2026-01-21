package org.Kafka.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer {
    String consumerId;
    Map<String, Integer> offsets = new ConcurrentHashMap<>();
    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }
    public String getConsumerId() {
        return consumerId;
    }

    public void resetOffset(String topicName, Integer offset) {
        offsets.put(topicName, offset);
    }
    public int getOffset(String topicId){
        return offsets.getOrDefault(topicId,0);
    }
    public void advanceOffset(String topicId){
         offsets.put(topicId, offsets.getOrDefault(topicId,0)+1);
    }
}
