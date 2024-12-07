package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import src.bricker.gameobjects.Heart;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.FALLING_HEART_VELOCITY;
import static src.bricker.utils.Constants.HEART_IMAGE_PATH;

public class ExtraLifeStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;

    public ExtraLifeStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader) {
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
    }

    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);

        // create falling heart
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Heart heart = new Heart(gameObject1.getCenter(), heartImage, this.brickerGameManager);
        heart.setVelocity(FALLING_HEART_VELOCITY);
        this.brickerGameManager.addGameObject(heart, Layer.DEFAULT);
        this.brickerGameManager.addFallingHeart(heart);
    }
}
