package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static pepse.util.Constants.BLOCK_TAG;
import static pepse.util.Constants.DEFAULT_BLOCK_SIZE;

public class Block extends GameObject {

    public static final int SIZE = DEFAULT_BLOCK_SIZE;

    public Block(Vector2 topLeftCorner, Renderable renderable){
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);

        physics().preventIntersectionsFromDirection(Vector2.UP);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(BLOCK_TAG);
    }
}
