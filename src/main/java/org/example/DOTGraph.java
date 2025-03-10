package org.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphXAdapter;

/**
 * A class to parse and represent DOT format graphs
 */
public class DOTGraph {
    private Graph<String, DefaultEdge> graph;
    private Map<String, Map<String, String>> vertexAttributes;

    /**
     * Constructor initializes an empty graph
     */
    public DOTGraph() {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        vertexAttributes = new HashMap<>();
    }

    /**
     * Parse a DOT graph file and create a graph object
     *
     * @param filepath the path to the DOT file
     * @return true if parsing was successful, false otherwise
     */
    public boolean parseGraph(String filepath) {
        try {
            // Create a new importer for DOT format
            DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();

            // Set up vertex provider (factory)
            importer.setVertexFactory(id -> id);

            // Create a map to store vertex attributes
            vertexAttributes = new HashMap<>();

            // Set up attribute consumers
            importer.addVertexAttributeConsumer((pair, attribute) -> {
                String vertex = pair.getFirst();
                String attributeName = pair.getSecond();

                if (!vertexAttributes.containsKey(vertex)) {
                    vertexAttributes.put(vertex, new HashMap<>());
                }
                vertexAttributes.get(vertex).put(attributeName, attribute.toString());
            });

            // Read the file content
            String dotContent = new String(Files.readAllBytes(Paths.get(filepath)));

            // Import the graph
            importer.importGraph(graph, new StringReader(dotContent));

            return true;
        } catch (IOException | ImportException e) {
            System.err.println("Error parsing DOT file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add a node to the graph with the specified label
     *
     * @param label the label for the new node
     * @return true if the node was added, false if a node with this label already exists
     */
    public boolean addNode(String label) {
        // Check if a node with this label already exists
        if (graph.containsVertex(label)) {
            System.out.println("Warning: Node with label '" + label + "' already exists.");
            return false;
        }

        // Add the node to the graph
        graph.addVertex(label);

        // Add an entry for vertex attributes
        if (!vertexAttributes.containsKey(label)) {
            vertexAttributes.put(label, new HashMap<>());
        }

        // Set the label attribute
        vertexAttributes.get(label).put("label", "\"" + label + "\"");

        System.out.println("Added node: " + label);
        return true;
    }

    /**
     * Add multiple nodes to the graph
     *
     * @param labels array of labels for new nodes
     * @return the number of nodes successfully added
     */
    public int addNodes(String[] labels) {
        int addedCount = 0;

        for (String label : labels) {
            if (addNode(label)) {
                addedCount++;
            }
        }

        return addedCount;
    }

    /**
     * Add an edge between two nodes in the graph
     *
     * @param srcLabel the source node label
     * @param dstLabel the destination node label
     * @return true if the edge was added, false if it already exists or nodes don't exist
     */
    public boolean addEdge(String srcLabel, String dstLabel) {
        // Check if both nodes exist
        if (!graph.containsVertex(srcLabel)) {
            System.out.println("Error: Source node '" + srcLabel + "' does not exist. Add it first.");
            return false;
        }

        if (!graph.containsVertex(dstLabel)) {
            System.out.println("Error: Destination node '" + dstLabel + "' does not exist. Add it first.");
            return false;
        }

        // Check if the edge already exists
        if (graph.containsEdge(srcLabel, dstLabel)) {
            System.out.println("Warning: Edge from '" + srcLabel + "' to '" + dstLabel + "' already exists.");
            return false;
        }

        // Add the edge
        graph.addEdge(srcLabel, dstLabel);
        System.out.println("Added edge: " + srcLabel + " -> " + dstLabel);
        return true;
    }

    /**
     * Get the number of vertices in the graph
     *
     * @return the vertex count
     */
    public int getVertexCount() {
        return graph.vertexSet().size();
    }

    /**
     * Get the number of edges in the graph
     *
     * @return the edge count
     */
    public int getEdgeCount() {
        return graph.edgeSet().size();
    }

    /**
     * Output the graph information to a file
     *
     * @param filepath the path where to save the output
     * @return true if successful, false otherwise
     */
    public boolean outputGraph(String filepath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Output the graph in DOT format to a file
     *
     * @param path the path where to save the DOT file
     * @return true if successful, false otherwise
     */
    public boolean outputDOTGraph(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();

            // Set vertex ID provider
            exporter.setVertexIdProvider(v -> v);

            // Set vertex attribute provider to include labels and other attributes
            exporter.setVertexAttributeProvider(v -> {
                Map<String, Attribute> attributes = new HashMap<>();

                // Add all stored attributes for this vertex
                if (vertexAttributes.containsKey(v)) {
                    for (Map.Entry<String, String> entry : vertexAttributes.get(v).entrySet()) {
                        attributes.put(entry.getKey(), DefaultAttribute.createAttribute(entry.getValue()));
                    }
                }

                // Ensure there's at least a label attribute
                if (!attributes.containsKey("label")) {
                    attributes.put("label", DefaultAttribute.createAttribute("\"" + v + "\""));
                }

                return attributes;
            });

            // Export to a StringWriter first to ensure proper formatting
            StringWriter stringWriter = new StringWriter();
            exporter.exportGraph(graph, stringWriter);

            // Write the DOT content to the file
            writer.write(stringWriter.toString());

            System.out.println("DOT graph exported successfully to: " + path);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting DOT graph: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Output the graph as a graphics file
     *
     * @param path the path where to save the graphics file
     * @param format the format of the graphics file (e.g., "png")
     * @return true if successful, false otherwise
     */
    public boolean outputGraphics(String path, String format) {
        // Check if format is supported
        if (!format.equalsIgnoreCase("png")) {
            System.err.println("Unsupported format: " + format + ". Only 'png' is supported.");
            return false;
        }

        try {
            // Create a JGraphXAdapter for visualization
            JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

            // Create a layout to organize the graph visually
            mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
            layout.execute(graphAdapter.getDefaultParent());

            // Create a buffered image to render the graph
            BufferedImage image = mxCellRenderer.createBufferedImage(
                    graphAdapter, null, 2, Color.WHITE, true, null);

            // Save the image
            File imgFile = new File(path);
            ImageIO.write(image, format, imgFile);

            System.out.println("Graph image exported successfully to: " + path);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting graph image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return a string representation of the graph
     *
     * @return string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Output basic graph information
        sb.append("Graph Summary:\n");
        sb.append("Number of nodes: ").append(getVertexCount()).append("\n");
        sb.append("Number of edges: ").append(getEdgeCount()).append("\n\n");

        // Output nodes and their labels
        sb.append("Nodes:\n");
        for (String vertex : graph.vertexSet()) {
            sb.append("  ").append(vertex);

            // Add label if available
            if (vertexAttributes.containsKey(vertex) &&
                    vertexAttributes.get(vertex).containsKey("label")) {
                String label = vertexAttributes.get(vertex).get("label");
                // Remove quotes if present
                if (label.startsWith("\"") && label.endsWith("\"")) {
                    label = label.substring(1, label.length() - 1);
                }
                sb.append(" [label=").append(label).append("]");
            }

            sb.append("\n");
        }

        // Output edges
        sb.append("\nEdges:\n");
        for (DefaultEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            sb.append("  ").append(source).append(" -> ").append(target).append("\n");
        }

        return sb.toString();
    }

    /**
     * Get the underlying graph object
     *
     * @return the graph
     */
    public Graph<String, DefaultEdge> getGraph() {
        return graph;
    }
}