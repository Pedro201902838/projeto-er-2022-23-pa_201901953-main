package pt.pa.StratLoader;

import pt.pa.model.Route;
import pt.pa.model.Stop;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Loader {
    public HashMap<Stop, ArrayList<Route>> loadAll() throws Exception;
}
