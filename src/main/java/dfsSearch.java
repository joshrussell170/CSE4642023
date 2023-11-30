import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.util.*;

public class dfsSearch extends Search implements Strategy{
    @Override
    public Path search(String srclabel, String dstLabel, MutableGraph graph) {
        MutableNode tmpNode = nodesExist(srclabel, dstLabel, graph);

        if(tmpNode != null){
            Map<String, String> parentMap = new HashMap<>();
            Stack<String> stack = new Stack<>();
            Set<String> visited = new HashSet<>();
            String neighborStr = null;

            stack.push(srclabel);
            visited.add(srclabel);

            while(!stack.isEmpty()){
                String currentNode = stack.pop();
                tmpNode = moveNode(currentNode, graph);

                Path myPath = returnPathCheck(currentNode, dstLabel, srclabel, parentMap);
                if(myPath != null){
                    return myPath;
                }

                for(Link neighbor : tmpNode.links()){
                    neighborStr = neighbor.to().name().toString();
                    if(!visited.contains(neighborStr)){
                        stack.push(neighborStr);
                        visited.add(neighborStr);
                        parentMap.put(neighborStr, currentNode);
                    }
                }


            }
        }
        return null;
    }
}
