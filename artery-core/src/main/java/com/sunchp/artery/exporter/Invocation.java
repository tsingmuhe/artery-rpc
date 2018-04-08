package com.sunchp.artery.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Invocation {
    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, Object> attributes;

    public Invocation() {
    }

    public Invocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public void addAttribute(String key, Object value) throws IllegalStateException {
        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>();
        }
        if (this.attributes.containsKey(key)) {
            throw new IllegalStateException("There is already an attribute with key '" + key + "' bound");
        }
        this.attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(key);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public Object invoke(Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = targetObject.getClass().getMethod(this.methodName, this.parameterTypes);
        return method.invoke(targetObject, this.arguments);
    }
}
