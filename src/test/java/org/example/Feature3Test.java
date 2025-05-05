package org.example;

import org.example.DOTGraphException;
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
        try {
            dotGraph.addEdge("SourceNode", "DestNode");
            // No exception means it succeeded
        } catch (DOTGraphException e) {
            fail("Adding edge between existing nodes should succeed, but got: " + e.getMessage());
        }

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
        try {
            dotGraph.addEdge("NodeA", "NodeB");
            assertThrows(DOTGraphException.class, () -> {
                dotGraph.addEdge("NodeA", "NodeB");
            }, "Adding duplicate edge should throw DOTGraphException");
        } catch (DOTGraphException e) {
            fail("First edge addition should succeed, but got: " + e.getMessage());
        }

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
        assertThrows(DOTGraphException.class, () -> {
            dotGraph.addEdge("NonExistentSource", "ExistingDest");
        }, "Adding edge with non-existent source should throw DOTGraphException");

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
        assertThrows(DOTGraphException.class, () -> {
            dotGraph.addEdge("ExistingSource", "NonExistentDest");
        }, "Adding edge with non-existent destination should throw DOTGraphException");

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
        try {
            dotGraph.addEdge("Node1", "Node2");
            dotGraph.addEdge("Node2", "Node3");
            // If we get here, both additions succeeded
        } catch (DOTGraphException e) {
            fail("Both edges should be added successfully, but got: " + e.getMessage());
        }
        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addMultipleEdgesEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }
}