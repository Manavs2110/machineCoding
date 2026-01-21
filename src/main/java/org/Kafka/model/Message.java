package org.Kafka.model;

public class Message {
    final String message;
    final long offSet;

    public Message(String message, long offSet) {
        this.message = message;
        this.offSet = offSet;
    }

    public String getMessage() {
        return message;
    }

    public long getOffSet() {
        return offSet;
    }
}
