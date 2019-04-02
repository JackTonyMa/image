package image.controller;

import image.service.ImageService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static image.utils.FileUtil.creatFolder;


public class Controller implements Initializable {

    public static String targetPath;
    public static String originalPath;

    @FXML
    private Pane pane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TextArea consolePane;

    @FXML
    public void openFolder() {

        //打开文件选择框
        Stage primaryStage = new Stage();
        //选择文件夹
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(primaryStage);

        if (file == null) {
            consolePane.appendText("未选择文件!");

            return;
        }
        String path = file.getPath();
        originalPath = file.getPath();
        String timeStamp = LocalDateTime.now().toString().substring(0, 19).replace(":", "-");


        if (path != null) {
            consolePane.clear();
            consolePane.appendText("待处理文件夹:....." + path + "\n");
            ImageService imageService = new ImageService(consolePane, originalPath);

            targetPath = creatFolder(path + timeStamp);
            //启用图片处理线程(只用主线程无法实时渲染UI,再高深的方式还不会o(╥﹏╥)o)
            //javafx渲染ui只能用Application Thread
            Thread thread = new Thread(imageService);
            thread.setName("ImageTask");
            thread.start();


        } else {
            consolePane.appendText("文件路径获取失败:.....\n");

        }

    }

    /**
     * 将顶部菜单和容器宽度绑定
     * 将textarea高宽和容器高宽绑定
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consolePane.prefWidthProperty().bind(pane.widthProperty());
        consolePane.prefHeightProperty().bind(pane.heightProperty());
        menuBar.prefWidthProperty().bind(pane.widthProperty());//宽度绑定为Pane宽度

    }

    //清屏
    @FXML
    public void clear() {
        consolePane.clear();


    }
}
