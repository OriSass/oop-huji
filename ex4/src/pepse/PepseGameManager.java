package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Statistics;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatar.jump.JumpNotifier;
import pepse.world.avatar.jump.observers.Cloud;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

import static pepse.util.Constants.*;

/**
 * The main game manager class for the Pepse game.
 */
public class PepseGameManager extends GameManager {

    /**
     * The default length of the day cycle.
     */
    public static final float DEFAULT_DAY_CYCLE_LENGTH = 30f;

    /**
     * The length of the cloud cycle.
     */
    public static final float CLOUD_CYCLE_LENGTH = 100f;

    /**
     * The opacity of the night at midnight.
     */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * The game object representing the energy display.
     */
    private GameObject energyDisplay;

    /**
     * The terrain in the game.
     */
    private Terrain terrain;

    /**
     * The avatar in the game.
     */
    private Avatar avatar;

    /**
     * The jump notifier for the avatar.
     */
    private JumpNotifier jumpNotifier;

    /**
     * The cloud observer for the avatar's jumps.
     */
    private Cloud cloud;

    /**
     * The start x-coordinate of the terrain.
     */
    private float terrainStart;

    /**
     * The end x-coordinate of the terrain.
     */
    private float terrainEnd;

    /**
     * The flora in the game.
     */
    private Flora flora;

    /**
     * The statistics utility for the game.
     */
    private Statistics statistics;

    /**
     * The main method to run the game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game.
     *
     * @param imageReader The image reader.
     * @param soundReader The sound reader.
     * @param inputListener The user input listener.
     * @param windowController The window controller.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(60);
        this.jumpNotifier = new JumpNotifier();
        this.statistics = new Statistics(RANDOM_SEED);
        createSky(windowController);
        initTerrain(windowController);
        createTerrain();
        createNight(windowController);
        createSun(windowController);
        createAvatar(imageReader, inputListener, windowController, this.terrain::groundHeightAt);
        createFlora(this.terrain::groundHeightAt);
        createCloud(windowController);
        setCamera(new Camera(this.avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }
    /**
     * Creates the cloud observer for the avatar's jumps.
     *
     * @param windowController The window controller.
     */
    private void createCloud(WindowController windowController) {
        this.cloud = new Cloud(windowController.getWindowDimensions(),
                (gameObj, layer) -> this.gameObjects().addGameObject(gameObj, layer),
                this.statistics::flipCoin);
        this.jumpNotifier.addObserver(this.cloud);

    }
    /**
     * Creates the flora in the game.
     *
     * @param getHeightByX Function to get the ground height by x-coordinate.
     */
    private void createFlora(Function<Float,Float> getHeightByX) {
        this.flora = new Flora(
                getHeightByX,
                (gameObj, layer) -> this.gameObjects().addGameObject(gameObj,
                        layer) ,this::fruitHandler,
                (gameObj, layer) -> this.gameObjects().removeGameObject(gameObj,
                        layer), this::removeGameObjList
        );
        this.flora.createInRange((int) this.terrainStart, (int) this.terrainEnd);
    }

    /**
     * Creates the energy display.
     *
     * @param energy The current energy level.
     * @param windowController The window controller.
     */
    public void createEnergyDisplay(float energy, WindowController windowController) {
        TextRenderable energyRenderable = new TextRenderable(String.valueOf((int) energy));
        energyRenderable.setColor(Color.green);
        Vector2 strikesPosition = new Vector2(windowController.getWindowDimensions().x() - 50,
                windowController.getWindowDimensions().y()-20);
        this.energyDisplay = new GameObject(strikesPosition, ENERGY_DISPLAY_DIMENSIONS, energyRenderable);
        this.energyDisplay.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(this.energyDisplay, Layer.UI);
    }

    /**
     * Removes the energy display.
     */
    public void removeEnergyDisplay() {
        this.gameObjects().removeGameObject(this.energyDisplay, Layer.UI);
    }

