package org.messagingSystem;

import org.messagingSystem.model.DeliveryType;
import org.messagingSystem.model.Message;
import org.messagingSystem.model.MessageFormat;
import org.messagingSystem.model.Receiver;
import org.messagingSystem.service.SendingServer;

public class Main {
    public static void main(String args[]){
        SendingServer sendingServer = new SendingServer();

        Receiver billing = new Receiver("billing", MessageFormat.JSON, DeliveryType.QUEUE);
        Receiver analytics = new Receiver("analytics", MessageFormat.XML, DeliveryType.HTTP);
        Receiver notifications = new Receiver("notifications", MessageFormat.TEXT, DeliveryType.RPC);

        sendingServer.registerReceiver(billing);
        sendingServer.registerReceiver(analytics);
        sendingServer.registerReceiver(notifications);

        sendingServer.send(new Message("order-service", "Order 101 placed"));

        sendingServer.removeReceiver(analytics);
        sendingServer.send(new Message("order-service", "Order 101 shipped"));

        System.out.println("billing got " + billing.getReceivedMessages().size() + " messages");   // 2
    }
}
