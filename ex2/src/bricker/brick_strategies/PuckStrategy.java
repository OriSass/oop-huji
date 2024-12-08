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

public class PuckStrategy extends BasicCollisionStrategy {
    private static final int NUMBER_OF_PUCKS = 2;
    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    public PuckStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader, SoundReader soundReader) {
        super(brickerGameManager);
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /*
    gameObject1 is brick
     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        super.onCollision(gameObject1, gameObject2);
        createPucks(gameObject1.getCenter());
    }

    private void createPucks(Vector2 location) {
        for (int i = 0; i < PuckStrategy.NUMBER_OF_PUCKS; i++){
            createPuck(location);
        }
    }

    private void createPuck(Vector2 location) {
        Renderable puckImage = this.imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = this.soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Puck puck = new Puck(Vector2.ZERO, PUCK_DIMENSIONS, puckImage,
                collisionSound, this.brickerGameManager);
        puck.setVelocity(getRandomUpperVelocity());
        puck.setCenter(location);
        this.brickerGameManager.addGameObject(puck, Layer.DEFAULT);
    }

    private Vector2 getRandomUpperVelocity(){
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        return new Vector2(velocityX, velocityY);
    }


}
