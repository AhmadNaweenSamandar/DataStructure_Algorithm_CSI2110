import java.util.*;

/**
 * Name: Ahmad Naween Samandar
 * ID: 300446112
 * Assignment: Part2 - A
 */

/**
 * IslandSurvey.java
 * 
 * This class reads a map of land (1) and water (0) from standard input and uses
 * a Partition (Union-Find) data structure to identify and count islands (connected
 * groups of land). It supports dynamic updates where new land cells are added,
 * and prints the number of islands, their sizes, and total land area after each update.
 */


public class IslandSurvey {
    static int S, T; // S = number of rows, T = number of columns in the map
    static int[][] map; // 2D grid representing land (1) and water (0)
    static Partition BP; // Partition data structure (Union-Find) to group connected land cells
    static Node[][] cluster; // Stores the Node object for each land cell

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read the size of the map
        S = sc.nextInt(); // number of rows
        T = sc.nextInt(); // number of columns

        map = new int[S][T];

        // Read the map grid (each row as a string of 0s and 1s)
        for (int i = 0; i < S; i++) {
            String line = sc.next();
            for (int j = 0; j < T; j++) {
                map[i][j] = line.charAt(j) - '0'; // convert character '0'/'1' to integer 0/1
            }
        }

        // Build the initial clusters (connected components of land)
        processMap();

        // Print the initial survey results
        printSurvey();

        // Read the number of updates (new land formations)
        int F = sc.nextInt();
        for (int f = 0; f < F; f++) {
            int L = sc.nextInt(); // number of new land cells in this update
            List<int[]> newLand = new ArrayList<>();

            // Read coordinates for each new land cell
            for (int i = 0; i < L; i++) {
                int r = sc.nextInt(); // row index
                int c = sc.nextInt(); // column index
                newLand.add(new int[]{r, c});
            }

            // Update the map and recalculate clusters
            updateMap(newLand);

            // Print the updated survey after adding new land
            printSurvey();
        }
    }

    // Build the initial clusters of land from the map
    static void processMap() {
        BP = new Partition(); // Initialize the partition (union-find) structure
        cluster = new Node[S][T]; // Initialize the Node grid

        // Step 1: Create a cluster (singleton set) for each land cell (value 1)
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 1) {
                    Node n = new Node(i, j); // create a Node representing this land cell
                    cluster[i][j] = BP.makeCluster(n); // make a new cluster in the Partition
                }
            }
        }

        // Step 2: Merge adjacent land cells (up, down, left, right)
        int[] dr = {-1, 0, 1, 0}; // row directions (N, E, S, W)
        int[] dc = {0, 1, 0, -1}; // column directions (N, E, S, W)
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 1) {
                    // Check all 4 neighbors
                    for (int d = 0; d < 4; d++) {
                        int ni = i + dr[d];
                        int nj = j + dc[d];
                        // If neighbor is inside the map and also land, merge them
                        if (inBounds(ni, nj) && map[ni][nj] == 1) {
                            BP.union(cluster[i][j], cluster[ni][nj]);
                        }
                    }
                }
            }
        }
    }

    // Add new land cells to the map and merge with nearby clusters
    static void updateMap(List<int[]> newLand) {
        for (int[] pos : newLand) {
            int i = pos[0], j = pos[1];

            // If the cell is already land, skip it
            if (map[i][j] == 1) continue;

            // Convert this water cell into land
            map[i][j] = 1;

            // Create a new Node and add it to the Partition structure
            Node n = new Node(i, j);
            cluster[i][j] = BP.makeCluster(n);

            // Try to merge with all adjacent land cells
            int[] dr = {-1, 0, 1, 0};
            int[] dc = {0, 1, 0, -1};
            for (int d = 0; d < 4; d++) {
                int ni = i + dr[d];
                int nj = j + dc[d];
                if (inBounds(ni, nj) && map[ni][nj] == 1) {
                    BP.union(cluster[i][j], cluster[ni][nj]);
                }
            }
        }
    }

    // Print the current survey results
    static void printSurvey() {
        // Get all clusters and their sizes from the Partition structure
        Map<Node, Integer> clusters = BP.getClusterSizes();

        // Extract and sort cluster sizes in descending order
        List<Integer> sizes = new ArrayList<>(clusters.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Print number of clusters, sizes, and total land cells
        if (sizes.isEmpty()) {
            // No land at all
            System.out.println(0);   // number of clusters
            System.out.println(-1);  // no sizes to show
            System.out.println(0);   // total land area
        } else {
            System.out.println(sizes.size()); // number of clusters (islands)

            // Print sizes separated by spaces
            for (int i = 0; i < sizes.size(); i++) {
                System.out.print(sizes.get(i));
                if (i < sizes.size() - 1) System.out.print(" ");
            }
            System.out.println();

            // Compute total number of land cells
            int total = sizes.stream().mapToInt(Integer::intValue).sum();
            System.out.println(total);
        }

        // Blank line to separate survey outputs
        System.out.println();
    }

    // Helper function: check if a position is within map boundaries
    static boolean inBounds(int r, int c) {
        return r >= 0 && r < S && c >= 0 && c < T;
    }
}
