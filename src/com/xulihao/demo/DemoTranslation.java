package com.xulihao.demo;

import java.lang.reflect.Method;

/**
 * @author： xulihao
 * @Description: 自定义InvocationHandler
 */

public class DemoTranslation implements DemoInvocationHandler {
    private Animal targetanimal;
    public  DemoTranslation(Animal animal){
        this.targetanimal=animal;
    }
    public static Object getInstance(Animal target) {
        try {
            Class<? extends Animal> clazz = target.getClass();
            System.out.println(clazz.getName());
            //生成自定义的代理
            Object poxy = DemoProxy.newProxyInstance(new DemoClassLoder(), clazz.getInterfaces(), new DemoTranslation(target));
            System.out.println(poxy.getClass().getName());
            return poxy;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("动物代理。。。");
        Object invoke = method.invoke(targetanimal,args);
        System.out.println("如果是二哈就不能睡觉。。");
        return invoke;
    }
}
