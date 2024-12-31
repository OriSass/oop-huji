package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static pepse.util.Constants.BLOCK_TAG;
import static pepse.util.Constants.DEFAULT_BLOCK_SIZE;

/**
 * A class representing a block in the game.
 */
public class Block extends GameObject {

    /**
     * The size of the block.
     */
    public static final int SIZE = DEFAULT_BLOCK_SIZE;

    /**
     * Constructs a new Block instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable){
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        setTag(BLOCK_TAG);
    }
}
