package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the feature 5: removing nodes and edges in DOTGraph
 */
public class Feature5Test {

    private DOTGraph graph;

    @BeforeEach
    public void setUp() {
        // Create a new graph for each test
        graph = new DOTGraph();

        // Add some nodes
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");

        // Add some edges
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "A");
    }

    /**
     * Scenario 1: Successfully removing nodes and edges
     */
    @Test
    public void testSuccessfulRemoval() {
        // Test removing an edge
        assertTrue(graph.removeEdge("A", "B"));
        assertEquals(3, graph.getEdgeCount());

        // Test removing a node
        assertTrue(graph.removeNode("D"));
        assertEquals(3, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount()); // Should remove edges to/from D

        // Test removing multiple nodes
        assertEquals(2, graph.removeNodes(new String[]{"B", "C"}));
        assertEquals(1, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount()); // Should remove all remaining edges
    }

    /**
     * Scenario 2: Attempting to remove nodes that don't exist
     */
    @Test
    public void testRemoveNonExistentNode() {
        // Test removing a non-existent node
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.removeNode("X");
        });

        assertTrue(exception.getMessage().contains("does not exist"));

        // Test removing an array with some non-existent nodes
        exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.removeNodes(new String[]{"A", "X", "Y"});
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    /**
     * Scenario 3: Attempting to remove edges that don't exist
     */
    @Test
    public void testRemoveNonExistentEdge() {
        // Test removing a non-existent edge between existing nodes
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.removeEdge("A", "C"); // A is not directly connected to C
        });

        assertTrue(exception.getMessage().contains("does not exist"));

        // Test removing an edge with a non-existent source node
        exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.removeEdge("X", "A");
        });

        assertTrue(exception.getMessage().contains("does not exist"));

        // Test removing an edge with a non-existent target node
        exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.removeEdge("A", "X");
        });

        assertTrue(exception.getMessage().contains("does not exist"));
    }
}