package org.Kafka.model;

public class Consumer {
    String consumerId;
    String groupId;

    public Consumer(String consumerId, String groupId) {
        this.consumerId = consumerId;
        this.groupId = groupId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getGroupId() {
        return groupId;
    }

    // What the consumer does with each message.
    public void consume(Message message){
        System.out.println(consumerId + " received: " + message.getMessage() + " (offset " + message.getOffset() + ")");
    }
}
