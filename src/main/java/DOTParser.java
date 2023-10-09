import guru.nidi.graphviz.model.MutableGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class DOTParser {
    public MutableGraph parseGraph(String filePath) {
        MutableGraph graph = null;
        try(InputStream dot = getClass().getResourceAsStream(filePath)) {
            graph = new guru.nidi.graphviz.parse.Parser().read(dot);
        }
        catch(IOException e) {
            System.err.println("Error reading file" + e.getMessage());
        }
        return graph;
    }

    public int getNumberOfNodes(MutableGraph graph) {
        return graph.nodes().size();
    }

    public int getNumberOfEdges(MutableGraph graph) {
        return graph.edges().size();
    }

    public void toStringGraph(MutableGraph graph){
        System.out.println("Number of Nodes: " + getNumberOfNodes(graph));
        System.out.println("Number of Edges: " + getNumberOfEdges(graph));
        System.out.println("Nodes and their Directions:");

        graph.nodes().forEach(node -> {
            Set<String> neighbors = new HashSet<>();
            graph.edges().forEach(edge -> {
                if(edge.from().toString().equals(node.toString())){
                    neighbors.add(edge.to().toString());
                }
            });
            if(!neighbors.isEmpty()){
                System.out.println(node.toString());
            }
            else{
                System.out.println(node.toString());
            }
        });
    }

    public void outputGraph(String filePath, MutableGraph graph){
        try(FileWriter writer = new FileWriter(filePath)){
            writer.write("Number of Nodes: " + getNumberOfNodes(graph) + "\n");
            writer.write(("Number of Edges: " + getNumberOfEdges(graph) + "\n"));
            writer.write("Nodes and Edge Directions:\n");
            graph.nodes().forEach(node -> {
                Set<String> neighbors = new HashSet<>();
                graph.edges().forEach(edge -> {
                    if (edge.from().equals(node)) {
                        neighbors.add(edge.to().toString());
                    }
                });
                if(!neighbors.isEmpty()) {
                    try {
                        writer.write(node.toString() + "\n");
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                }
                else{
                    try {
                        writer.write(node.toString() + "\n");
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                }
            });
            System.out.println("Graph information written to: " + filePath);
        } catch(IOException e){
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        System.out.println("Hello worlds");
        DOTParser parser = new DOTParser();
        MutableGraph myGraph = parser.parseGraph("/color.dot");
        parser.toStringGraph(myGraph);
        parser.outputGraph("src/main/resources/output.txt", myGraph);

    }
}
