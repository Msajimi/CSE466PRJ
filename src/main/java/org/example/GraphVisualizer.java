package org.example;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;


public class GraphVisualizer {
    public boolean outputGraphics(Graph<String, DefaultEdge> graph, String path, String format) {
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
}

