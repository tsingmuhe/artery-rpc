package com.sunchp.artery.utils;

public class ConstantThrowable extends Throwable {

    private String name;

    public ConstantThrowable() {
        this(null);
    }

    public ConstantThrowable(String name) {
        super(null, null, false, false);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
