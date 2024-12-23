package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class AsciiArtAlgorithm {

    private final SubImgCharMatcher subImgCharMatcher;
    char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    int DEFAULT_IMG_RESOLUTION = 2;

    private final String path;
    private char[] charset;
    private int resolution;
    private int minResolution;
    private int maxResolution;

    private RoundMethod roundMethod;
    private AsciiOutput asciiOutput;

    private Image image;

    public AsciiArtAlgorithm(String path) throws IOException {
        this.path = path;
        this.charset = DEFAULT_CHARSET;
        this.resolution = DEFAULT_IMG_RESOLUTION;
        this.subImgCharMatcher = new SubImgCharMatcher(charset);
        this.roundMethod = RoundMethod.ABS;
        this.asciiOutput = new ConsoleAsciiOutput();
        this.image = new Image(path);
        setMaxResolution(image.getWidth());
        setMinResolution(Math.max(1, image.getWidth() / image.getHeight()));
    }

    public char[][] run() {
        // create padded image
        PaddedImage paddedImage = new PaddedImage(image);
        // convert image to sub images
        Image[][] subImages = paddedImage.getSubImages(resolution);
        // convert sub images to characters
        char[][] asciiArt = new char[subImages.length][subImages[0].length];

        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Image currentImage = subImages[i][j];
                double brightness = currentImage.getImageBrightness();
                asciiArt[i][j] = this.subImgCharMatcher.getCharByImageBrightness(brightness, roundMethod);
            }
        }
        return asciiArt;
    }

    public char[] getCharset() {
        return this.charset;
    }

    public void addChar(char character) {
        this.subImgCharMatcher.addChar(character);
        this.charset = this.subImgCharMatcher.getChars();
    }

    public void removeChar(char character) {
        this.subImgCharMatcher.removeChar(character);
        this.charset = this.subImgCharMatcher.getChars();
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public void setMinResolution(int minResolution) {
        this.minResolution = minResolution;
    }

    public void setMaxResolution(int maxResolution) {
        this.maxResolution = maxResolution;
    }

    public int getMinResolution() {
        return minResolution;
    }

    public int getMaxResolution() {
        return maxResolution;
    }

    public void setRoundMethod(RoundMethod newRoundMethod) {
        this.roundMethod = newRoundMethod;
    }

    public AsciiOutput getAsciiOutput() {
        return asciiOutput;
    }

    public void setAsciiOutput(AsciiOutput asciiOutput) {
        this.asciiOutput = asciiOutput;
    }
}
