package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a paddle in the game.
 * Handles paddle-specific behavior such as movement and boundary constraints.
 */
public class Paddle extends GameObject {
    // The speed at which the paddle moves.
    private static final float MOVEMENT_SPEED = 300;
    // Listener for user input to control the paddle.
    private final UserInputListener inputListener;
    // The left boundary of the game area.
    private final float leftBoundary;
    // The right boundary of the game area.
    private final float rightBoundary;

    /**
     * Constructs a new Paddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener Allows to check what key is pressed for paddle movement.
     * @param leftBoundary  The left boundary of the game area.
     * @param rightBoundary The right boundary of the game area.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener,
                  float leftBoundary, float rightBoundary) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    /**
     * Updates the paddle's state.
     * Handles paddle movement based on user input.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        movePaddleByArrows();
    }

    /**
     * Moves the paddle based on arrow key input.
     * Sets the paddle's velocity based on the direction of the arrow keys pressed.
     */
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

    /**
     * Keeps the paddle within the game boundaries.
     * Adjusts the paddle's position if it moves outside the left or right boundary.
     */
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
