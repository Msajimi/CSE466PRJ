package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for graph search algorithms using the Template Method pattern.
 * Defines the common structure of graph search algorithms while allowing
 * subclasses to implement specific steps differently.
 */
public abstract class GraphSearchAlgorithm {
    protected Graph<String, DefaultEdge> graph;
    protected Set<String> visited;

    /**
     * Constructor that initializes the algorithm with a graph
     *
     * @param graph the graph to search
     */
    public GraphSearchAlgorithm(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
    }

    /**
     * Template method that defines the skeleton of the search algorithm.
     * The overall structure remains the same while specific steps are
     * implemented by subclasses.
     *
     * @param src the source node
     * @param dst the destination node
     * @return the path from source to destination if found, null otherwise
     */
    public final Path search(String src, String dst) {
        // Validate input
        validateNodes(src, dst);

        // If source and destination are the same, return a path with just this node
        if (src.equals(dst)) {
            return new Path(src);
        }

        // Initialize algorithm-specific data structures
        initializeSearch(src);

        // Main search loop
        while (hasNodesToExplore()) {
            String current = getNextNode();

            // Skip if already visited (for some algorithms this check might be redundant)
            if (visited.contains(current)) continue;

            // Mark as visited
            visited.add(current);

            // Check if we've reached destination
            if (current.equals(dst)) {
                return buildPath(src, dst);
            }

            // Process all neighbors
            for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge);

                // Process unvisited neighbors
                if (!visited.contains(neighbor)) {
                    processNeighbor(current, neighbor);
                }
            }
        }

        // No path found
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
     * Initialize algorithm-specific data structures and add the source node
     */
    protected abstract void initializeSearch(String src);

    /**
     * Check if there are more nodes to explore
     */
    protected abstract boolean hasNodesToExplore();

    /**
     * Get the next node to explore based on the algorithm's traversal strategy
     */
    protected abstract String getNextNode();

    /**
     * Process a neighbor node during traversal
     */
    protected abstract void processNeighbor(String current, String neighbor);

    /**
     * Build the path from source to destination
     */
    protected abstract Path buildPath(String src, String dst);
}