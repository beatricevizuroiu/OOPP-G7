package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.common.QuestionText;

public class StudentViewUIController {
    public ListView<String> listView = new ListView<>();
    public TextArea answerBox;


//    public void retrieveQuestions() {
//        StudentServerCommunication.retrieveAllQuestions();
//        listView.getItems();
//    }

    public void sendQuestion() {
        StudentServerCommunication.askQuestion(new QuestionText(answerBox.getText()));
        answerBox.setText("Type your question here");
    }


}
