package image.service;

import image.constants.Constant;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;

import static image.controller.Controller.originalPath;
import static image.controller.Controller.targetPath;
import static image.utils.FileUtil.creatFolder;
import static image.utils.FileUtil.getFileSuffix;
import static image.utils.imageUtil.*;

public class ImageService implements Runnable {

    private TextArea textArea;
    private String path;

    public ImageService(TextArea textArea, String path) {
        this.textArea = textArea;
        this.path = path;
    }

    /**
     * 递归遍历文件夹内容
     *
     * @param textArea
     * @param path
     */
    public void readFolder(TextArea textArea, String path) {

        File file = new File(path);
        File[] rootFiles = file.listFiles();

        for (File currentFile : rootFiles) {
            //当前是文件夹,递归调用
            if (currentFile.isDirectory()) {
                //获取到文件夹
                //创建目标子文件夹
                String subPath = currentFile.getPath().replace(originalPath, targetPath);

                creatFolder(subPath);

                readFolder(textArea, currentFile.getPath());
                //当前是文件
            } else if (currentFile.isFile()) {

                String fileName = currentFile.getName();
                //获取文件后缀
                String fileType = getFileSuffix(fileName).toLowerCase();
                //图片文件,调用修改方法

                if (Constant.imageType.contains(fileType)) {
                    String outputPath = currentFile.getPath().replace(originalPath, targetPath);
                    handleImages(currentFile, textArea, outputPath);

                } else {
                    //非图片文件,跳过
                    printMessage(textArea,"非图片文件,跳过\n");

                  //  textArea.setText("非图片文件,跳过\n");
                }
            }
        }

    }

    /**
     * 控制图片处理逻辑
     *
     * @param file
     * @param textArea
     * @param targetPath
     */
    public static void handleImages(File file, TextArea textArea, String targetPath) {
        printMessage(textArea, "开始处理图片:" + file.getPath() + file.getName() + "~~~~~\n");

        //textArea.setText("开始处理图片:" + file.getPath() + file.getName() + "~~~~~\n");
        int[] sizeInfo = null;
        try {
            sizeInfo = getSizeInfo(file);
        } catch (Exception e) {
            printMessage(textArea, "获取图片尺寸信息失败!\n");

            // textArea.setText("获取图片尺寸信息失败!\n");
            return;
        }

        int width = sizeInfo[0];
        int height = sizeInfo[1];
        //如果图片本来是高宽相等的,直接修改尺寸,不会失真
        if (width == height) {
            try {
                reSizeImage(file, targetPath, 800, 800);
                printMessage(textArea, "处理完毕!\n");

                //textArea.setText("处理完毕!\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //非正方形,需要先裁剪成正方形,再调整大小
        } else {
            printMessage(textArea, "需要裁剪!\n");

            // textArea.setText("需要裁剪!\n");


            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            int shorterSide = width > height ? height : width;
            int x = (width - shorterSide) / 2;
            int y = (height - shorterSide) / 2;
            cropImage(file.getPath(), targetPath, x, y, shorterSide, shorterSide, suffix, suffix);
            try {
                File tempFile = new File(targetPath);
                reSizeImage(tempFile, targetPath, 800, 800);
                printMessage(textArea, "裁剪完毕!\n");
                //textArea.setText("裁剪完毕!\n");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    public static void printMessage(TextArea textArea, String message) {
        Platform.runLater(() ->
                textArea.appendText(message));
    }


    @Override
    public void run() {
        readFolder(textArea, path);
        textArea.appendText("\n\n\n\n全部修改完成,存储路径: " + targetPath + "\n\n\n");
        try {
            Runtime.getRuntime().exec("explorer " + targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
