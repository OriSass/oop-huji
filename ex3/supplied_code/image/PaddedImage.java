package image;

import java.awt.*;

/**
 * The PaddedImage class represents an image that has been padded to the nearest power of two dimensions.
 * This class extends the BaseImage class and provides methods to get the padded image dimensions,
 * pixel colors, and sub-images.
 */
public class PaddedImage extends BaseImage{


    private final BaseImage baseImage;
    private int paddedWidth;
    private int paddedHeight;

    private int rowPadding;
    private int colPadding;
    private Color[][] pixelArray;


    /**
     * Constructs a PaddedImage instance from a base image.
     *
     * @param baseImage the base image to pad
     */
    public PaddedImage(BaseImage baseImage) {
        this.baseImage = baseImage;
        padPicture();
    }

    /**
     * Pads the base image to the nearest power of two dimensions.
     */
    private void padPicture(){

        int width = this.baseImage.getWidth();
        int height = this.baseImage.getHeight();

        this.paddedWidth = ImageUtils.getNextPowerOfTwo(width);
        this.paddedHeight = ImageUtils.getNextPowerOfTwo(height);
        Color[][] newPixelArray = new Color[this.paddedHeight][this.paddedWidth];

        if (width == paddedWidth && height == paddedHeight) {
            this.pixelArray = this.baseImage.getPixelArray();
            return;
        }

        // if pixel is in original picture, set as it was
        this.rowPadding = (paddedHeight - height) / 2;
        this.colPadding = (paddedWidth - width) / 2;

        // copy the original picture to the new picture
        Color[][] pixelArray = this.baseImage.getPixelArray();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color pixel = pixelArray[row][col];
                newPixelArray[row + this.rowPadding][col + this.colPadding] = pixel;
            }
        }
        // fill the rest with white
        for (int row = 0; row < this.paddedHeight; row++) {
            for (int col = 0; col < this.paddedWidth; col++) {
                if (newPixelArray[row][col] == null) {
                    newPixelArray[row][col] = Color.WHITE;
                }
            }
        }
        this.pixelArray = newPixelArray;
    }

    /**
     * Gets the width of the padded image.
     *
     * @return the width of the padded image
     */
    @Override
    public int getWidth() {
        return paddedWidth;
    }

    /**
     * Gets the height of the padded image.
     *
     * @return the height of the padded image
     */
    @Override
    public int getHeight() {
        return paddedHeight;
    }

    /**
     * Gets the pixel color at the specified coordinates.
     *
     * @param x the row coordinate of the pixel
     * @param y the column coordinate of the pixel
     * @return the color of the pixel at the specified coordinates
     */
    @Override
    public Color getPixel(int x, int y) {
        return this.pixelArray[x][y];
    }

    /**
     * Gets the sub-images of the padded image with the specified resolution.
     *
     * @param resolution the resolution of the sub-images
     * @return a 2D array of sub-images
     */
    public Image[][] getSubImages(int resolution){
        int subImageSide = this.paddedWidth / resolution;
        int subImageRows = this.paddedHeight / subImageSide;
        Image[][] subImages = new Image[subImageRows][resolution];
        for (int i = 0; i < subImageRows; i++) {
            for (int j = 0; j < resolution; j++) {
                subImages[i][j] = getSubImage(i * subImageSide, j * subImageSide, subImageSide);
            }
        }
        return subImages;
    }

    /**
     * Gets a sub-image of the padded image starting at the specified coordinates with the
     * specified side length.
     *
     * @param yStart the starting row coordinate of the sub-image
     * @param xStart the starting column coordinate of the sub-image
     * @param side the side length of the sub-image
     * @return the sub-image
     */
    public Image getSubImage(int yStart, int xStart, int side){
        Color[][] subImagePixelArray = new Color[side][side];
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                subImagePixelArray[row][col] =
                        this.pixelArray[yStart + row][xStart + col];
            }
        }
        return new Image(subImagePixelArray, side, side);
    }
}
