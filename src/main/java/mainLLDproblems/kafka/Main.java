package mainLLDproblems.kafka;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

//problem: https://workat.tech/machine-coding/practice/design-distributed-queue-cuudq0sk0v14/index.html


class Message{
    String text;
    Message(String text) { this.text = text; }
    String getText() { return text; }
}

class Topic{
    String name;
    List<Message> messages = new ArrayList<>();
    Topic(String name){
        this.name = name;
    }
    synchronized void addMessage(Message text){
        messages.add(text);
    }
    synchronized String getMessage(int i){
        return messages.get(i).getText();
    }
    synchronized int size(){
        return messages.size();
    }
}

class ConsumerWorker implements Runnable{
    Consumer consumer;
    Topic topic;
    AtomicInteger offset = new AtomicInteger(0);

    ConsumerWorker(Topic topic, Consumer consumer){
        this.topic = topic;
        this.consumer = consumer;
    }

    @Override
    public void run(){
        while(true){
            synchronized (this){
                while(topic.size() <= offset.get()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            consumer.consume(topic.getMessage(offset.getAndIncrement()));
        }
    }
    public synchronized void wakeup(){
        notifyAll();
    }
    public synchronized void resetOffset(int offset){
        this.offset.set(offset);
        wakeup();
    }
}

class Broker{
    Map<String, Topic> topicMap = new ConcurrentHashMap<>();
    Map<String, Map<String, ConsumerWorker>> workers = new ConcurrentHashMap<>();
    List<Thread> threads = new ArrayList<>();

    public Topic createTopic(String name){
        return topicMap.computeIfAbsent(name, Topic::new);
    }

    public void subscribe(Consumer consumer, Topic topic){
        workers.computeIfAbsent(topic.name, k->new ConcurrentHashMap<>()).computeIfAbsent(consumer.id, id->{
            ConsumerWorker consumerWorker = new ConsumerWorker(topic, consumer);
            Thread thread = new Thread(consumerWorker);
            threads.add(thread);
            thread.start();
            return consumerWorker;
        });
    }
    public void publish(Message message, Topic topic){
        topic.addMessage(message);
        workers.getOrDefault(topic.name, Map.of()).values().forEach(ConsumerWorker::wakeup);
    }

    public void shutdown(){
        threads.forEach(Thread::interrupt);
    }


}

class Consumer{
    String id;
    Consumer(String id){
        this.id=id;
    }
    void consume(String text){
        System.out.println(id+" received "+text);
    }
}


class Producer{
    String id;
    Broker broker;

    Producer(String id,Broker broker){
        this.id = id;
        this.broker = broker;
    }
    void publish(String text, Topic topic){
        broker.publish(new Message(text), topic);
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Broker broker = new Broker();
        Topic topic1 = broker.createTopic("topic1");
        Topic topic2 = broker.createTopic("topic2");

        Consumer consumer1 = new Consumer("consumer1");
        Consumer consumer2 = new Consumer("consumer2");

        broker.subscribe(consumer1,topic1);
        broker.subscribe(consumer1,topic2);
        broker.subscribe(consumer2, topic2);

        Producer producer = new Producer("producer1", broker);

        producer.publish("hello", topic1);
        producer.publish("no", topic2);

        Thread.sleep(500);
        broker.shutdown();
    }

}
