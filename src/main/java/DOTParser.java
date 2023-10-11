import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Link;

import java.io.File;
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
        catch(Exception ex){
            System.err.println("Error parsing DOT file: " + ex.getMessage());
            System.err.println("Ensure DOT File format is correct");
            graph = null;
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
                assert edge.from() != null;
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
                    assert edge.from() != null;
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

    public MutableGraph addNode(String label, MutableGraph graph){
        if(graph != null){
            //look for duplicates
            boolean nodeExists = graph.nodes().stream().anyMatch(node -> node.name().toString().equals(label));
            if(!nodeExists){
                MutableNode newNode = Factory.mutNode(label);
                newNode.addTo(graph);
            }
            else{
                System.out.println("Node with label '" + label + "' already exists");
            }
        }
        else{
            System.err.println("Cannot add node due to parsing error (ensure graph was created)");
        }
        return graph;
    }

    public MutableGraph addNodes(String[] labels, MutableGraph graph){
        for(String label : labels){
            graph = addNode(label, graph);
        }
        return graph;
    }

    public MutableGraph addEdge(String srcLabel, String dstLabel, MutableGraph graph){
        if(graph != null){
            MutableNode srcNode = null;
            MutableNode dstNode = null;

            //Check if source or destination label already exist
            for(MutableNode node : graph.nodes()){
                if(node.name().toString().equals(srcLabel)){
                    srcNode = node;
                    break;
                }
            }

            for(MutableNode node : graph.nodes()){
                if(node.name().toString().equals(dstLabel)){
                    dstNode = node;
                    break;
                }
            }

            //if the label doesnt already exist, create it and add to graph
            if(srcNode == null){
                srcNode = Factory.mutNode(srcLabel);
                srcNode.addTo(graph);
            }

            if(dstNode == null){
                dstNode = Factory.mutNode(dstLabel);
                dstNode.addTo(graph);
            }

            //check if edge already exists
            boolean edgeExists = false;
            for(Link link : srcNode.links()){
                if(link.name().toString().equals(srcLabel + "--" + dstLabel)){
                    edgeExists = true;
                    break;
                }
            }

            if(!edgeExists){
                //create the edge link between the nodes
                srcNode.addLink(dstNode);
            }
            else{
                System.out.println("Edge already exists between " + srcLabel + " and " + dstLabel);
            }


        }
        else{
            System.err.println("Cannot add edge due to parsing error (ensure graph is parsed correctly");
        }
        return graph;
    }

    public void outputDOTGraph(String path, MutableGraph graph){
        if(graph != null){
            try(FileWriter writer = new FileWriter(path)){
                writer.write(graph.toString());
                System.out.println("Graph saved to " + path);
            }
            catch (IOException e){
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
        else{
            System.err.println("Can't output DOT graph (ensure graph is actually created first)");
        }
    }

    public void outputGraphics(String path, MutableGraph graph){
        if(graph != null){
            try{
                File pngFile = new File(path);
                Graphviz.fromGraph(graph)
                        .width(800).render(Format.PNG).toFile(pngFile);
                System.out.println("Graph saved as PNG to " + path);
            }
            catch(IOException e){
                System.err.println("Error: " + e.getMessage());
            }
        }
        else{
            System.err.println("Can't output PNG (check to make sure graph was made)");
        }
    }


    public static void main(String[] args){
        DOTParser parser = new DOTParser();
        MutableGraph myGraph = parser.parseGraph("/color.dot");
        if(myGraph != null){
            //feature 1
            /*parser.toStringGraph(myGraph);
            parser.outputGraph("src/main/resources/output.txt", myGraph);*/

            //feature 2
            /*String label = "B";
            myGraph = parser.addNode(label, myGraph);
            String[] labels = {"L", "I", "S", "T", "A"};
            myGraph = parser.addNodes(labels, myGraph);
            parser.toStringGraph(myGraph);
            parser.outputGraph("src/main/resources/output.txt", myGraph);*/


            //feature 3
            /*myGraph = parser.addEdge("F", "E", myGraph);
            myGraph = parser.addEdge("Z", "A", myGraph);
            myGraph = parser.addEdge("W", "X", myGraph);
            myGraph = parser.addEdge("A", "B", myGraph);
            myGraph = parser.addEdge("A", "A", myGraph);
            myGraph = parser.addEdge("Z", "A", myGraph);
            parser.toStringGraph(myGraph);
            parser.outputGraph("src/main/resources/output.txt", myGraph);*/


            //feature 4
            /*myGraph = parser.addEdge("Z", "A", myGraph);
            parser.outputDOTGraph("src/main/resources/output.dot", myGraph);
            parser.outputGraphics("src/main/resources/output.png", myGraph);*/

        }
        else{
            System.err.println("!!Exiting due to parsing error!!");
        }
    }
}
