package pepse.util;

import java.util.Random;

public class Statistics {

    Random random;

    public Statistics(int seed){
        this.random = new Random(seed);
    }
    public boolean flipCoin(float chance){
        return random.nextFloat() > 1f - chance;
    }
}
