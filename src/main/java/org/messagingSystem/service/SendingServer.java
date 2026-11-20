package org.messagingSystem.service;

import org.messagingSystem.model.Message;
import org.messagingSystem.model.Receiver;

import java.util.ArrayList;
import java.util.List;

public class SendingServer {
    List<Receiver> receivers = new ArrayList<>();

    public void registerReceiver(Receiver receiver) {
        receivers.add(receiver);
    }

    public void removeReceiver(Receiver receiver) {
        receivers.remove(receiver);
    }

    // Push: the sender delivers to every receiver, each in its own format and over its own channel
    public void send(Message message) {
        System.out.println("Sending message " + message.getMessageId() + " to " + receivers.size() + " receivers");
        for (Receiver receiver : receivers) {
            try {
                deliver(receiver, format(message, receiver));
            } catch (RuntimeException e) {
                // one receiver failing must not stop delivery to the others
                System.out.println("Failed to deliver to " + receiver.getReceiverId() + ": " + e.getMessage());
            }
        }
    }

    private String format(Message message, Receiver receiver) {
        switch (receiver.getFormat()) {
            case JSON:
                return "{\"id\":" + message.getMessageId() + ",\"sender\":\"" + message.getSender()
                        + "\",\"content\":\"" + message.getContent() + "\"}";
            case XML:
                return "<message><id>" + message.getMessageId() + "</id><sender>" + message.getSender()
                        + "</sender><content>" + message.getContent() + "</content></message>";
            default:
                return message.getMessageId() + " | " + message.getSender() + " | " + message.getContent();
        }
    }

    // Simulated: the real versions would publish to Kafka, POST to a URL, or call a gRPC stub
    private void deliver(Receiver receiver, String formattedMessage) {
        switch (receiver.getDeliveryType()) {
            case QUEUE:
                System.out.println("[QUEUE] pushed to queue of " + receiver.getReceiverId());
                break;
            case HTTP:
                System.out.println("[HTTP] POST to " + receiver.getReceiverId());
                break;
            case RPC:
                System.out.println("[RPC] call " + receiver.getReceiverId() + ".onMessage()");
                break;
        }
        receiver.receive(formattedMessage);
    }
}
