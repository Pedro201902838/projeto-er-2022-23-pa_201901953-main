package pt.pa.model;

import com.brunomnsilva.smartgraph.graph2.Digraph;
import com.brunomnsilva.smartgraph.graph2.Edge;
import com.brunomnsilva.smartgraph.graph2.Graph;
import com.brunomnsilva.smartgraph.graph2.Vertex;
import pt.pa.graph.GraphImpl;
import pt.pa.graph.*;

import java.io.FileNotFoundException;
import java.util.*;

import pt.pa.StratLoader.Loader;
import pt.pa.StratLoader.LoaderDemo;
import pt.pa.memento.Memento;
import pt.pa.memento.Originator;

public class Network implements Originator {
    private Digraph<Stop, Route> graph;
    private Loader loader;

    public Network() throws FileNotFoundException {
        graph = new GraphImpl<>();
        loader = new LoaderDemo();
    }

    public void insertStop(Stop stop) {
        graph.insertVertex(stop);
    }

    public void insertRoute(String code1, String code2, Route route) {
        graph.insertEdge(getStopByCode(code1), getStopByCode(code2), route);
    }

    public void removeRoute(String id) {
        for (Edge<Route, Stop> e : graph.edges()) {
            if (e.element().getID().equalsIgnoreCase(id)) {
                graph.removeEdge(e);
            }
        }
    }

    public void removeRoute(Route route){
        graph.removeEdge(getEdgeByRoute(route));
    }

    public int getNoAdj(Vertex<Stop> a) {
        int count = 0;
        for (Vertex<Stop> v : graph.vertices()) {
            if (graph.areAdjacent(v, a)) {
                count++;
            }
        }
        return count;
    }

    public void insertRoute(String code1, String code2, int distance, int duration) {
        Route r = new Route(distance, duration, getStopByCode(code1), getStopByCode(code2));
    }


    public Stop getStopByCode(String code) {
        for (Vertex<Stop> r : graph.vertices()) {
            if (code.equalsIgnoreCase(r.element().getCode())) {
                return r.element();
            }
        }
        return null;
    }

    public Edge<Route, Stop> getEdgeByRoute(Route route) {
        for (Edge<Route, Stop> e : graph.edges()) {
            if (e.element().getID().equalsIgnoreCase(route.getID())) {
                return e;
            }
        }
        return null;
    }

    public int getNoStop() {
        return graph.numVertices();
    }

    public int getNoRoutes() {
        return graph.numEdges();
    }

    public void reset() {
        graph = new GraphImpl<>();
    }


    @Override
    public String toString(){
        return graph.toString();
    }

    public SortedMap<Vertex<Stop>,Integer> noAdj(){
        System.out.println("Number of Vertices: "+ graph.vertices());
        SortedMap<Vertex<Stop>, Integer> NumberAdj= new TreeMap<>((o1, o2) -> {
            int comp = Integer.compare(graph.incidentEdges(o2).size(), graph.incidentEdges(o1).size());
            return comp == 0 ? 1 : comp;
        });
        for(Vertex<Stop> vertex: graph.vertices()){
            NumberAdj.put(vertex,graph.incidentEdges(vertex).size());
        }
        return NumberAdj;
    }

    public Graph<Stop,Route> getGraph(){
        return graph;
    }

    @Override
    public Memento saveState() {
        return new MyMemento(graph);
    }

    @Override
    public void restoreState(Memento state) {
        graph= (GraphImpl<Stop, Route>) state.getGraph();
    }

    public void load() throws FileNotFoundException {

        try{

            HashMap<Stop, ArrayList<Route>> map = loader.loadAll();

            ArrayList<Route> routes = new ArrayList<>();

            for(Stop key : map.keySet()){
                insertStop(key);
                for(Route route : map.get(key)){
                    try{insertRoute(route.getStops().getKey().getCode(), route.getStops().getValue().getCode(), route);}
                    catch (InvalidVertexException e){}
                }
            }
        }catch(FileNotFoundException err){} catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Operation not successfull");
    }

