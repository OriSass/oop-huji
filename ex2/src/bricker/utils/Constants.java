package src.bricker.utils;

import danogl.util.Vector2;

public class Constants {
    // ball
    public static final float BALL_SPEED = 300f;
    public static final Vector2 BALL_DIMENSIONS = new Vector2(50, 50);

    // paddle
    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    public static final float PADDLE_PADDING_Y = 30f;

    // brick
    public static final Vector2 BRICK_DIMENSIONS = new Vector2(100f, 15f);
    public static final Vector2 BRICKS_START_POSITION = new Vector2(20f, 10f);
    public static final float BRICK_ROWS_PADDING = 20f;
    public static final int DEFAULT_NUMBER_OF_BRICK_ROWS = 7;
    public static final int DEFAULT_NUMBER_OF_BRICKS_IN_ROWS = 8;

    // puck
    public static final Vector2 PUCK_DIMENSIONS = BALL_DIMENSIONS.mult(0.75f) ;

    // turbo
    public static final float TURBO_SPEED_FACTOR = 1.4f;

    // paths
    public static final String PADDLE_IMAGE_PATH = "src/assets/paddle.png";
    public static final String BACKGROUND_IMAGE_PATH = "src/assets/DARK_BG2_small.jpeg";
    public static final String BRICK_IMAGE_PATH = "src/assets/brick.png";
    public static final String BALL_IMAGE_PATH = "src/assets/ball.png";
    public static final String BALL_COLLISION_SOUND_PATH = "src/assets/blop.wav";
    public static final String HEART_IMAGE_PATH = "src/assets/heart.png";
    public static final String PUCK_IMAGE_PATH = "src/assets/mockBall.png";
    public static final String TURBO_BALL_IMAGE_PATH = "src/assets/redball.png";

    // prompts
    public static final String WIN_PROMPT = "You win! Play again?";
    public static final String LOSE_PROMPT = "You lose! Play again?";

    // ceiling
    public static final float CEILING_DIMENSION_Y = 5;
    public static final float CEILING_Y_PADDING = 3;

    // wall
    public static final float WALL_DIMENSION_X = 5;
    public static final float WALL_X_PADDING = 3;
    public static final float WALL_Y_PADDING = 10;

    // game
    public static final String GAME_TITLE = "Bouncing Ball";
    public static final Vector2 GAME_DIMENSIONS = new Vector2(500, 700);

    // lifes | heart | strikes
    public static final int DEFAULT_LIFE_COUNT = 3;
    public static final int MAX_LIFE_COUNT = 4;
    public static final Vector2 DEFAULT_HEART_START_POSITION = new Vector2(10f, 10f);
    public static final Vector2 STRIKES_DIMENSIONS = new Vector2(15f, 15f);
    public static final Vector2 HEART_DIMENSION = new Vector2(10f, 15f);
    public static final Vector2 FALLING_HEART_VELOCITY = new Vector2(Vector2.ZERO.x(), 100f);


    public static final float EPSILON = 4;
    public static final float HALF = 0.5F;
}
