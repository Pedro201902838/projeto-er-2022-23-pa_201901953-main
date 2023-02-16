package pt.pa.StratLoader;

import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Loader {
    public HashMap<Stop, ArrayList<Route>> loadAll()throws FileNotFoundException;
    public void loadStops()throws FileNotFoundException;
    public void loadFXCoordinatesForStops()throws FileNotFoundException;
    public void loadRouteDistance()throws FileNotFoundException;
    public void loadRouteDuration()throws FileNotFoundException;
}
