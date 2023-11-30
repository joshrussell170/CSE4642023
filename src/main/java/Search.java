import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.util.Collections;
import java.util.Map;

abstract class Search {

    public MutableNode moveNode(String neighborStr, MutableGraph graph){
        for(MutableNode node : graph.nodes()){
            if(node.name().toString().equals(neighborStr)){
                return node;
            }
        }
        return null;
    }

    public Path returnPathCheck(String currentNode, String dstLabel, String srclabel, Map<String, String> parentMap){
        if(currentNode.equals(dstLabel)){
            return reconstructPath(parentMap, srclabel, dstLabel);
        }
        else{
            return null;
        }
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

    public MutableNode nodesExist(String srclabel, String dstLabel, MutableGraph graph){
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
            return tmpNode;
        }
        else {
            if (srcExists) {
                System.err.println("Source node exists but destination node doesnt");
                return null;
            } else if (dstExists) {
                System.err.println("Destination node exists but source node doesnt");
                return null;
            } else {
                System.err.println("Neither node exists");
                return null;
            }
        }
    }

}
