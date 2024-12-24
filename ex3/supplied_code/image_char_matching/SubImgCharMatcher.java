package image_char_matching;

import ascii_art.RoundMethod;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The SubImgCharMatcher class matches sub-images to characters based on their brightness.
 * It maintains a mapping of brightness values to characters and provides methods to add, remove,
 * and retrieve characters based on image brightness.
 */
public class SubImgCharMatcher {

    /**
     * The resolution of the character images in pixels.
     */
    private static final int CHAR_RESOLUTION = 16;
    private RoundMethod roundMethod;
    /**
     * A TreeMap that maps brightness values to sets of characters.
     */
    private TreeMap<Double, TreeSet<Character>> brightnessToCharTree;
    /**
     * A TreeMap that maps normalized brightness values to sets of characters.
     */
    private TreeMap<Double, TreeSet<Character>> normalizedBrightnessToCharTree;
    /**
     * The minimum brightness value in the brightness map.
     */
    private double minBrightness;
    /**
     * The maximum brightness value in the brightness map.
     */
    private double maxBrightness;

    /**
     * Constructs a SubImgCharMatcher instance with the specified character set.
     *
     * @param charset the character set to use for matching
     */
    public SubImgCharMatcher(char[] charset){
        initBrightnessMap(charset);
        this.roundMethod = RoundMethod.ABS;
    }

    /**
     * Initializes the brightness map with the specified character set.
     *
     * @param charset the character set to use for initializing the brightness map
     */
    private void initBrightnessMap(char[] charset) {
        brightnessToCharTree = new TreeMap<>();
        for (char character : charset) {
            Double brightness = calculateBrightnessByChar(character);
            assert brightnessToCharTree != null;
            if(brightnessToCharTree.containsKey(brightness)){
                brightnessToCharTree.get(brightness).add(character);
            }
            else{
                TreeSet<Character> charSet = new TreeSet<>();
                charSet.add(character);
                brightnessToCharTree.put(brightness, charSet);
            }
        }
        initNormalizedBrightnessMap();
    }

    /**
     * Calculates the brightness of the specified character.
     *
     * @param character the character to calculate the brightness for
     * @return the brightness of the character
     */
    private Double calculateBrightnessByChar(char character) {
        boolean[][] charBooleanArr = CharConverter.convertToBoolArray(character);
        int whiteCells = countWhiteCells(charBooleanArr);
        return (double)whiteCells / (double) (CHAR_RESOLUTION * CHAR_RESOLUTION);
    }

    /**
     * Initializes the normalized brightness map.
     */
    private void initNormalizedBrightnessMap() {
        normalizedBrightnessToCharTree = new TreeMap<>();
        this.minBrightness = calculateMinBrightness();
        this.maxBrightness = calculateMaxBrightness();
        for (Double brightness : brightnessToCharTree.keySet()) {
            double normalizedBrightness = (brightness - this.minBrightness) /
                    (this.maxBrightness - this.minBrightness);
            normalizedBrightnessToCharTree.put(normalizedBrightness, brightnessToCharTree.get(brightness));
        }
    }

    /**
     * Calculates the normalized brightness of the specified brightness value.
     *
     * @param brightness the brightness value to normalize
     * @return the normalized brightness value
     */
    private double calculateNormalizedBrightness(Double brightness){
        this.minBrightness = calculateMinBrightness();
        this.maxBrightness = calculateMaxBrightness();

        return (brightness - this.minBrightness) /
                (this.maxBrightness - this.minBrightness);
    }

    /**
     * Calculates the maximum brightness value in the brightness map.
     *
     * @return the maximum brightness value
     */
    private double calculateMaxBrightness() {
        double maxBrightness = 0;
        for (Double brightness : brightnessToCharTree.keySet()) {
            if(brightness > maxBrightness){
                maxBrightness = brightness;
            }
        }
        return maxBrightness;
    }

    /**
     * Calculates the minimum brightness value in the brightness map.
     *
     * @return the minimum brightness value
     */
    private double calculateMinBrightness() {
        double minBrightness = 1;
        for (Double brightness : brightnessToCharTree.keySet()) {
            if(brightness < minBrightness){
                minBrightness = brightness;
            }
        }
        return minBrightness;
    }

    /**
     * Counts the number of white cells in the specified boolean array.
     *
     * @param charBooleanArr the boolean array representing the character image
     * @return the number of white cells in the array
     */
    private int countWhiteCells(boolean[][] charBooleanArr) {
        int whiteCellsCount = 0;
        for (int i = 0; i < charBooleanArr.length; i++) {
            for (int j = 0; j < charBooleanArr[i].length; j++) {
                if(charBooleanArr[i][j]){
                    whiteCellsCount++;
                }
            }
        }
        return whiteCellsCount;
    }

