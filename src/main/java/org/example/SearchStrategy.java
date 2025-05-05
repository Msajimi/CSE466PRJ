package org.example;

/**
 * Strategy interface for graph search algorithms.
 * Defines the common contract that all search algorithms must fulfill.
 */
public interface SearchStrategy {

    /**
     * Execute a search from source to destination
     *
     * @param src the source node
     * @param dst the destination node
     * @return a Path object representing the path if found, null otherwise
     */
    Path findPath(String src, String dst);
}
