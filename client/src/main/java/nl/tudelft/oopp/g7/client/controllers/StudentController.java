package nl.tudelft.oopp.g7.client.controllers;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.common.NewQuestion;

public class StudentController {
    public ListView<String> listView = new ListView<>();
    public TextArea answerBox;

    // TODO: ListView

    public void sendQuestion() {
        StudentServerCommunication.askQuestion(new NewQuestion(answerBox.getText()));
        answerBox.setText("");
    }
}
