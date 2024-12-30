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

public class Flora {

    Function<Float,Float> getHeightByX;
    private final BiConsumer<GameObject, Integer> addGameObj;
    private final BiConsumer<Fruit, GameObject> fruitHandler;
    private final BiConsumer<GameObject, Integer> removeGameObj;
    private final BiConsumer<List<GameObject>, Integer> removeListOfGameObj;
    private List<StaticTree> trees;

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

    public List<StaticTree> createInRange(int minX, int maxX){
        List<StaticTree> addedTrees = new ArrayList<>();
        for (float x = minX;
             x < maxX;
             x += (TREE_TRUNK_WIDTH + AVATAR_DIMENSIONS.x())) {
            if(Statistics.flipCoin(TREE_CREATION_CHANCE)){
                float trunkHeight = (float) (Math.floor(getHeightByX.apply(x) / Block.SIZE) * Block.SIZE);
                Vector2 location = new Vector2(x, trunkHeight);
                StaticTree tree = new StaticTree(location,
                        this.addGameObj,
                        this.fruitHandler);
                this.trees.add(tree);
                addedTrees.add(tree);
            }
        }
        return addedTrees;
    }

    public List<StaticTree> removeInRange(float removeStart, float removeEnd) {
        List<StaticTree> toRemove = new ArrayList<>();
        for (StaticTree tree : this.trees){
            float treeX = tree.getTrunk().getCenter().x();
            if(treeX >= removeStart && treeX <= removeEnd){
                toRemove.add(tree);
            }
        }
        for (StaticTree tree : toRemove){
            removeTree(tree);
            this.trees.remove(tree);
        }
        return toRemove;
    }

    private void removeTree(StaticTree tree) {
        this.removeGameObj.accept(tree.getTrunk(), Layer.STATIC_OBJECTS);
        this.removeListOfGameObj.accept(tree.getLeaves(), Layer.STATIC_OBJECTS);
        this.removeListOfGameObj.accept(tree.getFruits(), Layer.DEFAULT);
    }

    public boolean treeExistsByFruit(GameObject fruit){
        if(!(fruit instanceof Fruit)){
            throw new IllegalArgumentException("arg isn't a fruit!");
        }
        return this.trees.stream().anyMatch(tree -> tree.getFruits().contains(fruit));
    }
}
