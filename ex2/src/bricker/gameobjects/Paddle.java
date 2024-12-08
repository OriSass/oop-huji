package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final float leftBoundary;
    private final float rightBoundary;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Allows to check what key is pressed for paddle movement
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener,
                  float leftBoundary, float rightBoundary) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        movePaddleByArrows();
    }


    private void movePaddleByArrows(){
        Vector2 movementDir = Vector2.ZERO;
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));

        keepBoundaries();
    }

    private void keepBoundaries(){
        float currentX = this.getTopLeftCorner().x();
        float currentY = this.getTopLeftCorner().y();

        float newX = currentX;
        if(currentX < this.leftBoundary){
            newX = this.leftBoundary;
        }
        else if(currentX > this.rightBoundary){
            newX = this.rightBoundary;
        }
        Vector2 newTopLeftCorner = new Vector2(newX, currentY);
        this.setTopLeftCorner( newTopLeftCorner);
    }
}
