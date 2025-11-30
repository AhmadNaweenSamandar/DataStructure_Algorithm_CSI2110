// Paris (P2- Part B)
// Startup code given in the Fall 2025 for csi2110/csi2510
// This file only contains basic commands to read the data from the input file 'metro.txt'
// Use and modify it freely
// 
//Name: Ahmad Naween Samandar
//ID: 300446112

import java.io.*;
import java.util.*;

/**
 * Solution for Part B - Expensive Subway in Paris.
 * * 1. Reads metro.txt using the provided scanner logic.
 * 2. Identifies "Hub Stations" (vertices connected by -1 walking edges).
 * 3. Builds a simplified graph where nodes are Hubs and edges are 
 * calculated path costs between them.
 * 4. Runs Kruskal's Algorithm to find the MST.
 * 
 */
public class ParisMetro {

    // --- Static Data Structures ---
    static int N, M;
    static String[] stationNames;
    
    // Graph for physical subway lines (weight > 0)
    // List of RawEdge objects containing target vertex and weight
    static ArrayList<ArrayList<RawEdge>> subGraph; 
    
    // Graph for walking transfers (weight = -1)
    // Simple integer list because weight is always -1 (undirected)
    static ArrayList<ArrayList<Integer>> transferGraph;

    // Maps original Vertex ID -> Hub ID. 
    // If a vertex is not part of a multi-line hub, this might remain -1 (based on logic).
    static int[] vertexToHubId; 
    static ArrayList<String> hubNames; // Name of each Hub ID

    // --- Inner Classes ---

    // Simple edge for the initial graph reading
    static class RawEdge {
        int to;
        int weight;
        public RawEdge(int to, int w) { this.to = to; this.weight = w; }
    }

    // Edge class for the "Expensive Graph" (Hub to Hub)
    static class Edge implements Comparable<Edge> {
        int u, v, weight;
        String startHubName, endHubName;

        public Edge(int u, int v, int weight, String sName, String eName) {
            this.u = u;
            this.v = v;
            this.weight = weight;
            this.startHubName = sName;
            this.endHubName = eName;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    // --- Partition ADT (Required from Part A) ---
    static class Node {
        int id;
        Cluster cluster;
        Node next;
        public Node(int id) { this.id = id; this.cluster = null; this.next = null; }
    }

    static class Cluster {
        Node head, tail;
        int size;
        public Cluster(Node n) {
            this.head = n; this.tail = n; this.size = 1;
            n.cluster = this;
        }
        public void merge(Cluster other) {
            this.tail.next = other.head;
            this.tail = other.tail;
            this.size += other.size;
            Node current = other.head;
            while (current != null) {
                current.cluster = this;
                current = current.next;
            }
        }
    }

    static class Partition {
        Node[] nodes;
        public Partition(int n) {
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
                new Cluster(nodes[i]);
            }
        }
        public Cluster find(int id) { return nodes[id].cluster; }
        public boolean union(int id1, int id2) {
            Cluster c1 = find(id1);
            Cluster c2 = find(id2);
            if (c1 == c2) return false;
            if (c1.size >= c2.size) c1.merge(c2);
            else c2.merge(c1);
            return true;
        }
    }

    // --- Main Execution ---

    public static void main(String[] args) {
        // 1. Read Data
        readMetro(); 

        // 2. Process Graph
        identifyHubs();

        // 3. Build Simplified Graph
        ArrayList<Edge> expensiveEdges = buildExpensiveGraph();

        // 4. Run Kruskal
        runKruskal(expensiveEdges);
    }

    public static void readMetro() {
        // Using Scanner as requested in the starter code
        Scanner scan = new Scanner(System.in); 

        if (!scan.hasNext()) return; // Safety check for empty file

        N = Integer.parseInt(scan.next()); // number of vertices
        M = Integer.parseInt(scan.next()); // number of edges
        System.out.println("Paris Metro Graph has "+N+" vertices and "+M+" edges.");
        
        // Initialize Data Structures
        stationNames = new String[N];
        subGraph = new ArrayList<>(N);
        transferGraph = new ArrayList<>(N);
        for(int i=0; i<N; i++) {
            subGraph.add(new ArrayList<>());
            transferGraph.add(new ArrayList<>());
        }

        // Read Vertices
        for (int i=0; i <N; i++) {   
            int vertexNumber = Integer.valueOf(scan.next());
            String stationName = scan.nextLine().trim(); // Trim to remove leading space
            stationNames[vertexNumber] = stationName;
        }

        String dollar = scan.nextLine(); // read the $ sign line (if present)
        // Note: scan.next() above might leave a newline, so we ensure we pass the $ line.
        // If the file format is strict, simply skipping to edges is fine.
        // We will assume the scanner is positioned correctly or handle the dollar logic implicitly 
        // by just looking for integers for the edges.

        // Read Edges
        for (int i=0; i <M; i++) {
            if (!scan.hasNext()) break;
            // Scan token by token to avoid newline issues
            String v1Str = scan.next();
            if (v1Str.equals("$")) { // Handle case where $ is read here
                v1Str = scan.next();
            }
            int v1 = Integer.parseInt(v1Str);
            int v2 = scan.nextInt();
            int weight = scan.nextInt();

            if (weight == -1) {
                // Walking connection
                transferGraph.get(v1).add(v2);
                transferGraph.get(v2).add(v1);
            } else {
                // Physical subway line
                subGraph.get(v1).add(new RawEdge(v2, weight));
            }
        }
        
        // System.out.println("End Reading Metro\n"); // Optional per prompt
    }

