import java.util.*;

/**
 * Name: Ahmad Naween Samandar
 * ID: 300446112
 * Assignment: Part2 - B
 */
 
// I have provided short description for the class and all methods to help TA understand the code flow if needed

/**
 * IslandLakeSurvey.java Class
 *
 * 
 * This program analyzes a grid map made up of land (1) and water (0),
 * identifying **islands** (connected land regions) and **lakes** (water bodies fully surrounded by land).
 * 
 * It supports:
 *  1. Initial map analysis (counting islands and lakes).
 *  2. Updating the map dynamically when new land is added.
 *  3. Printing survey results after each update.
 *
 * I make the program such that it uses the Partition ADT (Disjoint Set Union / Union-Find) 
 * to efficiently manage connected regions of land (black partitions) and water (white partitions).
 */
public class IslandLakeSurvey {

    static int S, T;               // S = number of rows, T = number of columns in the map
    static int[][] map;            // 2D grid representing the map (1 = land, 0 = water)
    static Partition BP;           // "Black Partitions" — manages disjoint sets of land cells (islands)
    static Node[][] cluster;       // Stores Node references for each map cell that belongs to an island

// to efficiently solve this problem I have divided the approach in several steps

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Step 1: Read the map size
        S = sc.nextInt(); // number of rows
        T = sc.nextInt(); // number of columns
        map = new int[S][T];

        // Step 2: Read the initial map layout
        for (int i = 0; i < S; i++) {
            String line = sc.next();
            for (int j = 0; j < T; j++) {
                map[i][j] = line.charAt(j) - '0'; // convert '0'/'1' chars to integers
            }
        }

        // Step 3: Process and display the initial survey
        processMap();
        printSurvey();

