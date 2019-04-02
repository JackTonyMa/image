package sample;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtil {

    /**
     * 创建用来存储处理完成的图片的文件夹
     *
     * @param path
     * @return
     */
    public static String creatFolder(String path) {

        File file = new File(path);


        if (file.exists()) {
            return path;
        } else {
            boolean mkdirResult = file.mkdirs();

            if (!mkdirResult) {
                return null;
            } else {
                return file.getPath();
            }
        }

    }

    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName) {
        String suffix;
        if (fileName == null) {
            return null;
        } else {
            suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        }
        if (StringUtils.isEmpty(suffix)) {
            return null;
        } else {

            return suffix;
        }
    }
}
