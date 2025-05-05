package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

/**
 * Demonstration class for Project Part 3
 * Demonstrates file reading, BFS, DFS, and Random Walk search algorithms
 */
public class GraphSearchDemo {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("GRAPH SEARCH ALGORITHM DEMONSTRATIONS");
        System.out.println("===========================================");
        System.out.println("Using Scheme A: Print the path at each visit step");

        // 1. Graph File Reading and Initialization
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter the path to the graph file: ");
        String filePath = scanner.nextLine();

        DOTGraph graph = new DOTGraph();
        if (graph.parseGraph(filePath)) {
            System.out.println("Graph loaded successfully!");
        } else {
            System.out.println("Failed to load graph. Exiting");
            return;
        }

        // Print the graph structure
        System.out.println("\nGraph Structure:");
        System.out.println(graph.toString());

        // Set start and destination nodes
        System.out.print("\nEnter the start node: ");
        String startNode = scanner.nextLine().trim();
        System.out.print("Enter the destination node: ");
        String destinationNode = scanner.nextLine().trim();

        // Validate nodes
        if (!validateNodes(graph, startNode, destinationNode)) {
            System.out.println("Using default nodes instead.");
            startNode = "A";
            destinationNode = "Z";
        }

        System.out.println("\n===========================================");
        System.out.println("2. BFS AND DFS SEARCH DEMONSTRATION");
        System.out.println("===========================================");

        // 2. BFS Search Demonstration
        System.out.println("\nBFS SEARCH DEMONSTRATION:");
        Path bfsPath = runBFSWithVisitPrinting(graph, startNode, destinationNode);
        if (bfsPath != null) {
            System.out.println("Final BFS Path: " + bfsPath);
        } else {
            System.out.println("No path found from " + startNode + " to " + destinationNode + " using BFS.");
        }

        // DFS Search Demonstration
        System.out.println("\nDFS SEARCH DEMONSTRATION:");
        Path dfsPath = runDFSWithVisitPrinting(graph, startNode, destinationNode);
        if (dfsPath != null) {
            System.out.println("Final DFS Path: " + dfsPath);
        } else {
            System.out.println("No path found from " + startNode + " to " + destinationNode + " using DFS.");
        }

        // 3. Random Walk Demonstration
        System.out.println("\n===========================================");
        System.out.println("3. RANDOM WALK SEARCH DEMONSTRATION");
        System.out.println("===========================================");
        System.out.println("Using Variation 1: Fully random selection among reachable neighbors");

        // Run at least 5 attempts of Random Walk
        int minAttempts = 5;  // Minimum required attempts
        int maxAttempts = 20; // Maximum attempts to try
        int attemptCount = 0;

        // Store successful paths to check for at least 2 different paths
        List<Path> successfulPaths = new ArrayList<>();

        System.out.println("\nRunning Random Walk searches:");

        while (attemptCount < maxAttempts) {
            attemptCount++;

            System.out.println("\nAttempt " + attemptCount + ":");
            Path randomPath = runRandomWalkWithPathPrinting(graph, startNode, destinationNode);

            if (randomPath != null) {

                // Check if this path is different from existing successful paths
                boolean isDifferent = true;
                for (Path existingPath : successfulPaths) {
                    if (areSamePaths(existingPath, randomPath)) {
                        isDifferent = false;
                        break;
                    }
                }

                if (isDifferent) {
                    successfulPaths.add(randomPath);
                }
            } else {
                System.out.println("DEAD END! No path found to target.");
            }

            // Stop if we've met the requirements: minimum attempts and at least 2 different successful paths
            if (attemptCount >= minAttempts && successfulPaths.size() >= 2) {
                break;
            }
        }

        // Summary of Random Walk results
        if (successfulPaths.size() >= 2) {
            System.out.println("\nAll Unique Successful Paths:");
            for (int i = 0; i < successfulPaths.size(); i++) {
                System.out.println("Path " + (i + 1) + ": " + successfulPaths.get(i));
            }
        } else {
            System.out.println("\nCould not find at least 2 different successful paths in " + attemptCount + " attempts.");
        }

