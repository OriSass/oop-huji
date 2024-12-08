package src.bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for defining collision strategies for game objects.
 * Implementations of this interface will define specific behaviors
 * when a collision occurs between two game objects.
 */
public interface CollisionStrategy {
    /**
     * Handles the collision between two game objects.
     *
     * @param gameObject1 The first game object involved in the collision.
     * @param gameObject2 The second game object involved in the collision.
     */
    void onCollision(GameObject gameObject1, GameObject gameObject2);
}
