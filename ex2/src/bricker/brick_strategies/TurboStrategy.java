package src.bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import src.bricker.gameobjects.Ball;
import src.bricker.gameobjects.Puck;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.TURBO_BALL_IMAGE_PATH;
import static src.bricker.utils.Constants.TURBO_SPEED_FACTOR;

public class TurboStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;

    public TurboStrategy(BrickerGameManager brickerGameManager, ImageReader imageReader) {
        this.brickerGameManager = brickerGameManager;
        this.imageReader = imageReader;
    }

    /*
    gameObject1 == brick
    gameObject2 == ball | puck

     */
    @Override
    public void onCollision(GameObject gameObject1, GameObject gameObject2) {
        this.brickerGameManager.removeGameObject(gameObject1, Layer.STATIC_OBJECTS);
        handleTurbo(gameObject2);
    }

    private void handleTurbo(GameObject gameObject2) {
        if (gameObject2 instanceof Puck){
            return;
        }
        // if we got here the collision was caused by the "main" ball

        // check not in turbo mode
        Ball ball = (Ball) gameObject2;
        if (ball.isTurboOn()){
            return;
        }
        // if we got here we need to turn on turbo mode
        Renderable turboBallImage = this.imageReader.readImage(TURBO_BALL_IMAGE_PATH, true);
        ball.renderer().setRenderable(turboBallImage);
        ball.setVelocity(ball.getVelocity().mult(TURBO_SPEED_FACTOR));
        ball.setTurboOn(true);
        ball.resetCollisionCounter();
    }

    @Override
    public String toString() {
        return "Turbo";
    }
}
