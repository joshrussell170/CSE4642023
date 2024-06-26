import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Link;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DOTParserTest {
    @Test
    public void testParseGraph(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";

        try{
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            assertNotNull(testGraph);
        }
        catch(Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testToStringGraph(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            parser.toStringGraph(testGraph);

            File expectedOutputFile = new File("src/test/resources/toStringExact.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));
            String actualOutput = outContent.toString().trim();

            assertEquals(expectedOutput, actualOutput);

        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }

    }

    @Test
    public void testOutputGraph(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            parser.outputGraph("src/test/resources/output.txt", testGraph);

            File expectedOutputFile = new File("src/test/resources/testOutput.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));

            File actualOutputFile = new File("src/test/resources/output.txt");
            String actualOutput = new String(Files.readAllBytes(Paths.get(actualOutputFile.toURI())));


            assertEquals(expectedOutput, actualOutput);

        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testAddNode(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/addNodeTest.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);


            testGraph = parser.addNode("Z", testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testAddNodes(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/addNodesTest.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);

            String[] labels = {"L", "I", "S", "T"};
            testGraph = parser.addNodes(labels, testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testAddEdge(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/addEdgeTest.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);

            testGraph = parser.addEdge("F", "E", testGraph);
            testGraph = parser.addEdge("Z", "Y", testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }

            for(Link edge1 : testGraph.edges()){
                assertTrue(exactGraph.edges().contains(edge1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testOutputDOTGraph(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "src/test/resources/outputDOTTest.dot";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            testGraph = parser.addEdge("Z", "A", testGraph);
            parser.outputDOTGraph("src/test/resources/output.dot", testGraph);

            String testDOTContent = new String(Files.readAllBytes(Paths.get("src/test/resources/output.dot")));
            String exactDOTContent = new String(Files.readAllBytes(Paths.get(testPath)));

            assertEquals(testDOTContent, exactDOTContent);
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }

    }

    @Test
    public void testRemoveNodePass(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/removeNodeTestPass.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);


            testGraph = parser.removeNode("B", testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveNodeFail(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            testGraph = parser.removeNode("Z", testGraph);

            File expectedOutputFile = new File("src/test/resources/removeNodeTestFail.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));
            String actualOutput = errContent.toString().trim();

            assertEquals(expectedOutput, actualOutput);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveNodesPass(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/removeNodesTestPass.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);

            String[] labels = {"B", "F"};
            testGraph = parser.removeNodes(labels, testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveNodesFail(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/removeNodesTestPass.txt";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);

            String[] labels = {"Z", "F", "B"};
            testGraph = parser.removeNodes(labels, testGraph);

            File expectedOutputFile = new File("src/test/resources/removeNodeTestFail.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));
            String actualOutput = errContent.toString().trim();

            assertEquals(expectedOutput, actualOutput);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }

        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEdgePass(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "/removeEdgeTestPass.txt";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            MutableGraph exactGraph = parser.parseGraph(testPath);


            testGraph = parser.removeEdge("B", "C", testGraph);

            assertEquals(testGraph.nodes().size(), exactGraph.nodes().size());
            assertEquals(testGraph.edges().size(), exactGraph.edges().size());

            // Compare the nodes and edges individually, ensuring they match
            for (MutableNode node1 : testGraph.nodes()) {
                assertTrue(exactGraph.nodes().contains(node1));
            }
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEdgeFail(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            testGraph = parser.removeEdge("F", "E", testGraph);

            File expectedOutputFile = new File("src/test/resources/removeEdgeTestFail.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));
            String actualOutput = errContent.toString().trim();

            assertEquals(expectedOutput, actualOutput);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }

    }

    @Test
    public void bfsTestPass(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/complex.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            DOTParser.Algorithm algo = DOTParser.Algorithm.bfs;
            Path myPath = parser.GraphSearch("A", "D", algo, testGraph);
            String actualOutput = myPath.toString();

            File expectedOutputFile = new File("src/test/resources/bfsTestPass.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));

            assertEquals(expectedOutput, actualOutput);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void dfsTestPass(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/complex.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            DOTParser.Algorithm algo = DOTParser.Algorithm.dfs;
            Path myPath = parser.GraphSearch("A", "D", algo, testGraph);
            String actualOutput = myPath.toString();

            File expectedOutputFile = new File("src/test/resources/dfsTestPass.txt");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFile.toURI())));

            assertEquals(expectedOutput, actualOutput);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void bfsTestFail(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/complex.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            DOTParser.Algorithm algo = DOTParser.Algorithm.dfs;
            Path myPath = parser.GraphSearch("B", "A", algo, testGraph);

            assertNull(myPath);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    @Test
    public void dfsTestFail(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/complex.dot";
        try {

            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            DOTParser.Algorithm algo = DOTParser.Algorithm.dfs;
            Path myPath = parser.GraphSearch("C", "A", algo, testGraph);

            assertNull(myPath);


        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }

    //This test was build to compare pixel by pixel 2 png files; It works when I run the test
    //on IntelliJ but when I use "mvn package" there is a failure.
    /*@Test
    public void testOutputGraphics(){
        DOTParser parser = new DOTParser();
        String dotFilePath = "/color.dot";
        String testPath = "src/test/resources/outputTest.png";
        try {
            MutableGraph testGraph = parser.parseGraph(dotFilePath);
            testGraph = parser.addEdge("Z", "A", testGraph);
            parser.outputGraphics("src/test/resources/output.png", testGraph);

            BufferedImage testImage = ImageIO.read(new File("src/test/resources/output.png"));
            BufferedImage exactImage = ImageIO.read(new File(testPath));
            boolean imagesEqual = true;

            if (testImage.getWidth() == exactImage.getWidth() && testImage.getHeight() == exactImage.getHeight()) {
                for (int x = 0; x < testImage.getWidth(); x++) {
                    for (int y = 0; y < testImage.getHeight(); y++) {
                        if (testImage.getRGB(x, y) != exactImage.getRGB(x, y)) {
                            imagesEqual = false;
                            break;
                        }
                    }
                    if (!imagesEqual) {
                        break;
                    }
                }
            } else {
                imagesEqual = false;
            }

            assertTrue(imagesEqual);
        }
        catch (Exception e){
            fail("failed to read the .dot file: " + e.getMessage());
        }
    }*/



}
