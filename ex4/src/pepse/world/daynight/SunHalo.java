package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;

import static pepse.util.Constants.*;

/**
 * A class representing the sun halo in the game.
 */
public class SunHalo {

    /**
     * Creates a sun halo game object.
     *
     * @param sun the sun game object
     * @return the created sun halo game object
     */
    public static GameObject create(GameObject sun){
        OvalRenderable haloRenderable = new OvalRenderable(HALO_COLOR);
        GameObject halo =
                new GameObject(sun.getTopLeftCorner(),
                        SUN_DIMENSIONS.mult(HALO_TO_SUN_RATIO), haloRenderable);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(SUN_HALO_TAG);
        halo.addComponent((_ -> halo.setCenter(sun.getCenter())));
        return halo;
    }
}
