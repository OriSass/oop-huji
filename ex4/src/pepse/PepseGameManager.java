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
import pepse.world.avatar.Avatar;
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

public class PepseGameManager extends GameManager {

    public static final float DEFAULT_DAY_CYCLE_LENGTH = 30f;
    public static final float CLOUD_CYCLE_LENGTH = 100f;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private GameObject energyDisplay;
    private Terrain terrain;
    private Avatar avatar;
    private JumpNotifier jumpNotifier;
    private Cloud cloud;

    private float terrainStart;
    private float terrainEnd;
    private Flora flora;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(60);
        this.jumpNotifier = new JumpNotifier();
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

    private void createCloud(WindowController windowController) {
        this.cloud = new Cloud(windowController.getWindowDimensions(),
                (gameObj, layer) -> this.gameObjects().addGameObject(gameObj, layer));
        this.jumpNotifier.addObserver(this.cloud);

    }

    private void createFlora(Function<Float,Float> getHeightByX) {
        this.flora = new Flora(
                getHeightByX,
                (gameObj, layer) -> this.gameObjects().addGameObject(gameObj,
                        layer) ,this::fruitHandler,
                (gameObj, layer) -> this.gameObjects().removeGameObject(gameObj,
                        layer), this::removeGameObjList);
        this.flora.createInRange((int) this.terrainStart, (int) this.terrainEnd);
    }

    /**
     * Creates the strikes left information display.
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

    public void removeEnergyDisplay() {
        this.gameObjects().removeGameObject(this.energyDisplay, Layer.UI);
    }


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

    private void createSun(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(), DEFAULT_DAY_CYCLE_LENGTH);
        createSunTransition(sun, windowController.getWindowDimensions());
        createSunHalo(sun);
        this.gameObjects().addGameObject(sun, Layer.BACKGROUND);
    }

    private void createNight(WindowController windowController) {
        GameObject night = Night.create(windowController.getWindowDimensions(), DEFAULT_DAY_CYCLE_LENGTH);
        createNightTransition(night);
        this.gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    private void initTerrain(WindowController windowController){
        this.terrain = new Terrain(windowController.getWindowDimensions(), 5);
        this.terrainStart = -1 * TERRAIN_GAP;
        this.terrainEnd = windowController.getWindowDimensions().x() + TERRAIN_GAP;
    }
    private void createTerrain() {
        List<Block> blocks = terrain.createInRange((int) this.terrainStart, (int) this.terrainEnd);
        for (Block block : blocks) {
            this.gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createSky(WindowController windowController) {
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        this.gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

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
    private void createSunTransition(GameObject sun, Vector2 windowDimensions) {
        Vector2 initialSunCenter = sun.getCenter();
        Vector2 cycleCenter =
                new Vector2(windowDimensions.x() / 2, Terrain.getGroundHeightAtX0(windowDimensions));
        new Transition<Float>(
                sun,
                (angle) -> sun.setCenter(initialSunCenter.
                        subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                0f,
                360f, // final opacity
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                DEFAULT_DAY_CYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }

    private void createSunHalo(GameObject sun) {
        GameObject sunHalo = SunHalo.create(sun);
        this.gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        adjustInfiniteWorld();
    }

    private void addGameObjList(List<GameObject> gameObjList, int layer){
        for(GameObject gameObj : gameObjList){
            this.gameObjects().addGameObject(gameObj, layer);
        }
    }

    public void removeGameObjList(List<GameObject> gameObjList, int layer){
        for(GameObject gameObj : gameObjList){
            this.gameObjects().removeGameObject(gameObj, layer);
        }
    }

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
                addEnd =  (int) this.terrainStart;
                removeStart = cameraEndX + TERRAIN_GAP;
                removeEnd = this.terrainEnd;
            }

            changeWorld(addStart, addEnd, removeStart, removeEnd);

            this.terrainStart = cameraStartX - TERRAIN_GAP;
            this.terrainEnd = cameraEndX + TERRAIN_GAP;
        }
    }

    private void changeWorld(int addStart, int addEnd, float removeStart, float removeEnd) {
        changeTerrain(addStart, addEnd, removeStart, removeEnd);
        changeFlora(addStart, addEnd, removeStart, removeEnd);
    }

    private void changeFlora(int addStart, int addEnd, float removeStart, float removeEnd) {
        this.flora.createInRange(addStart, addEnd);
        this.flora.removeInRange(removeStart, removeEnd);
    }

    private void changeTerrain(int addStart, int addEnd, float removeStart, float removeEnd) {
        List<GameObject> addedBlocks = this.terrain.addTerrainForInfinite(addStart, addEnd);
        this.addGameObjList(addedBlocks, Layer.STATIC_OBJECTS);

        List<GameObject> blocksToRemove = this.terrain.getBlocks(removeStart, removeEnd);
        this.removeGameObjList(blocksToRemove, Layer.STATIC_OBJECTS);

        this.terrain.removeTerrain(blocksToRemove);
    }
}
