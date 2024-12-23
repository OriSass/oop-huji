package image_char_matching;

import ascii_art.RoundMethod;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SubImgCharMatcher {

    private static final int CHAR_RESOLUTION = 16;
    private TreeMap<Double, TreeSet<Character>> brightnessToCharTree;
    private TreeMap<Double, TreeSet<Character>> normalizedBrightnessToCharTree;
    private List<Character> characters;
    private double minBrightness;
    private double maxBrightness;

    public SubImgCharMatcher(char[] charset){
        initBrightnessMap(charset);
    }

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

    private Double calculateBrightnessByChar(char character) {
        boolean[][] charBooleanArr = CharConverter.convertToBoolArray(character);
        int whiteCells = countWhiteCells(charBooleanArr);
        return (double)whiteCells / (double) (CHAR_RESOLUTION * CHAR_RESOLUTION);
    }

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

    private double calculateNormalizedBrightness(Double brightness){
        this.minBrightness = calculateMinBrightness();
        this.maxBrightness = calculateMaxBrightness();

        return (brightness - this.minBrightness) /
                (this.maxBrightness - this.minBrightness);
    }

    private double calculateMaxBrightness() {
        double maxBrightness = 0;
        for (Double brightness : brightnessToCharTree.keySet()) {
            if(brightness > maxBrightness){
                maxBrightness = brightness;
            }
        }
        return maxBrightness;
    }

    private double calculateMinBrightness() {
        double minBrightness = 1;
        for (Double brightness : brightnessToCharTree.keySet()) {
            if(brightness < minBrightness){
                minBrightness = brightness;
            }
        }
        return minBrightness;
    }

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

    public char getCharByImageBrightness(double brightness, RoundMethod roundMethod){

        if(normalizedBrightnessToCharTree.containsKey(brightness)){
            return normalizedBrightnessToCharTree.get(brightness).first();
        }
        else{
            return getClosesBrightnessChar(brightness, roundMethod);
        }
    }

    private char getClosesBrightnessChar(double brightness, RoundMethod roundMethod) {
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

    public char[] getChars() {
        return normalizedBrightnessToCharTree.values().stream()
                .flatMap(TreeSet::stream)
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining())
                .toCharArray();
    }
}
