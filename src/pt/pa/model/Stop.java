package pt.pa.model;

import javafx.util.Pair;

public class Stop{
    private String code;
    private String name;
    private Pair<Double, Double> coordsMap;
    private Pair<Integer, Integer> coordsView;

    public Stop(String code, String name, double xMap, double yMap, int xView, int yView){
        this.code = code;
        this.name = name;

        coordsMap = new Pair<>(xMap, yMap);
        coordsView = new Pair<>(xView, yView);
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Pair<Double, Double> getCoordsMap()throws Exception {
        if(coordsMap == null) throw new Exception("Geographical coordinates not set!");
        return coordsMap;
    }

    public Pair<Integer, Integer> getCoordsView()throws Exception {
        if(coordsView == null) throw new Exception("View coordinates not set!");
        return coordsView;
    }

    public void setCoordsView(int x, int y) {
        this.coordsView = new Pair<>(x,y);
    }

    @Override
    public String toString(){
        return "{\n" +
                "code: " + this.code +",\n" +
                "name: "+ this.name+",\n" +
                "coodsMap: {x: "+this.coordsMap.getKey() +", y: "+this.coordsMap.getValue()+"},\n" +
                "coodsView: {x: "+ this.coordsView.getKey() +", y: "+this.coordsView.getValue()+"},\n" +
                "}";
    }
}
