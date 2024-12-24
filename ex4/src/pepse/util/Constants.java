package pepse.util;

import danogl.util.Vector2;

import java.awt.*;

public class Constants {

    public static final Vector2 SUN_DIMENSIONS = new Vector2(50, 50);
    public static final int DEFAULT_BLOCK_SIZE = 30;
    public static final float HALO_TO_SUN_RATIO = 1.5f;

    // tags
    public static final String SKY_TAG = "sky";
    public static final String BLOCK_TAG = "block";
    public static final String NIGHT_TAG = "night";
    public static final String SUN_TAG = "sun";
    public static final String SUN_HALO_TAG = "sun halo";

    // colors
    public static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    public static final Color DEFAULT_SKY_COLOR = Color.decode("#80C6E5");
    public static final Color DEFAULT_BASE_GROUND_COLOR = new Color(212, 123, 74);



}
