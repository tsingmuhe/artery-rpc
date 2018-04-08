package com.sunchp.artery.exporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class DefaultExporter<T> extends AbstractExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExporter.class);

    public DefaultExporter(Class serviceInterface, Object service) {
        super(serviceInterface, service);
    }

    @Override
    public InvocationResult invokeAndCreateResult(Invocation invocation) {
        try {
            Object value = invoke(invocation);
            return new InvocationResult(value);
        } catch (Throwable ex) {
            return new InvocationResult(ex);
        }
    }

    protected Object invoke(Invocation invocation) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Executing " + invocation);
        }
        try {
            return invocation.invoke(getService());
        } catch (NoSuchMethodException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("Could not find target method for " + invocation, ex);
            }
            throw ex;
        } catch (IllegalAccessException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("Could not access target method for " + invocation, ex);
            }
            throw ex;
        } catch (InvocationTargetException ex) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Target method failed for " + invocation, ex.getTargetException());
            }
            throw ex;
        }
    }
}
