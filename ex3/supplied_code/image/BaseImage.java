package image;

import java.awt.*;

/**
 * The BaseImage class provides an abstract representation of an image.
 * It includes methods to get the image dimensions, pixel colors, and calculate the image brightness.
 */
public abstract class BaseImage {

    private static final double RED_GRAY_WEIGHT = 0.2126;
    private static final double GREEN_GRAY_WEIGHT = 0.7152;
    private static final double BLUE_GRAY_WEIGHT = 0.0722;
    private static final double MAX_RGB_SCORE = 255;


    /**
     * @return The width of the image
     */
    public abstract int getWidth();

    /**
     * @return The height of the image
     */
    public abstract int getHeight();

    /**
     * @param x The row coordinate of the pixel
     * @param y The column coordinate of the pixel
     * @return The pixel color values at the given coordinates
     */
    public abstract Color getPixel(int x, int y);

    /**
     * @return The pixel array of the image
     */
    public Color[][] getPixelArray() {
        Color[][] pixelArray = new Color[getHeight()][getWidth()];
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                pixelArray[i][j] = getPixel(i, j);
            }
        }
        return pixelArray;
    }

    /**
     * Calculates the brightness of the image.
     * @return The brightness of the image
     */
    public double getImageBrightness() {
        Color[][] pixelArray = getPixelArray();
        double grayscaleSum = 0;
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                Color currentPixel = pixelArray[row][col];
                double red = currentPixel.getRed() * RED_GRAY_WEIGHT;
                double green = currentPixel.getGreen() * GREEN_GRAY_WEIGHT;
                double blue = currentPixel.getBlue() * BLUE_GRAY_WEIGHT;
                grayscaleSum += red + green + blue;
            }
        }
        // normalize to between 0 and 1
        return grayscaleSum / (getHeight() * getWidth() * MAX_RGB_SCORE);
    }


}
