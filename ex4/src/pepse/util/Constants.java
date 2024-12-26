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


    public static final Vector2 AVATAR_DIMENSIONS = new Vector2(80, 120);
    public static final float AVATAR_MOVEMENT_SPEED = 300;
    public static final float AVATAR_JUMP_VELOCITY_Y = -450;
    public static final float GRAVITY = 600;

    // energy
    public static final float DEFAULT_ENERGY_MAX = 100f;
    public static final float JUMP_ENERGY_COST = 10f;
    public static final float HORIZONTAL_MOVEMENT_ENERGY_COST = 0.5f;
    public static final float ENERGY_REGENERATION_RATE = 1f;
    public static final Vector2 ENERGY_DISPLAY_DIMENSIONS = new Vector2(15f, 15f);


    public static final float ANIMATION_FRAME_DURATION = 0.5f;
    public static final String AVATAR_IMAGE_PATH = "assets/idle/idle_0.png";
    public static final String IDLE_ANIMATION_DIR_PATH = "assets/idle";
    public static final String[] IDLE_ANIMATION_ARR = new String[]{
            IDLE_ANIMATION_DIR_PATH + "/idle_0.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_1.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_2.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_3.png",
    };

    public static final String JUMP_ANIMATION_DIR_PATH = "assets/jump";
    public static final String[] JUMP_ANIMATION_ARR =new String[]{
            JUMP_ANIMATION_DIR_PATH + "/jump_0.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_1.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_2.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_3.png",
    };

    public static final String RUN_ANIMATION_DIR_PATH = "assets/run";
    public static final String[] RUN_ANIMATION_ARR =new String[]{
            RUN_ANIMATION_DIR_PATH + "/run_0.png",
            RUN_ANIMATION_DIR_PATH + "/run_1.png",
            RUN_ANIMATION_DIR_PATH + "/run_2.png",
            RUN_ANIMATION_DIR_PATH + "/run_3.png",
    };




}
