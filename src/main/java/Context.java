import guru.nidi.graphviz.model.MutableGraph;

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }

    public Path executeStrategy(String srclabel, String dstLabel, MutableGraph graph){
        return strategy.search(srclabel, dstLabel, graph);
    }
}
