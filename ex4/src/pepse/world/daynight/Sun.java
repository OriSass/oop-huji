package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

import static pepse.util.Constants.SUN_DIMENSIONS;

public class Sun {

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        OvalRenderable ovalRenderable = new OvalRenderable(Color.yellow);
        Vector2 topLeftCorner = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 3);

        GameObject sun = new GameObject(topLeftCorner, SUN_DIMENSIONS, ovalRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");

        return sun;
    }
}
