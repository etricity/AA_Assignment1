import java.io.PrintWriter;


/**
 * Linked Tree Representation implementation for the {@link BSPTree} interface.
 * <p>
 * Your task is to complete the implementation of this class.
 * You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016. 
 * @author Yongli Ren, 2019.
 */
public class LinkedRepresentation<T> implements BSPTree<T> {


    LNode<T> root;
    int noNodes;

    /**
     * Constructs empty tree.
     */
    public LinkedRepresentation() {
        root = null;
        noNodes = 0;

    } // end of LinkedRepresentation()

    @Override
    public void setRootNode(T nodeLabel) {
        if(root == null)
        {
            root = new LNode<>(nodeLabel);
        }
        else
        {
            System.out.println("Root node already exists");
        }

    } // end of setRootNode()

    /*Uses left & right references of node to set children
    * Also sets parent references of children nodes
    */
    @Override
    public void splitNode(T srcLabel, T leftChild, T rightChild) {

        LNode<T> parentNode = getNode(srcLabel, root);

        //IF node exists
        if(parentNode != null)
        {
            //IF NODE had no children
            if(parentNode.left == null && parentNode.right == null)
            {
                //Setting children & their parent references to parentNode
                parentNode.left = new LNode<T>(leftChild);
                parentNode.left.parent = parentNode;
                parentNode.right = new LNode<>(rightChild);
                parentNode.right.parent = parentNode;

            }
            else
            {
                System.out.println("Node already has children");
            }
        }
        else
        {
            System.out.println("Node does not exists");
        }


    } // end of splitNode

    /*
        I don't practically use this method, instead it calls getNode(T nodeLabel, LNode root)
        *** That method uses recurion to see if the node exists in the tree
     */
    @Override
    public boolean findNode(T nodeLabel) {
        boolean found = false;

        if(getNode(nodeLabel, root) != null)
        {
            found = true;
        }

        return found;
    } // end of findNode


    /*Uses parent reference of given node to find the parent
     */
    @Override
    public String findParent(T nodeLabel) {

        String output = "";
        LNode<T> child = getNode(nodeLabel, root);
        LNode<T> parent;

        //IF node exists
        if(child != null)
        {
            //IF child has a parent
            parent = child.parent;
            if(parent!= null)
            {
                output = child.nodeLabel + " " + parent.nodeLabel;
            }
            else
            {
                output = "No Parent Node exists";
            }
        }
        else
        {
            output = "Node does not exists";
        }


        return output;
    } // end of findParent

    @Override
    public String findChildren(T nodeLabel) {

        String output = "";
        LNode<T> parent = getNode(nodeLabel, root);

        //IF node exists
        if(parent != null)
        {
            //IF node has children
            if(parent.left != null && parent.right != null)
            {
                output = parent.nodeLabel + " " + parent.left.nodeLabel + " " + parent.right.nodeLabel;
            }
            else
            {
                output = "Node has no children";
            }
        }
        else
        {
            output = "Node does not exists";
        }

        return output;
    } // end of findParent

    /*
    The transversal methods provided simply call my transveral methods below
    My methods provide a parameter to the root node
     */
    @Override
    public void printInPreorder(PrintWriter writer) {
        preOrder(writer, root);
    } // end of printInPreorder

    @Override
    public void printInInorder(PrintWriter writer) {
        inOrder(writer, root);
    } // end of printInInorder

    @Override
    public void printInPostorder(PrintWriter writer) {
        postOrder(writer, root);
    } // end of printInPostorder


//The trasversal method updates root node each time it is called
    public LNode<T> getNode(T nodeLabel, LNode<T> root)
    {
        //IF tree is empty
        LNode<T> node;
        if(root == null)
        {
            return null;
        }

        //IF currentNode is sought after node
        if(root.nodeLabel.equals(nodeLabel))
        {
            return root;
        }

        //Checks the left side of root node
        node = getNode(nodeLabel, root.left);
        if(node != null)
        {
            return node;
        }

        //Checks the left right of root node
        node = getNode(nodeLabel, root.right);
        if(node != null)
        {
            return  node;
        }

        return null;
    }


    /*
     * My Transversal methods (preOrder, inOrder, postOrder)
     * @param    root node
     */

    public void preOrder(PrintWriter writer, LNode<T> root)
    {
        if(root == null)
        {
            return;
        }

        writer.println(root.nodeLabel);
        preOrder(writer, root.left);
        preOrder(writer, root.right);
    }

    public void inOrder(PrintWriter writer, LNode<T> root)
    {
        if(root == null)
        {
            return;
        }
        inOrder(writer, root.left);
        writer.println(root.nodeLabel);
        inOrder(writer, root.right);
    }

    public void postOrder(PrintWriter writer, LNode<T> root)
    {
        if(root == null)
        {
            return;
        }
        postOrder(writer, root.left);
        writer.println(root.nodeLabel);
        postOrder(writer, root.right);
    }

    /*Linked Node Inner Class
    This class holds info containing its value
    AND references to its left, right & parent nodes
     */
    public class LNode<T> extends Object {
        private T nodeLabel;
        private LNode<T> left, right, parent;

        public LNode(T nodeLabel){
            this.nodeLabel = nodeLabel;
            parent = null;
            left = null;
            right = null;
        }
    }

} // end of class LinkedRepresentation
