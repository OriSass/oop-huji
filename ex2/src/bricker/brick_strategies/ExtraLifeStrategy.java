package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import src.bricker.gameobjects.Heart;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.FALLING_HEART_VELOCITY;
import static src.bricker.utils.Constants.HEART_IMAGE_PATH;

/**
 * A collision strategy that adds an extra life to the game.
 * When a collision occurs, this strategy creates a falling heart object.
 */
public class ExtraLifeStrategy extends BasicCollisionStrategy {

    // The game manager.
    private final BrickerGameManager brickerGameManager;
    // Reads images for rendering.
    private final ImageReader imageReader;

    /**
     * Constructs an ExtraLifeStrategy.
     *
     * @param brickerGameManager The game manager.
     * @param imageReader        Reads images for rendering.
     */
    public ExtraLifeStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
    }

    /**
     * Handles the collision between two game objects.
     * Creates a falling heart object when a collision occurs.
     *
     * @param gameObject1 The first game object.
     * @param gameObject2 The second game object.
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        // create falling heart
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Heart heart = new Heart(gameObject1.getCenter(), heartImage, this.brickerGameManager);
        heart.setVelocity(FALLING_HEART_VELOCITY);
        this.brickerGameManager.addGameObject(heart, Layer.DEFAULT);
    }
}
