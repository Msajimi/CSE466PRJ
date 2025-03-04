package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite specifically for Feature 3: Adding edges
 */
public class Feature3Test {

    private DOTGraph dotGraph;

    @BeforeEach
    public void setUp() {
        // Create a new DOTGraph instance for each test
        dotGraph = new DOTGraph();
    }

    /**
     * Test for adding a valid edge between existing nodes
     */
    @Test
    public void testAddValidEdge() throws IOException {
        // Set up the graph with two nodes
        dotGraph.addNode("SourceNode");
        dotGraph.addNode("DestNode");

        // Add an edge between them
        boolean result = dotGraph.addEdge("SourceNode", "DestNode");

        // Verify edge was added successfully
        assertTrue(result, "Adding edge between existing nodes should succeed");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addValidEdgeEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding a duplicate edge
     */
    @Test
    public void testAddDuplicateEdge() throws IOException {
        // Set up the graph with two nodes
        dotGraph.addNode("NodeA");
        dotGraph.addNode("NodeB");

        // Add an edge between them
        dotGraph.addEdge("NodeA", "NodeB");

        // Try to add the same edge again
        boolean result = dotGraph.addEdge("NodeA", "NodeB");

        // Verify adding duplicate edge failed
        assertFalse(result, "Adding duplicate edge should fail");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addDuplicateEdgeEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding an edge with a non-existent source node
     */
    @Test
    public void testAddEdgeNonExistentSource() throws IOException {
        // Set up the graph with only the destination node
        dotGraph.addNode("ExistingDest");

        // Try to add an edge with non-existent source
        boolean result = dotGraph.addEdge("NonExistentSource", "ExistingDest");

        // Verify adding edge failed
        assertFalse(result, "Adding edge with non-existent source should fail");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addEdgeNonExistentSourceEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding an edge with a non-existent destination node
     */
    @Test
    public void testAddEdgeNonExistentDest() throws IOException {
        // Set up the graph with only the source node
        dotGraph.addNode("ExistingSource");

        // Try to add an edge with non-existent destination
        boolean result = dotGraph.addEdge("ExistingSource", "NonExistentDest");

        // Verify adding edge failed
        assertFalse(result, "Adding edge with non-existent destination should fail");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addEdgeNonExistentDestEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding multiple edges to create a path
     */
    @Test
    public void testAddMultipleEdges() throws IOException {
        // Set up the graph with multiple nodes
        dotGraph.addNode("Node1");
        dotGraph.addNode("Node2");
        dotGraph.addNode("Node3");

        // Add edges to create a path
        boolean result1 = dotGraph.addEdge("Node1", "Node2");
        boolean result2 = dotGraph.addEdge("Node2", "Node3");

        // Verify both edges were added successfully
        assertTrue(result1 && result2, "Both edges should be added successfully");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addMultipleEdgesEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }
}