public class BinarySearchTree<T extends Comparable<T>> {

    // Tracks the number of nodes in this Binary Search Tree
    private int nodeCount = 0;

    // This Binary Search Tree is a rooted tree so we maintain a handel on the root
    // node
    private Node root = null;

    // Internal node containing node references
    // an actual node data
    private class Node {
        T data;
        Node left;
        Node right;

        public Node(Node left, Node right, T data) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    // Check if this binary search tree is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // Get the number of nodes in this
    // binary search tree
    public int size() {
        return nodeCount;
    }

    // Add an element to this binary search tree. Returns
    // true if we successfully perform an insertion
    public boolean add(T elem) {
        // Check if the value already exists in this
        // binary search tree, if it does ignore adding it.
        if (contains(elem)) {
            return false;
        }

        // Outherwise add this element to the binary
        // search tree
        else {
            root = add(root, elem);
            nodeCount++;
            return true;
        }
    }

    // Private method to recursively add a value in the binary
    private Node add(Node node, T elem) {
        // Base case: found a leaf node
        if (node == null) {
            node = new Node(null, null, elem);
        } else {
            // Placer lower elements values in the left subtree
            if (elem.compareTo(node.data) < 0) {
                node.left = add(node.left, elem);
            } else {
                node.right = add(node.right, elem);
            }
        }
        return node;
    }

    // Remove this value from this binary search tree,
    // if it exists
    public boolean remove(T elem) {
        // Make sure the node we want to remove
        // actual exists before e remove it.
        if (contains(elem)) {
            root = remove(root, elem);
            nodeCount--;
            return true;
        }

        return false;
    }

    private Node remove(Node node, T elem) {
        if (node == null) {
            return null;
        }

        int cmp = elem.compareTo(node.data);

        // Dig into left subtree, the value we're looking
        // for is smaller than the current value
        if (cmp < 0) {
            node.left = remove(node.left, elem);
        }

        // Dig into right subtre, the value we're looking
        // for is greater than the current value
        else if (cmp > 0) {
            node.right = remove(node.right, elem);
        }

        // Found the node we wish to remove
        else {
            // This is the case with the only a right subtree
            // or no subtree at all. In this situatation just
            // swap the node we wish to remove with its
            // right child.
            if (node.left == null) {
                Node rightChild = node.right;

                node.data = null;
                node = null;

                return rightChild;
            }
            // This is the case with the only a left subtree
            // or no subtree at all. In this situatation just
            // swap the node we wish to remove with its
            // left child.
            else if (node.right == null) {
                Node leftChild = node.left;

                node.data = null;
                node = null;

                return leftChild;
            }

            // When we removing a from a binary search tree with two links the
            // succssor of the node being removed an either be the largest
            // value int the left subtree or the smallest value in right
            // subtree. In this implementation I have decide to find the
            // smallest value in the right sub tree which can be found by
            // traversing as far left as possible in the right subtree
            else {
                // Find the leftmost node in the right subtree
                Node tmp = digLeft(node.right);

                // Swap the data
                node.data = tmp.data;

                // Go into the right subtree and remove the leftmost node we
                // found and swapped data with. This prevents us for having
                // two nodes in our tree with the same value.
                node.right = remove(node.right, tmp.data);

                // If insted we wanted to find the largest node in the left
                // subtree as opposed to smallest node in the right subtree
                // here is what we would do:
                // Node tmp = digRight(node.left);
                // node.data = tmp.data;
                // node.left = remove(node.left, tmp.data);
            }
        }

        return node;
    }

    // Helper method to find the leftmost node
    private Node digLeft(Node node) {
        Node cur = node;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur;
    }

    // Helper method to find the rightmost node
    /*
     * private Node digRight(Node node) { Node cur = node; while (cur.right != null)
     * { cur = cur.right; } return cur; }
     */

    // Return true is the element exists in the tree
    public boolean contains(T elem) {
        return contains(root, elem);
    }

    // private recursive method to find an element in the binary search tree
    private boolean contains(Node node, T elem) {
        // Base case: reached bottom, value not find
        if (node == null) {
            return false;
        }
        int cmp = elem.compareTo(node.data);

        // Dig into the left subtree because value we're
        // looking for is smaller than the current value
        if (cmp < 0) {
            return contains(node.left, elem);
        }
        // Dig into the right subtree because value we're
        // looking for is greater than the current value
        else if (cmp > 0) {
            return contains(node.right, elem);
        }
        // We found the value we were looking
        else {
            return true;
        }
    }

    // Computes the height of the tree, O(n)
    public int height() {
        return height(root);
    }

    // Recursive helper method to compute the height of the tree
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    public java.util.Iterator<T> traverse(TreeTraversalOrder order) {
        switch (order) {
            case PRE_ORDER:
                return preOrderTraversal();
            case IN_ORDER:
                return inOrderTraversal();
            case POST_ORDER:
                return postOrderTraversal();
            case LEVEL_ORDER:
                return levelOrderTraversal();
            default:
                return null;
        }
    }

    private java.util.Iterator<T> preOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        java.util.Stack<Node> stack = new java.util.Stack<>();

        stack.push(root);

        return new java.util.Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                Node node = stack.pop();
                if (node.right != null) {
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                }
                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    private java.util.Iterator<T> inOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final java.util.Stack<Node> stack = new java.util.Stack<>();

        stack.push(root);

        return new java.util.Iterator<T>() {
            Node trav = root;

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack.isEmpty();
            }

            @Override
            public T next() {

                if (expectedNodeCount != nodeCount)
                    throw new java.util.ConcurrentModificationException();

                while (trav != null && trav.left != null) {
                    stack.push(trav.left);
                    trav = trav.left;
                }

                Node node = stack.pop();

                // Try moving down right once
                if (node.right != null) {
                    stack.push(node.right);
                    trav = node.right;
                }

                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    private java.util.Iterator<T> postOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final java.util.Stack<Node> stack1 = new java.util.Stack<>();
        final java.util.Stack<Node> stack2 = new java.util.Stack<>();

        stack1.push(root);

        while (!stack1.isEmpty()) {
            Node node = stack1.pop();
            stack2.push(node);

            if (node.left != null) {
                stack1.push(node.left);
            }

            if (node.right != null) {
                stack1.push(node.right);
            }
        }

        return new java.util.Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack2.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                return stack2.pop().data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    private java.util.Iterator<T> levelOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.offer(root);

        return new java.util.Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                if (expectedNodeCount != nodeCount) {
                    throw new java.util.ConcurrentModificationException();
                }
                Node node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

}
