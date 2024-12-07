package src.bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.bricker.brick_strategies.CollisionStrategy;
import src.bricker.brick_strategies.CollisionStrategyFactory;
import src.bricker.brick_strategies.ExtraPaddleStrategy;
import src.bricker.gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static src.bricker.utils.Constants.*;

public class BrickerGameManager extends GameManager {

    // general
    private static final Random random = new Random();

    // bricks
    private final int bricksInRow;
    private final int brickRows;

    private Counter brickCount;
    private Ball ball;
    private GameObject paddle;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private WindowController windowController;
    private UserInputListener inputListener;
    private int lifeCount;
    private Vector2 ballStartPosition;
    private Vector2 heartStartPosition;
    private Heart[] hearts;
    private GameObject strikes;
    private List<Puck> pucks;

    public BrickerGameManager(String bouncingBall, Vector2 vector2, int brickRows, int bricksInRow) {
        super(bouncingBall, vector2);
        this.brickRows = brickRows;
        this.bricksInRow = bricksInRow;
        this.brickCount = new Counter(brickRows * bricksInRow);
        this.pucks = new ArrayList<>();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.lifeCount = DEFAULT_LIFE_COUNT;
        this.hearts = new Heart[lifeCount];
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.ballStartPosition = windowDimensions.mult(HALF);
        this.heartStartPosition = new Vector2(DEFAULT_HEART_START_POSITION.x(), windowDimensions.y()-20);

        Vector2 wallDimensions = new Vector2(WALL_DIMENSION_X, windowDimensions.y() * 2);
        Vector2 leftWallStart = new Vector2(WALL_X_PADDING, WALL_Y_PADDING);
        Vector2 rightWallStart = new Vector2(windowDimensions.x() - WALL_X_PADDING, WALL_Y_PADDING);

        Vector2 ceilingDimensions = new Vector2(windowDimensions.x() * 2 - WALL_X_PADDING, CEILING_DIMENSION_Y);
        Vector2 ceilingStart = new Vector2(WALL_X_PADDING, CEILING_Y_PADDING);


        float leftBoundary = leftWallStart.x() + wallDimensions.x() + EPSILON;
        float rightBoundary = rightWallStart.x() - PADDLE_DIMENSIONS.x() - wallDimensions.x() - EPSILON;

        Renderable rectangleRender = new RectangleRenderable(Color.orange);

        createWalls(wallDimensions, leftWallStart, rightWallStart, rectangleRender);
        createCeiling(ceilingDimensions, ceilingStart, rectangleRender);
        createPaddle(imageReader, windowDimensions, leftBoundary, rightBoundary, inputListener);
        createBall(imageReader, soundReader);
        createBrickRows(imageReader, inputListener, this.bricksInRow, leftBoundary, rightBoundary, windowDimensions);
        createBackgroundImage(imageReader, windowDimensions);
        createHeartRow(imageReader, HEART_WIDTH, this.heartStartPosition);
        createStrikesLeftInfo(windowDimensions);

        ExtraPaddleStrategy.extraPaddleCounter.reset();
        ExtraPaddleStrategy.hitCounter.reset();
    }

