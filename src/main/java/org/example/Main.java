package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * Main class to test the DOTGraph implementation
 */
public class Main {
    /**
     * Main method for testing the DOTGraph class
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Test file path - replace with an actual path to test
        String testFilePath = "test.dot";
        String outputFilePath = "output.txt";

        // Create a new DOTGraph instance
        DOTGraph dotGraph = new DOTGraph();

        // Parse the graph
        System.out.println("Parsing graph from: " + testFilePath);
        boolean success = dotGraph.parseGraph(testFilePath);

        if (success) {
            // Print graph information
            System.out.println("\nGraph parsed successfully!");
            System.out.println(dotGraph);

            // Test Feature 2: Adding nodes
            System.out.println("\n==== Testing Feature 2: Adding Nodes ====");

            // Test adding a single node
            System.out.println("\nAdding a single node:");
            dotGraph.addNode("NewNode1");

            // Test adding a duplicate node (should fail)
            System.out.println("\nAttempting to add a duplicate node:");
            dotGraph.addNode("NewNode1");

            // Test adding multiple nodes
            System.out.println("\nAdding multiple nodes:");
            String[] nodesToAdd = {"NewNode2", "NewNode3", "NewNode4"};
            int nodesAdded = dotGraph.addNodes(nodesToAdd);
            System.out.println("Successfully added " + nodesAdded + " nodes out of " + nodesToAdd.length);

            // Test adding a mix of new and duplicate nodes
            System.out.println("\nAdding a mix of new and duplicate nodes:");
            String[] mixedNodes = {"NewNode1", "NewNode5", "NewNode3", "NewNode6"};
            nodesAdded = dotGraph.addNodes(mixedNodes);
            System.out.println("Successfully added " + nodesAdded + " nodes out of " + mixedNodes.length);

            // Print updated graph information
            System.out.println("\nUpdated graph information:");
            System.out.println(dotGraph);

            // Output to a file
            System.out.println("Writing graph information to: " + outputFilePath);
            dotGraph.outputGraph(outputFilePath);

            System.out.println("Done!");
        } else {
            System.out.println("Failed to parse the graph.");
        }
    }
}