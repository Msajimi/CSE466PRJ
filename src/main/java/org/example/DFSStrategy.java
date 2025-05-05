package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Concrete strategy implementing Depth-First Search algorithm.
 */
public class DFSStrategy implements SearchStrategy {
    // The graph to search in
    private Graph<String, DefaultEdge> graph;

    /**
     * Constructor that initializes the DFS strategy with a graph
     *
     * @param graph the graph to search
     */
    public DFSStrategy(Graph<String, DefaultEdge> graph) {
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

        // Stack for DFS traversal
        Deque<String> stack = new ArrayDeque<>();

        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Map to track parent nodes for path reconstruction
        Map<String, String> parentMap = new HashMap<>();

        // Start DFS from the source node
        stack.push(src);

        while (!stack.isEmpty()) {
            String current = stack.pop();

            // Skip if already visited
            if (visited.contains(current)) {
                continue;
            }

            // Mark as visited
            visited.add(current);

            // Check if we've reached destination
            if (current.equals(dst)) {
                // Reconstruct and return the path
                return reconstructPath(parentMap, src, dst);
            }

            // Get all neighbors (outgoing edges from current node)
            // We need to process them in reverse order to maintain the expected DFS behavior
            List<String> neighbors = new ArrayList<>();
            for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
                neighbors.add(graph.getEdgeTarget(edge));
            }

            // Process neighbors in reverse order (to match traditional DFS behavior)
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    // Record the parent for path reconstruction
                    parentMap.put(neighbor, current);
                    // Add to stack for DFS traversal
                    stack.push(neighbor);
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
        while (current != null && !current.equals(src)) {
            pathNodes.add(current);
            current = parentMap.get(current);
        }

        // Add the source node
        if (current != null) {
            pathNodes.add(src);
        }

        // Reverse the path to get the correct order
        Collections.reverse(pathNodes);

        // Create and return the path
        return new Path(pathNodes);
    }
}
