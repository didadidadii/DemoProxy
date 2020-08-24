package com.xulihao.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author： xulihao
 * @Description: 代替JDK的ClassLoader，重写findClass(String name)方法
 */

public class DemoClassLoder extends ClassLoader {
    private File classPathFile;
    public DemoClassLoder(){
        String path = DemoClassLoder.class.getResource("").getPath();
        this.classPathFile=new File(path);
    }
    /**
     * 通过类名称加载字节码到jvm中
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //获取类名
        String className = DemoClassLoder.class.getPackage().getName() + "." + name;
        if (null != classPathFile) {
            File classFile = new File(classPathFile, name + ".class");
            if (classFile.exists()) {
                FileInputStream in = null;
                ByteArrayOutputStream out = null;
                // 将类文件转换为字节数组
                try {
                    in = new FileInputStream(classFile);
                    out = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while((len = in.read(bytes)) != -1) {
                        out.write(bytes, 0, len);
                    }
                    // 调用父类方法生成class实例
                    return defineClass(className, out.toByteArray(), 0, out.size());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != in) {
                            in.close();
                        }
                        if (null != out) {
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
