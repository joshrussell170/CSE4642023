import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class bfsSearch extends Search{

    public Path bfsGraphSearch(String srclabel, String dstLabel, MutableGraph graph){
        MutableNode tmpNode = nodesExist(srclabel, dstLabel, graph);

        if(tmpNode != null){
            Map<String, String> parentMap = new HashMap<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            String neighborStr = null;

            queue.add(srclabel);
            visited.add(srclabel);

            while(!queue.isEmpty()){
                String currentNode = queue.poll();

                Path myPath = returnPathCheck(currentNode, dstLabel, srclabel, parentMap);
                if(myPath != null){
                    return myPath;
                }

                for(Link neighbor : tmpNode.links()){
                    neighborStr = neighbor.to().name().toString();
                    if(!visited.contains(neighborStr)){
                        queue.add(neighborStr);
                        visited.add(neighborStr);
                        parentMap.put(neighborStr, currentNode);
                    }
                }
                tmpNode = moveNode(neighborStr, graph);

            }
        }
        return null;
    }

}