    private void createCeiling(Vector2 ceilingDimensions, Vector2 ceilingStartPosition, Renderable rectangleRender) {
        GameObject ceiling = new GameObject(Vector2.ZERO, ceilingDimensions, rectangleRender);
        ceiling.setCenter(ceilingStartPosition);
        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);
    }

    private void createStrikesLeftInfo(Vector2 windowDimensions) {
        TextRenderable strikesRenderable = new TextRenderable(String.valueOf(this.lifeCount));
        strikesRenderable.setColor(getColorByLifeCount());
        Vector2 strikesPosition = new Vector2(windowDimensions.x() - 20, windowDimensions.y()-20);
        this.strikes = new GameObject(strikesPosition, STRIKES_DIMENSIONS, strikesRenderable);
        this.gameObjects().addGameObject(this.strikes, Layer.UI);
    }

    private Color getColorByLifeCount() {
        return switch (this.lifeCount) {
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            default -> Color.GREEN;
        };
    }

    private void createHeartRow(ImageReader imageReader, float heartWidth, Vector2 startPosition) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 heartPosition = startPosition;
        float heartPadding = 40f / this.lifeCount;


        Vector2 heartDimension = new Vector2(heartWidth, BRICK_DIMENSIONS.y());
        for (int heart_index = 0; heart_index < this.lifeCount; heart_index++) {
            this.hearts[heart_index] = new Heart(heartPosition, heartDimension, heartImage);
            this.gameObjects().addGameObject(this.hearts[heart_index], Layer.UI);
            heartPosition = heartPosition.add(new Vector2(heartWidth + heartPadding, Vector2.ZERO.y()));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfPlayerWon();
        checkIfGameOver();
        checkIfWPressed();
        updatePuckList();
    }

    private void updatePuckList() {
        for (int i = this.pucks.size() - 1; i >= 0; i--) {
            Puck puck = this.pucks.get(i);
            float puckHeight = puck.getCenter().y();
            if (puckHeight > this.paddle.getCenter().y() + this.paddle.getDimensions().y()) {
                this.gameObjects().removeGameObject(puck);
                this.pucks.remove(i); // Safe removal
            }
        }
    }

    private void checkIfWPressed() {
        if(this.inputListener.isKeyPressed(KeyEvent.VK_W)){
            openDialog(WIN_PROMPT);
            this.ball.setCenter(this.ballStartPosition);
            this.brickCount = new Counter(this.bricksInRow * this.brickRows);
        }
    }

    private void checkIfPlayerWon() {
        if(this.brickCount.value() <= 0){
            openDialog(WIN_PROMPT);
            this.ball.setCenter(this.ballStartPosition);
            this.brickCount = new Counter(this.bricksInRow * this.brickRows);
        }
    }
    
    private void openDialog(String prompt){
        if (this.windowController.openYesNoDialog(prompt)){
            this.windowController.resetGame();
        }
        else{
            this.windowController.closeWindow();
        }
    }

    private void checkIfGameOver() {
        float ballHeight = this.ball.getCenter().y();
        if (ballHeight > this.paddle.getCenter().y() + this.paddle.getDimensions().y()){
            this.lifeCount--;
            this.removeGameObject(this.hearts[this.lifeCount], Layer.UI);
            this.removeGameObject(this.strikes, Layer.UI);
            this.createStrikesLeftInfo(this.windowController.getWindowDimensions());

            if (this.lifeCount == 0) {
                openDialog(LOSE_PROMPT);
            }
            else {
                this.ball.setCenter(this.ballStartPosition);
                this.ball.setVelocity(getRandomDiagonalVelocity());
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

    private void createBrickRow(ImageReader imageReader, Vector2 startPosition, UserInputListener inputListener,
                                int numberOfBricks, float leftBoundary, float rightBoundary, Vector2 windowDimensions) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);

        CollisionStrategyFactory factory = new CollisionStrategyFactory();
        CollisionStrategy brickCollisionStrategy = factory.getCollisionStrategy(
                        this, leftBoundary, rightBoundary,
                        inputListener, imageReader, windowDimensions);

        float brickWidth = (rightBoundary - leftBoundary) / (numberOfBricks);

        Vector2 brickPosition = startPosition;
        float brickPadding = 40f / numberOfBricks;

        Vector2 brickDimension = new Vector2(brickWidth, BRICK_DIMENSIONS.y());
        for (int brickCount = 0; brickCount < numberOfBricks; brickCount++) {
            GameObject brick = new Brick(brickPosition, brickDimension,
                    brickImage, brickCollisionStrategy);
            this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            brickPosition = brickPosition.add(new Vector2(brickWidth + brickPadding, Vector2.ZERO.y()));
        }
    }

    private void createBrickRows(ImageReader imageReader, UserInputListener inputListener,
                                 int numberOfBricks, float leftBoundary, float rightBoundary,
                                 Vector2 windowDimensions) {
        Vector2 brickRowStart = new Vector2(leftBoundary, BRICKS_START_POSITION.y());
        for (int row = 0; row < this.brickRows; row++) {
            createBrickRow(imageReader, brickRowStart, inputListener, numberOfBricks,
                    leftBoundary, rightBoundary, windowDimensions);
            brickRowStart = brickRowStart.add(new Vector2(Vector2.ZERO.x(), Vector2.DOWN.y()).mult(BRICK_ROWS_PADDING));
        }
    }

    private Vector2 getRandomDiagonalVelocity(){
        float ballVelocityX = getRandomVelocityFlip(BALL_SPEED);
        float ballVelocityY = getRandomVelocityFlip(BALL_SPEED);

        return (new Vector2(ballVelocityX, ballVelocityY));
    }

    private Vector2 getRandomUpperVelocity(){
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        return new Vector2(velocityX, velocityY);
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        // create ball
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);

        this.ball.setVelocity(getRandomDiagonalVelocity());
        this.ball.setCenter(this.ballStartPosition);
        this.gameObjects().addGameObject(ball);
    }

    private float getRandomVelocityFlip(float velocity) {
        if(random.nextBoolean()){
            return velocity * -1;
        }
        return velocity;
    }

    private void createWalls(Vector2 wallDimensions, Vector2 leftWallStart, Vector2 rightWallStart, Renderable rectangleRender) {
        // create walls
        GameObject leftWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        leftWall.setCenter(leftWallStart);

        GameObject rightWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        rightWall.setCenter(rightWallStart);

        this.gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
    }



    public void removeGameObject(GameObject gameObject, int layer) {
        this.gameObjects().removeGameObject(gameObject, layer);
        if(gameObject instanceof Brick){
            this.brickCount.decrement();
        }
    }

    public void addGameObject(GameObject gameObject, int layer) {
        this.gameObjects().addGameObject(gameObject, layer);
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

    public void createPucks(int numberOfPucks, Vector2 location) {
        for (int i = 0; i < numberOfPucks; i++){
            createPuck(location);
        }
    }

    public void createPuck(Vector2 location) {
        Renderable puckImage = this.imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound collisionSound = this.soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        Puck puck = new Puck(Vector2.ZERO, PUCK_DIMENSIONS, puckImage, collisionSound);
        puck.setVelocity(getRandomUpperVelocity());
        puck.setCenter(location);
        this.pucks.add(puck);
        this.gameObjects().addGameObject(puck);
    }


}
