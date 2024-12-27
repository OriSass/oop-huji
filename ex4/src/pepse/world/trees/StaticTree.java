package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Statistics;
import supplied_code.ColorSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import static pepse.util.Constants.*;

public class StaticTree {

    static Random random = new Random();

    Vector2 location;
    Vector2 trunkDimensions;
    GameObject trunk;
    List<GameObject> leaves;

    public StaticTree(Vector2 location, BiConsumer<GameObject, Integer> addGameObj){
        this.trunkDimensions = new Vector2(TREE_TRUNK_WIDTH, getRandomTreeHeight());
        this.location = location.subtract(new Vector2(0, this.trunkDimensions.y()));
        createTrunk(addGameObj);
        createLeaves(addGameObj);
    }

    private void createTrunk(BiConsumer<GameObject, Integer> addGameObj) {
        RectangleRenderable trunkRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_BASE_COLOR));
        this.trunk = new GameObject(this.location, this.trunkDimensions, trunkRenderable);
        this.trunk.setTag(TRUNK_TAG);
        this.trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        addGameObj.accept(this.trunk, Layer.STATIC_OBJECTS);
    }

    private void createLeaves(BiConsumer<GameObject, Integer> addGameObj) {
        this.leaves = new ArrayList<>();

        Vector2 treeTopLeftCorner = getTreeTopLeftCorner();
        Vector2 lastLeafLocation = treeTopLeftCorner.add(LEAF_GRID);

        for (float y = treeTopLeftCorner.y(); y < lastLeafLocation.y(); y+= LEAF_DIMENSIONS.y()) {
            for (float x = treeTopLeftCorner.x(); x < lastLeafLocation.x(); x+= LEAF_DIMENSIONS.x()) {
                if(Statistics.flipCoin(LEAF_CREATION_CHANCE)){
                    createLeaf(addGameObj, x, y);
                }
            }
        }
    }

    private void createLeafAngleTransition(GameObject leaf) {
        new Transition<Float>(
                leaf,
                leaf.renderer()::setRenderableAngle,
                0f,
                LEAF_ANGLE_BOUND, // final angle
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                LEAF_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private void createLeafDimensionTransition(GameObject leaf) {
        new Transition<Vector2>(
                leaf,
                leaf::setDimensions,
                LEAF_DIMENSIONS,
                LEAF_DIMENSIONS.multX(LEAF_DIMENSION_TRANSITION_FACTOR), // final dimension
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                LEAF_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private void createLeaf(BiConsumer<GameObject, Integer> addGameObj, float x, float y) {
        RectangleRenderable leafRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_BASE_COLOR));
        Vector2 leafLocation = new Vector2(x, y);
        GameObject leaf = new GameObject(leafLocation, LEAF_DIMENSIONS, leafRenderable);
        leaf.setTag(LEAF_TAG);

        createLeafTransitions(leaf);

        addGameObj.accept(leaf, Layer.STATIC_OBJECTS);
        leaves.add(leaf);
    }

    private void createLeafTransitions(GameObject leaf) {
        float waitTime = random.nextFloat();
        new ScheduledTask(
                leaf,
                waitTime,
                false,
                () -> {
                    createLeafAngleTransition(leaf);
                    createLeafDimensionTransition(leaf);
                }
        );
    }

    private Vector2 getTreeTopLeftCorner() {
        float x = this.location.x() - LEAF_GRID.x() / 2 + TREE_TRUNK_WIDTH / 2;
        float y = this.location.y() - LEAF_GRID.y() / 2;
        return new Vector2(x,y);
    }

    private static float getRandomTreeHeight() {
        return random.nextFloat(TREE_MIN_HEIGHT, TREE_MAX_HEIGHT);
    }
}
