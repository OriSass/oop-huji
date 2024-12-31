package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static pepse.util.Constants.DEFAULT_BASE_GROUND_COLOR;

/**
 * A class representing the terrain in the game.
 */
public class Terrain {
    /**
     * The factor that determines the chaos in the terrain generation.
     */
    private static final float CHAOS_FACTOR = Block.SIZE * 7;
    /**
     * The initial ground height at x = 0.
     */
    private final float groundHeightAtX0;
    /**
     * The base color of the ground.
     */
    private static final Color BASE_GROUND_COLOR = DEFAULT_BASE_GROUND_COLOR;
    /**
     * The depth of the terrain in blocks.
     */
    private static final int TERRAIN_DEPTH = 20;
    /**
     * The noise generator for terrain height.
     */
    private final NoiseGenerator noiseGenerator;

    /**
     * The list of ground blocks in the terrain.
     */
    private List<Block> groundBlocks;
    /**
     * The end x-coordinate of the terrain.
     */
    private int endX;

    /**
     * The start x-coordinate of the terrain.
     */
    private int startX;

    /**
     * Constructs a new Terrain instance.
     *
     * @param windowDimensions The dimensions of the window.
     * @param seed The seed for the noise generator.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = getGroundHeightAtX0(windowDimensions);
        noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Gets the ground height at x = 0 based on the window dimensions.
     *
     * @param windowDimensions The dimensions of the window.
     * @return The ground height at x = 0.
     */
    public static float getGroundHeightAtX0(Vector2 windowDimensions){
        return windowDimensions.y() * ((float) 2 /3);
    }

    /**
     * Gets the ground height at a given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The ground height at the given x-coordinate.
     */
    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, CHAOS_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates terrain in the specified range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of ground blocks created in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX){
        this.startX = getTerrainStartX(minX);
        this.endX = getTerrainEndX(startX, maxX);
        this.groundBlocks = new ArrayList<>();

        addTerrain(this.startX, this.endX);
        return this.groundBlocks;
    }

    /**
     * Adds terrain blocks in the specified range.
     *
     * @param min The minimum x-coordinate.
     * @param max The maximum x-coordinate.
     * @return The list of game objects added.
     */
    private List<GameObject> addTerrain(int min, int max) {
        List<GameObject> nowAdded = new ArrayList<>();
        for (int blockRow = 0; blockRow < TERRAIN_DEPTH; blockRow++) {
            for (int x = min; x < max; x += Block.SIZE){
                float blockYCoordinate =
                        (float) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE +
                                blockRow * Block.SIZE);
                Vector2 topLeftCorner = new Vector2(x, blockYCoordinate);
                RectangleRenderable rectangleRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(topLeftCorner, rectangleRenderable);
                block.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                block.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
                this.groundBlocks.add(block);
                nowAdded.add(block);
            }
        }
        return nowAdded;
    }

    /**
     * Gets the end x-coordinate of the terrain based on the start x-coordinate and maximum x-coordinate.
     *
     * @param startX The start x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The end x-coordinate of the terrain.
     */
    public static int getTerrainEndX(int startX, int maxX) {
        while (startX < maxX){
            startX += Block.SIZE;
        }
        return startX;
    }

    /**
     * Gets the start x-coordinate of the terrain based on the minimum x-coordinate.
     *
     * @param minX The minimum x-coordinate.
     * @return The start x-coordinate of the terrain.
     */
    public static int getTerrainStartX(int minX) {
        if(minX % Block.SIZE == 0){
            return minX;
        }
        int x = 0;
        if (x < minX){
            while (x + Block.SIZE < minX){
                x += Block.SIZE;
            }
            return x;
        }
        else {
            // x > minx
            while (x > minX){
                x -= Block.SIZE;
            }
            return x;
        }
    }

    /**
     * Adds terrain blocks for infinite terrain generation in the specified range.
     *
     * @param min The minimum x-coordinate.
     * @param max The maximum x-coordinate.
     * @return The list of game objects added.
     */
    public List<GameObject> addTerrainForInfinite(int min, int max){
        return addTerrain(min, max);
    }

    /**
     * Gets the blocks in the specified x-coordinate range.
     *
     * @param minX The minimum x-coordinate.
     * @param maxX The maximum x-coordinate.
     * @return The list of blocks in the specified range.
     */
    public List<GameObject> getBlocks(float minX, float maxX) {
        List<GameObject> blocks = new ArrayList<>();
        for (Block block : this.groundBlocks){
            if(block.getTopLeftCorner().x() >= minX && block.getTopLeftCorner().x() <= maxX){
                blocks.add(block);
            }
        }
        return blocks;
    }

    /**
     * Removes the specified blocks from the terrain.
     *
     * @param blocksToRemove The list of blocks to remove.
     */
    public void removeTerrain(List<GameObject> blocksToRemove) {
        this.groundBlocks.removeAll(blocksToRemove);
    }
}
