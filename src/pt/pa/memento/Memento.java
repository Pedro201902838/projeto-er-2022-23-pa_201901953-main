package pt.pa.memento;


import com.brunomnsilva.smartgraph.graph2.Graph;
import pt.pa.model.Route;
import pt.pa.model.Stop;

public interface Memento {
    public Graph<Stop, Route> getGraph();

}