    // --- Algorithm Implementation ---

    public static void identifyHubs() {
        vertexToHubId = new int[N];
        Arrays.fill(vertexToHubId, -1);
        
        hubNames = new ArrayList<>();
        int hubCounter = 0;
        int totalHubVertices = 0;

        // Iterate through all vertices to find Connected Components via Transfer edges (-1)
        for (int i = 0; i < N; i++) {
            // We only care about vertices involved in transfers to define "Hubs"
            // based on the assignment description logic.
            // If a node has -1 edges OR hasn't been visited, we check it.
            if (vertexToHubId[i] == -1 && !transferGraph.get(i).isEmpty()) {
                
                // BFS to find all vertices in this Hub
                LinkedList<Integer> queue = new LinkedList<>();
                queue.add(i);
                vertexToHubId[i] = hubCounter;
                totalHubVertices++;
                
                String currentHubName = stationNames[i]; // Use name of first vertex found

                while(!queue.isEmpty()) {
                    int u = queue.poll();
                    for(int v : transferGraph.get(u)) {
                        if(vertexToHubId[v] == -1) {
                            vertexToHubId[v] = hubCounter;
                            totalHubVertices++;
                            queue.add(v);
                        }
                    }
                }
                hubNames.add(currentHubName);
                hubCounter++;
            }
        }
        
        // Print Output Matching Sample
        System.out.print("Hub Stations = [ ");
        for(int i=0; i<Math.min(hubNames.size(), 4); i++) {
            System.out.print(hubNames.get(i) + ", ");
        }
        System.out.println("... ]"); 
        System.out.println("Number of Hub Stations = " + hubCounter + " (total Hub Vertices = " + totalHubVertices + ")");
    }

    public static ArrayList<Edge> buildExpensiveGraph() {
        ArrayList<Edge> expensiveEdges = new ArrayList<>();

        // Traverse from every physical vertex that belongs to a Hub
        for (int startNode = 0; startNode < N; startNode++) {
            if (vertexToHubId[startNode] == -1) continue; // Not part of a hub

            int startHubId = vertexToHubId[startNode];
            
            // DFS along subway lines to find other Hubs
            for (RawEdge e : subGraph.get(startNode)) {
                dfsFindSegment(e.to, e.weight, startNode, startHubId, expensiveEdges);
            }
        }

        System.out.println("Number of Possible Segments = " + expensiveEdges.size());
        return expensiveEdges;
    }

    private static void dfsFindSegment(int curr, int currentCost, int prev, int startHubId, ArrayList<Edge> edges) {
        // If we reached a node that is part of a Hub
        if (vertexToHubId[curr] != -1) {
            int endHubId = vertexToHubId[curr];
            // If we reached a DIFFERENT Hub, we found a segment
            if (endHubId != startHubId) {
                // Add to list
                String sName = hubNames.get(startHubId);
                String eName = hubNames.get(endHubId);
                edges.add(new Edge(startHubId, endHubId, currentCost, sName, eName));
            }
            return; // Stop traversing this path
        }

        // If not a hub, continue traversing down the line
        for (RawEdge next : subGraph.get(curr)) {
            // Don't go back to where we came from
            if (next.to != prev) {
                dfsFindSegment(next.to, currentCost + next.weight, curr, startHubId, edges);
            }
        }
    }

    public static void runKruskal(ArrayList<Edge> edges) {
        int numHubs = hubNames.size();
        Partition partition = new Partition(numHubs);
        Collections.sort(edges);

        int mstCost = 0;
        int edgesCount = 0;
        ArrayList<Edge> mstEdges = new ArrayList<>();

        for (Edge e : edges) {
            if (partition.union(e.u, e.v)) {
                mstCost += e.weight;
                edgesCount++;
                mstEdges.add(e);
            }
        }

        if (edgesCount != numHubs - 1) {
            System.out.println("Impossible");
        } else {
            System.out.println("Total Cost = $" + mstCost);
            System.out.println("Segments to Buy:");
            int i = 1;
            for (Edge e : mstEdges) {
                System.out.println(i++ + "( " + e.startHubName + " - " + e.endHubName + " ) - $" + e.weight);
            }
        }
    }
}