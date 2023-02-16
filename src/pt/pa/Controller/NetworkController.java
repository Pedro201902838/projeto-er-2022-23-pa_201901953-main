package pt.pa.Controller;

import com.brunomnsilva.smartgraph.graph2.Vertex;
import pt.pa.memento.Caretaker;
import pt.pa.memento.NoMementoException;
import pt.pa.model.Network;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import java.util.SortedMap;

public class NetworkController {

    private Network network;

    private Caretaker c;


    public NetworkController(Network network){
        this.network=network;
        c=new Caretaker(network);
    }

    public void addRoute(String code1, String code2, int distance, int duration){
        c.save();
        network.insertRoute(code1,code2,distance,duration);
    }

    public void removeRoute(Route route){
        c.save();
        network.removeRoute(route);
    }

    public void removeRoute(String id){
        c.save();
        network.removeRoute(id);
    }

    public int getNoStops(){return network.getNoStop();}

    public int getNoRoutes(){return network.getNoRoutes();}

    public SortedMap<Vertex<Stop>, Integer> getAdjNo(){return network.noAdj();}

    public void undo() throws NoMementoException{
        c.restore();
    }
    public void reset(){
        c.save();
        network.reset();
    }
    public String showAll() {
        String str = network.toString();
        System.out.println(str);
        return str;
    }
}
