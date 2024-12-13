package bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.CollisionStrategyFactory;
import bricker.brick_strategies.ExtraPaddleStrategy;
import bricker.gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static bricker.utils.Constants.*;

/**
 * Manages the Bricker game, including creating and managing game objects such as bricks, balls, and walls.
 */
public class BrickerGameManager extends GameManager {

    // General random number generator
    private static final Random random = new Random();

    // Number of bricks in a row
    private final int bricksInRow;
    // Number of brick rows
    private final int brickRows;
    // Counter for the number of bricks
    private Counter brickCount;
    // The ball object
    private Ball ball;
    // The paddle object
    private Paddle paddle;
    // The image reader
    private ImageReader imageReader;
    // The sound reader
    private SoundReader soundReader;
    // The window controller
    private WindowController windowController;
    // The user input listener
    private UserInputListener inputListener;

    // Number of lives the player has
    private int lifeCount;
    // Initial position of the ball
    private Vector2 ballStartPosition;
    // Initial position of the heart
    private Vector2 heartStartPosition;
    // Array of heart objects representing lives
    private Heart[] hearts;
    // Game object representing the number of strikes left
    private GameObject strikes;
    // Dimensions of the game window
    private Vector2 windowDimensions;

    /**
     * Constructs a new BrickerGameManager instance.
     *
     * @param bouncingBall The title of the game.
     * @param vector2 The dimensions of the game window.
     * @param brickRows The number of rows of bricks.
     * @param bricksInRow The number of bricks in each row.
     */
    public BrickerGameManager(String bouncingBall, Vector2 vector2, int brickRows, int bricksInRow) {
        super(bouncingBall, vector2);
        this.brickRows = brickRows;
        this.bricksInRow = bricksInRow;
        this.brickCount = new Counter(brickRows * bricksInRow);
    }

    /**
     * Initializes the game by setting up game objects and their properties.
     *
     * @param imageReader Reads images for rendering.
     * @param soundReader Reads sounds for playback.
     * @param inputListener Listens for user input.
     * @param windowController Controls the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        initMembers(imageReader, soundReader, inputListener, windowController);

        ExtraPaddleStrategy.extraPaddleCounter.reset();
        ExtraPaddleStrategy.hitCounter.reset();

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 wallDimensions = new Vector2(WALL_DIMENSION_X, this.windowDimensions.y() * 2);
        Vector2 rightWallStart = new Vector2(this.windowDimensions.x() - WALL_X_PADDING, WALL_Y_PADDING);
        Vector2 ceilingDimensions = new Vector2(this.windowDimensions.x() * 2 - WALL_X_PADDING,
                CEILING_DIMENSION_Y);

        float leftBoundary = LEFT_WALL_START.x() + wallDimensions.x() + EPSILON;
        float rightBoundary = rightWallStart.x() - PADDLE_DIMENSIONS.x() - wallDimensions.x() - EPSILON;

        createWalls(wallDimensions, LEFT_WALL_START, rightWallStart, RECTANGLE_RENDER);
        createCeiling(ceilingDimensions, RECTANGLE_RENDER);
        createPaddle(imageReader, leftBoundary, rightBoundary, inputListener);
        createBall(imageReader, soundReader);
        createBrickRows(imageReader, inputListener, this.bricksInRow, leftBoundary, rightBoundary);
        createBackgroundImage(imageReader);
        createHeartRow(imageReader, this.heartStartPosition);
        createStrikesLeftInfo();
    }

    /**
     * Initializes member variables.
     *
     * @param imageReader Reads images for rendering.
     * @param soundReader Reads sounds for playback.
     * @param inputListener Listens for user input.
     * @param windowController Controls the game window.
     */
    private void initMembers(ImageReader imageReader, SoundReader soundReader,
                             UserInputListener inputListener, WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.inputListener = inputListener;
        this.lifeCount = DEFAULT_LIFE_COUNT;
        this.hearts = new Heart[MAX_LIFE_COUNT];
        this.windowDimensions = windowController.getWindowDimensions();
        this.ballStartPosition = this.windowDimensions.mult(HALF);
        this.heartStartPosition = new Vector2(
                DEFAULT_HEART_START_POSITION.x(), this.windowDimensions.y()-20);
    }