    /**
     * Creates the avatar in the game.
     *
     * @param imageReader The image reader.
     * @param inputListener The user input listener.
     * @param windowController The window controller.
     * @param getHeightByX Function to get the ground height by x-coordinate.
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              WindowController windowController, Function<Float,Float> getHeightByX) {
        float x = windowController.getWindowDimensions().x() / 2;
        Vector2 avatarPosition = new Vector2(x, getHeightByX.apply(x) - AVATAR_DIMENSIONS.y());
        this.avatar = new Avatar(avatarPosition, inputListener, imageReader,
                windowController, this::createEnergyDisplay, this::removeEnergyDisplay,
                this.jumpNotifier::notifyObservers);
        this.gameObjects().addGameObject(this.avatar);
    }

    /**
     * Handle the collision between a fruit and another object.
     * @param fruit The fruit that was eaten.
     * @param other The object that ate the fruit.
     */
    private void fruitHandler(GameObject fruit, GameObject other) {
        // If the other object is not the avatar, we don't care - Only the avatar can eat the fruit
        if (!other.getTag().equals(AVATAR_TAG)) {
            return;
        }

        boolean avatarAte = avatar.addToEnergy(FRUIT_ENERGY);
        if(avatarAte){
            gameObjects().removeGameObject(fruit);
            new ScheduledTask(
                    avatar,
                    DEFAULT_DAY_CYCLE_LENGTH,
                    false,
                    () -> {
                        // check if there is still a tree (it could have been removed)
                        // if there is respawn fruit
                        if(this.flora.treeExistsByFruit(fruit)){
                            gameObjects().addGameObject(fruit);
                        }
                    });
        }
    }

