package pt.pa.graph;

import com.brunomnsilva.smartgraph.graph2.Digraph;
import com.brunomnsilva.smartgraph.graph2.Edge;
import com.brunomnsilva.smartgraph.graph2.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GraphImpl<V,E> implements Digraph<V,E> {
    private ArrayList<Vertex<V>> vertices;
    private ArrayList<ArrayList<Edge<E,V>>> matrix;

    public GraphImpl(){
        vertices = new ArrayList<>();
        matrix = new ArrayList<>();
    }


    /**
     * Devolve o vértice e verifica se existe.
     * @param v
     * @return
     * @throws InvalidVertexException
     */
    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null) throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.contains(v)) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    /**
     * Devolve a aresta e verifica se existe.
     * @param e
     * @return
     * @throws InvalidVertexException
     */
    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if (e == null) throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an edge.");
        }

        if (!edges().contains(edge)) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }


    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        int count = 0;

        //iterate and increment non null values
        for(ArrayList<Edge<E,V>> list : matrix){
            for(Edge<E,V> edge : list){
                if(edge != null) count++;
            }
        }

        return count;
    }

    @Override
    public Collection<Vertex<V>> vertices() {
        return  vertices;
    }

    @Override
    public Collection<Edge<E,V>> edges() {
        List<Edge<E,V>> x = new ArrayList<>();
        for(ArrayList<Edge<E,V>> list : matrix){
            for(Edge<E,V> edge : list){
                if(edge != null) x.add(edge);
            }
        }
        return x;
    }

    @Override
    public Collection<Edge<E, V>> incidentEdges(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);
        ArrayList<Edge<E,V>> list = new ArrayList<>();
        int idx = vertices.indexOf(v);
        for(ArrayList<Edge<E,V>> incidentList : matrix){
            Edge<E,V> idxValue = incidentList.get(idx);
            if(idxValue != null){
                list.add(idxValue);
            }
        }
        return list;
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {

        checkVertex(v);
        int idxVertex = vertices.indexOf(v);
        for(ArrayList<Edge<E,V>> incidentList: matrix){
            if(incidentList.get(idxVertex) == e){
                return vertices.get(matrix.indexOf(incidentList));
            }
        }
        for(Edge<E,V> edge : matrix.get(idxVertex)){
            if(edge == e){
                return vertices.get(matrix.get(idxVertex).indexOf(edge));
            }
        }
        return null;
    }

    @Override
    public boolean areAdjacent(Vertex<V> u, Vertex<V> v) throws InvalidVertexException {
        checkVertex(u);
        checkVertex(v);
        int idx1 = vertices.indexOf(u);
        int idx2 = vertices.indexOf(v);
        return (matrix.get(idx1).get(idx2) != null || matrix.get(idx2).get(idx1) != null);
    }

    @Override
    public Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if(!vertices.contains(vElement)){
            MyVertex mv = new MyVertex(vElement);
            vertices.add(mv);
            for(ArrayList<Edge<E,V>> list : matrix){
                list.add(null);
            }
            matrix.add(new ArrayList<>());
            for(int i = 0; i< vertices.size();i++){
                matrix.get(vertices.size()-1).add(null);
            }
            return mv;
        }
        return null;
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> u, Vertex<V> v, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        checkVertex(u);

        MyEdge me = new MyEdge(edgeElement);

        int idx1 = vertices.indexOf(v);
        int idx2 = vertices.indexOf(u);
        matrix.get(idx1).set(idx2,me);
        return me;
    }

    @Override
    public Edge<E, V> insertEdge(V vElement1, V vElement2, E edgeElement) throws InvalidVertexException, InvalidEdgeException {

        if(!vertices.contains(vElement1) && !vertices.contains(vElement2)){throw new InvalidVertexException("Vertex does not exist");}
        if(edges().contains(edgeElement)){throw new InvalidEdgeException("Edge already exists");}
        int idx1 = vertices.indexOf(vElement1);
        int idx2 = vertices.indexOf(vElement2);
        MyEdge me = new MyEdge(edgeElement);
        matrix.get(idx1).set(idx2,me);
        return me;
    }

    @Override
    public V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);
        int idx = vertices.indexOf(v);
        vertices.remove(idx);
        for(ArrayList<Edge<E,V>> row : matrix){
            row.remove(idx);
        }
        matrix.remove(idx);
        return v.element();
    }

    @Override
    public E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        checkEdge(e);
        E temp = e.element();
        int matrixSize = matrix.size();
        for(int i = 0; i<matrixSize; i++){
            for(int y = 0; y<matrixSize; y++){
                if(matrix.get(i).get(y) == e){
                    matrix.get(i).set(y,null);
                    return temp;
                }
            }
        }
        return null;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if(vertices.contains(newElement)){throw new InvalidVertexException("New element already exists");}
        V temp = v.element();
        vertices.set(vertices.indexOf(v), new MyVertex(newElement));
        return temp;
    }

    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        int matrixSize = matrix.size();
        E temp = e.element();
        checkEdge(e);
        for(int i = 0; i<matrixSize; i++){
            for(int y = 0; y<matrixSize; y++){
                if(matrix.get(i).get(y) == e){
                    matrix.get(i).set(y,new MyEdge(newElement));
                    return temp;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        checkVertex(outbound);
        ArrayList<Edge<E,V>> list = new ArrayList<>();
        int idxVertex = vertices.indexOf(outbound);
        for(Edge<E,V> rowValue : matrix.get(idxVertex)){
            if(rowValue != null){
                list.add(rowValue);
            }
        }
        return list;
    }

    class MyVertex implements Vertex<V> {

        V element;

        public MyVertex(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return element;
        }
    }

    class MyEdge implements Edge<E, V> {

        E element;

        public MyEdge(E element) {
            this.element = element;
        }


        @Override
        public E element() {
            return element;
        }

        @Override
        public Vertex<V>[] vertices() {
            int matrixSize = GraphImpl.this.matrix.size();
            for(int i = 0; i<matrixSize; i++){
                for(int y = 0; y<matrixSize; y++){
                    if(matrix.get(i).get(y) == element){
                        Vertex<V>[] arr = new Vertex[2];
                        arr[0] = GraphImpl.this.vertices.get(i);
                        arr[1] = GraphImpl.this.vertices.get(y);
                        return arr;
                    }
                }
            }
            return null;
        }
    }

}
