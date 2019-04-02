package image.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtil {

    /**
     * 创建文件夹
     *
     * @param path
     * @return 如果文件已存在, 返回路径, 如果创建失败, 返回@{code=null},如果创建成功,返回路径
     */
    public static String creatFolder(String path) {

        File file = new File(path);

        //文件已存在,直接返回路径
        if (file.exists()) {
            return path;
        } else {
            boolean mkdirResult = file.mkdirs();
            //创建失败返回null
            if (!mkdirResult) {
                return null;
                //创建成功,返回新文件路径
            } else {
                return file.getPath();
            }
        }

    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName) {
        String suffix;
        if (fileName == null) {
            return null;
        } else {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        if (StringUtils.isEmpty(suffix)) {
            return null;
        } else {

            return suffix;
        }
    }
}
