package image.constants;

import java.util.HashSet;

/**
 * @author 66417
 */
public class Constant {

    public static final HashSet<String> imageType = new HashSet<>();

    static {
        imageType.add("png");
        imageType.add("jpg");
        imageType.add("jpeg");
    }


}
