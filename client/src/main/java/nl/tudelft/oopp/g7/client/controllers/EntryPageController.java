package nl.tudelft.oopp.g7.client.controllers;

import javafx.event.ActionEvent;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.logic.LocalData;

public class EntryPageController {

    /**
     * Handle button action for button Create.
     * @param event the event
     */
    public void handleButtonAction(ActionEvent event) {
        // if Create is clicked, change Scene to Create Room
        MainApp.setCurrentScene("/views/createRoom.fxml");
    }

    /**
     * Handle button action for button Join.
     * @param event the event
     */
    public void handleButtonAction2(ActionEvent event) {
        // if Join is clicked, change Scene to Join Room
        MainApp.setCurrentScene("/views/joinRoom.fxml");
    }

    /**
     * Handle button action for button Mode from Light.
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        LocalData.switchColorScheme();
    }

}
