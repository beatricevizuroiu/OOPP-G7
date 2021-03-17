package nl.tudelft.oopp.g7.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.util.ArrayList;
import java.util.List;

public class StudentController {
    public ListView<String> listView = new ListView<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    public TextArea answerBox;

    private String roomID;


    // TODO: ListView

    public void sendQuestion() {
        //StudentServerCommunication.askQuestion(roomId, new QuestionText(answerBox.getText()));
        items.add(answerBox.getText());
        answerBox.setText("");
    }

    public void updateQuestion() {
        List<Question> newQuestions = StudentServerCommunication.retrieveAllQuestions(roomID);
        items.removeAll();
        for (int i = 0; i < newQuestions.size(); i++) {
            items.add(newQuestions.get(i).getText());
        }
    }

}
