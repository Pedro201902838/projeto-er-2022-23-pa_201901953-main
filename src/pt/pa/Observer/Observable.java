package pt.pa.Observer;

public interface Observable {

    void addObserser(Observer obserser);
    void removeObserver(Observer observer);
    void notifyObsersers();
}
