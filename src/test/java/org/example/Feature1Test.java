package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.example.DOTGraphException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DOTGraph implementation using provided test files
 */
public class Feature1Test {

    private DOTGraph dotGraph;

    @BeforeEach
    public void setUp() {
        // Create a new DOTGraph instance for each test
        dotGraph = new DOTGraph();
    }

    /**
     * Test for parseGraph functionality (Test Case 1.1)
     */
    @Test
    public void testParseGraphTest1() throws IOException {
        // Parse first test graph
        boolean result = dotGraph.parseGraph("parseGraphTest1.dot");

        // Verify parsing was successful
        assertTrue(result, "Parsing should succeed with valid DOT file");

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("parseGraphTest1EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for parseGraph functionality (Test Case 1.2)
     */
    @Test
    public void testParseGraphTest2() throws IOException {
        // Parse second test graph
        boolean result = dotGraph.parseGraph("parseGraphTest2.dot");

        // Verify parsing was successful
        assertTrue(result, "Parsing should succeed with valid DOT file");

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("parseGraphTest2EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for addNode functionality (Test Case 2.1)
     */
    @Test
    public void testAddNodeTest1() throws IOException {
        // Add a new node
        boolean result = dotGraph.addNode("NewNode");

        // Verify node was added successfully
        assertTrue(result, "Adding new node should succeed");

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addNodeTest1EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for addNode functionality (Test Case 2.2)
     */
    @Test
    public void testAddNodeTest2() throws IOException {
        // Add an initial node
        dotGraph.addNode("ExistingNode");

        // Try to add a duplicate node
        boolean result = dotGraph.addNode("ExistingNode");

        // Verify adding duplicate failed
        assertFalse(result, "Adding duplicate node should fail");

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addNodeTest2EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for addEdge functionality (Test Case 3.1)
     */
    @Test
    public void testAddEdgeTest1() throws IOException {
        // Add two nodes
        dotGraph.addNode("NodeA");
        dotGraph.addNode("NodeB");

        try {
            dotGraph.addEdge("NodeA", "NodeB");
            // No exception means it succeeded
        } catch (DOTGraphException e) {
            fail("Adding edge between existing nodes should succeed, but got: " + e.getMessage());
        }

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addEdgeTest1EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for addEdge functionality (Test Case 3.2)
     */
    @Test
    public void testAddEdgeTest2() throws IOException {
        // Add one node
        dotGraph.addNode("NodeA");

        assertThrows(DOTGraphException.class, () -> {
            dotGraph.addEdge("NodeA", "NonExistentNode");
        }, "Adding edge to non-existent node should throw DOTGraphException");

        // Get the string representation
        String output = dotGraph.toString();

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addEdgeTest2EO.txt")));

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

}