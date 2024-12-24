package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * The AsciiArtAlgorithm class runs the main algorithm of converting an image to ASCII art.
 */
public class AsciiArtAlgorithm {

    /**
     * The matcher that matches sub-images to characters based on brightness.
     */
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * The default character set used in the algorithm.
     */
    char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * The default image resolution used in the algorithm.
     */
    int DEFAULT_IMG_RESOLUTION = 2;

    /**
     * The current character set used in the algorithm.
     */
    private char[] charset;

    /**
     * The current resolution of the algorithm.
     */
    private int resolution;

    /**
     * The minimum resolution of the algorithm.
     */
    private int minResolution;

    /**
     * The maximum resolution of the algorithm.
     */
    private int maxResolution;

    /**
     * The rounding method used in the algorithm.
     */
    private RoundMethod roundMethod;

    /**
     * The ASCII output method used in the algorithm.
     */
    private AsciiOutput asciiOutput;

    /**
     * The image to be converted to ASCII art.
     */
    private Image image;

    /**
     * Constructs an AsciiArtAlgorithm with the specified image path.
     *
     * @param path the path to the image file
     * @throws IOException if an I/O error occurs
     */
    public AsciiArtAlgorithm(String path) throws IOException {
        this.charset = DEFAULT_CHARSET;
        this.resolution = DEFAULT_IMG_RESOLUTION;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
        this.roundMethod = RoundMethod.ABS;
        this.asciiOutput = new ConsoleAsciiOutput();
        this.image = new Image(path);
        setMaxResolution(image.getWidth());
        setMinResolution(Math.max(1, image.getWidth() / image.getHeight()));
    }

    /**
     * Runs the ASCII art algorithm and returns the resulting ASCII art as a 2D char array.
     *
     * @return a 2D char array representing the ASCII art
     */
    public char[][] run() {
        // create padded image
        PaddedImage paddedImage = new PaddedImage(image);
        // convert image to sub images
        Image[][] subImages = paddedImage.getSubImages(resolution);
        // convert sub images to characters
        char[][] asciiArt = new char[subImages.length][subImages[0].length];
        this.subImgCharMatcher.setRoundMethod(this.roundMethod);
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Image currentImage = subImages[i][j];
                double brightness = currentImage.getImageBrightness();
                asciiArt[i][j] = this.subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return asciiArt;
    }

    /**
     * Returns the current character set used in the algorithm.
     *
     * @return the current character set
     */
    public char[] getCharset() {
        return this.charset;
    }

    /**
     * Adds a character to the character set.
     *
     * @param character the character to add
     */
    public void addChar(char character) {
        this.subImgCharMatcher.addChar(character);
        this.charset = this.subImgCharMatcher.getChars();
    }

    /**
     * Removes a character from the character set.
     *
     * @param character the character to remove
     */
    public void removeChar(char character) {
        this.subImgCharMatcher.removeChar(character);
        this.charset = this.subImgCharMatcher.getChars();
    }

    /**
     * Returns the current resolution of the algorithm.
     *
     * @return the current resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution of the algorithm.
     *
     * @param resolution the new resolution
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * Sets the minimum resolution of the algorithm.
     *
     * @param minResolution the new minimum resolution
     */
    public void setMinResolution(int minResolution) {
        this.minResolution = minResolution;
    }

    /**
     * Sets the maximum resolution of the algorithm.
     *
     * @param maxResolution the new maximum resolution
     */
    public void setMaxResolution(int maxResolution) {
        this.maxResolution = maxResolution;
    }

    /**
     * Returns the minimum resolution of the algorithm.
     *
     * @return the minimum resolution
     */
    public int getMinResolution() {
        return minResolution;
    }

    /**
     * Returns the maximum resolution of the algorithm.
     *
     * @return the maximum resolution
     */
    public int getMaxResolution() {
        return maxResolution;
    }

    /**
     * Sets the rounding method used in the algorithm.
     *
     * @param newRoundMethod the new rounding method
     */
    public void setRoundMethod(RoundMethod newRoundMethod) {
        this.roundMethod = newRoundMethod;
    }

    /**
     * Returns the current ASCII output method.
     *
     * @return the current ASCII output method
     */
    public AsciiOutput getAsciiOutput() {
        return asciiOutput;
    }

    /**
     * Sets the ASCII output method.
     *
     * @param asciiOutput the new ASCII output method
     */
    public void setAsciiOutput(AsciiOutput asciiOutput) {
        this.asciiOutput = asciiOutput;
    }
}
