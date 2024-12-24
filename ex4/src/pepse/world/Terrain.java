package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import supplied_code.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {

    private final float groundHeightAtX0;
    private final int seed;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;

    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = getGroundHeightAtX0(windowDimensions);
        this.seed = seed;
    }

    public static float getGroundHeightAtX0(Vector2 windowDimensions){
        return windowDimensions.y() * ((float) 2 /3);
    }

    // todo migrate to use NoiseGenerator later
    public float groundHeightAt(float x){
        return this.groundHeightAtX0 + 5;
    }

    public List<Block> createInRange(int minX, int maxX){
        RectangleRenderable rectangleRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

        int startX = getTerrainStartX(minX);
        int endX = getTerrainEndX(startX, maxX);
        List<Block> blocks = new ArrayList<>();

        for (int blockRow = 0; blockRow < TERRAIN_DEPTH; blockRow++) {
            for (int x = startX; x < endX; x += Block.SIZE){
                float blockYCoordinate =
                        (float) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE +
                                blockRow * Block.SIZE);
                Vector2 topLeftCorner = new Vector2(x, blockYCoordinate);
                blocks.add(new Block(topLeftCorner, rectangleRenderable));
            }
        }
        return blocks;
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
}
