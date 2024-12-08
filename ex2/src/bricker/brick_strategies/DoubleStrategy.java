package src.bricker.brick_strategies;

import danogl.GameObject;
import src.bricker.main.BrickerGameManager;

/**
 * A collision strategy that combines two different strategies.
 * This strategy applies both strategies when a collision occurs.
 */
public class DoubleStrategy extends BasicCollisionStrategy {

    // The first collision strategy.
    private final CollisionStrategy strategy1;
    // The second collision strategy.
    private final CollisionStrategy strategy2;

    /**
     * Constructs a DoubleStrategy.
     *
     * @param brickerGameManager The game manager.
     * @param strategy1          The first collision strategy.
     * @param strategy2          The second collision strategy.
     */
    public DoubleStrategy(BrickerGameManager brickerGameManager,
                          CollisionStrategy strategy1,
                          CollisionStrategy strategy2) {
        super(brickerGameManager);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * Handles the collision between two game objects.
     * Applies both collision strategies to the game objects.
     *
     * @param gameObject1 The first game object.
     * @param gameObject2 The second game object.
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        this.strategy1.onCollision(gameObject1, gameObject2);
        this.strategy2.onCollision(gameObject1, gameObject2);
    }
}
