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

    @Override
    public void splitNode(T srcLabel, T leftChild, T rightChild) {

        LNode<T> parentNode = getNode(srcLabel, root);

        if(parentNode != null)
        {
            if(parentNode.left == null && parentNode.right == null)
            {
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

    @Override
    public boolean findNode(T nodeLabel) {
        boolean found = false;

        if(getNode(nodeLabel, root) != null)
        {
            found = true;
        }

        return found;
    } // end of findNode

    @Override
    public String findParent(T nodeLabel) {

        String output = "";
        LNode<T> child = getNode(nodeLabel, root);
        LNode<T> parent;

        if(child != null)
        {
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

        if(parent != null)
        {
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

    public LNode<T> getNode(T nodeLabel, LNode<T> root)
    {
        LNode<T> node;
        if(root == null)
        {
            return null;
        }

        if(root.nodeLabel.equals(nodeLabel))
        {
            return root;
        }

        node = getNode(nodeLabel, root.left);
        if(node != null)
        {
            return node;
        }

        node = getNode(nodeLabel, root.right);
        if(node != null)
        {
            return  node;
        }

        return null;
    }

    public LNode<T> findParent(T nodeLabel, LNode<T> root)
    {
        //TODO FIXED THIS
        if(nodeLabel == null)
        {
            return null;
        }

        boolean compare = root.nodeLabel.equals(nodeLabel);

        if(root.left != null)
        {
            if(root.left.nodeLabel.equals(nodeLabel))
            {
                compare = true;
            }
        }

        if(!compare)
        {
            return findParent(nodeLabel, root.left);
        }

        if(root.right != null)
        {
            if(root.right.nodeLabel.equals(nodeLabel))
            {
                compare = true;
            }
        }

        if(compare)
        {
            return findParent(nodeLabel, root.right);
        }
        return null;
    }


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

} // end of class LinkedRepresentation
