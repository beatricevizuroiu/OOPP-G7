package nl.tudelft.oopp.g7.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.communication.StudentServerCommunication;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentController {
    public ListView<String> listView = new ListView<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    public TextArea answerBox;
    private String roomID = "TestRoom";


    // TODO: ListView

    /**
     * This is getting replaced.
     */
    public void sendQuestion() {
        StudentServerCommunication.askQuestion(roomID, new QuestionText(answerBox.getText()));
        answerBox.setText("");
    }

    /**
     * This is getting replaced.
     */
    public void updateQuestion() {
        List<Question> newQuestions = StudentServerCommunication.retrieveAllQuestions(roomID);
        items.remove(0, items.size());
        for (Question newQuestion : newQuestions) {
            items.add(newQuestion.getText());
        }
        listView.refresh();
    }

    /**
     * This is here for temporary testing purposes, it can be replaced with proper UI Controllers.
     */
    public void testCreateRoom() {
        NewRoom testRoom = new NewRoom("testroom", "", "VeryGoodPW", new Date());
        Room createdRoom = ServerCommunication.createRoom(testRoom);
        roomID = createdRoom.getId();
        listView.setItems(items);
    }

}
