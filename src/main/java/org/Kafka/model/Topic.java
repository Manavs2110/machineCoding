package org.Kafka.model;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    String topicId;
    List<Message> messages;
    int nextOffset=0;

    public Topic(String topicId) {
        this.topicId = topicId;
        this.messages = new ArrayList<>();
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public String getTopicId() {
        return topicId;
    }

    public synchronized void publish(String payload){
        messages.add(new Message(payload,nextOffset++));
        notifyAll();
    }

    public synchronized Message getMessage(int offset) {
        while(offset>= messages.size()) {
            try {
                wait(100);
                if(offset>= messages.size()) return null;
            } catch (InterruptedException ignored) {}
        }

        return messages.get(offset);
    }

}
