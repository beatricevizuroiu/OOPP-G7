package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.common.NewQuestion;

public class StudentController {
    @FXML TextArea answerBox;

    public void sendQuestion() {
        StudentServerCommunication.askQuestion(new NewQuestion(answerBox.getText()));
        answerBox.setText("");
    }
}
