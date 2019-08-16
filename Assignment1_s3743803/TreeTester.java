import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Framework to test the Binary Space Partitioning implementations.
 * <p>
 * There should be no need to change this for task A.
 * If you need to make changes for task B, please make a copy,
 * then modify the copy for task B.
 *
 * @author Jeffrey Chan, 2016.
 * @author Yongli Ren, 2019.
 */
public class TreeTester {

    private static final String SEQUENTIAL_TREE = "seqtree";
    private static final String LINKED_TREE = "linktree";
    private static final String SAMPLE = "sample";

    /**
     * Print help/usage message.
     */
    private static void usage() {
        String progName = TreeTester.class.getSimpleName();
        System.err.println(progName + ": <implementation> [-f <filename to load tree>] [filename to print results]");
        System.err.printf("<implementation> = <%s | %s | %s>%n", SEQUENTIAL_TREE, LINKED_TREE, SAMPLE);
        System.err.println("If the optional output filename is specified, then non-interactive mode will be used and respective output is written to that file.  Otherwise interactive mode is assumed and output is written to System.out.");
        System.exit(1);
    } // end of usage

    /**
     * Checks the number of specified tokens
     *
     * @param tokens      the given tokens
     * @param expectedNum the expected length of the given tokens
     */
    private static void verifyTokens(String[] tokens, int expectedNum) {
        if (tokens.length != expectedNum) {
            throw new IllegalArgumentException("incorrect number of tokens.");
        }
    }

    /**
     * Process the operation commands coming from inReader,
     * and updates the tree according to the operations.
     *
     * @param inReader Input reader where the operation commands are coming from.
     * @param tree     The tree which the operations are executed on.
     * @param writer   Where to output the results.
     * @throws IOException If there is an exception to do with I/O.
     */
    public static void processOperations(
            BufferedReader inReader,
            BSPTree<String> tree,
            PrintWriter writer
    ) throws IOException {
        String line;
        int lineNum = 1;
        boolean bQuit = false;

        // continue reading in commands until we either receive the quit signal
        // or there are no more input commands
        while (!bQuit && (line = inReader.readLine()) != null) {
            String[] tokens = line.split(" ");

            // check if there is at least an operation command
            if (tokens.length < 1) {
                System.err.println(lineNum + ": not enough tokens.");
                lineNum++;
                continue;
            }

            String command = tokens[0];
            try {
                // determine which operation to execute
                switch (command.toUpperCase()) {
                    // set root node
                    case "RN":
                        verifyTokens(tokens, 2);
                        tree.setRootNode(tokens[1]);
                        break;
                    // split node
                    case "SP":
                        verifyTokens(tokens, 4);
                        tree.splitNode(tokens[1], tokens[2], tokens[3]);
                        break;
                    // find node
                    case "FN":
                        verifyTokens(tokens, 2);
                        writer.println(tree.findNode(tokens[1]));
                        break;
                    // find parent node
                    case "FP":
                        verifyTokens(tokens, 2);
                        writer.println(tree.findParent(tokens[1]));
                        break;
                    // find children nodes
                    case "FC":
                        verifyTokens(tokens, 2);
                        writer.println(tree.findChildren(tokens[1]));
                        break;
                    // print all the nodes in the "preorder" traversal
                    case "TP":
                        tree.printInPreorder(writer);
                        break;
                    // print all the nodes in the "inorder" traversal
                    case "TI":
                        tree.printInInorder(writer);
                        break;
                    // print all the nodes in the "postorder" traversal
                    case "TS":
                        tree.printInPostorder(writer);
                        break;
                    // quit
                    case "Q":
                        bQuit = true;
                        break;
                    default:
                        System.err.println(lineNum + ": Unknown command.");
                } // end of switch()
            } catch (IllegalArgumentException e) {
                System.err.println(lineNum + ": " + e.getMessage());
            }
            lineNum++;
        }

    } // end of processOperations()

    /**
     * Creates new {@link PrintWriter}.
     *
     * @param filename the output filename
     * @return new {@link PrintWriter}
     * @throws IOException If there is an exception to do with I/O.
     */
    private static PrintWriter createWriter(String filename) throws IOException {
        OutputStream out = (filename == null || filename.trim().isEmpty())
                ? System.out
                : new FileOutputStream(filename);
        return new PrintWriter(out, true);
    }

    /**
     * Main method.  Determines which implementation to test and processes command line arguments.
     */
    public static void main(String[] args) {

        // parse command line options
        String fileOpt = "f";
        OptionParser parser = new OptionParser(fileOpt + ":");
        OptionSet options = parser.parse(args);

        String inputFilename = null;
        // -f <inputFilename> specifies the file that contains nodes information to construct the initial tree with.
        if (options.has(fileOpt)) {
            if (options.hasArgument(fileOpt)) {
                inputFilename = (String) options.valueOf(fileOpt);
            } else {
                System.err.printf("Missing filename argument for -%s option.\n", fileOpt);
                usage();
            }
        }

        // non option arguments
        List<String> remainArgs = options.nonOptionArguments()
                .stream().map(o -> (String) o)
                .collect(Collectors.toList());

        // check number of non-option command line arguments
        int maxArgs = 2, minArgs = 1;
        if (remainArgs.size() > maxArgs || remainArgs.size() < minArgs) {
            System.err.println("Incorrect number of arguments.");
            usage();
        }

        // parse non-option arguments
        String implementationType = remainArgs.get(0);

        String outFilename = null;

        // output files
        if (remainArgs.size() == maxArgs) {
            outFilename = remainArgs.get(1);
            System.out.println("Non-interactive mode.");
        } else {
            System.out.println("Interactive mode.");
        }


        // determine which implementation to test
        BSPTree<String> tree;
        switch (implementationType) {
            case SEQUENTIAL_TREE:
                tree = new SequentialRepresentation<>();
                break;
            case LINKED_TREE:
                tree = new LinkedRepresentation<>();
                break;
            case SAMPLE:
                tree = new SampleImplementation<>();
                break;
            default:
                System.err.println("Unknown implementation type.");
                usage();
                return;
        }


        // if file specified, then load file
        if (inputFilename != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
                String line;
                String delimiter = "[ \t,]+";
                String[] tokens;
                String srcLabel, leftChild, rightChild;
                boolean hasRoot = false;
                while ((line = reader.readLine()) != null) {
                    tokens = line.split(delimiter);
                    if (!hasRoot) {
                        verifyTokens(tokens, 1);
                        tree.setRootNode(tokens[0]);
                        hasRoot = true;
                        continue;
                    }
                    verifyTokens(tokens, 3);
                    srcLabel = tokens[0];
                    leftChild = tokens[1];
                    rightChild = tokens[2];
                    tree.splitNode(srcLabel, leftChild, rightChild);
                }
            } catch (FileNotFoundException ex) {
                System.err.println("File " + args[2] + " not found.");
            } catch (IOException ex) {
                System.err.println("Cannot open file " + args[2]);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

        // construct in and output streams/writers/readers, then process each operation.
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in))) {
            // output to writer
            PrintWriter writer = createWriter(outFilename);
            // process the operations
            processOperations(inReader, tree, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    } // end of main()

} // end of class TreeTester
