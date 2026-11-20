package org.messagingSystem.model;

import java.util.ArrayList;
import java.util.List;

public class Receiver {
    String receiverId;
    MessageFormat format;          // which format this receiver wants
    DeliveryType deliveryType;     // how this receiver wants to get it
    List<String> receivedMessages = new ArrayList<>();

    public Receiver(String receiverId, MessageFormat format, DeliveryType deliveryType) {
        this.receiverId = receiverId;
        this.format = format;
        this.deliveryType = deliveryType;
    }

    public void receive(String formattedMessage) {
        receivedMessages.add(formattedMessage);
        System.out.println("  " + receiverId + " received: " + formattedMessage);
    }

    public String getReceiverId() {
        return receiverId;
    }

    public MessageFormat getFormat() {
        return format;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}
