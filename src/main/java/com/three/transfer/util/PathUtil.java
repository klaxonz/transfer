package com.three.transfer.util;


import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    private static String winPath = PropertiesUtil.getProperty("base.path.win");
    private static String linuxPath = PropertiesUtil.getProperty("base.path.linux");
    private static String filePath = PropertiesUtil.getProperty("relative.file.path");

    public static String getSeparator() {
        return separator;
    }

    /**
     * 获取文件存储的根路径
     * @return
     */
    public static String getFileBasePath() {
        String os = System.getProperty("os.name");
        String basePath="";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getFilePath(int userId) {
        String userFilePath = filePath + userId + separator;
        return userFilePath.replace("/", separator);
    }


    public static String getTempShardFilePath(int userId, String tempDirName) {
        String tempShardFilePath = getFilePath(userId) + tempDirName + separator;
        return tempShardFilePath.replace("/", separator);

    }

}
