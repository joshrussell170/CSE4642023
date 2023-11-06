import guru.nidi.graphviz.attribute.Label;
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
import java.util.*;

public class DOTParser {

    enum Algorithm {
        bfs,
        dfs
    }

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

    public MutableGraph removeNode(String label, MutableGraph graph) {

        if(graph != null){
            boolean containsNode = false;

            for(MutableNode node : graph.nodes()){
                if(node.name().toString().equals(label)){
                    containsNode = true;
                }
            }

            if(containsNode){
                MutableGraph updatedGraph = Factory.mutGraph().setDirected(true);

                for(MutableNode node : graph.nodes()){
                    if(!node.name().toString().equals(label)){
                        addNode(node.name().toString(), updatedGraph);
                    }
                }

                for(MutableNode node : graph.nodes()){
                    for(Link target : node.links()){
                        if(!target.to().name().toString().equals(label) && !target.from().name().toString().equals(label)){
                            addEdge(node.name().toString(), target.to().name().toString(), updatedGraph);
                        }
                    }
                }

                return updatedGraph;
            }

            else{
                System.err.println("The original graph already doesn't contain the Node " + label);
                return graph;
            }
        }

        else{
            System.err.println("There is no graph; ensure graph was properly parsed");
            return null;
        }

    }

    public MutableGraph removeNodes(String[] label, MutableGraph graph){

        //make a loop for the labels to one by one
        //then call removeNode() for each label
        MutableGraph updatedGraph = graph;

        for(String name : label){
            updatedGraph = removeNode(name, updatedGraph);
        }

        return updatedGraph;
    }

    public MutableGraph removeEdge(String srcLabel, String dstLabel, MutableGraph graph){


        if(graph != null){
            boolean srcExists = false;
            boolean dstExists = false;
            MutableNode srcNode = null;
            MutableNode dstNode = null;

            //search for src node
            //if node exists, search for dst node
            for(MutableNode node : graph.nodes()){
                if(node.name().toString().equals(srcLabel)){
                    srcExists = true;
                    srcNode = node;
                }
                else if(node.name().toString().equals(dstLabel)){
                    dstExists = true;
                    dstNode = node;
                }
                if(srcExists && dstExists){
                    break;
                }
            }

            if(srcExists && dstExists){
                boolean linkExists = false;
                for(Link target : srcNode.links()){
                    if(target.to().name().toString().equals(dstLabel)){
                        linkExists = true;
                    }
                }

                if(linkExists){

                    MutableGraph updatedGraph = Factory.mutGraph().setDirected(true);

                    for(MutableNode node : graph.nodes()){
                            addNode(node.name().toString(), updatedGraph);
                    }

                    for(MutableNode node : graph.nodes()){
                        if(node.name().toString().equals(srcLabel)){
                            for(Link target : node.links()){
                                if(!target.to().name().toString().equals(dstLabel)){
                                    addEdge(node.name().toString(), target.to().name().toString(), updatedGraph);
                                }
                            }
                        }
                        else{
                            for(Link target : node.links()){
                                addEdge(node.name().toString(), target.to().name().toString(), updatedGraph);
                            }
                        }

                    }

                    return updatedGraph;
                }
                else{
                    System.err.println("Edge between " + srcLabel + " and " + dstLabel + " doesn't exist.");
                    return graph;
                }

            }
            else if(srcExists){
                System.err.println("Source Node Exists but Destination Node doesn't exist");
                return graph;
            }
            else if(dstExists){
                System.err.println("Destination Node Exists but Source Node doesn't exist");
                return graph;
            }
            else{
                System.err.println("Neither Node exists within the graph");
                return graph;
            }
        }
        else{
            System.err.println("There is no graph; ensure graph was properly parsed");
            return null;
        }

    }


    public Path GraphSearch(String srclabel, String dstLabel, Algorithm algo, MutableGraph graph){

        switch (algo){
            case bfs:
                return bfsGraphSearch(srclabel, dstLabel, graph);

            case dfs:
                return dfsGraphSearch(srclabel, dstLabel, graph);

            default:
                System.err.println("Not a valid algorithm");
        }
        return null;
    }

