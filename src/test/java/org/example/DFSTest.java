package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DFS-based GraphSearch functionality in DOTGraph
 */
public class DFSTest {

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
        graph.addNode("E");
        graph.addNode("F");

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "D");
        graph.addEdge("D", "E");
        graph.addEdge("E", "F");
        graph.addEdge("C", "F");
    }

    /**
     * Test finding a direct path between adjacent nodes
     */
    @Test
    public void testDirectPath() {
        Path path = graph.graphSearch("A", "B");
        assertNotNull(path);
        assertEquals(2, path.length());
        assertEquals("A -> B", path.toString());
    }

    /**
     * Test finding a path requiring multiple steps
     */
    @Test
    public void testMultiStepPath() {
        Path path = graph.graphSearch("A", "F");
        assertNotNull(path);

        // DFS will find a path, but it depends on the traversal order
        // Unlike BFS, DFS doesn't guarantee the shortest path
        assertTrue(path.toString().equals("A -> B -> C -> F") ||
                path.toString().equals("A -> D -> E -> F"));
    }

    /**
     * Test finding a path to the same node
     */
    @Test
    public void testSameNodePath() {
        Path path = graph.graphSearch("A", "A");
        assertNotNull(path);
        assertEquals(1, path.length());
        assertEquals("A", path.toString());
    }

    /**
     * Test trying to find a path when there isn't one
     */
    @Test
    public void testNoPath() {
        // Add an unreachable node
        graph.addNode("Z");

        Path path = graph.graphSearch("A", "Z");
        assertNull(path);
    }

    /**
     * Test that an exception is thrown for non-existent nodes
     */
    @Test
    public void testNonExistentNodes() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.graphSearch("X", "A");
        });
        assertTrue(exception.getMessage().contains("does not exist"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.graphSearch("A", "X");
        });
        assertTrue(exception.getMessage().contains("does not exist"));
    }

    /**
     * Test a more complex graph with cycles
     */
    @Test
    public void testGraphWithCycles() {
        // Add a cycle
        graph.addEdge("F", "A");

        Path path = graph.graphSearch("A", "F");
        assertNotNull(path);

        // With DFS, the path found might take any valid route
        // Since we added many possible paths
        assertTrue(path.getNodes().contains("A"));
        assertTrue(path.getNodes().contains("F"));
        assertTrue(path.getLastNode().equals("F"));
    }
}
