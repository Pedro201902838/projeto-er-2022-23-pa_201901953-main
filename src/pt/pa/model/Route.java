package pt.pa.model;

import javafx.util.Pair;

import java.util.Arrays;

public class Route {
    private String id;
    private int duration;
    private int distance;
    private Pair<Stop, Stop> stops;

    public Route(int distance, int duration, Stop stop1, Stop stop2){
        this.distance = distance;
        this.duration = duration;
        stops = new Pair<>(stop1, stop2);
        id = createID(stop1.getCode(), stop2.getCode());
    }

    public int getDuration() {
        return duration;
    }

    public boolean containsCity(String code){
        return (stops.getKey().getCode() == code ||stops.getValue().getCode() == code);
    }

    public int getDistance() {
        return distance;
    }

    public String getID() {
        return id;
    }
    public static String createID(String code1,  String code2) {
        String arr[] = {code1,code2};
        Arrays.sort(arr);
        return arr[0] + arr[1];
    }

    public Pair<Stop, Stop> getStops() {
        return stops;
    }


    public boolean equals(Object obj1, Object obj2){

        return false;
    }

    @Override
    public String toString(){
        return "{\n" +
                "ID: " + getID() +",\n" +
                "distance: " + this.distance +"kms,\n" +
                "duration: "+ this.duration+"m,\n" +
                "Stops: {stop1_code: "+this.stops.getKey().getCode() +", stop2_code: "+this.stops.getValue().getCode()+"}\n" +
                "}";
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
