package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Factory class for creating search strategy instances based on the algorithm type.
 */
public class SearchStrategyFactory {

    /**
     * Creates a search strategy instance based on the specified algorithm type
     *
     * @param graph the graph to search
     * @param algorithm the type of algorithm to create a strategy for
     * @return a SearchStrategy instance for the specified type
     * @throws IllegalArgumentException if an invalid algorithm is specified
     */
    public static SearchStrategy createStrategy(
            Graph<String, DefaultEdge> graph,
            Algorithm algorithm) {

        switch (algorithm) {
            case BFS:
                return new BFSStrategy(graph);
            case DFS:
                return new DFSStrategy(graph);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }
}
