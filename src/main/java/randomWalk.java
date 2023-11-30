import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;


import java.util.*;

public class randomWalk extends Search implements Strategy{
    @Override
    public Path search(String srclabel, String dstLabel, MutableGraph graph) {
        MutableNode tmpNode = nodesExist(srclabel, dstLabel, graph);
        ArrayList<String> myNodes = new ArrayList<>();
        String tmpDst = srclabel;
        int numIter = 0;
        Set<String> usedDst = new HashSet<>();
        dfsSearch utilSearch = new dfsSearch();
        int randomIndex;

        for(MutableNode node : graph.nodes()){
            myNodes.add(node.name().toString());
        }

        usedDst.add(srclabel);

        while(tmpDst.equals(srclabel)){
            randomIndex = getRandomIndex(myNodes);
            if(randomIndex != -1){
                tmpDst = myNodes.get(randomIndex);
            }
            else{
                System.out.println("Empty Nodes");
            }
        }
        usedDst.add(tmpDst);
        numIter++;


        if(tmpNode != null){
            while(numIter <= graph.nodes().size()){

                Path tmpPath = utilSearch.search(srclabel, tmpDst, graph);

                if(tmpDst.equals(dstLabel)){
                    if(tmpPath != null){
                        System.out.print("Visiting Path: ");
                        System.out.println(tmpPath.toString());
                        return tmpPath;
                    }
                    else{
                        System.out.println("null for some reason");
                        return null;
                    }

                }

                if(tmpPath.nodes.contains(dstLabel)){
                    tmpPath = utilSearch.search(srclabel, dstLabel, graph);
                    System.out.print("Visiting Path: ");
                    System.out.println(tmpPath.toString());
                    return tmpPath;
                }

                System.out.print("Visiting Path: ");
                System.out.println(tmpPath.toString());

                while(usedDst.contains(tmpDst)){
                    randomIndex = getRandomIndex(myNodes);
                    if(randomIndex != -1){
                        tmpDst = myNodes.get(randomIndex);
                    }
                    else{
                        System.out.println("Empty Nodes");
                    }
                }

                usedDst.add(tmpDst);
                numIter++;
            }
        }
        return null;
    }

    public int getRandomIndex(ArrayList<String> list){
        if (list != null && !list.isEmpty()) {
            Random random = new Random();
            return random.nextInt(list.size());
        }
        return -1; // Return -1 for an empty list
    }
}
