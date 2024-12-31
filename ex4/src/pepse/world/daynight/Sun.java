package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.util.Constants.SUN_DIMENSIONS;
import static pepse.util.Constants.SUN_TAG;

/**
 * A class representing the sun in the game.
 */
public class Sun {

    /**
     * Creates a sun game object.
     *
     * @param windowDimensions the dimensions of the game window
     * @param cycleLength the length of the day-night cycle
     * @return the created sun game object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        OvalRenderable ovalRenderable = new OvalRenderable(Color.yellow);
        Vector2 topLeftCorner = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 3);

        GameObject sun = new GameObject(topLeftCorner, SUN_DIMENSIONS, ovalRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        return sun;
    }
}
