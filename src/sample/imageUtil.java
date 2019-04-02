package sample;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javafx.scene.control.TextArea;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import static sample.Controller.originalPath;
import static sample.Controller.targetPath;
import static sample.FileUtil.creatFolder;
import static sample.FileUtil.getFileSuffix;

public class imageUtil {


    // private static MessagePrinter messagePrinter = new MessagePrinter();

    public static void printMessage(TextArea textArea, String message) {
        MessagePrinter messagePrinter = new MessagePrinter();
        messagePrinter.setMessage(message);
        messagePrinter.setTextArea(textArea);
        Thread thread=new Thread(messagePrinter);
        thread.start();
    }

    /**
     * 递归遍历文件夹内容
     *
     * @param textArea
     * @param path
     */
    public static void readFolder(TextArea textArea, String path) {

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
                String fileType = getFileSuffix(fileName).toLowerCase();
                //图片文件,调用修改方法

                if (Constant.imageType.contains(fileType)) {
                    String outputPath = currentFile.getPath().replace(originalPath, targetPath);
                    handleImages(currentFile, textArea, outputPath);

                } else {
                    //非图片文件,跳过
//                    printMessage(textArea, "非图片文件,跳过\n");
                     textArea.appendText("非图片文件,跳过\n");
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
//        printMessage(textArea, "开始处理图片:" + file.getPath() + file.getName() + "~~~~~\n");
        textArea.appendText("开始处理图片:" + file.getPath() + file.getName() + "~~~~~\n");
        int[] sizeInfo = null;
        try {
            sizeInfo = getSizeInfo(file);
        } catch (Exception e) {
//            printMessage(textArea, "获取图片尺寸信息失败!\n");
            textArea.appendText("获取图片尺寸信息失败!\n");
            return;
        }

        int width = sizeInfo[0];
        int height = sizeInfo[1];
        //如果图片本来是高宽相等的,直接修改尺寸,不会失真
        if (width == height) {
            try {
                reSizeImage(file, targetPath, 800, 800);
                textArea.appendText("处理完毕!\n");
//                printMessage(textArea, "处理完毕!\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //非正方形,需要先裁剪成正方形,再调整大小
        } else {
//            printMessage(textArea, "需要裁剪!\n");
            textArea.appendText("需要裁剪!\n");


            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            int shorterSide = width > height ? height : width;
            int x = (width - shorterSide) / 2;
            int y = (height - shorterSide) / 2;
            cropImage(file.getPath(), targetPath, x, y, shorterSide, shorterSide, suffix, suffix);
            try {
                File tempFile=new File(targetPath);
                reSizeImage(tempFile, targetPath, 800, 800);
//                printMessage(textArea, "裁剪完毕!\n");
                textArea.appendText("裁剪完毕!\n");
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    /**
     * 重置图片大小
     *
     * @param image       文件对象
     * @param toImagePath 写入图片路径
     * @param width       重新设置图片的宽
     * @param height      重新设置图片的高
     * @throws IOException
     */
    public static void reSizeImage(File image, String toImagePath, int width, int height) throws IOException {
        FileOutputStream out = null;
        try {
            // 构造Image对象
            BufferedImage src = javax.imageio.ImageIO.read(image);
            // 缩小边长
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 绘制 缩小  后的图片
            tag.getGraphics().drawImage(src, 0, 0, width, height, null);
            out = new FileOutputStream(toImagePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 对图片裁剪，并把裁剪新图片保存
     *
     * @param srcPath          读取源图片路径
     * @param toPath           写入图片路径
     * @param x                剪切起始点x坐标
     * @param y                剪切起始点y坐标
     * @param width            剪切宽度
     * @param height           剪切高度
     * @param readImageFormat  读取图片格式
     * @param writeImageFormat 写入图片格式
     */
    public static void cropImage(String srcPath, String toPath, int x, int y, int width, int height, String readImageFormat, String writeImageFormat) {
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {
            //读取图片文件
            fis = new FileInputStream(srcPath);
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(readImageFormat);
            ImageReader reader = readers.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(fis);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            //定义一个矩形
            Rectangle rect = new Rectangle(x, y, width, height);
            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            //保存新图片
            ImageIO.write(bi, writeImageFormat, new File(toPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (iis != null) {
                    iis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取图片尺寸信息
     *
     * @param file a {@link java.io.File} object.
     * @return [width, height]
     */
    public static int[] getSizeInfo(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("file " + file.getAbsolutePath() + " doesn't exist.");
        }
        BufferedInputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            return getSizeInfo(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            input.close();
        }
    }

    /**
     * 获取图片尺寸
     *
     * @param input a {@link java.io.InputStream} object.
     * @return [width, height]
     */
    public static int[] getSizeInfo(InputStream input) throws Exception {
        try {
            BufferedImage img = ImageIO.read(input);
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            return new int[]{w, h};
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


}
