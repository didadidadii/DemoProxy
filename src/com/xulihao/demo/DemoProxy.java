package com.xulihao.demo;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author： xulihao
 * @Description:
 */

public class DemoProxy {
    //回车换行
    private static final String LN = "\r\n";

    /**
     * 自定义代理类 返回给调用者
     * @param loader
     * @param interfaces
     * @param h
     * @return
     * @throws Exception
     */
    public static Object newProxyInstance(DemoClassLoder loader, Class<?>[] interfaces, DemoInvocationHandler h) throws Exception {
        // 动态生成源代码
        String srcClass = generateSrc(interfaces);
        // 输出Java文件
        String filePath = DemoProxy.class.getResource("").getPath()  + "$ProxyO.java";
        System.out.println(filePath);
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(srcClass);
        fileWriter.flush();
        fileWriter.close();
        // 编译Java文件为class文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable iterable = fileManager.getJavaFileObjects(filePath);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, iterable);
        task.call();
        fileManager.close();
        // 加载编译生成的class文件到JVM
        Class<?> proxyClass = loader.findClass("$ProxyO");
        Constructor<?> constructor = proxyClass.getConstructor(DemoInvocationHandler.class);
        // 删掉虚拟代理类
        File file = new File(filePath);
        file.delete();
        // 返回字节码重组以后的代理对象
        return constructor.newInstance(h);
    }
    //生成java文件

    /**
     *参考：https://github.com/mingchangkun/hero
     * @param interfaces
     * @return
     */
    private static String generateSrc(Class<?>[] interfaces) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package com.xulihao.demo;" + LN );
        //stringBuilder.append("import package com.xulihao.demo.Animal;" + LN);
        stringBuilder.append("import java.lang.reflect.Method;" + LN);
        stringBuilder.append("public class $ProxyO implements " + interfaces[0].getName() + "{" + LN);
        stringBuilder.append("DemoInvocationHandler h;" + LN);
        stringBuilder.append("public $ProxyO(DemoInvocationHandler h) {" + LN);
        stringBuilder.append("this.h = h;" + LN);
        stringBuilder.append("}" + LN);
        //填充方法
        for (Method method : interfaces[0].getMethods()) {
            stringBuilder.append("public " + method.getReturnType().getName() + " " + method.getName() + "() {" + LN);
            stringBuilder.append("try {" + LN);
            stringBuilder.append("Method m = " + interfaces[0].getName() + ".class.getMethod(\"" + method.getName() + "\", new Class[]{});" + LN);
            stringBuilder.append("this.h.invoke(this, m, null);" + LN);
            stringBuilder.append("} catch(Throwable able) {" + LN);
            stringBuilder.append("able.getMessage();" + LN);
            stringBuilder.append("}" + LN);
            stringBuilder.append("}" + LN );
        }
        stringBuilder.append("}" + LN);
        return stringBuilder.toString();
    }

}
