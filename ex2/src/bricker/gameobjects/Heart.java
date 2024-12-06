package src.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {
    public Heart(Vector2 heartPosition, Vector2 heartDimension, Renderable heartImage) {
        super(heartPosition, heartDimension, heartImage);

    }
}
