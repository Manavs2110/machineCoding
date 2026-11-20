package mainLLDproblems.elevatorSystem;


import java.util.*;



/*
Requirements:
1. Make a Elevator System with multiple Elevators
2. Able to serve Request from inside and Outside of elevator
3. Strategies for ElevatorSelection for Outside Requests

Non-functional Requirements:
1. Extensible
2. Thread-safe
3. Maintainability
 */

class Elevator implements Runnable{
    int id;
    volatile int currentFloor=0;
    Direction direction;
    TreeSet<Integer> upSet = new TreeSet<>();
    TreeSet<Integer> downSet = new TreeSet<>();

    volatile boolean isRunning = false;

    Elevator(int id){
        this.id = id;
    }

    synchronized void addRequest(Request request){
        if(request.direction == Direction.UP){
            upSet.add(request.floor);
        }else{
            downSet.add(request.floor);
        }
        notifyAll();
    }
    synchronized void addInternalRequest(int floorNumber){
        if(floorNumber>currentFloor){
            upSet.add(floorNumber);
        }else{
            downSet.add(floorNumber);
        }
        notifyAll();
    }
    synchronized void stopElevator(){
        isRunning = false;
        notifyAll();
    }
    synchronized void startElevator(){
        isRunning = true;
        notifyAll();
    }

    void step() throws InterruptedException {
        Thread.sleep(100);
        if(direction == Direction.UP){
            if(upSet.higher(currentFloor)!=null || downSet.higher(currentFloor) != null) currentFloor++;
            else direction = Direction.DOWN;
        }else if(direction == Direction.DOWN){
            if(downSet.lower(currentFloor)!= null || upSet.lower(currentFloor) != null) currentFloor--;
            else direction = Direction.UP;
        }else{
            boolean workAbove = upSet.higher(currentFloor) != null || downSet.higher(currentFloor) != null;
            direction = workAbove ? Direction.UP : Direction.DOWN;
        }
        System.out.println("Elevator is on : "+currentFloor+ " direction: "+ direction);
    }
    @Override
    public void run() {
        while(isRunning){
            synchronized (this){
                while(isRunning && upSet.isEmpty() && downSet.isEmpty()){
                    try {
                        direction = Direction.IDLE;
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(!isRunning) break;
                try {
                    step();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(direction == Direction.UP && upSet.remove(currentFloor)){
                    System.out.println("We have served the UP request for: "+ currentFloor);
                }
                if(direction == Direction.DOWN && downSet.remove(currentFloor)){
                    System.out.println("We have served the DOWN request for: "+ currentFloor);
                }
            }
        }
    }
}
class Request{
    Direction direction;
    int floor;

    Request(int floor, Direction direction){
        this.floor = floor;
        this.direction = direction;
    }
}
class ElevatorSystem{
    List<Elevator> elevators = new ArrayList<>();
    ElevatorStrategy strategy;

    ElevatorSystem(int count, ElevatorStrategy strategy){
        this.strategy = strategy;
        for(int i = 0; i < count; i++){
            elevators.add(new Elevator(i));
        }
    }

    void start(){
        for(Elevator elevator : elevators){
            elevator.startElevator();
            new Thread(elevator).start();
        }
    }

    void stop(){
        for(Elevator elevator : elevators){
            elevator.stopElevator();
        }
    }

    void requestElevator(int floor, Direction direction){
        Elevator elevator = strategy.chooseElevator(floor, elevators);
        System.out.println("Floor " + floor + " " + direction + " -> elevator " + elevator.id);
        elevator.addRequest(new Request(floor, direction));
    }

    void selectFloor(int elevatorId, int floor){
        elevators.get(elevatorId).addInternalRequest(floor);
    }
}
interface ElevatorStrategy{
    Elevator chooseElevator(Integer floor, List<Elevator> elevators);
}
class NearestFirst implements ElevatorStrategy{
    @Override
    public Elevator chooseElevator(Integer floor, List<Elevator> elevators){
        Elevator best = elevators.get(0);
        int minDistance = Integer.MAX_VALUE;
        for(Elevator elevator : elevators){
            int distance = Math.abs(elevator.currentFloor - floor);
            if(distance < minDistance){
                minDistance = distance;
                best = elevator;
            }
        }
        return best;
    }
}

enum Direction{
    UP, DOWN, IDLE
}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ElevatorSystem system = new ElevatorSystem(2, new NearestFirst());
        system.start();

        system.requestElevator(5, Direction.UP);
        system.selectFloor(0, 8);
        Thread.sleep(300);
        system.requestElevator(2, Direction.DOWN);

        Thread.sleep(2500);
        system.stop();
    }
}
