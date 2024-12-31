package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.util.Statistics;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static pepse.util.Constants.*;

/**
 * A class representing the flora in the game, responsible for creating and managing trees.
 */
public class Flora {

    /**
     * Function to get the height by x-coordinate.
     */
    Function<Float,Float> getHeightByX;
    /**
     * Consumer to add a game object.
     */
    private final BiConsumer<GameObject, Integer> addGameObj;
    /**
     * Consumer to handle fruit-related actions.
     */
    private final BiConsumer<Fruit, GameObject> fruitHandler;
    /**
     * Consumer to remove a game object.
     */
    private final BiConsumer<GameObject, Integer> removeGameObj;
    /**
     * Consumer to remove a list of game objects.
     */
    private final BiConsumer<List<GameObject>, Integer> removeListOfGameObj;
    /**
     * List of trees in the flora.
     */
    private List<PepseTree> trees;

    /**
     * Constructs a Flora object.
     *
     * @param getHeightByX function to get the height by x-coordinate
     * @param addGameObj consumer to add a game object
     * @param fruitHandler consumer to handle fruit-related actions
     * @param removeGameObj consumer to remove a game object
     * @param removeListOfGameObj consumer to remove a list of game objects
     */
    public Flora(Function<Float, Float> getHeightByX,
                 BiConsumer<GameObject, Integer> addGameObj,
                 BiConsumer<Fruit, GameObject> fruitHandler,
                 BiConsumer<GameObject, Integer> removeGameObj,
                 BiConsumer<List<GameObject>, Integer> removeListOfGameObj) {
        this.getHeightByX = getHeightByX;
        this.addGameObj = addGameObj;
        this.fruitHandler = fruitHandler;
        this.removeGameObj = removeGameObj;
        this.removeListOfGameObj = removeListOfGameObj;
        this.trees = new ArrayList<>();
    }

    /**
     * Creates trees in the specified range.
     *
     * @param minX the minimum x-coordinate
     * @param maxX the maximum x-coordinate
     * @return the list of added trees
     */
    public List<PepseTree> createInRange(int minX, int maxX){
        List<PepseTree> addedTrees = new ArrayList<>();
        for (float x = minX;
             x < maxX;
             x += (TREE_TRUNK_WIDTH + AVATAR_DIMENSIONS.x())) {
            Statistics statistics = new Statistics((int) x);

            if(statistics.flipCoin(TREE_CREATION_CHANCE)){
                float trunkHeight = (float) (Math.floor(getHeightByX.apply(x) / Block.SIZE) * Block.SIZE);
                Vector2 location = new Vector2(x, trunkHeight);
                PepseTree tree = new PepseTree(location,
                        this.addGameObj,
                        this.fruitHandler
                );
                this.trees.add(tree);
                addedTrees.add(tree);
            }
        }
        return addedTrees;
    }

    /**
     * Removes trees in the specified range.
     *
     * @param removeStart the start x-coordinate of the range
     * @param removeEnd the end x-coordinate of the range
     * @return the list of removed trees
     */
    public List<PepseTree> removeInRange(float removeStart, float removeEnd) {
        List<PepseTree> toRemove = new ArrayList<>();
        for (PepseTree tree : this.trees){
            float treeX = tree.getTrunk().getCenter().x();
            if(treeX >= removeStart && treeX <= removeEnd){
                toRemove.add(tree);
            }
        }
        for (PepseTree tree : toRemove){
            removeTree(tree);
            this.trees.remove(tree);
        }
        return toRemove;
    }

    /**
     * Removes a tree.
     *
     * @param tree the tree to be removed
     */
    private void removeTree(PepseTree tree) {
        this.removeGameObj.accept(tree.getTrunk(), Layer.STATIC_OBJECTS);
        this.removeListOfGameObj.accept(tree.getLeaves(), Layer.STATIC_OBJECTS);
        this.removeListOfGameObj.accept(tree.getFruits(), Layer.DEFAULT);
    }

    /**
     * Checks if a tree exists by a given fruit.
     * used for preventing fruit re-spawning on a non existing tree
     * @param fruit the fruit to check
     * @return true if the tree exists, false otherwise
     */
    public boolean treeExistsByFruit(GameObject fruit){
        if(!(fruit instanceof Fruit)){
            throw new IllegalArgumentException("arg isn't a fruit!");
        }
        return this.trees.stream().anyMatch(tree -> tree.getFruits().contains(fruit));
    }
}
