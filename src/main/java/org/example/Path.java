package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path between nodes in a graph
 */
public class Path {
    private List<String> nodes;

    /**
     * Creates an empty path
     */
    public Path() {
        nodes = new ArrayList<>();
    }

    /**
     * Creates a path with a single node
     *
     * @param startNode the initial node in the path
     */
    public Path(String startNode) {
        nodes = new ArrayList<>();
        nodes.add(startNode);
    }

    /**
     * Creates a path from a list of nodes
     *
     * @param nodes the list of nodes in the path
     */
    public Path(List<String> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    /**
     * Create a copy of an existing path
     *
     * @param other the path to copy
     */
    public Path(Path other) {
        this.nodes = new ArrayList<>(other.nodes);
    }

    /**
     * Adds a node to the end of the path
     *
     * @param node the node to add
     */
    public void addNode(String node) {
        nodes.add(node);
    }

    /**
     * Gets all nodes in the path
     *
     * @return a list of nodes in order
     */
    public List<String> getNodes() {
        return new ArrayList<>(nodes);
    }

    /**
     * Gets the last node in the path
     *
     * @return the last node
     */
    public String getLastNode() {
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.get(nodes.size() - 1);
    }

    /**
     * Gets the number of nodes in the path
     *
     * @return the path length
     */
    public int length() {
        return nodes.size();
    }

    /**
     * Checks if the path is empty
     *
     * @return true if the path has no nodes
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public String toString() {
        if (nodes.isEmpty()) {
            return "Empty path";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i));
            if (i < nodes.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}