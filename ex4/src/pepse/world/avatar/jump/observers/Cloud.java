package pepse.world.avatar.jump.observers;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.avatar.jump.JumpObserver;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static pepse.PepseGameManager.CLOUD_CYCLE_LENGTH;
import static pepse.util.Constants.*;

/**
 * A class representing a cloud that observes avatar jumps and creates rain.
 */
public class Cloud implements JumpObserver {

    /**
     * Random number generator for creating raindrops.
     */
    private static Random random = new Random();

    /**
     * Base color of the cloud.
     */
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    /**
     * Indexes representing the structure of the cloud blocks.
     */
    private static final List<List<Integer>> cloudBlockIndexes = List.of(
            List.of(0, 1, 1),
            List.of(1, 1, 0),
            List.of(1, 0, 1),
            List.of(1, 1, 1),
            List.of(1, 1, 0),
            List.of(1, 1, 1),
            List.of(0, 1, 1)
    );
    /**
     * Renderable for the cloud blocks.
     */
    private static final RectangleRenderable renderable =
            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));
    /**
     * List of cloud blocks that make up the cloud.
     */
    private final ArrayList<Block> cloudBlocks;
    /**
     * Callback to add a game object to the game.
     */
    private final BiConsumer<GameObject, Integer> addGameObj;
    /**
     * Callback to determine if a raindrop should be created.
     */
    private final Function<Float, Boolean> isRainDropCreatedCallback;

    /**
     * Constructs a Cloud object.
     *
     * @param windowDimensions the dimensions of the game window
     * @param addGameObj a callback to add a game object to the game
     * @param isRainDropCreatedCallback a callback to determine if a raindrop should be created
     */
    public Cloud(Vector2 windowDimensions,
                 BiConsumer<GameObject, Integer> addGameObj,
                 Function<Float, Boolean> isRainDropCreatedCallback
                 ) {
        this.addGameObj = addGameObj;
        this.isRainDropCreatedCallback = isRainDropCreatedCallback;
        this.cloudBlocks = new ArrayList<>();
        for (int i = 0; i < cloudBlockIndexes.size(); i++){
            List<Integer> cloudBlockRow = cloudBlockIndexes.get(i);
            for(int j = 0; j < cloudBlockRow.size(); j++){
                if(cloudBlockRow.get(j) == 1){
                    Block cloudCellBlock = createCloudCellBlock(windowDimensions, i, j);
                    cloudBlocks.add(cloudCellBlock);
                    this.addGameObj.accept(cloudCellBlock, Layer.BACKGROUND);
                }
            }
        }
    }

    /**
     * Creates a cloud cell block at the specified row and column.
     *
     * @param windowDimensions the dimensions of the game window
     * @param row the row index of the cloud cell block
     * @param col the column index of the cloud cell block
     * @return the created cloud cell block
     */
    private Block createCloudCellBlock(Vector2 windowDimensions, int row, int col) {
        Vector2 padding = new Vector2(row * Block.SIZE, col * Block.SIZE);
        Vector2 currentLocation = CLOUD_START_LOCATION.add(padding);

        Block cloudCellBlock = new Block(currentLocation, renderable);
        cloudCellBlock.setTag(CLOUD_TAG);
        cloudCellBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        createCloudTransition(cloudCellBlock, windowDimensions, currentLocation);

        return cloudCellBlock;
    }

    /**
     * Creates a transition for the cloud block to move across the screen.
     *
     * @param cloudBlock the cloud block to create the transition for
     * @param windowDimensions the dimensions of the game window
     * @param currentLocation the current location of the cloud block
     */
    public void createCloudTransition(Block cloudBlock, Vector2 windowDimensions, Vector2 currentLocation){
        new Transition<Float>(
                cloudBlock,
                (_) -> {
                    if(cloudBlock.getTopLeftCorner().x() > windowDimensions.x() + CLOUD_LENGTH * 2 ){
                        cloudBlock.setCenter(new Vector2(-cloudBlock.getDimensions().x(),
                                cloudBlock.getCenter().y()));
                    }
                    else{
                        cloudBlock.setTopLeftCorner(
                                cloudBlock.getTopLeftCorner().add(CLOUD_MOVEMENT_VECTOR));
                    }
                },
                0f,
                0f, // final cloud location
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CLOUD_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }

    /**
     * Called when the avatar jumps, triggering the creation of rain.
     */
    @Override
    public void onJump() {
        createRain();
    }

    /**
     * Creates rain by generating raindrops at random positions within the cloud.
     */
    private void createRain() {
        RectangleRenderable dropRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Color.BLUE));
        float cloudStartX = this.cloudBlocks.getFirst().getTopLeftCorner().x();
        float cloudEndX = this.cloudBlocks.getLast().getTopLeftCorner().x();
        float cloudEndY = this.cloudBlocks.getLast().getTopLeftCorner().y();
        Vector2 dimensions = new Vector2(20f, 20f);

        for (float dropX = cloudStartX; dropX <= cloudEndX; dropX+= Block.SIZE) {
            if (isRainDropCreatedCallback.apply(RAIN_DROP_CHANCE)){
                float dropY = random.nextFloat(cloudEndY, cloudEndY + Block.SIZE);
                Vector2 dropLocation = new Vector2(dropX, dropY);
                GameObject drop = new GameObject(dropLocation, dimensions, dropRenderable);
                createDropTransition(drop);
                drop.transform().setAccelerationY(GRAVITY);
                drop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                this.addGameObj.accept(drop, Layer.FOREGROUND);
            }
        }

    }

    /**
     * Creates a transition for the raindrop to fade out.
     *
     * @param drop the raindrop to create the transition for
     */
    private void createDropTransition(GameObject drop) {
        new Transition<Float>(
                drop,
                drop.renderer()::setOpaqueness,
                VISIBLE_OPACITY,
                INVISIBLE_OPACITY, // final opacity
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                RAIN_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_ONCE, null);
    }
}
