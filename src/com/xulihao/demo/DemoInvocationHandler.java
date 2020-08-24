package com.xulihao.demo;

import java.lang.reflect.Method;

/**
 * @author： xulihao
 * @Description:
 */

public interface DemoInvocationHandler {
    /**
     *
     * @param proxy 被代理对象
     * @param method 被代理方法
     * @param args  参数
     * @return
     * @throws Throwable
     */
     Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
