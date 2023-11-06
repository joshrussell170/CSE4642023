import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> nodes;

    public Path(){
        nodes = new ArrayList<>();
    }

    public void addNode(String node){
        nodes.add(node);
    }

    public List<String> getNodes(){
        return nodes;
    }

    public int size(){
        return nodes.size();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < nodes.size(); i++){
            sb.append(nodes.get(i));
            if(i < nodes.size() - 1){
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

}
