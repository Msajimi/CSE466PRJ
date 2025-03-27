package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GraphSearch functionality in DOTGraph
 */
public class BFSTest {

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

        // Create a graph with multiple paths
        //    A -> B -> C
        //    |         |
        //    v         v
        //    D -> E -> F
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

        // BFS should find the shortest path, which is A -> D -> E -> F
        // (with 4 nodes) rather than A -> B -> C -> F (with 4 nodes)
        // Since both paths have the same length, either is acceptable.
        // The exact path depends on the order of edge traversal in the BFS.
        assertEquals(4, path.length());

        // The path should either be A -> D -> E -> F or A -> B -> C -> F
        String pathStr = path.toString();
        assertTrue(pathStr.equals("A -> D -> E -> F") || pathStr.equals("A -> B -> C -> F"));
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
}
