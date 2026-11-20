package org.Kafka.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Consumers in the same group share one offset per topic, so each message goes to only one of them.
// Different groups have separate offsets, so every group gets every message.
// Offsets are only read/changed inside Topic's synchronized methods, so updates to them are safe.
public class ConsumerGroup {
    String groupId;
    Map<String, Integer> offsets = new ConcurrentHashMap<>(); // topic -> next offset to read

    public ConsumerGroup(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getOffset(String topicId){
        return offsets.getOrDefault(topicId, 0);
    }

    public void setOffset(String topicId, int offset){
        offsets.put(topicId, offset);
    }
}
