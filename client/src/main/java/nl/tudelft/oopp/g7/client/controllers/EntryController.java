package nl.tudelft.oopp.g7.client.controllers;

import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.LocalData;

public class EntryController {

    /**
     * Handle button action for button Create.
     */
    public void createRoom() {
        Views.navigateTo(Views.CREATE);
    }

    /**
     * Handle button action for button Join.
     */
    public void joinRoom() {
        Views.navigateTo(Views.JOIN);
    }

    /**
     * Handle button action for button Mode from Light.
     */
    public void changeMode() {
        LocalData.switchColorScheme();
    }

}