        // Step 4: Process updates (new land added)
        int F = sc.nextInt(); // number of future updates
        for (int f = 0; f < F; f++) {
            int L = sc.nextInt(); // number of new land cells in this update
            List<int[]> newLand = new ArrayList<>();
            for (int i = 0; i < L; i++) {
                int r = sc.nextInt(); // row index of new land
                int c = sc.nextInt(); // column index of new land
                newLand.add(new int[]{r, c});
            }
            updateMap(newLand); // apply the update
            printSurvey();      // reprint the survey results
        }
    }

    /**
     * processMap() method
     * It builds the initial partition of land cells (islands).
     * It uses 4-directional adjacency (up, down, left, right) to connect land cells.
     */
    static void processMap() {
        BP = new Partition();          // create a new disjoint set for land
        cluster = new Node[S][T];      // 2D array of Node references

	// again the same approach dividing the problem in steps
        // Step 1: Create clusters for each land cell
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 1) {  // land cell
                    Node n = new Node(i, j);
                    cluster[i][j] = BP.makeCluster(n); // initialize singleton cluster
                }
            }
        }

        // Step 2: Merge adjacent land cells (4-directional connectivity)
        int[] dr = {-1, 0, 1, 0}; // up, right, down, left
        int[] dc = {0, 1, 0, -1};
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 1) {
                    for (int d = 0; d < 4; d++) {
                        int ni = i + dr[d];
                        int nj = j + dc[d];
                        if (inBounds(ni, nj) && map[ni][nj] == 1)
                            BP.union(cluster[i][j], cluster[ni][nj]);
                    }
                }
            }
        }
    }

    /**
     * updateMap(newLand) method
     * it adds new land cells to the map and merges them with existing islands when adjacent.
     *
     * @param newLand: it is a list of [row, column] coordinates to convert from water to land.
     */
    static void updateMap(List<int[]> newLand) {
        for (int[] pos : newLand) {
            int i = pos[0], j = pos[1];
            if (map[i][j] == 1) continue; // skip if already land

            map[i][j] = 1; // convert to land
            Node n = new Node(i, j);
            cluster[i][j] = BP.makeCluster(n); // create a new cluster for it

            // Try to merge it with any adjacent land (4 directions)
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

    /**
     * detectLakes() method 
     * it detects all **lakes** (water regions fully surrounded by one island).
     *
     * following are the steps to implement the solution:
     *  1. Build white partitions (clusters of water cells).
     *  2. Determine which white clusters touch the border or multiple islands.
     *  3. Count those that form valid lakes (touch one island only and not the border).
     *
     * @return LakeResult object will contain the following:
     *         - mapping of island → total lake area inside it,
     *         - total number of lakes,
     *         - total combined lake area.
     */
    static LakeResult detectLakes() {
        Partition WP = new Partition();         // white partitions (water)
        Node[][] whiteCluster = new Node[S][T]; // white cell clusters

        // 8-directional connectivity for lakes (including diagonals)
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        // Step 1: Create a cluster for each water cell
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 0) {
                    Node n = new Node(i, j);
                    whiteCluster[i][j] = WP.makeCluster(n);
                }
            }
        }

        // Step 2: Merge connected water cells
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (map[i][j] == 0) {
                    for (int d = 0; d < 8; d++) {
                        int ni = i + dr[d];
                        int nj = j + dc[d];
                        if (inBounds(ni, nj) && map[ni][nj] == 0) {
                            WP.union(whiteCluster[i][j], whiteCluster[ni][nj]);
                        }
                    }
                }
            }
        }

        // Step 3: Analyze each water cluster
        Map<Node, Integer> whiteSizes = WP.getClusterSizes(); // size of each water body
        Map<Node, Set<Node>> touchingIslands = new HashMap<>(); // which islands each water body touches
        Set<Node> touchesBorder = new HashSet<>();              // lakes touching map border

        // Go through each white (water) node
        for (Node w : whiteClusterFlatten(whiteCluster)) {
            if (w == null) continue;
            Node root = WP.find(w);
            touchingIslands.putIfAbsent(root, new HashSet<>());

            // Check if the water region touches the border
            if (w.row == 0 || w.row == S - 1 || w.col == 0 || w.col == T - 1)
                touchesBorder.add(root);

            // Check adjacent land (4-directional) to find touching islands
            int[] dr4 = {-1, 0, 1, 0};
            int[] dc4 = {0, 1, 0, -1};
            for (int d = 0; d < 4; d++) {
                int ni = w.row + dr4[d];
                int nj = w.col + dc4[d];
                if (inBounds(ni, nj) && map[ni][nj] == 1) {
                    Node islandRoot = BP.find(cluster[ni][nj]);
                    touchingIslands.get(root).add(islandRoot);
                }
            }
        }

        // Step 4: Identify valid lakes
        int totalLakes = 0;
        int totalLakeArea = 0;
        Map<Node, Integer> lakeAreasPerIsland = new HashMap<>();

        for (Node lakeRoot : whiteSizes.keySet()) {
            boolean closed = !touchesBorder.contains(lakeRoot);
            boolean singleIsland = touchingIslands.get(lakeRoot).size() == 1;

            // Valid lake: touches exactly one island and not the map border
            if (closed && singleIsland) {
                Node island = touchingIslands.get(lakeRoot).iterator().next();
                int lakeArea = whiteSizes.get(lakeRoot);
                totalLakes++;
                totalLakeArea += lakeArea;

                // Add lake area to that island's total
                lakeAreasPerIsland.put(island,
                    lakeAreasPerIsland.getOrDefault(island, 0) + lakeArea);
            }
        }

        return new LakeResult(lakeAreasPerIsland, totalLakes, totalLakeArea);
    }

    /**
     * This method converts a 2D array of Nodes (white clusters) into a flat list for iteration.
     */
    static List<Node> whiteClusterFlatten(Node[][] w) {
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < S; i++)
            for (int j = 0; j < T; j++)
                if (w[i][j] != null)
                    list.add(w[i][j]);
        return list;
    }

    /**
     * printSurvey() method 
     * It combines island and lake information and prints:
     *   - Number of islands
     *   - Sizes of each island (including lakes)
     *   - Total area of all islands
     *   - Number of lakes
     *   - Total lake area
     */
    static void printSurvey() {
        Map<Node, Integer> islandSizes = BP.getClusterSizes(); // land cluster sizes
        LakeResult lakes = detectLakes();                      // detect all lakes

        // Add lake areas to corresponding island sizes
        for (Node island : lakes.lakeAreasPerIsland.keySet()) {
            Node root = BP.find(island);
            int newSize = islandSizes.getOrDefault(root, 0)
                        + lakes.lakeAreasPerIsland.get(island);
            islandSizes.put(root, newSize);
        }

        // Sort island sizes in descending order
        List<Integer> sizes = new ArrayList<>(islandSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // Print survey results
        if (sizes.isEmpty()) {
            // No islands
            System.out.println(0);
            System.out.println(-1);
            System.out.println(0);
            System.out.println(0);
            System.out.println(0);
        } else {
            // Print island data
            System.out.println(sizes.size()); // number of islands
            for (int i = 0; i < sizes.size(); i++) {
                System.out.print(sizes.get(i));
                if (i < sizes.size() - 1) System.out.print(" ");
            }
            System.out.println();

            int total = sizes.stream().mapToInt(Integer::intValue).sum();
            System.out.println(total);             // total island area
            System.out.println(lakes.totalLakes);  // total number of lakes
            System.out.println(lakes.totalLakeArea); // total lake area
        }
        System.out.println();
    }

    /**
     * Helper: checks if a coordinate (r, c) is within map bounds.
     */
    static boolean inBounds(int r, int c) {
        return r >= 0 && r < S && c >= 0 && c < T;
    }

    /**
     * Helper record class storing lake analysis results.
     *  - lakeAreasPerIsland: maps each island to total area of lakes inside it.
     *  - totalLakes: total number of lakes found.
     *  - totalLakeArea: combined size (in cells) of all lakes.
     */
    static class LakeResult {
        Map<Node, Integer> lakeAreasPerIsland;
        int totalLakes;
        int totalLakeArea;

        LakeResult(Map<Node, Integer> map, int c, int a) {
            this.lakeAreasPerIsland = map;
            this.totalLakes = c;
            this.totalLakeArea = a;
        }
    }
}
