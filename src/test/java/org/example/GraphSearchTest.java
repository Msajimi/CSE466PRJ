package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GraphSearch functionality in DOTGraph
 */
public class GraphSearchTest {

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
     * Test BFS direct path between adjacent nodes
     */
    @Test
    public void testBFSDirectPath() {
        Path path = graph.graphSearch("A", "B", Algorithm.BFS);
        assertNotNull(path);
        assertEquals(2, path.length());
        assertEquals("A -> B", path.toString());
    }

    /**
     * Test DFS direct path between adjacent nodes
     */
    @Test
    public void testDFSDirectPath() {
        Path path = graph.graphSearch("A", "B", Algorithm.DFS);
        assertNotNull(path);
        assertEquals(2, path.length());
        assertEquals("A -> B", path.toString());
    }

    /**
     * Test BFS multi-step path (should find shortest)
     */
    @Test
    public void testBFSMultiStepPath() {
        Path path = graph.graphSearch("A", "F", Algorithm.BFS);
        assertNotNull(path);

        // For this graph, either path has the same length
        // but BFS will prefer breadth-first ordering
        String pathStr = path.toString();
        assertTrue(pathStr.equals("A -> D -> E -> F") || pathStr.equals("A -> B -> C -> F"));
        assertEquals(4, path.length());
    }

    /**
     * Test DFS multi-step path
     */
    @Test
    public void testDFSMultiStepPath() {
        Path path = graph.graphSearch("A", "F", Algorithm.DFS);
        assertNotNull(path);

        // DFS will find a path, but it depends on the traversal order
        String pathStr = path.toString();
        assertTrue(pathStr.equals("A -> B -> C -> F") || pathStr.equals("A -> D -> E -> F"));
        assertEquals(4, path.length());
    }

    /**
     * Test finding a path to the same node
     */
    @Test
    public void testSameNodePath() {
        // Test with both algorithms
        Path bfsPath = graph.graphSearch("A", "A", Algorithm.BFS);
        Path dfsPath = graph.graphSearch("A", "A", Algorithm.DFS);

        assertNotNull(bfsPath);
        assertNotNull(dfsPath);
        assertEquals(1, bfsPath.length());
        assertEquals(1, dfsPath.length());
        assertEquals("A", bfsPath.toString());
        assertEquals("A", dfsPath.toString());
    }

    /**
     * Test trying to find a path when there isn't one
     */
    @Test
    public void testNoPath() {
        // Add an unreachable node
        graph.addNode("Z");

        Path bfsPath = graph.graphSearch("A", "Z", Algorithm.BFS);
        Path dfsPath = graph.graphSearch("A", "Z", Algorithm.DFS);

        assertNull(bfsPath);
        assertNull(dfsPath);
    }

    /**
     * Test that an exception is thrown for non-existent nodes
     */
    @Test
    public void testNonExistentNodes() {
        // Test for both algorithms
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.graphSearch("X", "A", Algorithm.BFS);
        });
        assertTrue(exception.getMessage().contains("does not exist"));

        exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.graphSearch("A", "X", Algorithm.DFS);
        });
        assertTrue(exception.getMessage().contains("does not exist"));
    }
}