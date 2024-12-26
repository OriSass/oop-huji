package pepse.world.avatar.jump;

import java.util.ArrayList;
import java.util.List;

public class JumpNotifier {
    private final List<JumpObserver> observers = new ArrayList<>();

    // Method to add an observer
    public void addObserver(JumpObserver observer) {
        observers.add(observer);
    }

    // Method to remove an observer
    public void removeObserver(JumpObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers about the jump
    public void notifyObservers() {
        for (JumpObserver observer : observers) {
            observer.onJump();
        }
    }
}
