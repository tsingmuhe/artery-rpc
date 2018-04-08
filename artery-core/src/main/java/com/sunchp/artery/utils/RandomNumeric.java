package com.sunchp.artery.utils;

import java.util.Random;

public class RandomNumeric {
    protected final static RandomNumeric INSTANCE = new RandomNumeric();

    public static RandomNumeric getInstance() {
        return INSTANCE;
    }

    protected final Random rnd;

    public RandomNumeric() {
        this(new Random());
    }

    public RandomNumeric(Random rnd) {
        this.rnd = rnd;
    }

    public int random(int start, int end) {
        int len = end - start + 1;
        return rnd.nextInt(len) + start;
    }

    public double random() {
        return rnd.nextDouble();
    }
}