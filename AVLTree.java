// AVL Tree class to manage parking lots
class AVLTree {
    private AVLNode root; // Root node of the AVL tree

    // Inserting a new parking lot into the AVL tree
    public void insert(int key, ParkingLot value) {
        root = insert(root, key, value, null);
    }

    // Inserting a node and keeping the balance of AVL tree
    private AVLNode insert(AVLNode node, int key, ParkingLot value, AVLNode parent) {
        // If there is a position to insert the new node
        if (node == null)
            return new AVLNode(key, value, parent);
        if (key < node.key)
            node.left = insert(node.left, key, value, node);
        else if (key > node.key)
            node.right = insert(node.right, key, value, node);
        else
            return node;

        // Update the height of the ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Balance the node if necessary
        return balance(node);
    }

    // Helper method to calculate the height of a node
    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.height;
        }
    }

    // Helper method to calculate the balance factor of a node
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }

    }

    // Balance the AVL tree starting from the given node
    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalance(node);

        // Left Left Case
        if (balanceFactor > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // Left Right Case
        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balanceFactor < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // Right Left Case
        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // when node is balanced return it
        return node;
    }

    // Right rotation to balance the tree
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;     // x becomes the new root
        AVLNode T2 = x.right;   // T2 is subtree that will be moved

        // rotation
        x.right = y;
        y.left = T2;

        // Update parents
        x.parent = y.parent;
        y.parent = x;
        if (T2 != null)
            T2.parent = y;

        // Update heights of affected nodes
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Return new root node
        return x;
    }

    // Left rotation to balance the tree
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;    // y becomes the new root
        AVLNode T2 = y.left;    // T2 is subtree that will be moved

        // rotation
        y.left = x;
        x.right = T2;

        // Update parents
        y.parent = x.parent;
        x.parent = y;
        if (T2 != null)
            T2.parent = x;

        // Update heights of affected nodes
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Return new root node
        return y;
    }

    // Method to find a node with a specific key (capacity)
    public AVLNode getNode(int key) {
        AVLNode node = root;
        while (node != null) {
            if (key < node.key)
                node = node.left; // Go left
            else if (key > node.key)
                node = node.right; // Go right
            else
                return node; // Key found
        }
        return null; // Key not found
    }

    // Find where a new key would be inserted
    public AVLNode findInsertPosition(int key) {
        AVLNode node = root;
        AVLNode parent = null;
        while (node != null) {
            parent = node;
            if (key < node.key)
                node = node.left;
            else if (key > node.key)
                node = node.right;
            else
                return node; // Key exists
        }
        return parent; // Return parent node for insertion point
    }

    // Method to find the in-order predecessor of a node
    public AVLNode getInOrderPredecessor(AVLNode node) {
        if (node.left != null) {
            // The predecessor is the rightmost node in the left subtree
            node = node.left;
            while (node.right != null)
                node = node.right;
            return node;
        } else {
            // Until finding a node that is a right child
            AVLNode parent = node.parent;
            while (parent != null && node == parent.left) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    // Method to find the in-order successor of a node
    public AVLNode getInOrderSuccessor(AVLNode node) {
        if (node.right != null) {
            // The successor is the leftmost node in the right subtree
            node = node.right;
            while (node.left != null)
                node = node.left;
            return node;
        } else {
            // Move up until we find a node that is a left child
            AVLNode parent = node.parent;
            while (parent != null && node == parent.right) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }
}

