package pepse.util;

import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final Vector2 SUN_DIMENSIONS = new Vector2(50, 50);
    public static final float SUN_TRANSITION_START_VALUE = 0f;
    public static final float SUN_TRANSITION_END_VALUE = 360f;
    public static final int DEFAULT_BLOCK_SIZE = 30;
    public static final float HALO_TO_SUN_RATIO = 1.5f;

    // tags
    public static final String SKY_TAG = "sky";
    public static final String BLOCK_TAG = "block";
    public static final String NIGHT_TAG = "night";
    public static final String SUN_TAG = "sun";
    public static final String SUN_HALO_TAG = "sun halo";
    public static final String LEAF_TAG = "leaf";
    public static final String TRUNK_TAG = "trunk";
    public static final String FRUIT_TAG = "fruit";
    public static final String AVATAR_TAG = "avatar";
    public static final String CLOUD_TAG = "cloud";

    public static final List<String> ALLOWED_TO_COLLIDE_WITH_AVATAR =
            Arrays.asList(BLOCK_TAG, TRUNK_TAG, FRUIT_TAG);

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

    public static final float TREE_TRUNK_WIDTH = 20f;
    public static final float TREE_MIN_HEIGHT = 60f;
    public static final float TREE_MAX_HEIGHT = 130f;

    public static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);

    // leaf specs
    public static final Vector2 LEAF_DIMENSIONS = new Vector2(10,10);
    public static final int LEAVES_IN_LEAF_GRID = 5;
    public static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);
    public static final float LEAF_CREATION_CHANCE = 0.5f;
    public static final float TREE_CREATION_CHANCE = 0.1f;
    public static final float LEAF_PADDING = 3.5f;
    public static Vector2 LEAF_GRID =
            new Vector2(LEAVES_IN_LEAF_GRID * LEAF_DIMENSIONS.x(),
            LEAVES_IN_LEAF_GRID * LEAF_DIMENSIONS.y());

    // leaf transitions
    public static final float LEAF_ANGLE_BOUND = 45f;
    public static final float LEAF_TRANSITION_TIME = 0.6f;
    public static final float LEAF_DIMENSION_TRANSITION_FACTOR = 0.7f;

    // fruit
    public static final float FRUIT_CREATION_CHANCE = 0.5f;
    public static final Color FRUIT_BASE_COLOR = Color.red;
    public static final Vector2 FRUIT_DIMENSIONS = LEAF_DIMENSIONS.mult(0.8f);
    public static final float FRUIT_PADDING = LEAF_DIMENSIONS.x() / 4;
    public static final float FRUIT_ENERGY = 10f;

    // cloud
    public static final float CLOUD_LENGTH = 7f * 1.5f * Block.SIZE;
    public static final Vector2 CLOUD_START_LOCATION = new Vector2(CLOUD_LENGTH * -1, 40f);
    public static final Vector2 CLOUD_MOVEMENT_VECTOR = new Vector2(2f,0);


    // rain
    public static final float RAIN_DROP_CHANCE = 0.35f;
    public static final float RAIN_TRANSITION_TIME = 2f;
    public static final float VISIBLE_OPACITY = 1f;
    public static final float INVISIBLE_OPACITY = 0f;

    // infinite world
    public static final float TERRAIN_GAP = Block.SIZE * 3;

    public static final int RANDOM_SEED = 5;
}
