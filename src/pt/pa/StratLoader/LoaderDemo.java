package pt.pa.StratLoader;

import com.google.gson.*;
import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class LoaderDemo implements Loader {
//É possivel tornar esta classe mais amigavel (criando um metodo que retorna um array de arrays (um pra cada linha),
// o metodo seria chamado apenas quando precisamos de 1 valor de um ficheiro do dataset (ex: as coordenadas do FX para os stops))
    private HashMap<String, Stop> stops;
    private HashMap<String, Route> routes;


    public LoaderDemo() throws FileNotFoundException {
        stops = new HashMap<>();
        routes = new HashMap<>();
        //Gson gson = new Gson();
        //BufferedReader br;

        File input = new File("datasets/demo/data.json");
        try{
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();
            JsonArray jsonArrStop = fileObject.get("stops").getAsJsonArray();
            JsonArray jsonArrRoutes = fileObject.get("routes").getAsJsonArray();

            for(JsonElement stopElem : jsonArrStop){
                JsonObject stopJsonObj = stopElem.getAsJsonObject();

                String code = stopJsonObj.get("stopCode").getAsString();
                String name = stopJsonObj.get("stopName").getAsString();
                double xMap = Double.parseDouble(stopJsonObj.get("lat").getAsString());
                double yMap = Double.parseDouble(stopJsonObj.get("lon").getAsString());
                int xView = Integer.parseInt(stopJsonObj.get("x").getAsString());
                int yView = Integer.parseInt(stopJsonObj.get("y").getAsString());

                Stop stop = new Stop(code,name,xMap,yMap,xView,yView);

                stops.put(stop.getCode(),stop);
                System.out.println(stop.toString());
            }
            for(JsonElement routeElem : jsonArrRoutes){
                JsonObject routeJsonObj = routeElem.getAsJsonObject();

                int distance =Integer.parseInt(routeJsonObj.get("distance").getAsString());
                int duration =Integer.parseInt(routeJsonObj.get("duration").getAsString());
                Stop s1 = stops.get(routeJsonObj.get("stopCodeStart").getAsString());
                Stop s2 = stops.get(routeJsonObj.get("stopCodeEnd").getAsString());

                Route r = new Route(distance,duration,s1,s2);

                routes.put(r.getID(),r);

            }
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    @Override
    public HashMap<Stop, ArrayList<Route>> loadAll() throws FileNotFoundException {

    HashMap<Stop, ArrayList<Route>> map = new HashMap<>();
    for(Stop stop : stops.values()){
        map.put(stop, new ArrayList<>());
        for(Route route : routes.values()){
            int count = 0;
            if(route.containsCity(stop.getCode())){
                map.get(stop).add(route);
                count++;
            }
            /**if(count >=2){
                break;
            }**/
        }
    }
    return map;
}
}


