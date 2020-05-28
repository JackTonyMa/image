package image.service;

import image.constants.ImageFileType;
import image.controller.Controller;
import image.utils.FileUtil;
import image.utils.ImageUtil;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;

/**
 * @author 66417
 */
public class ImageService implements Runnable {
    private final TextArea textArea;

    private final String path;

    public ImageService(TextArea textArea, String path) {
        this.textArea = textArea;
        this.path = path;
    }


    public void readFolder(TextArea textArea, String path) {
        File file = new File(path);
        File[] rootFiles = file.listFiles();
        for (File currentFile : rootFiles != null ? rootFiles : new File[0]) {
            if (currentFile.isDirectory()) {
                String subPath = currentFile.getPath().replace(Controller.originalPath, Controller.targetPath);
                FileUtil.creatFolder(subPath);
                readFolder(textArea, currentFile.getPath());
            } else if (currentFile.isFile()) {
                String fileName = currentFile.getName();
                String fileType = FileUtil.getFileSuffix(fileName).toLowerCase();
                if (ImageFileType.contains(fileType)) {
                    String outputPath = currentFile.getPath().replace(Controller.originalPath, Controller.targetPath);
                    handleImages(currentFile, textArea, outputPath);
                } else {
                    printMessage(textArea, "非图片文件,跳过\n");
                    Controller.rateOfProgress++;
                }
            }
        }
    }

    public void handleImages(File file, TextArea textArea, String targetPath) {
        printMessage(textArea,  "开始处理图片"+file.getPath() + file.getName() +"~~~~~\n");
        int[] sizeInfo;
        try {
            sizeInfo = ImageUtil.getSizeInfo(file);
        } catch (Exception e) {
            printMessage(textArea, "获取图片尺寸信息失败!\n");
            Controller.failCount++;
            Controller.rateOfProgress++;
            return;
        }
        int width = sizeInfo[0];
        int height = sizeInfo[1];
        if (width == height) {
            try {
                ImageUtil.reSizeImage(file, targetPath, 800, 800);
                printMessage(textArea, "处理完毕!\n");
                Controller.successCount++;
                Controller.rateOfProgress++;
            } catch (Exception e) {
                Controller.failCount++;
                Controller.rateOfProgress++;
                e.printStackTrace();
            }
        } else {
            printMessage(textArea, "需要裁剪\n");
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            int shorterSide = Math.min(width, height);
            int x = (width - shorterSide) / 2;
            int y = (height - shorterSide) / 2;
            ImageUtil.cropImage(file.getPath(), targetPath, x, y, shorterSide, shorterSide, suffix, suffix);
            try {
                File tempFile = new File(targetPath);
                ImageUtil.reSizeImage(tempFile, targetPath, 800, 800);
                printMessage(textArea, "裁剪完毕!\n");
                Controller.successCount++;
                Controller.rateOfProgress++;
            } catch (Exception e) {
                Controller.failCount++;
                Controller.rateOfProgress++;
                e.printStackTrace();
            }
        }
    }

    public void printMessage(TextArea textArea, String message) {
        Platform.runLater(() -> textArea.appendText(message));
    }

    @Override
    public void run() {
        readFolder(textArea, path);
        printMessage(textArea, "\n\n\n\n全部修改完成,成功修改"+ Controller.successCount + "张,失败"+ Controller.failCount + "张,存储路径" + Controller.targetPath + "\n\n\n");
        try {
            Runtime.getRuntime().exec("explorer " + Controller.targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}