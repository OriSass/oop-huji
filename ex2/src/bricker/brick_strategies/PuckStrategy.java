package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import src.bricker.main.BrickerGameManager;

public class PuckStrategy implements CollisionStrategy {
    private static final int NUMBER_OF_PUCKS = 2;
    private final BrickerGameManager brickerGameManager;

    public PuckStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /*
    gameObject1 is brick
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);
        this.brickerGameManager.createPucks(NUMBER_OF_PUCKS, gameObject1.getCenter());
    }


}
