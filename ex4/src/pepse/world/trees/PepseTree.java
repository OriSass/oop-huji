package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.Statistics;
import pepse.util.ColorSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;

import static pepse.util.Constants.*;

/**
 * A class representing a tree in the game.
 */
public class PepseTree {

    /**
     * Random number generator for tree properties.
     */
    Random random;

    /**
     * Location of the tree.
     */
    Vector2 location;

    /**
     * Dimensions of the tree trunk.
     */
    Vector2 trunkDimensions;

    /**
     * The trunk game object of the tree.
     */
    private GameObject trunk;

    /**
     * List of leaf game objects of the tree.
     */
    private List<GameObject> leaves;

    /**
     * List of fruit game objects of the tree.
     */
    private List<GameObject> fruits;

    /**
     * Callback for handling fruit-related actions.
     */
    private BiConsumer<Fruit, GameObject> fruitHandler;

    /**
     * Constructs a new PepseTree instance.
     *
     * @param location The location of the tree.
     * @param addGameObj Callback to add a game object.
     * @param fruitHandler Callback to handle fruit-related actions.
     */
    public PepseTree(Vector2 location, BiConsumer<GameObject, Integer> addGameObj,
                     BiConsumer<Fruit, GameObject> fruitHandler){
        this.fruitHandler = fruitHandler;
        this.random = new Random(RANDOM_SEED);
        this.trunkDimensions = new Vector2(TREE_TRUNK_WIDTH, getRandomTreeHeight());
        this.location = location.subtract(new Vector2(0, this.trunkDimensions.y()));
        createTrunk(addGameObj);
        createTreeTop(addGameObj);
    }

    /**
     * Creates the trunk of the tree.
     *
     * @param addGameObj Callback to add a game object.
     */
    private void createTrunk(BiConsumer<GameObject, Integer> addGameObj) {
        RectangleRenderable trunkRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_BASE_COLOR));
        this.trunk = new GameObject(this.location, this.trunkDimensions, trunkRenderable);
        this.trunk.setTag(TRUNK_TAG);
        this.trunk.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.trunk.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        addGameObj.accept(this.trunk, Layer.STATIC_OBJECTS);
    }

    /**
     * Creates the top part of the tree, including leaves and fruits.
     *
     * @param addGameObj Callback to add a game object.
     */
    private void createTreeTop(BiConsumer<GameObject, Integer> addGameObj) {
        this.leaves = new ArrayList<>();
        this.fruits = new ArrayList<>();

        Vector2 treeTopLeftCorner = getTreeTopLeftCorner();
        Vector2 lastLeafLocation = treeTopLeftCorner.add(LEAF_GRID);

        Statistics statistics = new Statistics(Objects.hash(this.trunk.getCenter().x(), RANDOM_SEED));

        for (float y = treeTopLeftCorner.y(); y < lastLeafLocation.y();
             y+= LEAF_DIMENSIONS.y() + LEAF_PADDING) {
            for (float x = treeTopLeftCorner.x(); x < lastLeafLocation.x();
                 x+= LEAF_DIMENSIONS.x() + LEAF_PADDING) {
                if(statistics.flipCoin(LEAF_CREATION_CHANCE)){
                    createLeaf(addGameObj, x, y);
                }
                else if(statistics.flipCoin(FRUIT_CREATION_CHANCE)){
                    createFruit(addGameObj, x + FRUIT_PADDING, y + FRUIT_PADDING);
                }
            }
        }
    }

    /**
     * Creates a fruit game object.
     *
     * @param addGameObj Callback to add a game object.
     * @param x The x-coordinate of the fruit.
     * @param y The y-coordinate of the fruit.
     */
    private void createFruit(BiConsumer<GameObject, Integer> addGameObj, float x, float y) {
        OvalRenderable fruitRenderable =
                new OvalRenderable(ColorSupplier.approximateColor(FRUIT_BASE_COLOR));
        Vector2 fruitLocation = new Vector2(x, y);
        GameObject fruit = new Fruit(fruitLocation, FRUIT_DIMENSIONS, fruitRenderable, this.fruitHandler);
        fruit.setTag(FRUIT_TAG);

        addGameObj.accept(fruit, Layer.DEFAULT);
        fruits.add(fruit);
    }

    /**
     * Creates a transition for the leaf's angle.
     *
     * @param leaf The leaf game object.
     */
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

    /**
     * Creates a transition for the leaf's dimensions.
     *
     * @param leaf The leaf game object.
     */
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

    /**
     * Creates a leaf game object.
     *
     * @param addGameObj Callback to add a game object.
     * @param x The x-coordinate of the leaf.
     * @param y The y-coordinate of the leaf.
     */
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

    /**
     * Creates transitions for the leaf's properties.
     *
     * @param leaf The leaf game object.
     */
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

    /**
     * Gets the top-left corner of the tree's top part.
     *
     * @return The top-left corner of the tree's top part.
     */
    private Vector2 getTreeTopLeftCorner() {
        float x = this.location.x() - LEAF_GRID.x() / 2 + TREE_TRUNK_WIDTH / 2;
        float y = this.location.y() - LEAF_GRID.y() / 2;
        return new Vector2(x,y);
    }

    /**
     * Gets a random height for the tree trunk.
     *
     * @return A random height for the tree trunk.
     */
    private float getRandomTreeHeight() {
        return random.nextFloat(TREE_MIN_HEIGHT, TREE_MAX_HEIGHT);
    }

    /**
     * Gets the trunk game object.
     *
     * @return The trunk game object.
     */
    public GameObject getTrunk() {
        return trunk;
    }

    /**
     * Sets the trunk game object.
     *
     * @param trunk The trunk game object.
     */
    public void setTrunk(GameObject trunk) {
        this.trunk = trunk;
    }

    /**
     * Gets the list of leaf game objects.
     *
     * @return The list of leaf game objects.
     */
    public List<GameObject> getLeaves() {
        return leaves;
    }

    /**
     * Sets the list of leaf game objects.
     *
     * @param leaves The list of leaf game objects.
     */
    public void setLeaves(List<GameObject> leaves) {
        this.leaves = leaves;
    }

    /**
     * Gets the list of fruit game objects.
     *
     * @return The list of fruit game objects.
     */
    public List<GameObject> getFruits() {
        return fruits;
    }

    /**
     * Sets the list of fruit game objects.
     *
     * @param fruits The list of fruit game objects.
     */
    public void setFruits(List<GameObject> fruits) {
        this.fruits = fruits;
    }
}
