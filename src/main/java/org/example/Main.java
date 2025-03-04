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

            // Output to a file
            System.out.println("Writing graph information to: " + outputFilePath);
            dotGraph.outputGraph(outputFilePath);

            System.out.println("Done!");
        } else {
            System.out.println("Failed to parse the graph.");
        }
    }
}