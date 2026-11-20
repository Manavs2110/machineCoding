package org.Kafka.model;

public class Message {
    final String message;
    final int offset;

    public Message(String message, int offset) {
        this.message = message;
        this.offset = offset;
    }

    public String getMessage() {
        return message;
    }

    public int getOffset() {
        return offset;
    }
}
