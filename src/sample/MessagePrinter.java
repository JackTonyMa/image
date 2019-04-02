package sample;

import javafx.scene.control.TextArea;

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
//        System.out.println("test");/
    }
}
