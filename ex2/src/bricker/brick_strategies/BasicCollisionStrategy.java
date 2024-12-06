package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import src.bricker.BrickerGameManager;

public class BasicCollisionStrategy implements CollisionStrategy {


    private final BrickerGameManager brickerGameManager;

    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);
    }
}
