package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import static pepse.util.Constants.*;

public class Avatar extends GameObject {

    private final UserInputListener inputListener;
    private final Runnable energyDisplayRemover;
    BiConsumer<Float, WindowController> energyDisplayCallback;
    private Float energy;
    WindowController windowController;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, WindowController windowController,
                  BiConsumer<Float, WindowController> energyDisplayCallback, Runnable energyDisplayRemover) {
        super(topLeftCorner, AVATAR_DIMENSIONS, imageReader.readImage(AVATAR_IMAGE_PATH, true));
        this.windowController = windowController;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = DEFAULT_ENERGY_MAX;
        this.energyDisplayCallback = energyDisplayCallback;
        this.energyDisplayRemover = energyDisplayRemover;
        energyDisplayCallback.accept(this.energy, windowController);
    }

    /**
     * Updates the avatar's state.
     * Handles avatar movement based on user input.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        moveAvatarByArrows();
        jumpByEnter();
        regenerateEnergy();
        displayEnergy();
    }

    private void displayEnergy() {
        this.energyDisplayRemover.run();
        this.energyDisplayCallback.accept(this.energy, this.windowController);
    }

    private void regenerateEnergy() {
        if(transform().getVelocity().isZero()){
            this.energy += ENERGY_REGENERATION_RATE;
            if(this.energy > DEFAULT_ENERGY_MAX){
                this.energy = DEFAULT_ENERGY_MAX;
            }
        }
    }

    private void jumpByEnter() {
        if(this.energy < JUMP_ENERGY_COST){
            return;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            transform().setVelocityY(AVATAR_JUMP_VELOCITY_Y);
            this.energy -= JUMP_ENERGY_COST;
        }
    }

    /**
     * Moves the paddle based on arrow key input.
     * Sets the paddle's velocity based on the direction of the arrow keys pressed.
     */
    private void moveAvatarByArrows(){
        if(this.energy < HORIZONTAL_MOVEMENT_ENERGY_COST){
            transform().setVelocityX(0);
            return;
        }
        // if we got here we have enough energy to move
        float xVelocity = 0;
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVelocity -= AVATAR_MOVEMENT_SPEED;
        }
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVelocity += AVATAR_MOVEMENT_SPEED;;
        }
        transform().setVelocityX(xVelocity);
        if(xVelocity != 0){
            this.energy -= HORIZONTAL_MOVEMENT_ENERGY_COST;
        }
    }
}
