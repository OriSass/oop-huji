package src.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import src.bricker.brick_strategies.BasicCollisionStrategy;
import src.bricker.brick_strategies.CollisionStrategy;
import src.bricker.gameobjects.Ball;
import src.bricker.gameobjects.Brick;
import src.bricker.gameobjects.Heart;
import src.bricker.gameobjects.Paddle;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    // general
    private static final Random random = new Random();
    private static final float EPSILON = 4;
    private static final float HALF = 0.5F;
    private static final float HEART_WIDTH = 10f;
    private static final Vector2 DEFAULT_HEART_START_POSITION = new Vector2(10f, 10f);
    private static final Vector2 STRIKES_DIMENSIONS = new Vector2(15f, 15f);

    // bricks
    private final int bricksInRow;
    private final int brickRows;

    private static final float BRICK_ROWS_PADDING = 20f;
    private static final int DEFAULT_NUMBER_OF_BRICK_ROWS = 7;
    private static final int DEFAULT_NUMBER_OF_BRICKS_IN_ROWS = 8;


    // wall
    private static final float WALL_DIMENSION_X = 5;
    private static final float WALL_X_PADDING = 5;
    private static final float WALL_Y_PADDING = 5;

    // game
    private static final String GAME_TITLE = "Bouncing Ball";
    private static final Vector2 GAME_DIMENSIONS = new Vector2(500, 700);
    private static final int DEFAULT_LIFE_COUNT = 4;
    private GameObject ball;
    private GameObject paddle;
    private WindowController windowController;
    private int life_count;
    private Vector2 ballStartPosition;
    private Vector2 heartStartPosition;
    private Heart[] hearts;
    private GameObject strikes;




    // ball
    private static final float BALL_SPEED = 300f;
    private static final Vector2 BALL_DIMENSIONS = new Vector2(50, 50);

    // paddle
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    private static final int PADDLE_PADDING_Y = 30;

    // brick
    private static final Vector2 BRICK_DIMENSIONS = new Vector2(100f, 15f);
    private static final Vector2 BRICKS_START_POSITION = new Vector2(20f, 10f);

    // paths
    private static final String PADDLE_IMAGE_PATH = "src/assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "src/assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMAGE_PATH = "src/assets/brick.png";
    private static final String BALL_IMAGE_PATH = "src/assets/ball.png";
    private static final String BALL_COLLISION_SOUND_PATH = "src/assets/blop.wav";
    private static final String HEART_IMAGE_PATH = "src/assets/heart.png";


    public BrickerGameManager(String bouncingBall, Vector2 vector2, int brickRows, int bricksInCol) {
        super(bouncingBall, vector2);
        this.brickRows = brickRows;
        this.bricksInRow = bricksInCol;
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        this.life_count = DEFAULT_LIFE_COUNT;
        this.hearts = new Heart[life_count];
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.ballStartPosition = windowDimensions.mult(HALF);
        this.heartStartPosition = new Vector2(DEFAULT_HEART_START_POSITION.x(), windowDimensions.y()-20);

        Vector2 wallDimensions = new Vector2(WALL_DIMENSION_X, windowDimensions.y() * 2);
        Vector2 leftWallStart = new Vector2(WALL_X_PADDING, WALL_Y_PADDING);
        Vector2 rightWallStart = new Vector2(windowDimensions.x() - WALL_X_PADDING, WALL_Y_PADDING);

        float leftBoundary = leftWallStart.x() + wallDimensions.x() + EPSILON;
        float rightBoundary = rightWallStart.x() - PADDLE_DIMENSIONS.x() - wallDimensions.x() - EPSILON;


        createWalls(wallDimensions, leftWallStart, rightWallStart);
        createPaddle(imageReader, windowDimensions, leftBoundary, rightBoundary, inputListener);
        createBall(imageReader, soundReader);
        createBrickRows(imageReader, this.bricksInRow, leftBoundary, rightBoundary);
        createBackgroundImage(imageReader, windowDimensions);
        createHeartRow(imageReader, HEART_WIDTH, this.heartStartPosition);
        createStrikesLeftInfo(windowDimensions);
    }

    private void createStrikesLeftInfo(Vector2 windowDimensions) {
        TextRenderable strikesRenderable = new TextRenderable(String.valueOf(this.life_count));
        strikesRenderable.setColor(getColorByLifeCount());
        Vector2 strikesPosition = new Vector2(windowDimensions.x() - 20, windowDimensions.y()-20);
        this.strikes = new GameObject(strikesPosition, STRIKES_DIMENSIONS, strikesRenderable);
        this.gameObjects().addGameObject(this.strikes, Layer.BACKGROUND);
    }

    private Color getColorByLifeCount() {
        return switch (this.life_count) {
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            default -> Color.GREEN;
        };
    }

    private void createHeartRow(ImageReader imageReader, float heartWidth, Vector2 startPosition) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 heartPosition = startPosition;
        float heartPadding = 40f / this.life_count;


        Vector2 heartDimension = new Vector2(heartWidth, BRICK_DIMENSIONS.y());
        for (int heart_index = 0; heart_index < this.life_count; heart_index++) {
            this.hearts[heart_index] = new Heart(heartPosition, heartDimension, heartImage);
            this.gameObjects().addGameObject(this.hearts[heart_index], Layer.BACKGROUND);
            heartPosition = heartPosition.add(new Vector2(heartWidth + heartPadding, Vector2.ZERO.y()));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfGameOver();
    }

    private void checkIfGameOver() {
        float ballHeight = this.ball.getCenter().y();
        if (ballHeight > this.paddle.getCenter().y() + this.paddle.getDimensions().y()){
            this.life_count--;
            this.removeGameObject(this.hearts[this.life_count], Layer.BACKGROUND);
            this.removeGameObject(this.strikes, Layer.BACKGROUND);
            this.createStrikesLeftInfo(this.windowController.getWindowDimensions());

            if (this.life_count == 0) {
                String prompt = "You lose! Play again?";
                if (this.windowController.openYesNoDialog(prompt)){
                    this.windowController.resetGame();
                }
                else{
                    this.windowController.closeWindow();
                }
            }
            else {
                this.ball.setCenter(this.ballStartPosition);
            }
        }
    }

    private void createPaddle(ImageReader imageReader, Vector2 windowDimensions, float leftBoundary,
                              float rightBoundary,
                              UserInputListener inputListener) {
        // create paddle
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        this.paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS,
                paddleImage, inputListener, leftBoundary, rightBoundary);
        Vector2 paddlePosition = new Vector2(windowDimensions.x() / 2, (int) (windowDimensions.y() - PADDLE_PADDING_Y));
        paddle.setCenter(paddlePosition);
        this.gameObjects().addGameObject(paddle);

    }

    private void createBackgroundImage(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable bgImageRender = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject bgImage = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), bgImageRender);
        this.gameObjects().addGameObject(bgImage, Layer.BACKGROUND);
    }

    private void createBrickRow(ImageReader imageReader, Vector2 startPosition,
                                int numberOfBricks, float leftBoundary, float rightBoundary) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);
        CollisionStrategy brickCollisionStrategy = new BasicCollisionStrategy(this);

        float brickWidth = (rightBoundary - leftBoundary) / (numberOfBricks);

        Vector2 brickPosition = startPosition;
        float brickPadding = 40f / numberOfBricks;

        Vector2 brickDimension = new Vector2(brickWidth, BRICK_DIMENSIONS.y());
        for (int brickCount = 0; brickCount < numberOfBricks; brickCount++) {
            GameObject brick = new Brick(brickPosition, brickDimension,
                    brickImage, brickCollisionStrategy);
            this.gameObjects().addGameObject(brick);
            brickPosition = brickPosition.add(new Vector2(brickWidth + brickPadding, Vector2.ZERO.y()));
        }
    }

    private void createBrickRows(ImageReader imageReader, int numberOfBricks, float leftBoundary, float rightBoundary) {
        Vector2 brickRowStart = new Vector2(leftBoundary, BRICKS_START_POSITION.y());
        for (int row = 0; row < this.brickRows; row++) {
            createBrickRow(imageReader, brickRowStart, numberOfBricks, leftBoundary, rightBoundary);
            brickRowStart = brickRowStart.add(new Vector2(Vector2.ZERO.x(), Vector2.DOWN.y()).mult(BRICK_ROWS_PADDING));
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        // create ball
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);

        float ballVelocityX = getRandomVelocityFlip(BALL_SPEED);
        float ballVelocityY = getRandomVelocityFlip(BALL_SPEED);

        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));
        ball.setCenter(this.ballStartPosition);
        this.gameObjects().addGameObject(ball);
    }

    private float getRandomVelocityFlip(float velocity) {
        if(random.nextBoolean()){
            return velocity * -1;
        }
        return velocity;
    }

    private void createWalls(Vector2 wallDimensions,Vector2 leftWallStart, Vector2 rightWallStart) {
        // create walls
        Renderable rectangleRender = new RectangleRenderable(Color.orange);
        GameObject leftWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        leftWall.setCenter(leftWallStart);
        GameObject rightWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        rightWall.setCenter(rightWallStart);

        this.gameObjects().addGameObject(leftWall);
        this.gameObjects().addGameObject(rightWall);
    }

    public void removeGameObject(GameObject gameObject, int layer) {
        this.gameObjects().removeGameObject(gameObject, layer);
    }

    private static int getNumberOfRows(String[] args){
        if(args.length >= 2){
            return Integer.parseInt(args[1]);
        }
        return DEFAULT_NUMBER_OF_BRICK_ROWS;
    }

    private static int getNumberOfBricksInARow(String[] args){
        if(args.length >= 1){
            return Integer.parseInt(args[0]);
        }
        return DEFAULT_NUMBER_OF_BRICKS_IN_ROWS;
    }

    public static void main(String[] args) {
        int rows = getNumberOfRows(args);
        int bricks_in_row = getNumberOfBricksInARow(args);
        GameManager gm = new BrickerGameManager(GAME_TITLE, GAME_DIMENSIONS, rows, bricks_in_row);
        gm.run();
    }
}
