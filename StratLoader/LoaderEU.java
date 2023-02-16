package pt.pa.StratLoader;

import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class LoaderEU implements Loader {
//É possivel tornar esta classe mais amigavel (criando um metodo que retorna um array de arrays (um pra cada linha),
// o metodo seria chamado apenas quando precisamos de 1 valor de um ficheiro do dataset (ex: as coordenadas do FX para os stops))
    private HashMap<String, Stop> stops;
    private HashMap<String, Route> routes;


    public LoaderEU(){
        stops = new HashMap<>();
        routes = new HashMap<>();
    }

    @Override
    public HashMap<Stop, ArrayList<Route>> loadAll() throws FileNotFoundException {
    stops  = new HashMap<>();
    routes  = new HashMap<>();
    loadStops();
    loadFXCoordinatesForStops();
    loadRouteDuration();
    loadRouteDistance();

    HashMap<Stop, ArrayList<Route>> map = new HashMap<>();
    for(Stop stop : stops.values()){
        map.put(stop, new ArrayList<>());
        for(Route route : routes.values()){
            int count = 0;
            if(route.containsCity(stop.getCode())){
                map.get(stop).add(route);
                count++;
            }
            if(count >=2){
                break;
            }
        }
    }
    return map;
}

@Override
public void loadStops() throws FileNotFoundException{
    try {

        Scanner scanner = new Scanner(new File("datasets/europe/stops.txt"));
        String line = "";
        int count = 0;
        while (scanner.hasNext()) {

            line = scanner.nextLine();
            String tokens[] = line.split("\\s+");
            int tokenSize = tokens.length;
            if(count >= 4){

                double lat = Double.parseDouble(tokens[tokenSize-1]);
                double lon = Double.parseDouble(tokens[tokenSize-2]);
                String code = tokens[0];
                String name = "";
                for(int i= 1; i<(tokenSize-2);i++){

                    name += (" " + tokens[i]);
                }
                Stop stop = new Stop(code, name, lat, lon, -1, -1);
                stops.put(code, stop);
                }
            count++;
        }

        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
    }



    public void loadFXCoordinatesForStops() throws FileNotFoundException{
            try {

                Scanner scanner = new Scanner(new File("datasets/europe/xy.txt"));
                String line = "";
                int count = 0;
                while (scanner.hasNext()) {

                    line = scanner.nextLine();
                    String tokens[] = line.split("\\s+");
                    int tokenSize = tokens.length;
                    if(count >= 3){

                        int x = Integer.parseInt(tokens[tokenSize-1]);
                        int y = Integer.parseInt(tokens[tokenSize-2]);
                        String code = tokens[0];

                        if(stops.containsKey(code)){
                            stops.get(code).setCoordsView(x,y);
                        }
                        }
                    count++;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
    public void loadRouteDuration() throws FileNotFoundException{
            try {

                Scanner scanner = new Scanner(new File("datasets/europe/routes-duration.txt"));
                String line = "";
                int count = 0;
                while (scanner.hasNext()) {

                    line = scanner.nextLine();
                    String tokens[] = line.split("\\s+");
                    int tokenSize = tokens.length;
                    if(count >= 4){

                        int duration = Integer.parseInt(tokens[tokenSize-1]);
                        String code1 = tokens[tokenSize-2];
                        String code2 = tokens[tokenSize-3];

                        Route route = new Route(-1, duration, stops.get(code1), stops.get(code2));
                        if(stops.containsKey(code1) && stops.containsKey(code2) && !routes.containsKey(route.getID())){
                            routes.put(route.getID(),route);
                        }

                    }
                    count++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }
    public void loadRouteDistance() throws FileNotFoundException{
            try {

                Scanner scanner = new Scanner(new File("datasets/europe/routes-distance.txt"));
                String line = "";
                int count = 0;
                while (scanner.hasNext()) {

                    line = scanner.nextLine();
                    String tokens[] = line.split("\\s+");
                    int tokenSize = tokens.length;
                    if(count >= 4){

                        int distance = Integer.parseInt(tokens[tokenSize-1]);
                        String id = Route.createID(tokens[tokenSize-2], tokens[tokenSize-3]);

                        if(routes.containsKey(id)){
                            routes.get(id).setDistance(distance);
                        }
                        }
                    count++;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

}


