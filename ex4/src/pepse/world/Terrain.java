package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import supplied_code.ColorSupplier;
import supplied_code.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static pepse.util.Constants.DEFAULT_BASE_GROUND_COLOR;

public class Terrain {
    private static final float CHAOS_FACTOR = Block.SIZE * 7;


    private final float groundHeightAtX0;
    private final int seed;
    private static final Color BASE_GROUND_COLOR = DEFAULT_BASE_GROUND_COLOR;
    private static final int TERRAIN_DEPTH = 20;
    private final NoiseGenerator noiseGenerator;

    private List<Block> groundBlocks;
    private int endX;
    private int startX;
    private static  RectangleRenderable rectangleRenderable =
            new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = getGroundHeightAtX0(windowDimensions);
        this.seed = seed;
        noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    public static float getGroundHeightAtX0(Vector2 windowDimensions){
        return windowDimensions.y() * ((float) 2 /3);
    }

    public float groundHeightAt(float x){
        float noise = (float) noiseGenerator.noise(x, CHAOS_FACTOR);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX){
        this.startX = getTerrainStartX(minX);
        this.endX = getTerrainEndX(startX, maxX);
        this.groundBlocks = new ArrayList<>();

        addTerrain(this.startX, this.endX);
        return this.groundBlocks;
    }

    private List<GameObject> addTerrain(int min, int max) {
        List<GameObject> nowAdded = new ArrayList<>();
        for (int blockRow = 0; blockRow < TERRAIN_DEPTH; blockRow++) {
            for (int x = min; x < max; x += Block.SIZE){
                float blockYCoordinate =
                        (float) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE +
                                blockRow * Block.SIZE);
                Vector2 topLeftCorner = new Vector2(x, blockYCoordinate);
                Block block = new Block(topLeftCorner, rectangleRenderable);
                block.physics().preventIntersectionsFromDirection(Vector2.ZERO);
                block.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
                this.groundBlocks.add(block);
                nowAdded.add(block);
            }
        }
        return nowAdded;
    }

    private int getTerrainEndX(int startX, int maxX) {
        while (startX < maxX){
            startX += Block.SIZE;
        }
        return startX;
    }

    private int getTerrainStartX(int minX) {
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

    public List<GameObject> addTerrainForInfinite(int min, int max){
        return addTerrain(min, max);
    }

    public List<GameObject> getBlocks(float minX, float maxX) {
        List<GameObject> blocks = new ArrayList<>();
        for (Block block : this.groundBlocks){
            if(block.getTopLeftCorner().x() >= minX && block.getTopLeftCorner().x() <= maxX){
                blocks.add(block);
            }
        }
        return blocks;
    }

    public void removeTerrain(List<GameObject> blocksToRemove) {
        this.groundBlocks.removeAll(blocksToRemove);
    }
}