    /**
     * Creates the sun in the game.
     *
     * @param windowController The window controller.
     */
    private void createSun(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(), DEFAULT_DAY_CYCLE_LENGTH);
        createSunTransition(sun, windowController.getWindowDimensions());
        createSunHalo(sun);
        this.gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    /**
     * Creates the night effect in the game.
     *
     * @param windowController The window controller.
     */
    private void createNight(WindowController windowController) {
        GameObject night = Night.create(windowController.getWindowDimensions(), DEFAULT_DAY_CYCLE_LENGTH);
        createNightTransition(night);
        this.gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    /**
     * Initializes the terrain in the game.
     *
     * @param windowController The window controller.
     */
    private void initTerrain(WindowController windowController){
        this.terrain = new Terrain(windowController.getWindowDimensions(), RANDOM_SEED);
        this.terrainStart = -1 * TERRAIN_GAP;
        this.terrainEnd = windowController.getWindowDimensions().x() + TERRAIN_GAP;
    }
    /**
     * Creates the terrain in the game.
     */
    private void createTerrain() {
        List<Block> blocks = terrain.createInRange((int) this.terrainStart, (int) this.terrainEnd);
        for (Block block : blocks) {
            this.gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Creates the sky in the game.
     *
     * @param windowController The window controller.
     */
    private void createSky(WindowController windowController) {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        this.gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    /**
     * Creates the transition for the night effect.
     *
     * @param night The night game object.
     */
    private void createNightTransition(GameObject night) {
        new Transition<Float>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY, // final opacity
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                DEFAULT_DAY_CYCLE_LENGTH / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Creates the transition for the sun.
     *
     * @param sun The sun game object.
     * @param windowDimensions The dimensions of the window.
     */
    private void createSunTransition(GameObject sun, Vector2 windowDimensions) {
        Vector2 initialSunCenter = sun.getCenter();
        Vector2 cycleCenter =
                new Vector2(windowDimensions.x() / 2, Terrain.getGroundHeightAtX0(windowDimensions));
        new Transition<Float>(
                sun,
                (angle) -> sun.setCenter(initialSunCenter.
                        subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                SUN_TRANSITION_START_VALUE,
                SUN_TRANSITION_END_VALUE, // final opacity
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                DEFAULT_DAY_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }

    /**
     * Creates the sun halo in the game.
     *
     * @param sun The sun game object.
     */
    private void createSunHalo(GameObject sun) {
        GameObject sunHalo = SunHalo.create(sun);
        this.gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    /**
     * Updates the game state.
     *
     * @param deltaTime The time that has passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        adjustInfiniteWorld();
    }

    /**
     * Adds a list of game objects to the game.
     *
     * @param gameObjList The list of game objects.
     * @param layer The layer to add the game objects to.
     */
    private void addGameObjList(List<GameObject> gameObjList, int layer){
        for(GameObject gameObj : gameObjList){
            this.gameObjects().addGameObject(gameObj, layer);
        }
    }

    /**
     * Removes a list of game objects from the game.
     *
     * @param gameObjList The list of game objects.
     * @param layer The layer to remove the game objects from.
     */
    public void removeGameObjList(List<GameObject> gameObjList, int layer){
        for(GameObject gameObj : gameObjList){
            this.gameObjects().removeGameObject(gameObj, layer);
        }
    }

    /**
     * Adjusts the infinite world by adding and removing terrain and flora as needed.
     */
    private void adjustInfiniteWorld() {
        float cameraStartX = this.camera().getTopLeftCorner().x();
        float cameraEndX = cameraStartX + this.camera().getDimensions().x();

        boolean needToCreateLeftWorld = cameraStartX < this.terrainStart;
        boolean needToCreateRightWorld = cameraEndX > this.terrainEnd;

        if (needToCreateLeftWorld || needToCreateRightWorld) {
            // right
            int addStart = (int) (this.terrainEnd);
            int addEnd = (int) (cameraEndX + TERRAIN_GAP);
            float removeStart = this.terrainStart;
            float removeEnd = cameraStartX - TERRAIN_GAP;

            // left
            if(needToCreateLeftWorld){
                addStart = (int) (cameraStartX - TERRAIN_GAP);
                addEnd = (int) Math.ceil(this.terrainStart);
                removeStart = cameraEndX + TERRAIN_GAP;
                removeEnd = this.terrainEnd;
            }

            changeWorld(addStart, addEnd, removeStart, removeEnd);

            this.terrainStart = cameraStartX - TERRAIN_GAP;
            this.terrainEnd = cameraEndX + TERRAIN_GAP;
        }
    }

    /**
     * Changes the world by adding and removing terrain and flora.
     *
     * @param addStart The start x-coordinate to add.
     * @param addEnd The end x-coordinate to add.
     * @param removeStart The start x-coordinate to remove.
     * @param removeEnd The end x-coordinate to remove.
     */
    private void changeWorld(int addStart, int addEnd, float removeStart, float removeEnd) {
        changeTerrain(addStart, addEnd, removeStart, removeEnd);
        changeFlora(addStart, addEnd, removeStart, removeEnd);
    }

    /**
     * Changes the flora by adding and removing flora.
     *
     * @param addStart The start x-coordinate to add.
     * @param addEnd The end x-coordinate to add.
     * @param removeStart The start x-coordinate to remove.
     * @param removeEnd The end x-coordinate to remove.
     */
    private void changeFlora(int addStart, int addEnd, float removeStart, float removeEnd) {
        this.flora.createInRange(addStart, addEnd);
        this.flora.removeInRange(removeStart, removeEnd);
    }

    /**
     * Changes the terrain by adding and removing terrain blocks.
     *
     * @param addStart The start x-coordinate to add.
     * @param addEnd The end x-coordinate to add.
     * @param removeStart The start x-coordinate to remove.
     * @param removeEnd The end x-coordinate to remove.
     */
    private void changeTerrain(int addStart, int addEnd, float removeStart, float removeEnd) {
        List<GameObject> addedBlocks = this.terrain.addTerrainForInfinite(addStart, addEnd);
        this.addGameObjList(addedBlocks, Layer.STATIC_OBJECTS);

        List<GameObject> blocksToRemove = this.terrain.getBlocks(removeStart, removeEnd);
        this.removeGameObjList(blocksToRemove, Layer.STATIC_OBJECTS);

        this.terrain.removeTerrain(blocksToRemove);
    }
}
