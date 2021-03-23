package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class AnsweredQuestionsController {

/*    *//**
     * Handle button action for button Mode from Light.
     *
     * @param event the event
     *//*
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestions(DARKMODE).fxml");
    }

    *//**
     * Handle button action for button Mode from Dark.
     *
     * @param event the event
     *//*
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestions.fxml");
    }

    public void handleHelpButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestions.fxml");
    }

    public void handleHelpButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestions(DARKMODE).fxml");
    }*/
}
