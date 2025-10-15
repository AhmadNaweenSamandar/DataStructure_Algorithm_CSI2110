import java.util.*;

public class PartitionTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Partition ADT ===\n");

        // Test 1: Basic functionality
        System.out.println("Test 1: Basic Singleton Clusters");
        Partition<String> partition = new Partition<>();
        
        Partition.Node<String> a = partition.makeCluster("A");
        Partition.Node<String> b = partition.makeCluster("B");
        Partition.Node<String> c = partition.makeCluster("C");
        Partition.Node<String> d = partition.makeCluster("D");
        Partition.Node<String> e = partition.makeCluster("E");

        System.out.println("Number of clusters: " + partition.numberOfClusters());
        System.out.println("Cluster size of A: " + partition.clusterSize(a));
        System.out.println("Find A: " + partition.find(a).getElement());
        System.out.println("Element at A: " + partition.element(a));
        
        // Test 2: Union operations
        System.out.println("\nTest 2: Union Operations");
        partition.union(a, b);
        System.out.println("After union(A,B):");
        System.out.println("Number of clusters: " + partition.numberOfClusters());
        System.out.println("Cluster size of A: " + partition.clusterSize(a));
        System.out.println("Find A: " + partition.find(a).getElement());
        System.out.println("Find B: " + partition.find(b).getElement());
        System.out.println("A and B have same leader: " + (partition.find(a) == partition.find(b)));
        
        // Test 3: clusterPositions
        System.out.println("\nTest 3: clusterPositions Method");
        List<Partition.Node<String>> clusterNodes = partition.clusterPositions(a);
        System.out.println("Nodes in A's cluster: " + clusterNodes.size());
        System.out.print("Elements: ");
        for (Partition.Node<String> node : clusterNodes) {
            System.out.print(partition.element(node) + " ");
        }
        System.out.println();

        // Test 4: More unions
        System.out.println("\nTest 4: More Unions");
        partition.union(c, d);
        partition.union(a, c);
        System.out.println("After more unions:");
        System.out.println("Number of clusters: " + partition.numberOfClusters());
        System.out.println("Cluster sizes: " + partition.clusterSizes());

        System.out.println("\n=== All tests completed! ===");
    }
}