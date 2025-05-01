package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Concrete strategy implementing Random Walk Search algorithm.
 * This algorithm randomly chooses the next node to visit from
 * the available neighbors of the current node.
 */
public class RandomWalkStrategy implements SearchStrategy {
    // The graph to search in
    private Graph<String, DefaultEdge> graph;

    // Random number generator for selecting neighbors
    private Random random;

    // Maximum number of steps to prevent infinite loops
    private static final int MAX_STEPS = 1000;

    /**
     * Constructor that initializes the Random Walk strategy with a graph
     *
     * @param graph the graph to search
     */
    public RandomWalkStrategy(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
        this.random = new Random();
    }

    @Override
    public Path findPath(String src, String dst) {
        // Validate input nodes
        validateNodes(src, dst);

        // If source and destination are the same, return a path with just this node
        if (src.equals(dst)) {
            return new Path(src);
        }

        // Set to track visited nodes (to detect cycles)
        Set<String> visited = new HashSet<>();

        // List to store the current path
        List<String> currentPath = new ArrayList<>();
        currentPath.add(src);

        // Current node in the walk
        String current = src;
        visited.add(current);

        // Counter to prevent infinite loops
        int steps = 0;

        while (steps < MAX_STEPS) {
            // Check if we've reached the destination
            if (current.equals(dst)) {
                return new Path(currentPath);
            }

            // Get all outgoing edges from current node
            List<DefaultEdge> outgoingEdges = new ArrayList<>(graph.outgoingEdgesOf(current));

            // If there are no outgoing edges, we've reached a dead end
            if (outgoingEdges.isEmpty()) {
                // Start over from the source with a clean path
                current = src;
                currentPath.clear();
                currentPath.add(src);
                visited.clear();
                visited.add(src);
                continue;
            }

            // Select a random outgoing edge
            DefaultEdge randomEdge = outgoingEdges.get(random.nextInt(outgoingEdges.size()));
            String nextNode = graph.getEdgeTarget(randomEdge);

            // Add the next node to our path
            current = nextNode;
            currentPath.add(current);
            visited.add(current);

            // Increment the step counter
            steps++;

            // If we've visited all nodes in the graph and still haven't found the destination,
            // we might be stuck in a cycle - let's restart
            if (visited.size() >= graph.vertexSet().size()) {
                // Start over from the source with a clean path
                current = src;
                currentPath.clear();
                currentPath.add(src);
                visited.clear();
                visited.add(src);
            }
        }

        // If we've exceeded the maximum number of steps without finding the destination,
        // return null to indicate no path was found
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
}
