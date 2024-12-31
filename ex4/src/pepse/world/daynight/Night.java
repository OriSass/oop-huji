package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.util.Constants.NIGHT_TAG;

/**
 * A class representing the night effect in the game.
 */
public class Night {

    /**
     * Creates a night effect game object.
     *
     * @param windowDimensions the dimensions of the game window
     * @param cycleLength the length of the night-day cycle
     * @return the created night game object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        RectangleRenderable blackRectangleRenderable = new RectangleRenderable(Color.black);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, blackRectangleRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        return night;
    }
}
