package com.xulihao.demo;



/**
 * @author： xulihao
 * @Description:
 */

public class Test {
    public static void main(String[] args) {
        Animal translationInstance = (Animal) DemoTranslation.getInstance(new Dog());
        translationInstance.sleep();
    }
}
