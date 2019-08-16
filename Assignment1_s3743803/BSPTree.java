import java.io.PrintWriter;

/**
 * Binary Space Partitioning Tree interface.
 * <p>
 * Note, you should <b>not</b> need to modify this.
 *
 * @author Jeffrey Chan, 2016.
 * @author Yongli Ren, 2019.
 */
public interface BSPTree<T> {

    String EMPTY_NODE = "";

    /**
     * Sets a root node to the tree.
     * If root node has been already set in the tree, no changes are made,
     * but a warning to {@link System#err} should be issued.
     *
     * @param nodeLabel the node to add.
     */
    void setRootNode(T nodeLabel);

    /**
     * Splits a node into two nodes.
     * If the node doesn't exists in the tree or children nodes are {@code null},
     * no changes are made, but a warning to {@link System#err} should be issued.
     *
     * @param srcLabel   the node to split.
     * @param leftChild  the label of left child, cannot be {@code null}.
     * @param rightChild the label of right child, cannot be {@code null}.
     */
    void splitNode(T srcLabel, T leftChild, T rightChild);

    /**
     * Returns {@code true} if the specified node exists in the tree;
     * and {@code false} otherwise.
     *
     * @param nodeLabel the node to look for.
     * @return {@code true} if the specified node exists in the tree;
     * and {@code false} otherwise
     */
    boolean findNode(T nodeLabel);

    /**
     * Finds the parent node of the specified node.
     * If the node doesn't exists in the tree, nothing to print,
     * but a warning to {@link System#err} should be issued.
     *
     * @param nodeLabel the node to look for
     * @return the result taking the form: {@code <nodeLabel parentLabel>};
     * returns {@code <nodeLabel >} if no parent node exists in the tree.
     */
    String findParent(T nodeLabel);

    /**
     * Finds the children nodes of the specified node.
     * If the given node doesn't exists in the tree, nothing to print,
     * but a warning to {@link System#err} should be issued.
     *
     * @param nodeLabel the node to look for
     * @return the result taking the form: {@code <nodeLabel leftChild rightChild>};
     * returns {@code <nodeLabel >} if no children node exists in the tree.
     */
    String findChildren(T nodeLabel);

    /**
     * Prints the tree in preorder traversal to {@link PrintWriter}
     *
     * @param writer {@link PrintWriter} to print to.
     */
    void printInPreorder(PrintWriter writer);

    /**
     * Prints the tree in inorder traversal to {@link PrintWriter}
     *
     * @param writer {@link PrintWriter} to print to.
     */
    void printInInorder(PrintWriter writer);

    /**
     * Prints the tree in postorder traversal to {@link PrintWriter}
     *
     * @param writer {@link PrintWriter} to print to.
     */
    void printInPostorder(PrintWriter writer);
} // end of interface BSPTree