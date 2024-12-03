package src.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.bricker.brick_strategies.BasicCollisionStrategy;
import src.bricker.brick_strategies.CollisionStrategy;
import src.bricker.gameobjects.Ball;
import src.bricker.gameobjects.Brick;
import src.bricker.gameobjects.Paddle;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final Random random = new Random();
    private static final float EPSILON = 4;
    private static final float HALF = 0.5F;

    // wall
    private static final float WALL_DIMENSION_X = 5;
    private static final float WALL_X_PADDING = 5;
    private static final float WALL_Y_PADDING = 5;

    // ball
    private static final float BALL_SPEED = 200f;
    private static final Vector2 BALL_DIMENSIONS = new Vector2(50, 50);

    // paddle
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    private static final int PADDLE_PADDING_Y = 30;

    // brick
    private static final Vector2 BRICK_DIMENSIONS = new Vector2(100f, 15f);
    private static final Vector2 BRICK_POSITION = new Vector2(150f, 10f);

    // paths
    private static final String PADDLE_IMAGE_PATH = "src/assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "src/assets/DARK_BG2_small.jpeg";
    private static final String BRICK_IMAGE_PATH = "src/assets/brick.png";
    private static final String BALL_IMAGE_PATH = "src/assets/ball.png";
    private static final String BALL_COLLISION_SOUND_PATH = "src/assets/blop.wav";


    public BrickerGameManager(String bouncingBall, Vector2 vector2) {
        super(bouncingBall, vector2);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Vector2 wallDimensions = new Vector2(WALL_DIMENSION_X, windowDimensions.y() * 2);
        Vector2 leftWallStart = new Vector2(WALL_X_PADDING, WALL_Y_PADDING);
        Vector2 rightWallStart = new Vector2(windowDimensions.x() - WALL_X_PADDING, WALL_Y_PADDING);

        createWalls(wallDimensions, leftWallStart, rightWallStart);
        createPaddle(imageReader, windowDimensions, leftWallStart, rightWallStart, wallDimensions, inputListener);
        createBall(imageReader, soundReader, windowDimensions);
        createBrick(imageReader);
        createBackgroundImage(imageReader, windowDimensions);
    }

    private void createPaddle(ImageReader imageReader, Vector2 windowDimensions, Vector2 leftWallStart, Vector2 rightWallStart, Vector2 wallDimensions, UserInputListener inputListener) {
        // create paddle
        float leftBoundary = leftWallStart.x() + wallDimensions.x() + EPSILON;
        float rightBoundary = rightWallStart.x() - PADDLE_DIMENSIONS.x() - wallDimensions.x() - EPSILON;

        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        GameObject paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS,
                paddleImage, inputListener, leftBoundary, rightBoundary);
        Vector2 paddlePosition = new Vector2(windowDimensions.x() / 2, (int) (windowDimensions.y() - PADDLE_PADDING_Y));
        paddle.setCenter(paddlePosition);
        this.gameObjects().addGameObject(paddle);

    }

    private void createBackgroundImage(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable bgImageRender = imageReader.readImage(BACKGROUND_IMAGE_PATH, true);
        GameObject bgImage = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), bgImageRender);
        this.gameObjects().addGameObject(bgImage, Layer.BACKGROUND);
    }

    private void createBrick(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);
        CollisionStrategy brickCollisionStrategy = new BasicCollisionStrategy(this);
        GameObject brick = new Brick(BRICK_POSITION, BRICK_DIMENSIONS,
                brickImage, brickCollisionStrategy);
        this.gameObjects().addGameObject(brick);

    }

    private void createBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        // create ball
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        GameObject ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);

        float ballVelocityX = getRandomVelocityFlip(BALL_SPEED);
        float ballVelocityY = getRandomVelocityFlip(BALL_SPEED);

        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));
        ball.setCenter(windowDimensions.mult(HALF));
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

    public void removeGameObject(GameObject gameObject) {
        this.gameObjects().removeGameObject(gameObject);
    }

    public static void main(String[] args) {
        GameManager gm = new BrickerGameManager("Bouncing Ball", new Vector2(500, 700));
        gm.run();
    }
}
