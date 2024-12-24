package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        RectangleRenderable blackRectangleRenderable = new RectangleRenderable(Color.black);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, blackRectangleRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        return night;
    }
}