    public Path bfsGraphSearch(String srclabel, String dstLabel, MutableGraph graph){
        MutableNode tmpNode = null;
        boolean srcExists = false;
        boolean dstExists = false;

        //check if nodes are in the graph and set the source node
        for(MutableNode node : graph.nodes()){
            if(node.name().toString().equals(srclabel)){
                tmpNode = node;
                srcExists = true;
            }
            if(node.name().toString().equals(dstLabel)){
                dstExists = true;
            }
        }

        if(srcExists && dstExists){
            Map<String, String> parentMap = new HashMap<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            String neighborStr = null;

            queue.add(srclabel);
            visited.add(srclabel);

            while(!queue.isEmpty()){
                String currentNode = queue.poll();

                if(currentNode.equals(dstLabel)){
                    return reconstructPath(parentMap, srclabel, dstLabel);
                }
                for(Link neighbor : tmpNode.links()){
                    neighborStr = neighbor.to().name().toString();
                    if(!visited.contains(neighborStr)){
                        queue.add(neighborStr);
                        visited.add(neighborStr);
                        parentMap.put(neighborStr, currentNode);
                    }
                }
                for(MutableNode node : graph.nodes()){
                    if(node.name().toString().equals(neighborStr)){
                        tmpNode = node;
                        break;
                    }
                }

            }
        }
        else if(srcExists){
            System.err.println("Source node exists but destination node doesnt");
            return null;
        }

        else if(dstExists){
            System.err.println("Destination node exists but source node doesnt");
            return null;
        }

        else{
            System.err.println("Neither node exists");
            return null;
        }

        return null;
    }

    public Path dfsGraphSearch(String srclabel, String dstLabel, MutableGraph graph){
        MutableNode tmpNode = null;
        boolean srcExists = false;
        boolean dstExists = false;

        //check if nodes are in the graph and set the source node
        for(MutableNode node : graph.nodes()){
            if(node.name().toString().equals(srclabel)){
                tmpNode = node;
                srcExists = true;
            }
            if(node.name().toString().equals(dstLabel)){
                dstExists = true;
            }
        }

        if(srcExists && dstExists){
            Map<String, String> parentMap = new HashMap<>();
            Stack<String> stack = new Stack<>();
            Set<String> visited = new HashSet<>();
            String neighborStr = null;

            stack.push(srclabel);
            visited.add(srclabel);

            while(!stack.isEmpty()){
                String currentNode = stack.pop();

                if(currentNode.equals(dstLabel)){
                    return reconstructPath(parentMap, srclabel, dstLabel);
                }
                for(Link neighbor : tmpNode.links()){
                    neighborStr = neighbor.to().name().toString();
                    if(!visited.contains(neighborStr)){
                        stack.push(neighborStr);
                        visited.add(neighborStr);
                        parentMap.put(neighborStr, currentNode);
                    }
                }
                for(MutableNode node : graph.nodes()){
                    if(node.name().toString().equals(neighborStr)){
                        tmpNode = node;
                        break;
                    }
                }

            }
        }
        else if(srcExists){
            System.err.println("Source node exists but destination node doesnt");
            return null;
        }

        else if(dstExists){
            System.err.println("Destination node exists but source node doesnt");
            return null;
        }

        else{
            System.err.println("Neither node exists");
            return null;
        }

        return null;
    }

    public static Path reconstructPath(Map<String, String> parentMap, String startNode, String targetNode) {
        Path path = new Path();
        String currentNode = targetNode;

        while (currentNode != null) {
            path.addNode(currentNode);
            currentNode = parentMap.get(currentNode);
        }

        Collections.reverse(path.getNodes());
        return path;
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

            //feature 5
            /*myGraph = parser.removeNode("Z", myGraph);
           parser.toStringGraph(myGraph);*/

            //feature 6
            /*String[] labels = {"B", "F", "Z"};
            myGraph = parser.removeNodes(labels, myGraph);
            parser.toStringGraph(myGraph);*/

            //feature 7
            /*myGraph = parser.removeEdge("E", "B", myGraph);
            parser.toStringGraph(myGraph);*/

            //feature 9

            /*Path myPath = parser.GraphSearch("A", "C", myGraph);
            if(myPath != null){
                System.out.println(myPath.toString());
            }
            else{
                System.err.println("Path doesnt exist between nodes");
            }*/

            //feature 10
            /*Algorithm myAlgo = Algorithm.bfs;
            Path myPath = parser.GraphSearch("A", "C", myAlgo, myGraph);
            if(myPath != null) {
                System.out.println(myPath.toString());
            }
            else{
                System.err.println("Path is null");
            }*/




        }
        else{
            System.err.println("!!Exiting due to parsing error!!");
        }
    }
}
