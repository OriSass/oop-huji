package ascii_art;

import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class AsciiArtAlgorithm {

    private final String path;
    private final char[] charset;
    private final int resolution;

    public AsciiArtAlgorithm(String path, char[] charset, int resolution) {
        this.path = path;
        this.charset = charset;
        this.resolution = resolution;
    }

    public char[][] run() {
        // create image
        Image image = null;
        try {
            image = new Image(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // create padded image
        PaddedImage paddedImage = new PaddedImage(image);
        // convert image to sub images
        Image[][] subImages = paddedImage.getSubImages(resolution);
        // convert sub images to characters
        char[][] asciiArt = new char[subImages.length][subImages[0].length];
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charset);

        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                Image currentImage = subImages[i][j];
                double brightness = currentImage.getImageBrightness();
                asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(brightness);
            }
        }
        return asciiArt;
    }
}
