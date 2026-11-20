package org.Kafka.model;

import java.util.ArrayList;
import java.util.List;

// An append-only log. Messages are never removed when read; each consumer group tracks its own offset.
public class Topic {
    String topicId;
    List<Message> messages;

    public Topic(String topicId) {
        this.topicId = topicId;
        this.messages = new ArrayList<>();
    }

    public String getTopicId() {
        return topicId;
    }

    public synchronized void publish(String payload){
        messages.add(new Message(payload, messages.size()));
        notifyAll(); // wake up consumers waiting for a new message
    }

    // Waits until the group has an unread message, then gives it to the caller and moves the group's offset.
    // Done under the topic's lock, so two consumers of the same group never get the same message.
    // Returns null if the thread is interrupted (shutdown).
    public synchronized Message poll(ConsumerGroup group) {
        while(group.getOffset(topicId) >= messages.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // keep the interrupt so the worker loop can stop
                return null;
            }
        }
        int offset = group.getOffset(topicId);
        group.setOffset(topicId, offset + 1);
        return messages.get(offset);
    }

    // Replay: the group reads this topic again from the given offset.
    public synchronized void resetOffset(ConsumerGroup group, int offset){
        if(offset < 0) throw new IllegalArgumentException("Offset cannot be negative");
        group.setOffset(topicId, offset);
        notifyAll(); // waiting consumers re-check their position
    }
}
