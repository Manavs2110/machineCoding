package org.concurrency;

public class main {
    public static void main(String args[]){
        SharedResource sharedResource = new SharedResource(3);
        Thread producerThread = new Thread(()->{
            try{
                for(int i=1;i<=6;i++){
                    sharedResource.addResource(i);
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        });
        Thread consumerThread = new Thread(()->{
            try{
                for(int i=1;i<=6;i++){
                    sharedResource.consumeItem();
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        });
        producerThread.start();
        consumerThread.start();
    }
}
