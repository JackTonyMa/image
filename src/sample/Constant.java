package sample;

import java.util.HashSet;

public class Constant {

    public static final HashSet<String> imageType=new HashSet<>();

    static {
        imageType.add("png");
        imageType.add("jpg");
        imageType.add("jpeg");
    }



}
