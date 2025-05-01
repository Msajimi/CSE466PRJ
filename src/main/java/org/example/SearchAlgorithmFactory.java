package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Factory class for creating graph search algorithm instances.
 * This follows the Factory Method pattern to create appropriate algorithm objects.
 */
public class SearchAlgorithmFactory {

    /**
     * Creates a search algorithm instance based on the specified algorithm type
     *
     * @param graph the graph to search
     * @param algorithm the type of algorithm to create
     * @return a GraphSearchAlgorithm instance for the specified type
     * @throws IllegalArgumentException if an invalid algorithm is specified
     */
    public static GraphSearchAlgorithm createAlgorithm(
            Graph<String, DefaultEdge> graph,
            Algorithm algorithm) {

        switch (algorithm) {
            case BFS:
                return new BFSAlgorithm(graph);
            case DFS:
                return new DFSAlgorithm(graph);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }
}