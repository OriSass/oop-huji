package pepse.world.avatar;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.world.avatar.jump.JumpNotifier;

import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import static pepse.util.Constants.*;

public class Avatar extends GameObject {

    private final UserInputListener inputListener;
    private final Runnable energyDisplayRemover;
    BiConsumer<Float, WindowController> energyDisplayCallback;
    private Float energy;
    private final ImageReader imageReader;
    WindowController windowController;

    AnimationRenderable idleAnimation;
    AnimationRenderable jumpAnimation;
    AnimationRenderable runAnimation;

    JumpNotifier jumpNotifier;

    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, WindowController windowController,
                  BiConsumer<Float, WindowController> energyDisplayCallback, Runnable energyDisplayRemover) {
        super(topLeftCorner, AVATAR_DIMENSIONS, imageReader.readImage(AVATAR_IMAGE_PATH, true));

        this.imageReader = imageReader;
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.energyDisplayCallback = energyDisplayCallback;
        this.energyDisplayRemover = energyDisplayRemover;
        initAvatar();
        energyDisplayCallback.accept(this.energy, windowController);

    }

    private void initAvatar() {
        this.energy = DEFAULT_ENERGY_MAX;
        this.jumpNotifier = new JumpNotifier();

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        initAnimations();
        renderer().setRenderable(this.idleAnimation);
    }

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

    // will be used later in game dev
    public void addToEnergy(float energy){
        this.energy += energy;
        if(this.energy > DEFAULT_ENERGY_MAX){
            this.energy = DEFAULT_ENERGY_MAX;
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
}