    /**
     * Creates the ceiling game object.
     *
     * @param ceilingDimensions The dimensions of the ceiling.
     * @param rectangleRender The renderable for the ceiling.
     */
    private void createCeiling(Vector2 ceilingDimensions,
                               Renderable rectangleRender) {
        GameObject ceiling = new GameObject(Vector2.ZERO, ceilingDimensions, rectangleRender);
        ceiling.setCenter(CEILING_START);
        this.gameObjects().addGameObject(ceiling, Layer.STATIC_OBJECTS);
    }

    /**
     * Creates the strikes left information display.
     */
    private void createStrikesLeftInfo() {
        TextRenderable strikesRenderable = new TextRenderable(String.valueOf(this.lifeCount));
        strikesRenderable.setColor(getColorByLifeCount());
        Vector2 strikesPosition = new Vector2(this.windowDimensions.x() - 20,
                this.windowDimensions.y()-20);
        this.strikes = new GameObject(strikesPosition, STRIKES_DIMENSIONS, strikesRenderable);
        this.gameObjects().addGameObject(this.strikes, Layer.UI);
    }

    /**
     * Gets the color based on the current life count.
     *
     * @return The color representing the current life count.
     */
    private Color getColorByLifeCount() {
        return switch (this.lifeCount) {
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            default -> Color.GREEN;
        };
    }

