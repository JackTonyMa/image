package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static sample.FileUtil.creatFolder;
import static sample.imageUtil.readFolder;


public class Controller implements Initializable {

    public static String targetPath;
    public static String originalPath;

    @FXML
    private Pane pane;
    @FXML
    private ProgressBar progressBar;


    @FXML
    private MenuBar menuBar;
    @FXML
    private TextArea consolePane;


    public static void main(String[] args) {

        test();
    }


    public static void test(){

        MessagePrinter messagePrinter = new MessagePrinter();

//        messagePrinter.setMessage("text");
//        messagePrinter.setTextArea(consolePane);
        Thread thread=new Thread(messagePrinter);
        for(int i=0;i<10;i++) {
//            messagePrinter.run();
            thread.start();
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void openFolder(Event event) {

        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(primaryStage);
        /**
         * 全局变量用来传递
         */
        String path = file.getPath();
        originalPath = file.getPath();
        String timeStamp = LocalDateTime.now().toString().substring(0, 19).replace(":", "-");


        if (path != null) {
            consolePane.setText("待处理文件夹:....." + path + "\n");

            targetPath = creatFolder(path + timeStamp);
            readFolder(consolePane, originalPath);


        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consolePane.prefWidthProperty().bind(pane.widthProperty());
        consolePane.prefHeightProperty().bind(pane.heightProperty());
        menuBar.prefWidthProperty().bind(pane.widthProperty());//宽度绑定为Pane宽度

    }

//    @FXML
//    public void clear(){
//        MessagePrinter messagePrinter = new MessagePrinter();
//
//        messagePrinter.setMessage("text");
//        messagePrinter.setTextArea(consolePane);
//        Thread thread=new Thread(messagePrinter);
//        for(int i=0;i<10;i++) {
////            messagePrinter.run();
//            thread.start();
//            try {
//
//                thread.wait(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
}
