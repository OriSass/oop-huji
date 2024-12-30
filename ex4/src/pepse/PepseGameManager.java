package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.util.Statistics;
import pepse.world.avatar.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatar.jump.JumpNotifier;
import pepse.world.avatar.jump.observers.Cloud;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.StaticTree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static pepse.util.Constants.*;

public class PepseGameManager extends GameManager {

    public static final float DEFAULT_DAY_CYCLE_LENGTH = 30f;
    public static final float CLOUD_CYCLE_LENGTH = 100f;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private GameObject energyDisplay;
    private Terrain terrain;
    private List<StaticTree> trees;
    private Avatar avatar;
    private JumpNotifier jumpNotifier;
    private Cloud cloud;

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
        createTerrain(windowController);
        createNight(windowController);
        createSun(windowController);
        createAvatar(imageReader, inputListener, windowController, this.terrain::groundHeightAt);
        createTrees(this.terrain::groundHeightAt, windowController);
        createCloud(windowController);
    }

    private void createCloud(WindowController windowController) {
        this.cloud = new Cloud(windowController.getWindowDimensions(),
                (gameObj, layer) -> this.gameObjects().addGameObject(gameObj, layer));
        this.jumpNotifier.addObserver(this.cloud);

    }

    private void createTrees(Function<Float,Float> getHeightByX, WindowController windowController) {
        this.trees = new ArrayList<>();
        for (float x = 0;
             x < windowController.getWindowDimensions().x();
             x += (TREE_TRUNK_WIDTH + AVATAR_DIMENSIONS.x())) {
            if(Statistics.flipCoin(TREE_CREATION_CHANCE)){
                float trunkHeight = (float) (Math.floor(getHeightByX.apply(x) / Block.SIZE) * Block.SIZE);
                Vector2 location = new Vector2(x, trunkHeight);
                StaticTree tree = new StaticTree(location,
                        (gameObj, layer) -> this.gameObjects().addGameObject(gameObj, layer),
                        this::fruitHandler);
                trees.add(tree);
            }
        }
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
                    () -> gameObjects().addGameObject(fruit));
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

    private void createTerrain(WindowController windowController) {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 5);
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
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

}
