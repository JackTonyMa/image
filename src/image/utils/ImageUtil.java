package image.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * @author 66417
 */
public class ImageUtil {


    /**
     * 重置图片大小
     *
     * @param image       文件对象
     * @param toImagePath 写入图片路径
     * @param width       重新设置图片的宽
     * @param height      重新设置图片的高
     */
    public static void reSizeImage(File image, String toImagePath, int width, int height) {
        try (FileOutputStream out = new FileOutputStream(toImagePath)){
            // 构造Image对象
            BufferedImage src = javax.imageio.ImageIO.read(image);
            // 缩小边长
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 绘制 缩小  后的图片
            tag.getGraphics().drawImage(src, 0, 0, width, height, null);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
        } catch (Exception e) {
            e.printStackTrace();
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
        try (FileInputStream fis = new FileInputStream(srcPath);
             ImageInputStream iis = ImageIO.createImageInputStream(fis)

        ) {
            //读取图片文件`
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(readImageFormat);
            ImageReader reader = readers.next();
            //获取图片流
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
        try (
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))
        ) {
            BufferedImage img = ImageIO.read(input);
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            return new int[]{w, h};

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }


}
