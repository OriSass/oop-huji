package pepse.util;

import java.util.Random;

/**
 * A utility class for performing statistical operations.
 */
public class Statistics {

    Random random;

    /**
     * Constructs a Statistics object with a specified seed for random number generation.
     *
     * @param seed the seed for the random number generator
     */
    public Statistics(int seed){
        this.random = new Random(seed);
    }
    /**
     * Simulates a coin flip with a given chance of success.
     *
     * @param chance the probability of the coin flip being successful (between 0 and 1)
     * @return true if the coin flip is successful, false otherwise
     */
    public boolean flipCoin(float chance){
        return random.nextFloat() > 1f - chance;
    }
}
