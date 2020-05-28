package image.utils;

import image.controller.Controller;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtil {
    public static String creatFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        boolean mkdirResult = file.mkdirs();
        if (!mkdirResult) {
            return null;
        }
        return file.getPath();
    }

    public static String getFileSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (StringUtils.isEmpty(suffix)) {
            return null;
        }
        return suffix;
    }

    public static void getFileNum(String path) {
        File file = new File(path);
        File[] rootFiles = file.listFiles();
        if (rootFiles != null) {

            for (File currentFile : rootFiles) {
                if (currentFile.isDirectory()) {
                    getFileNum(currentFile.getPath());
                } else if (currentFile.isFile()) {
                    Controller.fileNums++;
                }
            }
        }
    }
}