package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.bricker.main.BrickerGameManager;

import static src.bricker.utils.Constants.*;

public class Heart extends GameObject {
    private final BrickerGameManager brickerGameManager;

    public Heart(Vector2 heartPosition, Renderable heartImage, BrickerGameManager brickerGameManager) {
        super(heartPosition, HEART_DIMENSION, heartImage);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(PADDLE_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(this.brickerGameManager.getLifeCount() < MAX_LIFE_COUNT){
            this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
            this.brickerGameManager.updateLives();
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!this.getVelocity().isZero()){
            if(this.brickerGameManager.isOutOfBounds(this.getCenter())){
                this.brickerGameManager.removeGameObject(this, Layer.DEFAULT);
            }
        }
    }
}
