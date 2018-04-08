package com.sunchp.artery.exporter;

import java.lang.reflect.InvocationTargetException;

public class InvocationResult {
    private Object value;

    private Throwable exception;

    public InvocationResult(Object value) {
        this.value = value;
    }

    public InvocationResult(Throwable exception) {
        this.exception = exception;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return this.exception;
    }


    public boolean hasException() {
        return (this.exception != null);
    }

    public boolean hasInvocationTargetException() {
        return (this.exception instanceof InvocationTargetException);
    }
}
