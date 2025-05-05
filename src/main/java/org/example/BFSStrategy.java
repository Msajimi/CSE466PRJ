package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Concrete strategy implementing Breadth-First Search algorithm.
 */
public class BFSStrategy implements SearchStrategy {
    // The graph to search in
    private Graph<String, DefaultEdge> graph;

    /**
     * Constructor that initializes the BFS strategy with a graph
     *
     * @param graph the graph to search
     */
    public BFSStrategy(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
    }

    @Override
    public Path findPath(String src, String dst) {
        // Validate input nodes
        validateNodes(src, dst);

        // If source and destination are the same, return a path with just this node
        if (src.equals(dst)) {
            return new Path(src);
        }

        // Queue for BFS traversal
        Queue<String> queue = new LinkedList<>();

        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Map to track parent nodes for path reconstruction
        Map<String, String> parentMap = new HashMap<>();

        // Start BFS from the source node
        queue.add(src);
        visited.add(src);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // Get all neighbors (outgoing edges from current node)
            for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge);

                // If we haven't visited this neighbor yet
                if (!visited.contains(neighbor)) {
                    // Record the parent
                    parentMap.put(neighbor, current);

                    // Check if we've reached the destination
                    if (neighbor.equals(dst)) {
                        // Reconstruct the path
                        return reconstructPath(parentMap, src, dst);
                    }

                    // Add to queue and mark as visited
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        // If we get here, no path was found
        return null;
    }

    /**
     * Validates that both source and destination nodes exist in the graph
     */
    private void validateNodes(String src, String dst) {
        if (!graph.containsVertex(src)) {
            throw new IllegalArgumentException("Error: Source node '" + src + "' does not exist.");
        }

        if (!graph.containsVertex(dst)) {
            throw new IllegalArgumentException("Error: Destination node '" + dst + "' does not exist.");
        }
    }

    /**
     * Helper method to reconstruct the path from the parent map
     *
     * @param parentMap a map of child -> parent relationships
     * @param src the source node
     * @param dst the destination node
     * @return a Path object representing the path
     */
    private Path reconstructPath(Map<String, String> parentMap, String src, String dst) {
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