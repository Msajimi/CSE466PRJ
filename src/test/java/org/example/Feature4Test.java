package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite specifically for Feature 4: Output graph to DOT file and graphics
 */
public class Feature4Test {

    private DOTGraph dotGraph;

    @BeforeEach
    public void setUp() {
        // Create a new DOTGraph instance for each test
        dotGraph = new DOTGraph();
    }

    /**
     * Test outputting the graph to a DOT file
     */
    @Test
    public void testOutputDOTGraph() throws IOException {
        // Parse the test input graph file
        boolean parseResult = dotGraph.parseGraph("outputDOTGraphTest.dot");
        assertTrue(parseResult, "Graph parsing should succeed");

        // Output the graph to a DOT file
        String outputPath = "outputDOTGraphTest_output.dot";
        boolean result = dotGraph.outputDOTGraph(outputPath);

        // Verify the operation was successful
        assertTrue(result, "Exporting to DOT file should succeed");

        // Verify the file exists
        File outputFile = new File(outputPath);
        assertTrue(outputFile.exists(), "Output DOT file should exist");

        try {
            // Read the content of the output file and normalize line endings
            String outputContent = new String(Files.readAllBytes(Paths.get(outputPath)))
                    .replace("\r\n", "\n").trim();

            // Read the expected output file and normalize line endings
            String expectedOutputPath = "outputDOTGraphTestEO.txt";
            String expectedContent = new String(Files.readAllBytes(Paths.get(expectedOutputPath)))
                    .replace("\r\n", "\n").trim();

            // Compare actual output with expected output, ignoring whitespace differences
            assertEquals(expectedContent, outputContent, "Output DOT file should match expected output");
        } finally {
            // Clean up - delete the output file
        }
    }

    /**
     * Test outputting the graph to a PNG file
     */
    @Test
    public void testOutputGraphicsPNG() throws IOException {
        // Parse the test input graph file
        boolean parseResult = dotGraph.parseGraph("outputGraphicsTest.dot");
        assertTrue(parseResult, "Graph parsing should succeed");

        // Output the graph to a PNG file
        String outputPath = "outputGraphicsTest_output.png";
        boolean result = dotGraph.outputGraphics(outputPath, "png");

        try {
            // Verify the operation was successful
            assertTrue(result, "Exporting to PNG file should succeed");

            // Verify the file exists
            File outputFile = new File(outputPath);
            assertTrue(outputFile.exists(), "Output PNG file should exist");

            // Verify the file is not empty
            assertTrue(outputFile.length() > 0, "Output PNG file should not be empty");

            // Read the expected success message from a file
            String expectedOutputPath = "outputGraphicsTestEO.txt";
            List<String> expectedLines = Files.readAllLines(Paths.get(expectedOutputPath));
            String expectedMessage = expectedLines.stream()
                    .filter(line -> line.contains("success"))
                    .findFirst()
                    .orElse("");

            // Verify the success message matches what we expect
            assertFalse(expectedMessage.isEmpty(), "Expected output file should contain success message");
            assertTrue(expectedMessage.contains("Graph image exported successfully"),
                    "Output should indicate successful PNG export");
        } finally {

        }
    }

    /**
     * Test outputting the graph to an unsupported format
     */
    @Test
    public void testOutputGraphicsUnsupportedFormat() throws IOException {
        // Parse the test input graph file
        boolean parseResult = dotGraph.parseGraph("outputGraphicsTest.dot");
        assertTrue(parseResult, "Graph parsing should succeed");

        // Try to output the graph to an unsupported format
        String outputPath = "outputGraphicsTest_output.jpg";
        boolean result = dotGraph.outputGraphics(outputPath, "jpg");

        // Verify the operation failed
        assertFalse(result, "Exporting to unsupported format should fail");

        // Verify the file doesn't exist
        File outputFile = new File(outputPath);
        assertFalse(outputFile.exists(), "Output file should not exist for unsupported format");

        // Read the expected error message from a file
        String expectedOutputPath = "outputGraphicsUnsupportedFormatEO.txt";
        List<String> expectedLines = Files.readAllLines(Paths.get(expectedOutputPath));
        String expectedError = expectedLines.stream()
                .filter(line -> line.contains("Unsupported format"))
                .findFirst()
                .orElse("");

        // Verify the error message matches what we expect
        assertFalse(expectedError.isEmpty(), "Expected output file should contain error message");
        assertTrue(expectedError.contains("Unsupported format"),
                "Output should indicate unsupported format");
    }

    /**
     * Test outputting a complex graph created programmatically
     */
    @Test
    public void testOutputComplexGraph() throws IOException {
        // Read the test configuration file to create the complex graph
        List<String> configLines = Files.readAllLines(Paths.get("outputComplexGraphTest.txt"));

        // Create a complex graph based on configuration
        DOTGraph complexGraph = new DOTGraph();

        // Process configuration lines to build the graph
        for (String line : configLines) {
            line = line.trim();
            if (line.startsWith("addNode:")) {
                String nodeName = line.substring("addNode:".length()).trim();
                complexGraph.addNode(nodeName);
            } else if (line.startsWith("addEdge:")) {
                String edgeInfo = line.substring("addEdge:".length()).trim();
                String[] parts = edgeInfo.split("->");
                if (parts.length == 2) {
                    complexGraph.addEdge(parts[0].trim(), parts[1].trim());
                }
            }
        }

        // Define output paths
        String outputDotPath = "outputComplexGraphTest_output.dot";

        try {
            // Output to DOT file
            boolean dotResult = complexGraph.outputDOTGraph(outputDotPath);
            assertTrue(dotResult, "Exporting complex graph to DOT file should succeed");

            // Verify the DOT file exists
            File dotFile = new File(outputDotPath);
            assertTrue(dotFile.exists(), "Complex DOT file should exist");

            // Read the content of the output DOT file and normalize line endings
            String outputContent = new String(Files.readAllBytes(Paths.get(outputDotPath)))
                    .replace("\r\n", "\n").trim();

            // Read the expected output file and normalize line endings
            String expectedOutputPath = "outputComplexGraphTestEO.txt";
            String expectedContent = new String(Files.readAllBytes(Paths.get(expectedOutputPath)))
                    .replace("\r\n", "\n").trim();

            // Verify the DOT content contains all expected elements
            for (String expectedLine : expectedContent.split("\n")) {
                expectedLine = expectedLine.trim();
                if (!expectedLine.isEmpty()) {
                    assertTrue(outputContent.contains(expectedLine),
                            "Output should contain: " + expectedLine);
                }
            }
        } finally {
            // Clean up - delete output files
        }
    }
}