    private static Vertex<Stop> findMinCostVertex(Map<Vertex<Stop>, Integer> distances,
                                                   List<Vertex<Stop>> unvisited) {
        if(unvisited.isEmpty())
            return null; //embora nao esperado -> prog. defensiva.

        Vertex<Stop> minVertex = unvisited.get(0);
        int minCost = distances.get(minVertex);

        for(int i=1; i < unvisited.size(); i++) {
            Vertex<Stop> current = unvisited.get(i);
            int currentCost = distances.get(current);

            if(currentCost < minCost) {
                minVertex = current;
                minCost = currentCost;
            }
        }
        return minVertex;
    }

    public static void dijkstra(Digraph<Stop, Route> graph, Vertex<Stop> start,
                                Map<Vertex<Stop>, Integer> distances,
                                Map<Vertex<Stop>, Vertex<Stop>> predecessors,
                                Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors) {

        List<Vertex<Stop>> unvisited = new ArrayList<>();

        for (Vertex<Stop> v : graph.vertices()) {
            unvisited.add(v);
            distances.put(v, Integer.MAX_VALUE);
            predecessors.put(v, null);
            edgePredecessors.put(v, null);
        }
        distances.put(start, 0);

        while(!unvisited.isEmpty()) {
            Vertex<Stop> current = findMinCostVertex(distances, unvisited);

            if(current == null || distances.get(current) == Integer.MAX_VALUE)
                break; //escusado continuar, só restam vértices não atingíveis a partir de 'start'

            for (Edge<Route, Stop> e : graph.incidentEdges(current)) {
                Vertex<Stop> neighbor = graph.opposite(current, e);

                if(!unvisited.contains(neighbor)) continue;

                int pathCost = distances.get( current ) + e.element().getDistance();

                if(pathCost < distances.get(neighbor)) {
                    distances.put(neighbor, pathCost);
                    predecessors.put(neighbor, current);
                    edgePredecessors.put(neighbor, e);
                }
            }
            unvisited.remove(current);
        }
    }

    public static DijsktraResult<Stop, Route> minimumCostPath(Digraph<Stop, Route> graph,
                                                                Vertex<Stop> start,
                                                                Vertex<Stop> destination) {

        Map<Vertex<Stop>, Integer> distances = new HashMap<>();
        Map<Vertex<Stop>, Vertex<Stop>> predecessors = new HashMap<>();
        Map<Vertex<Stop>, Edge<Route, Stop>> edgePredecessors = new HashMap<>();

        dijkstra(graph, start, distances, predecessors, edgePredecessors);

        //consigo consultar distances e predecessors aqui!

        //qual o custo entre 'start' e 'destination'?
        int cost = distances.get(destination);

        //se distancia for "infinita", é porque não existe caminho até
        //o vértice 'destination'
        if(cost == Integer.MAX_VALUE)
            return new DijsktraResult<>(Integer.MAX_VALUE, null);

        //qual o caminho entre 'start' e 'destination'?
        List<Vertex<Stop>> path = new ArrayList<>();
        List<Edge<Route, Stop>> edgePath = new ArrayList<>();
        Vertex<Stop> current = destination;
        while (current != start) {
            path.add(0, current);
            edgePath.add(0, edgePredecessors.get(current));

            current = predecessors.get(current);
        }
        path.add(0, start);

        return new DijsktraResult<>(cost, path, edgePath);
    }


    private class MyMemento implements Memento{

        private Graph<Stop,Route> currGraph;

        public MyMemento(Graph<Stop,Route> g){
            currGraph=new GraphImpl<>();
            for(Vertex<Stop> s: graph.vertices()){
                currGraph.insertVertex(s.element());
            }
            for(Edge<Route,Stop> e : graph.edges()){
                currGraph.insertEdge(e.vertices()[0],e.vertices()[1],e.element() );
            }
        }

        @Override
        public Graph<Stop, Route> getGraph() {
            return currGraph;
        }
    }




}
