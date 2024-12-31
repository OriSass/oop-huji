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
    private List<PespseTree> trees;

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

    public List<PespseTree> createInRange(int minX, int maxX){
        List<PespseTree> addedTrees = new ArrayList<>();
        for (float x = minX;
             x < maxX;
             x += (TREE_TRUNK_WIDTH + AVATAR_DIMENSIONS.x())) {
            Statistics statistics = new Statistics((int) x);

            if(statistics.flipCoin(TREE_CREATION_CHANCE)){
                float trunkHeight = (float) (Math.floor(getHeightByX.apply(x) / Block.SIZE) * Block.SIZE);
                Vector2 location = new Vector2(x, trunkHeight);
                PespseTree tree = new PespseTree(location,
                        this.addGameObj,
                        this.fruitHandler
                );
                this.trees.add(tree);
                addedTrees.add(tree);
            }
        }
        return addedTrees;
    }

    public List<PespseTree> removeInRange(float removeStart, float removeEnd) {
        List<PespseTree> toRemove = new ArrayList<>();
        for (PespseTree tree : this.trees){
            float treeX = tree.getTrunk().getCenter().x();
            if(treeX >= removeStart && treeX <= removeEnd){
                toRemove.add(tree);
            }
        }
        for (PespseTree tree : toRemove){
            removeTree(tree);
            this.trees.remove(tree);
        }
        return toRemove;
    }

    private void removeTree(PespseTree tree) {
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
