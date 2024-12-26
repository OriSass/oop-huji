package pepse.util;

import java.util.Random;

public class Statistics {

    static Random random = new Random();

    public static boolean flipCoin(float chance){
        return random.nextFloat() > 1f - chance;
    }
}
