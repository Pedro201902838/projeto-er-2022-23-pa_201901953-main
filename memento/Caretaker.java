package pt.pa.memento;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;


public class Caretaker implements Iterable<Memento>{
    private Originator originator;
    private Stack<Memento> stateStack;

    public Caretaker(Originator originator) {
        this.originator = originator;
        this.stateStack = new Stack<>();
    }

    public void save() {
        Memento state = originator.saveState();
        stateStack.push(state);
    }

    public boolean canUndo() {
        return !stateStack.isEmpty();
    }

    public void restore() throws EmptyStackException {
        if(!canUndo())
            throw new EmptyStackException();
        originator.restoreState(stateStack.pop());
    }

    @Override
    public Iterator<Memento> iterator() {
        return stateStack.iterator();
    }
}