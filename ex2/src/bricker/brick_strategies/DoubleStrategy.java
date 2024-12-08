package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import src.bricker.main.BrickerGameManager;

public class DoubleStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    public DoubleStrategy(BrickerGameManager brickerGameManager,
                          CollisionStrategy strategy1,
                          CollisionStrategy strategy2) {
        this.brickerGameManager = brickerGameManager;
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);
        this.strategy1.onCollision(gameObject1, gameObject2);
        this.strategy2.onCollision(gameObject1, gameObject2);
    }
}
