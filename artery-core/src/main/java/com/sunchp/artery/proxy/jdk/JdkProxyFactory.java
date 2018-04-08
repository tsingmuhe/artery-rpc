package com.sunchp.artery.proxy.jdk;

import com.sunchp.artery.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, invocationHandler);
    }
}
