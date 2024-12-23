package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * Represents an image and provides methods to manipulate and save the image.
 * This class extends the BaseImage class.
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image extends BaseImage{

    private Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs an Image instance from a file.
     *
     * @param filename the name of the file to read the image from
     * @throws IOException if an error occurs during reading the file
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs an Image instance from a pixel array, width, and height.
     *
     * @param pixelArray the array of pixel colors
     * @param width the width of the image
     * @param height the height of the image
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of the image.
     *
     * @return the width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the image.
     *
     * @return the height of the image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the pixel color at the specified coordinates.
     *
     * @param x the row coordinate of the pixel
     * @param y the column coordinate of the pixel
     * @return the color of the pixel at the specified coordinates
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Saves the image to a file.
     *
     * @param fileName the name of the file to save the image to
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