        System.out.println("\n===========================================");
        System.out.println("DEMONSTRATION COMPLETE");
        System.out.println("===========================================");
    }

    /**
     * Validate that the specified nodes exist in the graph
     */
    private static boolean validateNodes(DOTGraph graph, String start, String destination) {
        Graph<String, DefaultEdge> g = graph.getGraph();
        boolean valid = true;

        if (!g.containsVertex(start)) {
            System.out.println("Error: Start node '" + start + "' does not exist in the graph.");
            valid = false;
        }

        if (!g.containsVertex(destination)) {
            System.out.println("Error: Destination node '" + destination + "' does not exist in the graph.");
            valid = false;
        }

        return valid;
    }

    /**
     * Run BFS and print the path at each visit step (Scheme A)
     */
    private static Path runBFSWithVisitPrinting(DOTGraph dotGraph, String src, String dst) {
        Graph<String, DefaultEdge> graph = dotGraph.getGraph();

        // Queue for BFS traversal
        Queue<Path> pathQueue = new LinkedList<>();

        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Start BFS from the source node
        Path initialPath = new Path(src);
        pathQueue.add(initialPath);
        visited.add(src);

        System.out.println("visiting " + initialPath);

        while (!pathQueue.isEmpty()) {
            Path currentPath = pathQueue.poll();
            String currentNode = currentPath.getLastNode();

            // Check if we've reached the destination
            if (currentNode.equals(dst)) {
                return currentPath;
            }

            // Get all neighbors (outgoing edges from current node)
            for (DefaultEdge edge : graph.outgoingEdgesOf(currentNode)) {
                String neighbor = graph.getEdgeTarget(edge);

                // If we haven't visited this neighbor yet
                if (!visited.contains(neighbor)) {
                    // Create a new path by adding this neighbor
                    Path newPath = new Path(currentPath);
                    newPath.addNode(neighbor);

                    // Print the visit
                    System.out.println("visiting " + newPath);

                    // Add to queue and mark as visited
                    visited.add(neighbor);
                    pathQueue.add(newPath);
                }
            }
        }

        // If we get here, no path was found
        return null;
    }

    /**
     * Run DFS and print the path at each visit step (Scheme A)
     */
    private static Path runDFSWithVisitPrinting(DOTGraph dotGraph, String src, String dst) {
        Graph<String, DefaultEdge> graph = dotGraph.getGraph();

        // Stack for DFS traversal
        Deque<Path> pathStack = new ArrayDeque<>();

        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Start DFS from the source node
        Path initialPath = new Path(src);
        pathStack.push(initialPath);

        while (!pathStack.isEmpty()) {
            Path currentPath = pathStack.pop();
            String currentNode = currentPath.getLastNode();

            // Skip if already visited
            if (visited.contains(currentNode)) {
                continue;
            }

            // Print the visit
            System.out.println("visiting " + currentPath);

            // Mark as visited
            visited.add(currentNode);

            // Check if we've reached destination
            if (currentNode.equals(dst)) {
                return currentPath;
            }

            // Get all neighbors (outgoing edges from current node)
            List<DefaultEdge> outgoingEdges = new ArrayList<>(graph.outgoingEdgesOf(currentNode));

            // Process in reverse order to maintain expected DFS behavior
            for (int i = outgoingEdges.size() - 1; i >= 0; i--) {
                DefaultEdge edge = outgoingEdges.get(i);
                String neighbor = graph.getEdgeTarget(edge);

                if (!visited.contains(neighbor)) {
                    // Create a new path by adding this neighbor
                    Path newPath = new Path(currentPath);
                    newPath.addNode(neighbor);

                    // Add to stack for DFS traversal
                    pathStack.push(newPath);
                }
            }
        }

        // If we get here, no path was found
        return null;
    }

    /**
     * Run Random Walk search with path printing for each step
     */
    private static Path runRandomWalkWithPathPrinting(DOTGraph dotGraph, String src, String dst) {
        Graph<String, DefaultEdge> graph = dotGraph.getGraph();
        Random random = new Random();

        // Maximum steps to prevent infinite loops
        final int MAX_STEPS = 100;

        // Current path
        Path currentPath = new Path(src);
        System.out.print(src);

        // Current node
        String currentNode = src;

        // Step counter
        int steps = 0;

        while (steps < MAX_STEPS) {
            // Check if we've reached the destination
            if (currentNode.equals(dst)) {
                System.out.println();
                return currentPath;
            }

            // Get all outgoing edges from current node
            List<DefaultEdge> outgoingEdges = new ArrayList<>(graph.outgoingEdgesOf(currentNode));

            // If there are no outgoing edges, we've reached a dead end
            if (outgoingEdges.isEmpty()) {
                System.out.println(" (Dead end)");
                return null;
            }

            // Select a random outgoing edge
            DefaultEdge randomEdge = outgoingEdges.get(random.nextInt(outgoingEdges.size()));
            String nextNode = graph.getEdgeTarget(randomEdge);

            // Add the next node to our path
            currentNode = nextNode;
            currentPath.addNode(currentNode);

            // Print the current path step
            System.out.print("->" + currentNode);

            // Increment the step counter
            steps++;

            // If we've taken too many steps, consider it a failure
            if (steps >= MAX_STEPS) {
                System.out.println(" (Too many steps - gave up)");
                return null;
            }
        }

        System.out.println(" (Reached step limit)");
        return null;
    }

    /**
     * Check if two paths are the same (have the same nodes in the same order)
     */
    private static boolean areSamePaths(Path path1, Path path2) {
        if (path1 == null || path2 == null) {
            return path1 == path2; // both null or one null
        }

        List<String> nodes1 = path1.getNodes();
        List<String> nodes2 = path2.getNodes();

        if (nodes1.size() != nodes2.size()) {
            return false;
        }

        for (int i = 0; i < nodes1.size(); i++) {
            if (!nodes1.get(i).equals(nodes2.get(i))) {
                return false;
            }
        }

        return true;
    }
}