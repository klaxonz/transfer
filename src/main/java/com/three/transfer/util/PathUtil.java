package com.three.transfer.util;


import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    private static String winPath = "G:/projectdev/file";
    private static String linuxPath = "/home/work/transfer/file";
    private static String filePath = "/upload/user";
    private static String shardFilePath = "temp";


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


    public static String getTempShardFilePath(int userId) {
        String tempShardFilePath = getFilePath(userId) + shardFilePath + separator;
        return tempShardFilePath.replace("/", separator);

    }



}