    /**
     * Creates a row of heart objects representing lives.
     *
     * @param imageReader Reads images for rendering.
     * @param startPosition The starting position of the first heart.
     */
    private void createHeartRow(ImageReader imageReader, Vector2 startPosition) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH, true);
        Vector2 heartPosition = startPosition;
        float heartPadding = 40f / this.lifeCount;

        for (int heart_index = 0; heart_index < this.lifeCount; heart_index++) {
            this.hearts[heart_index] = new Heart(heartPosition, heartImage, this);
            this.gameObjects().addGameObject(this.hearts[heart_index], Layer.UI);
            heartPosition = heartPosition.add(new Vector2(
                    HEART_DIMENSION.x() + heartPadding, Vector2.ZERO.y()));
        }
    }

    /**
     * Updates the game state.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkIfPlayerWon();
        checkIfGameOver();
    }

    /**
     * Checks if a game object is out of bounds.
     *
     * @param gameObjectPosition The position of the game object.
     * @return True if the game object is out of bounds, false otherwise.
     */
    public boolean isOutOfBounds(Vector2 gameObjectPosition) {
        float height = gameObjectPosition.y();
        boolean outOfBounds = height > this.paddle.getCenter().y() + this.paddle.getDimensions().y();
        return outOfBounds;
    }

    /**
     * Checks if the player has won the game.
     */
    private void checkIfPlayerWon() {
        boolean pressedW = this.inputListener.isKeyPressed(KeyEvent.VK_W);
        boolean noMoreBricks = this.brickCount.value() <= 0;
        if(noMoreBricks || pressedW){
            openDialog(WIN_PROMPT);
            this.ball.setCenter(this.ballStartPosition);
        }
    }

    /**
     * Opens a dialog with the specified prompt.
     *
     * @param prompt The prompt to display in the dialog.
     */
    private void openDialog(String prompt){
        if (this.windowController.openYesNoDialog(prompt)){
            this.brickCount = new Counter(this.bricksInRow * this.brickRows);
            this.windowController.resetGame();
        }
        else{
            this.windowController.closeWindow();
        }
    }

    /**
     * Checks if the game is over.
     */
    private void checkIfGameOver() {
        float ballHeight = this.ball.getCenter().y();
        if (ballHeight > this.paddle.getCenter().y() + this.paddle.getDimensions().y()){
            this.lifeCount--;
            this.removeGameObject(this.hearts[this.lifeCount], Layer.UI);
            this.removeGameObject(this.strikes, Layer.UI);
            this.createStrikesLeftInfo();
            if (this.lifeCount == 0) {
                openDialog(LOSE_PROMPT);
            }
            else {
                this.ball.setCenter(this.ballStartPosition);
                this.ball.setVelocity(getRandomDiagonalVelocity());
            }
        }
    }

    /**
     * Creates the paddle game object.
     *
     * @param imageReader Reads images for rendering.
     * @param leftBoundary The left boundary of the game area.
     * @param rightBoundary The right boundary of the game area.
     * @param inputListener Listens for user input.
     */
    private void createPaddle(ImageReader imageReader, float leftBoundary,
                              float rightBoundary,
                              UserInputListener inputListener) {
        // create paddle
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, true);
        this.paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS,
                paddleImage, inputListener, leftBoundary, rightBoundary);

        Vector2 paddlePosition = new Vector2(this.windowDimensions.x() / 2,
                (this.windowDimensions.y() - PADDLE_PADDING_Y));
        this.paddle.setCenter(paddlePosition);
        this.paddle.setTag(PADDLE_TAG);
        this.gameObjects().addGameObject(this.paddle);

    }

    /**
     * Creates the background image game object.
     *
     * @param imageReader Reads images for rendering.
     */
    private void createBackgroundImage(ImageReader imageReader) {
        Renderable bgImageRender = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject bgImage = new GameObject(Vector2.ZERO,
                new Vector2(this.windowDimensions.x(), this.windowDimensions.y()), bgImageRender);
        this.gameObjects().addGameObject(bgImage, Layer.BACKGROUND);
    }

    /**
     * Creates a row of bricks.
     *
     * @param imageReader   Reads images for rendering.
     * @param startPosition The starting position of the first brick.
     * @param inputListener Listens for user input.
     * @param numberOfBricks The number of bricks in the row.
     * @param leftBoundary  The left boundary of the game area.
     * @param rightBoundary The right boundary of the game area.
     */
    private void createBrickRow(ImageReader imageReader, Vector2 startPosition,
                                UserInputListener inputListener,
                                int numberOfBricks, float leftBoundary, float rightBoundary) {
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, true);

        CollisionStrategyFactory factory = new CollisionStrategyFactory(
                this, leftBoundary, rightBoundary,
                inputListener, imageReader, soundReader, this.windowDimensions);

        float brickWidth = (rightBoundary - leftBoundary) / (numberOfBricks);

        Vector2 brickPosition = startPosition;
        float brickPadding = 40f / numberOfBricks;

        Vector2 brickDimension = new Vector2(brickWidth, BRICK_DIMENSIONS.y());
        for (int brickCount = 0; brickCount < numberOfBricks; brickCount++) {
            CollisionStrategy brickCollisionStrategy = factory.getCollisionStrategy();
            GameObject brick = new Brick(brickPosition, brickDimension,
                    brickImage, brickCollisionStrategy);
            this.gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            brickPosition = brickPosition.add(new Vector2(brickWidth + brickPadding, Vector2.ZERO.y()));
        }
    }

    /**
     * Creates multiple rows of bricks.
     *
     * @param imageReader   Reads images for rendering.
     * @param inputListener Listens for user input.
     * @param numberOfBricks The number of bricks in each row.
     * @param leftBoundary  The left boundary of the game area.
     * @param rightBoundary The right boundary of the game area.
     */
    private void createBrickRows(ImageReader imageReader, UserInputListener inputListener,
                                 int numberOfBricks, float leftBoundary, float rightBoundary) {
        Vector2 brickRowStart = new Vector2(leftBoundary, BRICKS_START_POSITION.y());
        for (int row = 0; row < this.brickRows; row++) {
            createBrickRow(imageReader, brickRowStart, inputListener, numberOfBricks,
                    leftBoundary, rightBoundary);
            brickRowStart = brickRowStart.add(new Vector2(Vector2.ZERO.x(), Vector2.DOWN.y())
                    .mult(BRICK_ROWS_PADDING));
        }
    }

    /**
     * Generates a random diagonal velocity for the ball.
     *
     * @return A Vector2 representing the random diagonal velocity.
     */
    private Vector2 getRandomDiagonalVelocity(){
        float ballVelocityX = getRandomVelocityFlip(BALL_SPEED);
        float ballVelocityY = getRandomVelocityFlip(BALL_SPEED);

        return (new Vector2(ballVelocityX, ballVelocityY));
    }

    /**
     * Creates the ball object and sets its initial properties.
     *
     * @param imageReader Reads images for rendering.
     * @param soundReader Reads sounds for playback.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        // create ball
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_COLLISION_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS, ballImage, collisionSound);

        this.ball.setVelocity(getRandomDiagonalVelocity());
        this.ball.setCenter(this.ballStartPosition);
        this.gameObjects().addGameObject(ball);
    }

    /**
     * Generates a random velocity with a possible flip in direction.
     *
     * @param velocity The base velocity value.
     * @return The possibly flipped velocity.
     */
    private float getRandomVelocityFlip(float velocity) {
        if(random.nextBoolean()){
            return velocity * -1;
        }
        return velocity;
    }

    /**
     * Creates the walls of the game area.
     *
     * @param wallDimensions The dimensions of the walls.
     * @param leftWallStart The starting position of the left wall.
     * @param rightWallStart The starting position of the right wall.
     * @param rectangleRender The renderable for the walls.
     */
    private void createWalls(Vector2 wallDimensions, Vector2 leftWallStart, Vector2 rightWallStart,
                             Renderable rectangleRender) {
        // create walls
        GameObject leftWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        leftWall.setCenter(leftWallStart);

        GameObject rightWall = new GameObject(Vector2.ZERO, wallDimensions, rectangleRender);
        rightWall.setCenter(rightWallStart);

        this.gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);
        this.gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
    }

    /**
     * Removes a game object from the specified layer.
     *
     * @param gameObject The game object to remove.
     * @param layer The layer from which to remove the game object.
     */
    public void removeGameObject(GameObject gameObject, int layer) {
        boolean removedSuccessfully = this.gameObjects().removeGameObject(gameObject, layer);
        if(removedSuccessfully && gameObject.getTag().equals(BRICK_TAG)){
            this.brickCount.decrement();
        }
    }

    /**
     * Adds a game object to the specified layer.
     *
     * @param gameObject The game object to add.
     * @param layer The layer to which to add the game object.
     */
    public void addGameObject(GameObject gameObject, int layer) {
        this.gameObjects().addGameObject(gameObject, layer);
    }

    /**
     * Gets the number of rows from the command line arguments.
     *
     * @param args The command line arguments.
     * @return The number of rows.
     */
    private static int getNumberOfRows(String[] args){
        if(args.length >= 2){
            return Integer.parseInt(args[1]);
        }
        return DEFAULT_NUMBER_OF_BRICK_ROWS;
    }

    /**
     * Gets the number of bricks in a row from the command line arguments.
     *
     * @param args The command line arguments.
     * @return The number of bricks in a row.
     */
    private static int getNumberOfBricksInARow(String[] args){
        if(args.length >= 1){
            return Integer.parseInt(args[0]);
        }
        return DEFAULT_NUMBER_OF_BRICKS_IN_ROWS;
    }

    /**
     * The main method to start the game.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int rows = getNumberOfRows(args);
        int bricks_in_row = getNumberOfBricksInARow(args);
        GameManager gm = new BrickerGameManager(GAME_TITLE, GAME_DIMENSIONS, rows, bricks_in_row);
        gm.run();
    }

    /**
     * Updates the number of lives and the heart row display.
     */
    public void updateLives() {
        for (int i = this.lifeCount - 1; i >= 0; i--) {
            this.removeGameObject(this.hearts[i], Layer.UI);
            this.hearts[i] = null;
        }
        this.removeGameObject(this.strikes, Layer.UI);
        this.lifeCount++;
        createHeartRow(imageReader, this.heartStartPosition);
        createStrikesLeftInfo();
    }

    /**
     * Gets the current life count.
     *
     * @return The current life count.
     */
    public int getLifeCount() {
        return lifeCount;
    }
}
