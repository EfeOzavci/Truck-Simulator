// Node class for the AVLTree which is representing a parking lot in the tree
class AVLNode {
    int key;          // Used for balancing the tree (capacity value)
    ParkingLot value; // The parking lot associated with this node
    int height;       // The height of this node in the AVL tree
    AVLNode left;     // Left child node
    AVLNode right;    // Right child node
    AVLNode parent;   // Parent node

    public AVLNode(int key, ParkingLot value, AVLNode parent) {
        this.key = key;
        this.value = value;
        this.height = 1; // New nodes are added as leaf nodes, so height is 1
        this.parent = parent;
    }
}
