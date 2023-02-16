package pt.pa.memento;

public interface Originator {
    public Memento saveState();
    public void restoreState(Memento state);
}
