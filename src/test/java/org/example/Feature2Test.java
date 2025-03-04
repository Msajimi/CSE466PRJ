package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite specifically for Feature 2: Adding nodes
 */
public class Feature2Test {

    private DOTGraph dotGraph;

    @BeforeEach
    public void setUp() {
        // Create a new DOTGraph instance for each test
        dotGraph = new DOTGraph();
    }

    /**
     * Test for adding a single node
     */
    @Test
    public void testAddSingleNode() throws IOException {
        // Add a new node
        boolean result = dotGraph.addNode("Node1");

        // Verify node was added successfully
        assertTrue(result, "Adding new node should succeed");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addSingleNodeEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding a duplicate node
     */
    @Test
    public void testAddDuplicateNode() throws IOException {
        // Add an initial node
        dotGraph.addNode("DuplicateNode");

        // Try to add a duplicate node
        boolean result = dotGraph.addNode("DuplicateNode");

        // Verify adding duplicate failed
        assertFalse(result, "Adding duplicate node should fail");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addDuplicateNodeEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding multiple nodes
     */
    @Test
    public void testAddMultipleNodes() throws IOException {
        // Add multiple nodes using addNodes method
        String[] nodesToAdd = {"MultiNode1", "MultiNode2", "MultiNode3"};
        int result = dotGraph.addNodes(nodesToAdd);

        // Verify all nodes were added
        assertEquals(3, result, "All three nodes should be added successfully");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addMultipleNodesEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }

    /**
     * Test for adding mixed nodes (new and duplicate)
     */
    @Test
    public void testAddMixedNodes() throws IOException {
        // First add some initial nodes
        dotGraph.addNode("ExistingNode1");
        dotGraph.addNode("ExistingNode2");

        // Then try to add a mix of new and duplicate nodes
        String[] mixedNodes = {"ExistingNode1", "NewNode1", "ExistingNode2", "NewNode2"};
        int result = dotGraph.addNodes(mixedNodes);

        // Verify only the new nodes were added (should be 2)
        assertEquals(2, result, "Only 2 new nodes should be added successfully");

        // Get the string representation
        String output = dotGraph.toString().replace("\r\n", "\n");

        // Read expected output file
        String expectedOutput = new String(Files.readAllBytes(Paths.get("addMixedNodesEO.txt")))
                .replace("\r\n", "\n");

        // Compare actual output with expected output
        assertEquals(expectedOutput.trim(), output.trim(), "Graph output should match expected output");
    }
}