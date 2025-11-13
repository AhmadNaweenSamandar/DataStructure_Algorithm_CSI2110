// ==========================================================================
// $Id: Verify.java,v 1.1 2006/10/24 00:21:19 jlang Exp $
// CSI2110 Lab code: BST height comparison
// ==========================================================================
// (C)opyright:
//
//   Jochen Lang
//   SITE, University of Ottawa
//   800 King Edward Ave.
//   Ottawa, On., K1N 6N5
//   Canada. 
//   http://www.site.uottawa.ca
// 
// Creator: jlang (Jochen Lang)
// Email:   jlang@site.uottawa.ca
// ==========================================================================
// $Log: Verify.java,v $
// Revision 1.1  2006/10/24 00:21:19  jlang
// Added Lab 6
// Revision 1.2 2015/10/31 Lachlan Plant
// (Lachlan needs to add here the details on his update)
// ==========================================================================
import net.datastructures.AVLTreeMap;
import net.datastructures.TreeMap;
import net.datastructures.BinaryTree;
import net.datastructures.Position;
import net.datastructures.Entry;
//import net.datastructures.EulerTour;

public class Verify {
  protected int d_totalHeightAVL;
  protected int d_totalHeightBST;
  protected int d_minAVL, d_maxAVL;
  protected int d_minBST, d_maxBST;
  protected int d_noTrees;


  /**
   * Initialize internal height counters
   */
  protected void initCounters(int _maxHeight, int _noTrees) {
    d_minAVL = _maxHeight+1;
    d_maxAVL = -1;
    d_minBST = _maxHeight+1;
    d_maxBST = -1;
    d_totalHeightAVL = 0;
    d_totalHeightBST = 0;
    d_noTrees = _noTrees;
    return;
  }


  /**
   * Update internal height counters after new tree has been constructed
   */
  protected void updateCounters(int _heightAVL, int _heightBST ) {
    d_totalHeightAVL += _heightAVL;
    d_totalHeightBST += _heightBST;
    if ( _heightAVL > d_maxAVL ) d_maxAVL = _heightAVL;
    if ( _heightAVL < d_minAVL ) d_minAVL = _heightAVL;
    if ( _heightBST > d_maxBST ) d_maxBST = _heightBST;
    if ( _heightBST < d_minBST ) d_minBST = _heightBST;
    return;
  }


  /**
   * Print statistics about the trees generated
   */
  public void printStatistics() {
    System.out.printf( "%10d %10d %10.3f %10d %10d %10.3f %10.3f\n", 
		       d_minBST,
		       d_maxBST, 
		       ((double)d_totalHeightBST)/d_noTrees,
		       d_minAVL,
		       d_maxAVL, 
		       ((double)d_totalHeightAVL)/d_noTrees,
		       Math.log(d_noTrees));
  }

  /**
   * Generate _nTrees random trees each with _noNodes
   */
  public void randomTrees( int _noNodes, int _nTrees ) {
    // Initialize minimum and maximum counters
    initCounters( _noNodes, _nTrees );
    // generate _nTrees and update statistics

    // We'll use reflection to access the protected 'tree' field inside TreeMap,
    // which holds the actual BinaryTree implementation (BalanceableBinaryTree).
    Field treeField = null;
    try {
      treeField = TreeMap.class.getDeclaredField("tree");
      treeField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      System.err.println("Reflection error: cannot find 'tree' field in TreeMap.");
      e.printStackTrace();
      return;
    }

    for(int i=0; i<_nTrees; ++i) { 
      // Create AVL and BST trees using AVLTreeMap and TreeMap respectively.
      TreeMap<Integer,Integer> treeBST = new TreeMap<>();
      AVLTreeMap<Integer,Integer> treeAVL = new AVLTreeMap<>();

      // loop to insert _noNodes keys into tree
      int count = 0;
      do {
        // create random key between 0 and the maximum integer (non-negative)
        int randKey = (int) (Math.random() * Integer.MAX_VALUE);
        // make sure the generated random key is not used before 
        // by checking in the BST (both structures will be updated together)
        if ( treeBST.get(randKey) == null ) {
          // insert into AVL and BST
          treeBST.put(randKey, randKey);
          treeAVL.put(randKey, randKey);
          // next key
          ++count;
        }
      } while ( count < _noNodes );

      // obtain underlying BinaryTree instances using reflection
      try {
        @SuppressWarnings("unchecked")
        BinaryTree<Entry<Integer,Integer>> bTreeAVL = (BinaryTree<Entry<Integer,Integer>>) treeField.get(treeAVL);
        @SuppressWarnings("unchecked")
        BinaryTree<Entry<Integer,Integer>> bTreeBST = (BinaryTree<Entry<Integer,Integer>>) treeField.get(treeBST);

        // updateCounters with computed heights
        updateCounters( height(bTreeAVL), height(bTreeBST) );
      } catch (IllegalAccessException e) {
        System.err.println("Reflection error: cannot access 'tree' field.");
        e.printStackTrace();
        // fall back to zeros so program can continue
        updateCounters( 0, 0 );
      }
    }
    return;
  }


  /**
   * Adapt the Euler tour to calculate height (see Lab 4)
   * Note: BST and AVL trees are binary trees
   */
  public <T> int height( BinaryTree<T> bTree ) {
    if ( bTree == null ) return 0;
    Position<T> root = bTree.root();
    if ( root == null ) return 0;
    return heightRec(bTree, root);
  }

  // recursive helper: an external (sentinel) node has height 0,
  // internal node height = 1 + max(height(left), height(right))
  private <T> int heightRec(BinaryTree<T> bTree, Position<T> p) {
    if ( bTree.isExternal(p) ) return 0;
    Position<T> left = bTree.left(p);
    Position<T> right = bTree.right(p);
    int hl = (left == null ? 0 : heightRec(bTree, left));
    int hr = (right == null ? 0 : heightRec(bTree, right));
    return 1 + Math.max(hl, hr);
  }
  /**
   * Entry point: compare height statistics 
   * of different size BST and AVL trees
   */
  public static void main(String[] _argv) {
    Verify test = new Verify();
    int numNodesStart = 2;
    int numSteps = 8;

    if ( _argv.length > 0 ) numSteps = Integer.valueOf( _argv[0] );

    System.out.println( "Height Comparison: with and without AVL balancing" );
    System.out.printf( "%6s %10s %10s %10s %10s %10s %10s %10s\n", 
		       "N", "min", "max", "avg", 
		       "min", "max", "avg", "log(N)");

    int numNodes = numNodesStart;
    for ( int i=0; i<numSteps; ++i ) {
      // create regular BST and AVL search trees
      test.randomTrees( numNodes, Math.max( 10, numNodes<<2) );
      // print statistics
      System.out.printf( "%6s ", numNodes );
      test.printStatistics();
      numNodes <<= 1; // multiply by 2
    }
    return;
  }

}
