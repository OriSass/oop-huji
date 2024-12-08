package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.bricker.gameobjects.Puck;
import src.bricker.main.BrickerGameManager;

import java.util.Random;

import static src.bricker.utils.Constants.*;

/**
 * A collision strategy that creates multiple pucks when a collision occurs.
 * This strategy creates a specified number of pucks at the location of the collision.
 */
public class PuckStrategy extends BasicCollisionStrategy {

    // The game manager
    private final BrickerGameManager brickerGameManager;
    // Reads images for rendering
    private final ImageReader imageReader;
    // Reads sounds for playback
    private final SoundReader soundReader;

    /**
     * Constructs a PuckStrategy.
     *
     * @param brickerGameManager The game manager.
     * @param imageReader        Reads images for rendering.
     * @param soundReader        Reads sounds for playback.
     */
    public PuckStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader, SoundReader soundReader) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * Handles the collision between two game objects.
     * Creates multiple pucks at the location of the collision.
     *
     * @param gameObject1 The first game object (brick).
     * @param gameObject2 The second game object.
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        createPucks(gameObject1.getCenter());
    }

    /**
     * Creates multiple pucks at the specified location.
     *
     * @param location The location to create the pucks.
     */
    private void createPucks(Vector2 location) {
        for (int i = 0; i < NUMBER_OF_PUCKS; i++){
            createPuck(location);
        }
    }

    /**
     * Creates a puck at the specified location.
     *
     * @param location The location to create the puck.
     */
    private void createPuck(Vector2 location) {
        Renderable puckImage = this.imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = this.soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Puck puck = new Puck(Vector2.ZERO, PUCK_DIMENSIONS, puckImage,
                collisionSound, this.brickerGameManager);
        puck.setVelocity(getRandomUpperVelocity());
        puck.setCenter(location);
        this.brickerGameManager.addGameObject(puck, Layer.DEFAULT);
    }

    /**
     * Generates a random velocity vector pointing upwards.
     *
     * @return A random velocity vector.
     */
    private Vector2 getRandomUpperVelocity(){
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        return new Vector2(velocityX, velocityY);
    }


}
