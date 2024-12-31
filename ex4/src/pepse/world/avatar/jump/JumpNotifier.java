package pepse.world.avatar.jump;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that notifies registered observers when a jump event occurs.
 */
public class JumpNotifier {
    /**
     * List of observers that are notified of jump events.
     */
    private final List<JumpObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer the observer to be added
     */
    public void addObserver(JumpObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer the observer to be removed
     */
    public void removeObserver(JumpObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a jump event.
     */
    public void notifyObservers() {
        for (JumpObserver observer : observers) {
            observer.onJump();
        }
    }
}
