package image;

/**
 * The ImageUtils class provides utility methods for image processing.
 */
public class ImageUtils {

    /**
     * Returns the smallest power of two that is greater than or equal to the given number.
     * @param number the number to find the next power of two for.
     * @return the smallest power of two that is greater than or equal to the given number.
     *
     * used to create the padded picture
     */
    public static int getNextPowerOfTwo(int number) {
        int power = 1;
        while (power < number) {
            power *= 2;
        }
        return power;
    }
}
