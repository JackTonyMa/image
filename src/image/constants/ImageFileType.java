package image.constants;
import java.util.Objects;

/**
 * 支持裁剪的图片文件类型枚举值
 *
 * @author zhouchang
 */
public enum ImageFileType {
    jpg, png, jpeg;

    /**
     * 判断是否支持此类型的文件
     * @param type 要判断的文件类型
     * @return 支持返回true 不支持返回false
     */
    public static boolean contains(String type) {
        for (ImageFileType imageFileType : ImageFileType.values()) {
            if (!Objects.equals(type, imageFileType)) {
                continue;
            }
            return true;
        }
        return false;
    }
}
