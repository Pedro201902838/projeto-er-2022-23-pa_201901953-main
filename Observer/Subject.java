package pt.pa.Observer;
import java.util.ArrayList;
import java.util.List;


public class Subject implements Observable {
    private List<Observer> observers;
    public Subject(){
        this.observers= new ArrayList<>();
    }
    @Override
    public void addObserser(Observer obserser) {
        if (!this.observers.contains(obserser)) {
            this.observers.add(obserser);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if(this.observers.contains(observer)){
            this.observers.remove(observer);
        }

    }

    @Override
    public void notifyObsersers() {
     for(Observer observer:this.observers){
         observer.update();
        }
    }
}
