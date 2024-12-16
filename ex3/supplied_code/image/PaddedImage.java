package image;

import java.awt.*;

public class PaddedImage extends BaseImage{


    private final BaseImage baseImage;
    private int paddedWidth;
    private int paddedHeight;

    private int rowPadding;
    private int colPadding;
    private Color[][] pixelArray;


    public PaddedImage(BaseImage baseImage) {
        this.baseImage = baseImage;
        padPicture();
    }

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
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                newPixelArray[row + this.rowPadding][col + this.colPadding] =
                        this.baseImage.getPixelArray()[row][col];
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

    @Override
    public int getWidth() {
        return paddedWidth;
    }

    @Override
    public int getHeight() {
        return paddedHeight;
    }

    @Override
    public Color getPixel(int x, int y) {
        return this.pixelArray[x][y];
    }

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