    /**
     * Gets the character that best matches the specified image brightness using the specified
     * rounding method.
     *
     * @param brightness the brightness of the image
     * @return the character that best matches the image brightness
     */
    public char getCharByImageBrightness(double brightness){

        if(normalizedBrightnessToCharTree.containsKey(brightness)){
            return normalizedBrightnessToCharTree.get(brightness).first();
        }
        else{
            return getClosesBrightnessChar(brightness);
        }
    }

    /**
     * Gets the character that is closest to the specified brightness value using the specified
     * rounding method.
     *
     * @param brightness the brightness value to match
     * @return the character that is closest to the brightness value
     */
    private char getClosesBrightnessChar(double brightness) {
        Double lowerBrightness = normalizedBrightnessToCharTree.lowerKey(brightness);
        Double higherBrightness = normalizedBrightnessToCharTree.higherKey(brightness);
        if(lowerBrightness == null){
            return normalizedBrightnessToCharTree.get(higherBrightness).first();
        }
        if(higherBrightness == null){
            return normalizedBrightnessToCharTree.get(lowerBrightness).first();
        }
        if(roundMethod == RoundMethod.MIN){
            return normalizedBrightnessToCharTree.get(lowerBrightness).first();
        }
        else if(roundMethod == RoundMethod.MAX){
            return normalizedBrightnessToCharTree.get(higherBrightness).first();
        }
        else{
            // ABS

            // if closer to upper value
            if(brightness - lowerBrightness < higherBrightness - brightness){
                return normalizedBrightnessToCharTree.get(lowerBrightness).first();
            }
            // else closer to lower value
            else{
                return normalizedBrightnessToCharTree.get(higherBrightness).first();
            }
        }
    }

    /**
     * Adds a character to the brightness map.
     *
     * @param c the character to add
     */
    public void addChar(char c){
        double newBrightness = calculateBrightnessByChar(c);
        boolean barriersChanged = false;
        // add to tree
        addCharToBrightnessTree(c, newBrightness);

        // if boundaries changed - recalculate normalized tree
        if(updateBounds(newBrightness)){
            initNormalizedBrightnessMap();
        }
        // else just add char to normalized tree
        double normalizedBrightness = calculateNormalizedBrightness(newBrightness);
        normalizedBrightnessToCharTree.put(normalizedBrightness, brightnessToCharTree.get(newBrightness));
    }

    /**
     * Adds a character to the brightness tree.
     *
     * @param c the character to add
     * @param newBrightness the brightness of the character
     */
    private void addCharToBrightnessTree(char c, double newBrightness) {
        if(brightnessToCharTree.containsKey(newBrightness)){
            brightnessToCharTree.get(newBrightness).add(c);
        }
        else{
            TreeSet<Character> charSet = new TreeSet<>();
            charSet.add(c);
            brightnessToCharTree.put(newBrightness, charSet);
        }
    }

    /**
     * Updates the brightness bounds if the new brightness value is outside the current bounds.
     *
     * @param newBrightness the new brightness value
     * @return true if the bounds were updated, false otherwise
     */
    private boolean updateBounds(double newBrightness) {
        if(newBrightness < this.minBrightness){
            this.minBrightness = newBrightness;
            return true;
        }
        if(newBrightness > this.maxBrightness){
            this.maxBrightness = newBrightness;
            return true;
        }
        return false;
    }

    /**
     * Removes a character from the brightness map.
     *
     * @param c the character to remove
     */
    public void removeChar(char c){
        if (brightnessToCharTree.isEmpty()){
            return;
        }
        Double brightness = calculateBrightnessByChar(c);
        if(brightnessToCharTree.containsKey(brightness)){
            brightnessToCharTree.get(brightness).remove(c);
            if(brightnessToCharTree.get(brightness).isEmpty()){
                brightnessToCharTree.remove(brightness);
            }
            if (brightness == this.minBrightness || brightness == this.maxBrightness){
                initNormalizedBrightnessMap();
            }
            else{
                double normalBrightness = calculateNormalizedBrightness(brightness);
                if(normalizedBrightnessToCharTree.containsKey(normalBrightness)){
                    normalizedBrightnessToCharTree.get(normalBrightness).remove(c);
                    if(normalizedBrightnessToCharTree.get(normalBrightness).isEmpty()){
                        normalizedBrightnessToCharTree.remove(normalBrightness);
                    }
                }
            }
        }
    }

    /**
     * Gets the characters in the brightness map.
     *
     * @return an array of characters in the brightness map
     */
    public char[] getChars() {
        return normalizedBrightnessToCharTree.values().stream()
                .flatMap(TreeSet::stream)
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining())
                .toCharArray();
    }

    /**
     * Sets the rounding method used for matching brightness values to characters.
     *
     * @param roundMethod the new rounding method to use
     */
    public void setRoundMethod(RoundMethod roundMethod) {
        this.roundMethod = roundMethod;
    }
}
