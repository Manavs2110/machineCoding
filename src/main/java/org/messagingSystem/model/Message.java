package org.messagingSystem.model;

public class Message {
    private static int nextId = 1;

    int messageId;
    String sender;
    String content;

    public Message(String sender, String content) {
        this.messageId = nextId++;
        this.sender = sender;
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
