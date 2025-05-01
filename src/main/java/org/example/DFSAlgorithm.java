package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Concrete implementation of Depth-First Search algorithm
 * using the Template Method pattern.
 */
public class DFSAlgorithm extends GraphSearchAlgorithm {
    // Stack for iterative DFS traversal
    private Deque<String> stack;

    // Map to track parent nodes for path reconstruction
    private Map<String, String> parentMap;

    /**
     * Constructor that initializes the DFS algorithm with a graph
     *
     * @param graph the graph to search
     */
    public DFSAlgorithm(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected void initializeSearch(String src) {
        stack = new ArrayDeque<>();
        parentMap = new HashMap<>();
        visited.clear();

        // Add source node to the stack
        stack.push(src);
    }

    @Override
    protected boolean hasNodesToExplore() {
        return !stack.isEmpty();
    }

    @Override
    protected String getNextNode() {
        return stack.pop();
    }

    @Override
    protected void processNeighbor(String current, String neighbor) {
        // Record the parent for path reconstruction
        parentMap.put(neighbor, current);

        // Add to stack for DFS traversal
        stack.push(neighbor);
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