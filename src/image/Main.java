package image;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @program: image
 * @description: 启动程序
 * @author: zhouchang
 * @create: 2019-04-02 16:12
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("照片尺寸修改");
        primaryStage.setScene(new Scene(root, 700, 600));


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
