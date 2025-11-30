import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This is solution for UVa 11710 - Expensive Subway.
 * It implements Kruskal's Algorithm using a custom Partition ADT 
 * (Disjoint Set) based on Linked Lists/Sequences (Clusters and Nodes).
 * Name: Ahmad Naween Samandar
 * ID: 300446112
 */
public class Main {

    //These are Inner Classes for Partition ADT (Linked List Implementation) ---

    /**
     * It represents an element in the set.
     * It contains a reference to the Cluster it belongs to.
     */
    static class Node {
        int id;
        Cluster cluster;
        Node next; // This is a pointer to next node in the linked list (Cluster)

        public Node(int id) {
            this.id = id;
            this.cluster = null;
            this.next = null;
        }
    }

    /**
     * It represents a set (a connected component).
     * It maintains a linked list of Nodes.
     */
    static class Cluster {
        Node head;
        Node tail;
        int size;

        public Cluster(Node n) {
            this.head = n;
            this.tail = n;
            this.size = 1;
            n.cluster = this;
        }

        // It merges 'other' cluster into 'this' cluster
        // We always append the smaller cluster to the larger one for efficiency (Weighted Union)
        public void merge(Cluster other) {
            this.tail.next = other.head; // It links tail of this to head of other
            this.tail = other.tail;      // It updates tail
            this.size += other.size;

            // It updates the cluster reference for all nodes in the 'other' list
            Node current = other.head;
            while (current != null) {
                current.cluster = this;
                current = current.next;
            }
        }
    }

    /**
     * This is Partition ADT managing the Disjoint Sets.
     */
    static class Partition {
        Node[] nodes;

        public Partition(int n) {
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
                new Cluster(nodes[i]); // Each node starts in its own cluster
            }
        }

        // It finds which Cluster a node ID belongs to
        public Cluster find(int id) {
            return nodes[id].cluster;
        }

        // Union two sets. It returns true if a merge happened, false if already same set.
        public boolean union(int id1, int id2) {
            Cluster c1 = find(id1);
            Cluster c2 = find(id2);

            if (c1 == c2) {
                return false; // Cycle detected, already connected
            }

            // It union by Size optimization: Merge smaller into larger
            if (c1.size >= c2.size) {
                c1.merge(c2);
            } else {
                c2.merge(c1);
            }
            return true;
        }
    }

    // This is Graph Edge Class	 

    static class Edge implements Comparable<Edge> {
        int u, v, weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    //This is Main Solver Logic

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        while (true) {
            String line = br.readLine();
            if (line == null) break;
            
            st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) continue;

            int s = Integer.parseInt(st.nextToken()); // Number of stations
            int c = Integer.parseInt(st.nextToken()); // Number of connections

            if (s == 0 && c == 0) break;

            // Map station names to integer IDs (0 to s-1)
            HashMap<String, Integer> stationMap = new HashMap<>();
            for (int i = 0; i < s; i++) {
                String stationName = br.readLine().trim();
                stationMap.put(stationName, i);
            }

            // Read edges
            ArrayList<Edge> edges = new ArrayList<>();
            for (int i = 0; i < c; i++) {
                st = new StringTokenizer(br.readLine());
                String uName = st.nextToken();
                String vName = st.nextToken();
                int cost = Integer.parseInt(st.nextToken());

                int u = stationMap.get(uName);
                int v = stationMap.get(vName);
                edges.add(new Edge(u, v, cost));
            }

            //It	reads	the starting station (Required by input format, though MST covers all anyway)
            String startStation = br.readLine().trim();

            // --- Kruskal's Algorithm ---
            
            // 1. Sort edges by weight
            Collections.sort(edges);

            // 2. Initialize Partition ADT
            Partition partition = new Partition(s);

            int mstCost = 0;
            int edgesCount = 0;

            // 3. Iterate edges and build MST
            for (Edge e : edges) {
                // Try to union the two stations
                if (partition.union(e.u, e.v)) {
                    mstCost += e.weight;
                    edgesCount++;
                }
            }

            // --- Output Result ---
            // If we selected s-1 edges, the graph is fully connected (spanning tree exists).
            // Special case: If s=1, cost is 0 and edgesCount is 0.
            if (s == 1) {
                System.out.println(0);
            } else if (edgesCount == s - 1) {
                System.out.println(mstCost);
            } else {
                System.out.println("Impossible");
            }
        }
    }
}