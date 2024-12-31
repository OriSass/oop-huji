package pepse.util;

import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * A class containing various constants used throughout the game.
 */
public class Constants {

    /**
     * The dimensions of the sun.
     */
    public static final Vector2 SUN_DIMENSIONS = new Vector2(50, 50);

    /**
     * The start value for the sun transition.
     */
    public static final float SUN_TRANSITION_START_VALUE = 0f;

    /**
     * The end value for the sun transition.
     */
    public static final float SUN_TRANSITION_END_VALUE = 360f;

    /**
     * The default size of a block.
     */
    public static final int DEFAULT_BLOCK_SIZE = 30;

    /**
     * The ratio of the halo size to the sun size.
     */
    public static final float HALO_TO_SUN_RATIO = 1.5f;

    // Tags
    /**
     * The tag for the sky.
     */
    public static final String SKY_TAG = "sky";

    /**
     * The tag for a block.
     */
    public static final String BLOCK_TAG = "block";

    /**
     * The tag for the night.
     */
    public static final String NIGHT_TAG = "night";

    /**
     * The tag for the sun.
     */
    public static final String SUN_TAG = "sun";

    /**
     * The tag for the sun halo.
     */
    public static final String SUN_HALO_TAG = "sun halo";

    /**
     * The tag for a leaf.
     */
    public static final String LEAF_TAG = "leaf";

    /**
     * The tag for a tree trunk.
     */
    public static final String TRUNK_TAG = "trunk";

    /**
     * The tag for a fruit.
     */
    public static final String FRUIT_TAG = "fruit";

    /**
     * The tag for the avatar.
     */
    public static final String AVATAR_TAG = "avatar";

    /**
     * The tag for a cloud.
     */
    public static final String CLOUD_TAG = "cloud";

    /**
     * The list of tags that are allowed to collide with the avatar.
     */
    public static final List<String> ALLOWED_TO_COLLIDE_WITH_AVATAR =
            Arrays.asList(BLOCK_TAG, TRUNK_TAG, FRUIT_TAG);

    // Colors
    /**
     * The color of the halo.
     */
    public static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    /**
     * The default color of the sky.
     */
    public static final Color DEFAULT_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * The default base color of the ground.
     */
    public static final Color DEFAULT_BASE_GROUND_COLOR = new Color(212, 123, 74);

    /**
     * The dimensions of the avatar.
     */
    public static final Vector2 AVATAR_DIMENSIONS = new Vector2(80, 120);

    /**
     * The movement speed of the avatar.
     */
    public static final float AVATAR_MOVEMENT_SPEED = 300;

    /**
     * The jump velocity of the avatar in the y direction.
     */
    public static final float AVATAR_JUMP_VELOCITY_Y = -450;

    /**
     * The gravity in the game.
     */
    public static final float GRAVITY = 600;

    // Energy
    /**
     * The default maximum energy level.
     */
    public static final float DEFAULT_ENERGY_MAX = 100f;

    /**
     * The energy cost of jumping.
     */
    public static final float JUMP_ENERGY_COST = 10f;

    /**
     * The energy cost of horizontal movement.
     */
    public static final float HORIZONTAL_MOVEMENT_ENERGY_COST = 0.5f;

    /**
     * The rate of energy regeneration.
     */
    public static final float ENERGY_REGENERATION_RATE = 1f;

    /**
     * The dimensions of the energy display.
     */
    public static final Vector2 ENERGY_DISPLAY_DIMENSIONS = new Vector2(15f, 15f);

    /**
     * The duration of each frame in an animation.
     */
    public static final float ANIMATION_FRAME_DURATION = 0.5f;

    /**
     * The path to the avatar image.
     */
    public static final String AVATAR_IMAGE_PATH = "assets/idle/idle_0.png";

    /**
     * The directory path for the idle animation.
     */
    public static final String IDLE_ANIMATION_DIR_PATH = "assets/idle";

    /**
     * The array of paths for the idle animation frames.
     */
    public static final String[] IDLE_ANIMATION_ARR = new String[]{
            IDLE_ANIMATION_DIR_PATH + "/idle_0.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_1.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_2.png",
            IDLE_ANIMATION_DIR_PATH + "/idle_3.png",
    };

    /**
     * The directory path for the jump animation.
     */
    public static final String JUMP_ANIMATION_DIR_PATH = "assets/jump";

    /**
     * The array of paths for the jump animation frames.
     */
    public static final String[] JUMP_ANIMATION_ARR = new String[]{
            JUMP_ANIMATION_DIR_PATH + "/jump_0.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_1.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_2.png",
            JUMP_ANIMATION_DIR_PATH + "/jump_3.png",
    };

