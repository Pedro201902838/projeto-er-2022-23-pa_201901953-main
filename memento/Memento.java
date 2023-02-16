package pt.pa.memento;

import pt.pa.graph.Graph;
import pt.pa.model.Route;
import pt.pa.model.Stop;

public interface Memento {
    public Graph<Stop, Route> getGraph();

}
