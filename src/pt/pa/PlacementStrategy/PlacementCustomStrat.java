package pt.pa.PlacementStrategy;

import com.brunomnsilva.smartgraph.graph2.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import java.util.Random;

import java.util.Collection;

import pt.pa.model.Stop;

    /**
     * Scatters the vertices randomly.
     *
     * @see SmartPlacementStrategy
     *
     * @author brunomnsilva
     */
    public class PlacementCustomStrat implements SmartPlacementStrategy {
        @Override
        public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<? extends SmartGraphVertex<V>> vertices) {
            Random rand = new Random();

            for (SmartGraphVertex<V> vertex : vertices) {
                try{
                    Stop stop = (Stop) vertex.getUnderlyingVertex().element();
                    double y = stop.getCoordsView().getKey();
                    double x = stop.getCoordsView().getValue();
                    vertex.setPosition(x, y);
                }catch(Exception e){
                    System.out.println("Coordinates not set");
                    double x = rand.nextDouble() * width;
                    double y = rand.nextDouble() * height;
                    vertex.setPosition(x, y);
                }
            }

        }
    }
