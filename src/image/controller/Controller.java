package image.controller;

import image.service.ImageService;
import image.utils.FileUtil;
import javafx.concurrent.Task;
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

/**
 * @author 66417
 */
public class Controller implements Initializable {
    Task copyWorker;

    public static int rateOfProgress = 0;

    public static String targetPath;

    public static String originalPath;

    public static int successCount;

    public static int failCount;

    public static int fileNums;

    @FXML
    private Pane pane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextArea consolePane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void openFolder() {
        successCount = 0;
        failCount = 0;
        fileNums = 0;
        rateOfProgress = 0;
        Stage primaryStage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(primaryStage);
        if (file == null) {
            this.consolePane.appendText("未选择文件!");
            return;
        }
        String path = file.getPath();
        originalPath = file.getPath();
        String timeStamp = LocalDateTime.now().toString().substring(0, 19).replace(":", "-");
        FileUtil.getFileNum(path);
        consolePane.clear();
        consolePane.appendText("待处理文件夹>>>" + path + "\n");
        ImageService imageService = new ImageService(consolePane, originalPath);
        targetPath = FileUtil.creatFolder(path + timeStamp);
        Thread thread1 = new Thread(imageService);
        thread1.setName("ImageTask");
        thread1.start();
        copyWorker = createWorker();
        progressBar.progressProperty().bind(copyWorker.progressProperty());
        Thread thread = new Thread(copyWorker);
        thread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.progressBar.setVisible(false);
        this.consolePane.prefWidthProperty().bind(this.pane.widthProperty());
        this.consolePane.prefHeightProperty().bind(this.pane.heightProperty());
        this.menuBar.prefWidthProperty().bind(this.pane.widthProperty());
        this.progressBar.prefWidthProperty().bind(this.pane.widthProperty());
    }

    @FXML
    public void clear() {
        this.consolePane.clear();
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                progressBar.setVisible(true);
                updateProgress(0L, Controller.fileNums);
                do {
                    updateMessage((Controller.rateOfProgress / Controller.fileNums) + "%");
                    updateProgress(Controller.rateOfProgress, Controller.fileNums);
                } while (Controller.rateOfProgress != Controller.fileNums && failCount + successCount != fileNums);
                updateProgress(fileNums, fileNums);
                Thread.sleep(1000L);
                progressBar.setVisible(false);
                return Boolean.TRUE;
            }
        };
    }
}