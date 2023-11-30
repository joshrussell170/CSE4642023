import guru.nidi.graphviz.model.MutableGraph;

public interface Strategy {
    Path search(String srclabel, String dstLabel, MutableGraph graph);
}
