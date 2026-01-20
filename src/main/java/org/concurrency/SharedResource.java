package org.concurrency;

import java.util.LinkedList;
import java.util.Queue;

public class SharedResource {
    private Queue<Integer> sharedBuffer;
    private int bufferInteger;

    public SharedResource(int bufferInteger) {
        this.sharedBuffer = new LinkedList<>();
        this.bufferInteger = bufferInteger;
    }

    public synchronized void addResource(int item) throws InterruptedException {
        while(sharedBuffer.size() == bufferInteger){
            System.out.println("waiting for consumer to consume Item");
            wait();
        }
        sharedBuffer.add(item);
        System.out.println("item added : "+item);
        notifyAll();
    }
    public synchronized int consumeItem() throws InterruptedException {
        while(sharedBuffer.isEmpty()){
            System.out.println("waiting for addResource to call");
            wait();
        }
        int item= sharedBuffer.poll();
        System.out.println("consumed Item: "+item);
        notify();
        return item;
    }
}
