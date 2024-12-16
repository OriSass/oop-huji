package image_char_matching;

import java.util.TreeMap;
import java.util.TreeSet;

public class SubImgCharMatcher {

    private static final int DEFAULT_RESOLUTION = 16;
    private TreeMap<Double, TreeSet<Character>> brightnessToCharTree;
    private TreeMap<Double, TreeSet<Character>> normalizedBrightnessToCharTree;

    public SubImgCharMatcher(char[] charset){
        initBrightnessMap(charset);
    }

    private void initBrightnessMap(char[] charset) {
        brightnessToCharTree = new TreeMap<>();
        for (char character : charset) {
            boolean[][] charBooleanArr = CharConverter.convertToBoolArray(character);
            int whiteCells = countWhiteCells(charBooleanArr);
            Double brightness = (double)whiteCells / (double)DEFAULT_RESOLUTION;
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

    private void initNormalizedBrightnessMap() {
        normalizedBrightnessToCharTree = new TreeMap<>();
        double minBrightness = getMinBrightness();
        double maxBrightness = getMaxBrightness();
        for (Double brightness : brightnessToCharTree.keySet()) {
            double normalizedBrightness = (brightness - minBrightness) / (maxBrightness - minBrightness);
            normalizedBrightnessToCharTree.put(normalizedBrightness, brightnessToCharTree.get(brightness));
        }
    }

    private double getMaxBrightness() {
        double maxBrightness = 0;
        for (Double brightness : brightnessToCharTree.keySet()) {
            if(brightness > maxBrightness){
                maxBrightness = brightness;
            }
        }
        return maxBrightness;
    }

    private double getMinBrightness() {
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

    public char getCharByImageBrightness(double brightness){
        return ' ';
    }

    public void addChar(char c){

    }

    public void removeChar(char c){

    }
}
