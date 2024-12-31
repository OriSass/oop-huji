package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.util.Constants.DEFAULT_SKY_COLOR;
import static pepse.util.Constants.SKY_TAG;

/**
 * A class representing the sky in the game.
 */
public class Sky {

    /**
     * The basic color of the sky.
     */
    private static final Color BASIC_SKY_COLOR = DEFAULT_SKY_COLOR;

    /**
     * Creates a sky game object.
     *
     * @param windowDimensions The dimensions of the window.
     * @return A new GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));

        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }
}
