package sample;

import javafx.scene.control.TextArea;

/**
 * @author 66417
 */
public class MessagePrinter implements Runnable {

    private TextArea textArea;
    private String message;


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void run() {
        textArea.appendText(message);
    }
}
