package src.bricker;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Paddle;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 200f;
    private static final float EPSILON = 4;

    public BrickerGameManager(String bouncingBall, Vector2 vector2) {
        super(bouncingBall, vector2);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Vector2 wallDimensions = new Vector2(5, windowDimensions.y() * 2);
        Vector2 leftWallStart = new Vector2(5, 5);
        Vector2 rightWallStart = new Vector2(windowDimensions.x() - 5, 5);
        createWalls(wallDimensions, leftWallStart, rightWallStart);

        // create paddles
        Vector2 paddleDimensions = new Vector2(100, 15);
        float leftBoundary = leftWallStart.x() + wallDimensions.x() + EPSILON;
        float rightBoundary = rightWallStart.x() - paddleDimensions.x() - wallDimensions.x() - EPSILON;

        Renderable paddleImage = imageReader.readImage("src/assets/paddle.png", true);
        int[] paddleHeights = {(int) (windowDimensions.y() - 30), 30};

        for (int paddleHeight : paddleHeights) {
            GameObject paddle = new Paddle(Vector2.ZERO, paddleDimensions,
                    paddleImage, inputListener, leftBoundary, rightBoundary);
            paddle.setCenter(new Vector2(windowDimensions.x() / 2, paddleHeight));
            this.gameObjects().addGameObject(paddle);
        }

        // create ball
        Renderable ballImage = imageReader.readImage("src/assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("src/assets/blop.wav");


        GameObject ball = new Ball(Vector2.ZERO, new Vector2(50, 50), ballImage, collisionSound);
        float ballVelocityX = BALL_SPEED;
        float ballVelocityY = BALL_SPEED;
        Random random = new Random();
        if(random.nextBoolean()){
            ballVelocityX *= -1;
        }
        if(random.nextBoolean()){
            ballVelocityY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelocityX, ballVelocityY));

        ball.setCenter(windowDimensions.mult(0.5F));
        this.gameObjects().addGameObject(ball);


        Renderable bgImageRender = imageReader.readImage("src/assets/DARK_BG2_small.jpeg", true);
        GameObject bgImage = new GameObject(Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), bgImageRender);
        this.gameObjects().addGameObject(bgImage, Layer.BACKGROUND);
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

    public static void main(String[] args) {
        GameManager gm = new BrickerGameManager("Bouncing Ball", new Vector2(500, 700));
        gm.run();

    }
}