    /**
     * The directory path for the run animation.
     */
    public static final String RUN_ANIMATION_DIR_PATH = "assets/run";

    /**
     * The array of paths for the run animation frames.
     */
    public static final String[] RUN_ANIMATION_ARR = new String[]{
            RUN_ANIMATION_DIR_PATH + "/run_0.png",
            RUN_ANIMATION_DIR_PATH + "/run_1.png",
            RUN_ANIMATION_DIR_PATH + "/run_2.png",
            RUN_ANIMATION_DIR_PATH + "/run_3.png",
    };

    /**
     * The width of the tree trunk.
     */
    public static final float TREE_TRUNK_WIDTH = 20f;

    /**
     * The minimum height of a tree.
     */
    public static final float TREE_MIN_HEIGHT = 60f;

    /**
     * The maximum height of a tree.
     */
    public static final float TREE_MAX_HEIGHT = 130f;

    /**
     * The base color of the tree trunk.
     */
    public static final Color TRUNK_BASE_COLOR = new Color(100, 50, 20);

    // Leaf specs
    /**
     * The dimensions of a leaf.
     */
    public static final Vector2 LEAF_DIMENSIONS = new Vector2(10, 10);

    /**
     * The number of leaves in a leaf grid.
     */
    public static final int LEAVES_IN_LEAF_GRID = 5;

    /**
     * The base color of a leaf.
     */
    public static final Color LEAF_BASE_COLOR = new Color(50, 200, 30);

    /**
     * The chance of creating a leaf.
     */
    public static final float LEAF_CREATION_CHANCE = 0.5f;

    /**
     * The chance of creating a tree.
     */
    public static final float TREE_CREATION_CHANCE = 0.1f;

    /**
     * The padding between leaves.
     */
    public static final float LEAF_PADDING = 3.5f;

    /**
     * The dimensions of the leaf grid.
     */
    public static Vector2 LEAF_GRID =
            new Vector2(LEAVES_IN_LEAF_GRID * LEAF_DIMENSIONS.x(),
                    LEAVES_IN_LEAF_GRID * LEAF_DIMENSIONS.y());

    // Leaf transitions
    /**
     * The angle bound for leaf transitions.
     */
    public static final float LEAF_ANGLE_BOUND = 45f;

    /**
     * The time for leaf transitions.
     */
    public static final float LEAF_TRANSITION_TIME = 0.6f;

    /**
     * The factor for leaf dimension transitions.
     */
    public static final float LEAF_DIMENSION_TRANSITION_FACTOR = 0.7f;

    // Fruit
    /**
     * The chance of creating a fruit.
     */
    public static final float FRUIT_CREATION_CHANCE = 0.5f;

    /**
     * The base color of a fruit.
     */
    public static final Color FRUIT_BASE_COLOR = Color.red;

    /**
     * The dimensions of a fruit.
     */
    public static final Vector2 FRUIT_DIMENSIONS = LEAF_DIMENSIONS.mult(0.8f);

    /**
     * The padding between fruits.
     */
    public static final float FRUIT_PADDING = LEAF_DIMENSIONS.x() / 4;

    /**
     * The energy provided by a fruit.
     */
    public static final float FRUIT_ENERGY = 10f;

    // Cloud
    /**
     * The length of a cloud.
     */
    public static final float CLOUD_LENGTH = 7f * 1.5f * Block.SIZE;

    /**
     * The start location of a cloud.
     */
    public static final Vector2 CLOUD_START_LOCATION = new Vector2(CLOUD_LENGTH * -1, 40f);

    /**
     * The movement vector of a cloud.
     */
    public static final Vector2 CLOUD_MOVEMENT_VECTOR = new Vector2(2f, 0);

    // Rain
    /**
     * The chance of a raindrop.
     */
    public static final float RAIN_DROP_CHANCE = 0.35f;

    /**
     * The time for rain transitions.
     */
    public static final float RAIN_TRANSITION_TIME = 2f;

    /**
     * The opacity when rain is visible.
     */
    public static final float VISIBLE_OPACITY = 1f;

    /**
     * The opacity when rain is invisible.
     */
    public static final float INVISIBLE_OPACITY = 0f;

    // Infinite world
    /**
     * The gap between terrain segments.
     */
    public static final float TERRAIN_GAP = Block.SIZE * 3;

    /**
     * The random seed for the game.
     */
    public static final int RANDOM_SEED = 5;
}