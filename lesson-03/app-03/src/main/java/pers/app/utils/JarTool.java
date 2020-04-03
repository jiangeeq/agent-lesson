package pers.app.utils;

import java.io.File;

/**
 * @author jiangpeng
 * @date 2019/12/3017:23
 */
public class JarTool {
    //获取jar绝对路径
    public static String getJarPath() {
        File file = getFile();
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    //获取jar目录
    public static String getJarDir() {
        File file = getFile();
        if (file == null) {
            return null;
        }
        return getFile().getParent();
    }
    //获取jar包名
    public static String getJarName(){
        final File parentFile = getFile().getParentFile();
        final File[] files = parentFile.listFiles();
        return files[0].getName();
    }

    private static File getFile() {
        //关键是这行...
        String path = JarTool.class.getProtectionDomain().getCodeSource()
                .getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");//转换处理中文及空格
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return new File(path);
    }
}
