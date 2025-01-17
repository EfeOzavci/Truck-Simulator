import java.io.*;
// Manager class to handle operations of parking lots and trucks
class ParkingLotManager {
    private AVLTree parkingLots;   // AVL tree to store parking lots by capacity
    private BufferedWriter writer; // Writer to write the output to output.txt file

    public ParkingLotManager() {
        this.parkingLots = new AVLTree();
    }

    // Method to create a new parking lot and add it to the AVL tree
    public void createParkingLot(int capacity, int truckLimit) {
        if (parkingLots.getNode(capacity) == null) { // If any parking lot with this capacity does not exist create a new one
            ParkingLot newLot = new ParkingLot(capacity, truckLimit);
            parkingLots.insert(capacity, newLot);
        }
        // If a parking lot with this capacity already exists do nothing
    }

    // Method to add a truck to a suitable parking lot
    public void addTruck(Truck truck) {
        AVLNode node = parkingLots.getNode(truck.capacity); // Find parking lot with wanted capacity
        ParkingLot targetLot = null; // The parking lot where the truck will be added

        if (node != null) { // If there exist a parking lot with wanted capacity
            ParkingLot exactLot = node.value;
            if (exactLot.waitingSection + exactLot.readySection < exactLot.truckLimit) { // If there is space in this parking lot
                targetLot = exactLot;
            } else { // If parking lot is full look for a smaller capacity parking lot
                node = parkingLots.getInOrderPredecessor(node);
            }
        } else {// If a parking lot with wanted capacity doesn't exist find where it would be inserted
            node = parkingLots.findInsertPosition(truck.capacity);
            if (node != null && node.key < truck.capacity) {
                // If node's key is less than truck's capacity then node is predecessor
                // No action is done
            } else if (node != null && node.key > truck.capacity) {
                // Node's key is greater so we need to find predecessor
                node = parkingLots.getInOrderPredecessor(node);
            } else {
                node = null; // No suitable node found
            }
        }

        // Search for a parking lot with capacity less than or equal to the truck's capacity
        while (node != null) {
            ParkingLot lot = node.value;
            if (lot.waitingSection + lot.readySection < lot.truckLimit) { // If there exists a parking lot with available space
                targetLot = lot;
                break;
            }
            // Move to the next smaller parking lot
            node = parkingLots.getInOrderPredecessor(node);
        }

        if (targetLot != null) {
            // Add truck to the waiting section of the parking lot
            targetLot.waitingTrucks.add(truck);
            targetLot.waitingSection++; // Increment the number of waiting trucks in waiting section
            writeOutput(String.valueOf(targetLot.capacity)); // write the capacity of the parking lot to output.txt
        } else {
            // If any suitable parking lot is not found
            writeOutput("-1");
        }
    }

    // Method to move a truck from waiting to ready section based on capacity
    public void ready(int capacity) {
        AVLNode node = parkingLots.getNode(capacity); // Find parking lot with exact capacity
        ParkingLot lot = null;

        if (node != null) {
            // Parking lot with exact capacity found
            lot = node.value;
            if (lot.waitingIndex < lot.waitingTrucks.size()) {
                // There is a truck in the waiting section
                moveTruckToReady(lot);
                return;
            } else {
                // No trucks in waiting; look for larger capacity parking lots
                node = parkingLots.getInOrderSuccessor(node);
            }
        } else {
            // No parking lot with exact capacity; find where it would be inserted
            node = parkingLots.findInsertPosition(capacity);
            if (node != null && node.key > capacity) {
                // Node's key is greater than capacity; node is successor
                // No action needed
            } else if (node != null && node.key < capacity) {
                // Node's key is less; need to find successor
                node = parkingLots.getInOrderSuccessor(node);
            } else {
                node = null; // No suitable node found
            }
        }

        // Search for parking lots with capacity greater than or equal to the given capacity
        while (node != null) {
            ParkingLot nextLot = node.value;
            if (nextLot.waitingIndex < nextLot.waitingTrucks.size()) {
                // There is a truck in the waiting section
                moveTruckToReady(nextLot);
                return;
            }
            // Move to the next larger parking lot
            node = parkingLots.getInOrderSuccessor(node);
        }

        // No suitable truck found
        writeOutput("-1");
    }

    // Helper method to move a truck from waiting to ready section in a parking lot
    private void moveTruckToReady(ParkingLot lot) {
        // Get the next truck from the waiting list
        Truck truck = lot.waitingTrucks.get(lot.waitingIndex);
        lot.waitingIndex++;      // Increment the waiting index
        lot.waitingSection--;    // Decrement the number of waiting trucks
        lot.readyTrucks.add(truck); // Add truck to ready list
        lot.readySection++;      // Increment the number of ready trucks
        writeOutput(truck.id + " " + lot.capacity); // Output truck ID and parking lot capacity
    }

    // Method to process commands from the input file
    public void processInputFile(String inputFilePath, String output) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(output))) {

            this.writer = bw; // Initialize the writer for output
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into command and arguments
                String[] parts = line.split(" ");
                if (parts[0].equals("create_parking_lot")) {
                    // Command to create a new parking lot
                    int capacity = Integer.parseInt(parts[1]);
                    int truckLimit = Integer.parseInt(parts[2]);
                    createParkingLot(capacity, truckLimit);
                } else if (parts[0].equals("add_truck")) {
                    // Command to add a truck
                    int truckId = Integer.parseInt(parts[1]);
                    int capacity = Integer.parseInt(parts[2]);
                    Truck truck = new Truck(truckId, capacity);
                    addTruck(truck);
                }
                else if (parts[0].equals("ready")) {
                    // Command to move a truck to ready section
                    int readyCapacity = Integer.parseInt(parts[1]);
                    ready(readyCapacity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to write output to the output file
    private void writeOutput(String output) {
        try {
            writer.write(output + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
