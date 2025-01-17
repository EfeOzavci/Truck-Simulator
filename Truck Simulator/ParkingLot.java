import java.util.ArrayList;

// Class representing a parking lot with trucks in waiting and ready sections
class ParkingLot {
    int capacity;         // Capacity of the parking lot
    int truckLimit;       // Maximum number of trucks the parking lot can hold
    int waitingSection;   // Current number of trucks in the waiting section
    int readySection;     // Current number of trucks in the ready section
    ArrayList<Truck> waitingTrucks; // List of trucks in the waiting section
    ArrayList<Truck> readyTrucks;   // List of trucks in the ready section
    int waitingIndex;     // Index to track which trucks have been moved to ready

    public ParkingLot(int capacity, int truckLimit) {
        this.capacity = capacity;
        this.truckLimit = truckLimit;
        this.waitingSection = 0;  // Initially, no trucks are waiting
        this.readySection = 0;    // Initially, no trucks are ready
        this.waitingTrucks = new ArrayList<>(truckLimit); // Initialize list with capacity
        this.readyTrucks = new ArrayList<>(truckLimit);   // Initialize list with capacity
        this.waitingIndex = 0;    // Start index at 0
    }
}