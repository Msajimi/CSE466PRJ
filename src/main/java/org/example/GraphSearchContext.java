package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Context class that uses a search strategy to perform graph searches.
 * This class follows the Strategy pattern, allowing different search
 * algorithms to be used interchangeably.
 */
public class GraphSearchContext {
    private SearchStrategy strategy;
    private Graph<String, DefaultEdge> graph;

    /**
     * Constructor that initializes the context with a graph
     *
     * @param graph the graph to search
     */
    public GraphSearchContext(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
    }

    /**
     * Sets the search strategy to use
     *
     * @param algorithm the algorithm type to use
     */
    public void setStrategy(Algorithm algorithm) {
        this.strategy = SearchStrategyFactory.createStrategy(graph, algorithm);
    }

    /**
     * Finds a path from source to destination using the current strategy
     *
     * @param src the source node
     * @param dst the destination node
     * @return a Path object representing the path if found, null otherwise
     */
    public Path findPath(String src, String dst) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set. Call setStrategy() first.");
        }

        return strategy.findPath(src, dst);
    }

    /**
     * Convenience method to set strategy and find path in one call
     *
     * @param src the source node
     * @param dst the destination node
     * @param algorithm the algorithm to use
     * @return a Path object representing the path if found, null otherwise
     */
    public Path findPath(String src, String dst, Algorithm algorithm) {
        setStrategy(algorithm);
        return findPath(src, dst);
    }
}
