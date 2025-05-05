package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Concrete implementation of Breadth-First Search algorithm
 * using the Template Method pattern.
 */
public class BFSAlgorithm extends GraphSearchAlgorithm {
    // Queue for BFS traversal
    private Queue<String> queue;

    // Map to track parent nodes for path reconstruction
    private Map<String, String> parentMap;

    /**
     * Constructor that initializes the BFS algorithm with a graph
     *
     * @param graph the graph to search
     */
    public BFSAlgorithm(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected void initializeSearch(String src) {
        queue = new LinkedList<>();
        parentMap = new HashMap<>();
        visited.clear();

        // Add source node to the queue
        queue.add(src);
    }

    @Override
    protected boolean hasNodesToExplore() {
        return !queue.isEmpty();
    }

    @Override
    protected String getNextNode() {
        return queue.poll();
    }

    @Override
    protected void processNeighbor(String current, String neighbor) {
        // Record the parent for path reconstruction
        parentMap.put(neighbor, current);

        // Add to queue for BFS traversal
        queue.add(neighbor);
    }

    @Override
    protected Path buildPath(String src, String dst) {
        // Create a list to store the path in reverse order
        List<String> pathNodes = new ArrayList<>();

        // Start from the destination
        String current = dst;

        // Work backwards to the source
        while (current != null) {
            pathNodes.add(current);
            current = parentMap.get(current);
        }

        // Reverse the path to get the correct order
        Collections.reverse(pathNodes);

        // Create and return the path
        return new Path(pathNodes);
    }
}