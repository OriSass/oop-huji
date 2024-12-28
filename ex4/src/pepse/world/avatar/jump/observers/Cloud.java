package pepse.world.avatar.jump.observers;

import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.avatar.jump.JumpObserver;
import supplied_code.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static pepse.PepseGameManager.CLOUD_CYCLE_LENGTH;
import static pepse.util.Constants.*;

public class Cloud implements JumpObserver {

    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    private static final List<List<Integer>> cloudBlockIndexes = List.of(
            List.of(0, 1, 1),
            List.of(1, 1, 0),
            List.of(1, 0, 1),
            List.of(1, 1, 1),
            List.of(1, 1, 0),
            List.of(1, 1, 1),
            List.of(0, 1, 1)
    );

    private static final RectangleRenderable renderable =
            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));


    public static List<Block> create(Vector2 windowDimensions){
        List<Block> cloudBlocks = new ArrayList<>();
        for (int i = 0; i < cloudBlockIndexes.size(); i++){
            List<Integer> cloudBlockRow = cloudBlockIndexes.get(i);
            for(int j = 0; j < cloudBlockRow.size(); j++){
                if(cloudBlockRow.get(j) == 1){
                    Block cloudCellBlock = createCloudCellBlock(windowDimensions, i, j);
                    cloudBlocks.add(cloudCellBlock);
                }
            }
        }
        return cloudBlocks;
    }

    private static Block createCloudCellBlock(Vector2 windowDimensions, int row, int col) {
        Vector2 padding = new Vector2(row * Block.SIZE, col * Block.SIZE);
        Vector2 currentLocation = CLOUD_START_LOCATION.add(padding);
        Block cloudCellBlock = new Block(currentLocation, renderable);
        cloudCellBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        createCloudTransition(cloudCellBlock, windowDimensions, currentLocation);
        return cloudCellBlock;
    }

    public static void createCloudTransition(Block cloudBlock, Vector2 windowDimensions, Vector2 currentLocation){
        new Transition<Float>(
                cloudBlock,
                (_) -> {
                    if(cloudBlock.getTopLeftCorner().x() > windowDimensions.x() + CLOUD_LENGTH * 2 ){
                        cloudBlock.setCenter(new Vector2(-cloudBlock.getDimensions().x(), cloudBlock.getCenter().y()));
                    }
                    else{
                        cloudBlock.setTopLeftCorner(cloudBlock.getTopLeftCorner().add(CLOUD_MOVEMENT_VECTOR));
                    }
                },
                0f,
                0f, // final cloud location
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                CLOUD_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }

    @Override
    public void onJump() {

    }
}
