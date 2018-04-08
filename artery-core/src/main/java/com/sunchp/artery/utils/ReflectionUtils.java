package com.sunchp.artery.utils;

import java.lang.reflect.UndeclaredThrowableException;

public class ReflectionUtils {
    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else if (ex instanceof Error) {
            throw (Error) ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }
}
