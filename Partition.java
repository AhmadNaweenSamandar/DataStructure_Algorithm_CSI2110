import java.util.*;

/**
 * Name: Ahmad Naween Samandar
 * Student ID: 300446112
 * Course: CSI 2105
 * Programming Assignment 1 - Part 1 
*/

public class Partition<E> {

    /**
     * The following is a Node class representing a position in the partition.
     * Each node stores an element and maintains links for the cluster linked list as per the index attached to the assignment.
     */
    public static class Node<E> {
        E element;          // This is the element stored at this position
        Node<E> next;       // This is the Next node in the cluster linked list 
        Node<E> prev;       // This will store Previous node in the cluster linked list  
        Node<E> leader;     // Since every cluster has a leader and it is the first element it Reference to that specfic leader node of this cluster
        int clusterSize;    // Size of the cluster (only valid for leader nodes)
        Node<E> tail;       // Tail node of the cluster (only valid for leader nodes)

        /**
         * Node Constructor for creating a new singleton node.
         * Time Complexity: O(1)
         */
        public Node(E element) {
            this.element = element;
            this.next = null;
            this.prev = null;
            this.leader = this;     // Initially, each node is its own leader
            this.clusterSize = 1;   // Singleton cluster has size 1
            this.tail = this;       // Singleton cluster tail points to itself
        }

        public E getElement() {
            return element;
        }
    }

    private int clusterCount = 0;                    // Counter for number of clusters, it will be used for returning number of counter
    private final Set<Node<E>> leaders = new HashSet<>();  // Using Set as Hashmap with key and value it will store all cluster leaders

    /**
     * This creates a singleton cluster containing element x and returns its position.
     * After analyzing the time Complexity is: O(1)
     * - time complexity of node creation: O(1)
     * - time complexity of HashSet addition: O(1) average case
     * - time complexity of Counter increment: O(1)
     */
    public Node<E> makeCluster(E x) {
        Node<E> newNode = new Node<>(x);
        leaders.add(newNode);
        clusterCount++;
        return newNode;
    }

    /**
     * Returns the position of the leader of the cluster containing position p.
     * here the time complexity is: O(1)
     * - Direct field access to p.leader
     */
    public Node<E> find(Node<E> p) {
        if (p == null) return null;
        return p.leader;
    }

    /**
     * Merges the clusters containing positions p and q.
     * after carefull consideration the time complexity: O(min(n1, n2)) where n1 = size of p's cluster, n2 = size of q's cluster
     * - find operations: O(1) each
     * - attachCluster: O(min(n1, n2)) - only traverses the smaller cluster
     */
    public void union(Node<E> p, Node<E> q) {
        if (p == null || q == null) return;
        
        Node<E> leaderP = find(p);  // O(1)
        Node<E> leaderQ = find(q);  // O(1)
        
        if (leaderP == leaderQ) return; // Already in same cluster

        // Union by size: attach smaller cluster to larger cluster
        // This ensures we only traverse the smaller cluster
        if (leaderP.clusterSize < leaderQ.clusterSize) {
            // Merge p's cluster into q's cluster - O(size of p's cluster)
            attachCluster(leaderP, leaderQ);
        } else {
            // Merge q's cluster into p's cluster - O(size of q's cluster)  
            attachCluster(leaderQ, leaderP);
        }
        
        clusterCount--;
    }

    /**
     * Helper method to attach a smaller cluster to a larger cluster.
     * Time Complexity: O(smallSize) - only traverses the smaller cluster
     * @param smallLeader The leader of the smaller cluster to be merged
     * @param largeLeader The leader of the larger cluster that will absorb the smaller one
     */
    private void attachCluster(Node<E> smallLeader, Node<E> largeLeader) {
        // Step 1: Update leader references for all nodes in the smaller cluster
        // This traversal is O(smallSize)
        Node<E> current = smallLeader;
        while (current != null) {
            current.leader = largeLeader;  // Update leader reference
            current = current.next;        // Move to next node in smaller cluster
        }
        
        // Step 2: Append the smaller cluster linked list to the larger cluster
        // Using tail pointers makes this operation O(1)
        if (largeLeader.tail != null && smallLeader != null) {
            // Connect the tail of large cluster to head of small cluster
            largeLeader.tail.next = smallLeader;
            smallLeader.prev = largeLeader.tail;
            
            // Update the tail of large cluster to be the tail of small cluster
            largeLeader.tail = smallLeader.tail;
        }
        
        // Step 3: Update cluster size - O(1)
        largeLeader.clusterSize += smallLeader.clusterSize;
        
        // Step 4: Remove smallLeader from leaders set - O(1) average case
        leaders.remove(smallLeader);
        
        // Step 5: Clear the small cluster's leadership information
        smallLeader.clusterSize = 0;
        smallLeader.tail = null;
    }

    /**
     * Returns the element stored at position p.
     * Time Complexity: O(1) - direct field access
     */
    public E element(Node<E> p) {
        if (p == null) return null;
        return p.element;
    }

    /**
     * Returns the current number of different clusters.
     * Time Complexity: O(1) - counter access
     */
    public int numberOfClusters() {
        return clusterCount;
    }

    /**
     * Returns the size of the cluster containing p.
     * Time Complexity: O(1) - find is O(1) + field access
     */
    public int clusterSize(Node<E> p) {
        if (p == null) return 0;
        return find(p).clusterSize;  // find is O(1), field access is O(1)
    }

    /**
     * Returns a list of all positions that belong to the same cluster as position p.
     * Time Complexity: O(s) where s is the size of the returned list
     * - Traverses the entire cluster linked list
     */
    public List<Node<E>> clusterPositions(Node<E> p) {
        List<Node<E>> list = new ArrayList<>();
        if (p == null) return list;
        
        Node<E> leader = find(p);  // O(1)
        Node<E> current = leader;
        
        // Traverse the entire cluster: O(cluster size)
        while (current != null) {
            list.add(current);
            current = current.next;
        }
        return list;
    }

    /**
     * Returns a list of all cluster sizes in decreasing order of size.
     * time Complexity: O(c log c) where c is number of clusters
     * - gathering sizes: O(c)
     * - sorting: O(c log c)
     */
    public List<Integer> clusterSizes() {
        List<Integer> sizes = new ArrayList<>();
        // Iterate through all leaders: O(c)
        for (Node<E> leader : leaders) {
            sizes.add(leader.clusterSize);
        }
        // Sort in descending order: O(c log c)
        sizes.sort(Collections.reverseOrder());
        return sizes;
    }
}