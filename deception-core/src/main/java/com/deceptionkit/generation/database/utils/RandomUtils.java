package com.deceptionkit.generation.database.utils;

import java.util.Random;

public class RandomUtils {

    public static Random randomizer = new Random();

    /**
     * This method generates a random number within the specified range.
     *
     * @param min The lower bound of the range (inclusive).
     * @param max The upper bound of the range (inclusive).
     * @return A random integer within the specified range.
     */
    public synchronized static int generateRandomNumber(int min, int max) {
        return randomizer.nextInt(min, max + 1);
    }
}
