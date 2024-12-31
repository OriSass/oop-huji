package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import static pepse.util.Constants.*;

/**
 * A class representing the avatar in the game.
 */
public class Avatar extends GameObject {

    /**
     * Listener for user input.
     */
    private final UserInputListener inputListener;

    /**
     * Runnable to remove the energy display.
     */
    private final Runnable energyDisplayRemover;

    /**
     * Runnable to notify observers when the avatar jumps.
     */
    private final Runnable notifyJumpObservers;

    /**
     * Callback to update the energy display.
     */
    BiConsumer<Float, WindowController> energyDisplayCallback;

    /**
     * The current energy level of the avatar.
     */
    private Float energy;

    /**
     * Reader for loading images.
     */
    private final ImageReader imageReader;

    /**
     * Controller for the game window.
     */
    WindowController windowController;

    /**
     * Animation for the avatar when idle.
     */
    AnimationRenderable idleAnimation;

    /**
     * Animation for the avatar when jumping.
     */
    AnimationRenderable jumpAnimation;

    /**
     * Animation for the avatar when running.
     */
    AnimationRenderable runAnimation;

    /**
     * Constructs a new Avatar instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param inputListener Listener for user input.
     * @param imageReader Reader for loading images.
     * @param windowController Controller for the game window.
     * @param energyDisplayCallback Callback to update the energy display.
     * @param energyDisplayRemover Runnable to remove the energy display.
     * @param notifyJumpObservers Runnable to notify observers when the avatar jumps.
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, WindowController windowController,
                  BiConsumer<Float, WindowController> energyDisplayCallback, Runnable energyDisplayRemover,
                  Runnable notifyJumpObservers) {
        super(topLeftCorner, AVATAR_DIMENSIONS, imageReader.readImage(AVATAR_IMAGE_PATH, true));

        this.imageReader = imageReader;
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.energyDisplayCallback = energyDisplayCallback;
        this.energyDisplayRemover = energyDisplayRemover;
        this.notifyJumpObservers = notifyJumpObservers;
        initAvatar();
        energyDisplayCallback.accept(this.energy, windowController);

    }

    /**
     * Initializes the avatar's properties.
     */
    private void initAvatar() {
        this.energy = DEFAULT_ENERGY_MAX;
        setTag(AVATAR_TAG);

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        initAnimations();
        renderer().setRenderable(this.idleAnimation);
    }

    /**
     * Initializes the avatar's animations.
     */
    private void initAnimations() {
        this.idleAnimation = new AnimationRenderable(
                IDLE_ANIMATION_ARR,
                this.imageReader, true, ANIMATION_FRAME_DURATION);
        this.jumpAnimation = new AnimationRenderable(
                JUMP_ANIMATION_ARR,
                this.imageReader, true, ANIMATION_FRAME_DURATION);
        this.runAnimation = new AnimationRenderable(
                RUN_ANIMATION_ARR,
                this.imageReader, true, ANIMATION_FRAME_DURATION);
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
        updateAnimation();
    }

    /**
     * Updates the avatar's animation based on its state.
     */
    private void updateAnimation() {
        if(getVelocity().isZero()){
            renderer().setRenderable(this.idleAnimation);
        }
        else if (getVelocity().y() != 0){
            renderer().setRenderable(this.jumpAnimation);
        }
        else{
            renderer().setRenderable(this.runAnimation);
        }
    }

    /**
     * Displays the avatar's energy level.
     */
    private void displayEnergy() {
        this.energyDisplayRemover.run();
        this.energyDisplayCallback.accept(this.energy, this.windowController);
    }

    /**
     * Regenerates the avatar's energy when it is not moving.
     */
    private void regenerateEnergy() {
        if(transform().getVelocity().isZero()){
            this.energy += ENERGY_REGENERATION_RATE;
            if(this.energy > DEFAULT_ENERGY_MAX){
                this.energy = DEFAULT_ENERGY_MAX;
            }
        }
    }

    /**
     * Adds energy to the avatar.
     * called when the avatar eats a fruit.
     *
     * @param energy The amount of energy to add.
     * @return True if energy was added, false otherwise.
     */
    public boolean addToEnergy(float energy){
        if(this.energy >= DEFAULT_ENERGY_MAX){
            return false;
        }
        this.energy += energy;
        if(this.energy > DEFAULT_ENERGY_MAX){
            this.energy = DEFAULT_ENERGY_MAX;
        }
        return true;
    }

    /**
     * Makes the avatar jump if the space key is pressed and the avatar has enough energy.
     */
    private void jumpByEnter() {
        if(this.energy < JUMP_ENERGY_COST){
            return;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            this.notifyJumpObservers.run();
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
            renderer().setIsFlippedHorizontally(true);
        }
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVelocity += AVATAR_MOVEMENT_SPEED;
            renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVelocity);
        if(xVelocity != 0){
            this.energy -= HORIZONTAL_MOVEMENT_ENERGY_COST;
        }
    }

    /**
     * Determines if this avatar should collide with another game object.
     *
     * @param other The other game object.
     * @return True if the other game object is allowed to collide with the avatar, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return ALLOWED_TO_COLLIDE_WITH_AVATAR.stream().anyMatch(tag -> tag.equals(other.getTag()));
    }
}
