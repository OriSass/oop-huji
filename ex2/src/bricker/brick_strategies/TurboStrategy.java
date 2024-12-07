package src.bricker.brick_strategies;

import danogl.GameObject;
import src.bricker.main.BrickerGameManager;

public class TurboStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    public TurboStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {

    }
}
