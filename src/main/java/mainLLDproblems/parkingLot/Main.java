package mainLLDproblems.parkingLot;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/*
Requirements:
1. Design a Parking lot system with
2. Able to add floors
3. able to park 2-wheeler, 4-wheeler
4. Add a parking lot slot to any of the floors.
5. find best slot
6. park and unpark vehicle
7. get to see all available spots
8. calculate price and generate bill
9. If not slot reject parking

Non functional:
thread safe
Extensible
*/
enum VehicleType{
    TWO_WHEELER, FOUR_WHEELER, TRUCK
}
class ParkingSlot{
    int id;
    VehicleType vehicleType;
    boolean isEmpty = true;

    ParkingSlot(int id, VehicleType vehicleType){
        this.id = id;
        this.vehicleType = vehicleType;
    }
}

class Floor{
    int floorNumber;
    List<ParkingSlot> slots = new ArrayList<>();

    Floor(int floorNumber){
        this.floorNumber = floorNumber;
    }
}
class Vehicle{
    String number;
    VehicleType vehicleType;

    Vehicle(String number, VehicleType vehicleType){
        this.number = number;
        this.vehicleType = vehicleType;
    }
}
class Ticket{
    String ticketNumber;
    ParkingSlot parkingSlot;
    Vehicle vehicle;
    Instant entryTime;

    Ticket(ParkingSlot parkingSlot, Vehicle vehicle){
        this.ticketNumber = UUID.randomUUID().toString();
        this.parkingSlot = parkingSlot;
        this.vehicle = vehicle;
        this.entryTime = Instant.now();
    }
}
class Bill{
    Ticket ticket;
    Instant exitTime;
    double amount;

    Bill(Ticket ticket, Instant exitTime, double amount){
        this.ticket = ticket;
        this.exitTime = exitTime;
        this.amount = amount;
    }
}
class ParkingLot{
    List<Floor> floors = new ArrayList<>();
    SlotStrategy slotStrategy;
    PricingStrategy pricingStrategy;

    ParkingLot(SlotStrategy slotStrategy, PricingStrategy pricingStrategy){
        this.slotStrategy = slotStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    synchronized void addFloor(int floorNumber){
        floors.add(new Floor(floorNumber));
        floors.sort(Comparator.comparingInt(f -> f.floorNumber));
    }

    synchronized void addSlot(int floorNumber, ParkingSlot slot){
        for(Floor floor : floors){
            if(floor.floorNumber == floorNumber){
                floor.slots.add(slot);
                return;
            }
        }
        throw new IllegalArgumentException("Floor not found: " + floorNumber);
    }

    synchronized List<ParkingSlot> getAvailableSlots(){
        List<ParkingSlot> available = new ArrayList<>();
        for(Floor floor : floors){
            for(ParkingSlot slot : floor.slots){
                if(slot.isEmpty) available.add(slot);
            }
        }
        return available;
    }

    synchronized Ticket park(Vehicle vehicle){
        ParkingSlot slot = slotStrategy.findParkingSlot(floors, vehicle.vehicleType);
        if(slot == null) throw new IllegalStateException("No slot available for " + vehicle.vehicleType);
        slot.isEmpty = false;
        return new Ticket(slot, vehicle);
    }

    synchronized Bill unpark(Ticket ticket){
        Instant exitTime = Instant.now();
        ticket.parkingSlot.isEmpty = true;
        return new Bill(ticket, exitTime, pricingStrategy.calculate(ticket, exitTime));
    }
}

interface SlotStrategy{
    ParkingSlot findParkingSlot(List<Floor> floors, VehicleType vehicleType);
}

class FirstAvailableSlot implements SlotStrategy{
    public ParkingSlot findParkingSlot(List<Floor> floors, VehicleType vehicleType){
        for(Floor floor : floors){
            for(ParkingSlot slot : floor.slots){
                if(slot.vehicleType == vehicleType && slot.isEmpty) return slot;
            }
        }
        return null;
    }
}

interface PricingStrategy{
    double calculate(Ticket ticket, Instant exitTime);
}

class HourlyPricing implements PricingStrategy{
    Map<VehicleType, Double> ratePerHour = Map.of(
            VehicleType.TWO_WHEELER, 20.0,
            VehicleType.FOUR_WHEELER, 50.0,
            VehicleType.TRUCK, 100.0);

    public double calculate(Ticket ticket, Instant exitTime){
        long minutes = Duration.between(ticket.entryTime, exitTime).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60);
        return hours * ratePerHour.get(ticket.vehicle.vehicleType);
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(new FirstAvailableSlot(), new HourlyPricing());
        lot.addFloor(1);
        lot.addFloor(0);
        lot.addSlot(0, new ParkingSlot(1, VehicleType.FOUR_WHEELER));
        lot.addSlot(0, new ParkingSlot(2, VehicleType.TWO_WHEELER));
        lot.addSlot(1, new ParkingSlot(3, VehicleType.FOUR_WHEELER));

        Ticket t1 = lot.park(new Vehicle("KA01", VehicleType.FOUR_WHEELER));
        Ticket t2 = lot.park(new Vehicle("KA02", VehicleType.FOUR_WHEELER));
        System.out.println("Parked at slots " + t1.parkingSlot.id + ", " + t2.parkingSlot.id);
        System.out.println("Available: " + lot.getAvailableSlots().size());

        try {
            lot.park(new Vehicle("KA03", VehicleType.FOUR_WHEELER));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        Bill bill = lot.unpark(t1);
        System.out.println("Bill for " + bill.ticket.vehicle.number + ": " + bill.amount);
        System.out.println("Available: " + lot.getAvailableSlots().size());
    }
}
