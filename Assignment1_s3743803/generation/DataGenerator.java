package generation;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class DataGenerator {

    private ArrayList<Integer> nodeLabels = new ArrayList<>();
    private Scanner input = new Scanner(System.in);
    private PrintWriter output;
    private double noNodes;


    //Generates a ArrayList of integers 1...10^n & randomly shuffles them (These are nodeLabels)
    public void generateData()
    {
        System.out.println("Enter complexity (10^[NumberEntered] nodes to be generated)");
        int power = input.nextInt();

        noNodes = Math.pow(10, power) - 1;

        for(int i = 0; i < noNodes; i++)
        {
            nodeLabels.add(i);
        }
       Collections.shuffle(nodeLabels);
    }

//Initial set-up to construct the output of the tree
    public void generateTestIn()
    {
        //Constructing tree from nodeLabels
        Node root = null;
        root = constructTree(nodeLabels, root, 0);

        //Outputing the expected output to tree.txt
        try {
            output = new PrintWriter("tree.txt");

            output.println(root.nodeLabel);
            generateTestLoop(output,root);
        } catch (FileNotFoundException e){
            e.getMessage();
        }
        output.close();
    }

    //Loop to output the correct SP commands for the already constructed tree
    public void generateTestLoop(PrintWriter output, Node root)
    {
        if (root != null && (root.left != null && root.right != null)) {
            output.print(root.nodeLabel + " " + root.left.nodeLabel + " " + root.right.nodeLabel + "\n");
            generateTestLoop(output, root.left);
            generateTestLoop(output, root.right);
        }
    }


    //Takes the nodeLabels Arraylist & uses it to generate a Binary Search Tree
    public Node constructTree(ArrayList<Integer> nodeLabels, Node root, int i)
    {
        // Base case for recursion
        if (i < nodeLabels.size()) {
            Node temp = new Node(nodeLabels.get(i));
            root = temp;

            // insert left child
            root.left = constructTree(nodeLabels, root.left,
                    2 * i + 1);

            // insert right child
            root.right = constructTree(nodeLabels, root.right,
                    2 * i + 2);
        }
        return root;
    }


    //Node Class
    public class Node<T> extends Object {
        private T nodeLabel;
        private Node<T> left, right, parent;

        public Node(T nodeLabel){
            this.nodeLabel = nodeLabel;
            left = null;
            right = null;
        }
    }
}
