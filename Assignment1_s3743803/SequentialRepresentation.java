import java.io.PrintWriter;

/**
 * Sequential Tree Representation implementation for the {@link BSPTree} interface.
 * <p>
 * Your task is to complete the implementation of this class.
 * You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 * @author Yongli Ren, 2019.
 */
public class SequentialRepresentation<T> implements BSPTree<T> {

    private class Node<T> extends Object {
        private T nodeLabel;

        public Node(T nodeLabel){
            this.nodeLabel = nodeLabel;
        }
    }

    private Node<T>[] binaryTreeArray;

    //This only exists to make it easier to increase size of array if needed
    private int noNodes;
    /**
     * Constructs empty graph.
     */
    public SequentialRepresentation() {
        binaryTreeArray = new Node[99];
        noNodes = 0;


    } // end of SequentialRepresentation()

    @Override
    public void setRootNode(T nodeLabel) {
        if(binaryTreeArray[0] == null)
        {
            Node<T> root = new Node<>(nodeLabel);
            binaryTreeArray[0] = root;
            noNodes++;
        } else
        {
            System.out.println("Root Node already Exists.");
        }

    } // end of setRootNode()

    @Override
    public void splitNode(T srcLabel, T leftChild, T rightChild) {
        //TODO is there a better way to insert nodes 
        if(findNode(srcLabel))
        {
           int parentIndex = getIndex(srcLabel);

           if(binaryTreeArray[(2 * parentIndex) + 1] == null && binaryTreeArray[(2 * parentIndex) + 2] == null)
           {
               if(noNodes >= binaryTreeArray.length - 1)
               {
                   binaryTreeArray = doubleArray(binaryTreeArray);
               }
               Node<T> leftC = new Node<>(leftChild);
               Node<T> rightC = new Node<>(rightChild);
               binaryTreeArray[(2 * parentIndex) + 1] = leftC;
               noNodes++;
               binaryTreeArray[(2 * parentIndex) + 2] = rightC;
               noNodes++;
           } else
           {
               System.out.println("Node already has children");
           }
        }
        else
        {
            System.out.println("Node does not exist");
        }

    } // end of splitNode

    @Override
    public boolean findNode(T nodeLabel) {
        boolean found = false;

        if(getIndex(nodeLabel) >= 0)
        {
            found = true;
        }
        return found;
    } // end of findNode

    @Override
    public String findParent(T nodeLabel) {

        String output = "";
        Node<T> parentNode;

        int childIndex = getIndex(nodeLabel);
        //IF CHILD EXISTS
        if(childIndex > 0)
        {
            //LEFT CHILD
            if(childIndex % 2 == 1)
            {
                parentNode = binaryTreeArray[(childIndex - 1) / 2];
            //RIGHT CHILD
            } else{
                parentNode = binaryTreeArray[(childIndex - 2) / 2];
            }
            output = nodeLabel + " " + parentNode.nodeLabel;
        } else{
            output = "No parent exists";
        }
        return output;
    } // end of findParent

    @Override
    public String findChildren(T nodeLabel) {
        String output = "";
        boolean hasChildren = false;

        int parentIndex = getIndex(nodeLabel);
        //IF PARENT EXISTS
        if(parentIndex >= 0)
        {
            Node<T> leftChild = binaryTreeArray[(2 * parentIndex) + 1];
            Node<T> rightChild = binaryTreeArray[(2 * parentIndex) + 2];
           // IF PARENT NODE HAS CHILDREN
            if(leftChild != null && rightChild != null)
            {
                hasChildren = true;
                output = nodeLabel + " " + leftChild.nodeLabel + " " + rightChild.nodeLabel;
            }

        }

        if(!hasChildren)
        {
            output = "No Children exists";
        }

        return output;
    } // end of findParent

    @Override
    public void printInPreorder(PrintWriter writer) {
        preOrder(writer, binaryTreeArray, 0);

    } // end of printInPreorder

    @Override
    public void printInInorder(PrintWriter writer) {
        inOrder(writer, binaryTreeArray, 0);
    } // end of printInInorder

    @Override
    public void printInPostorder(PrintWriter writer) {
        postOrder(writer, binaryTreeArray, 0);
    } // end of printInPostorder

    /*
    * Returns the index of a node in the array binaryTreeArray.
    * @param nodeLabel   the node to get the index of.
     */
    public int getIndex(T nodeLabel)
    {
        boolean endLoop = false;
        int i = 0;

        while(!endLoop && i < binaryTreeArray.length)
        {
            if(binaryTreeArray[i] != null)
            {
                if(binaryTreeArray[i].nodeLabel.equals(nodeLabel))
                {
                    endLoop = true;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

        //returns an invalid index if the element is not in the array
        if(i == binaryTreeArray.length)
        {
            i = -1;
        }
        return i;
    }

    //Prints the array (Test Purposes only) TODO DELETE FOR SUBMISSION
    public void printArray()
    {
        for(int i = 0; i < noNodes; i++)
        {
            System.out.println(binaryTreeArray[i].nodeLabel);
        }
    }

    /*
    * Duplicates array passed in with a double the size + 1
    * @param BTArray    the array to be enlarged
     */
    public Node<T>[] doubleArray(Node<T>[] BTArray)
    {
        Node<T>[] doubledArray = new Node[(noNodes * 2) + 1];

        for(int i = 0; i < noNodes; i++)
        {
            doubledArray[i] = BTArray[i];
        }

        return doubledArray;
    }

    /*
    * the actually implementation of preOrder Transversal
    * @param    root node
     */


    public void preOrder(PrintWriter writer, Node<T>[] BTArray, int root)
    {
        writer.println(BTArray[root].nodeLabel);
        if(BTArray[(2 * root) + 1] != null)
        {
            preOrder(writer, BTArray,  2* root +1);
        }
        if(BTArray[(2 * root) + 2] != null)
        {
            preOrder(writer, BTArray,  2* root +2);
        }
    }

    public void inOrder(PrintWriter writer, Node<T>[] BTArray, int root)
    {

        if(BTArray[(2 * root) + 1] != null)
        {
            inOrder(writer, BTArray,  2* root +1);
        }
        writer.println(BTArray[root].nodeLabel);
        if(BTArray[(2 * root) + 2] != null)
        {
            inOrder(writer, BTArray,  2* root +2);
        }
    }

    public void postOrder(PrintWriter writer, Node<T>[] BTArray, int root)
    {

        if(BTArray[(2 * root) + 1] != null)
        {
            postOrder(writer, BTArray,  2* root +1);
        }
        if(BTArray[(2 * root) + 2] != null)
        {
            postOrder(writer, BTArray,  2* root +2);
        }
        writer.println(BTArray[root].nodeLabel);
    }



} // end of class SequentialRepresentation