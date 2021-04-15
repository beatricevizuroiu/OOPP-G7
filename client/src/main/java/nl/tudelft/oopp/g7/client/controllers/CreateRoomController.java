package nl.tudelft.oopp.g7.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.logic.CreateRoomLogic;
import nl.tudelft.oopp.g7.client.logic.LocalData;

import java.time.LocalDate;
import java.util.Calendar;

public class CreateRoomController {
    @FXML
    TextField roomName;
    @FXML
    TextField lecturerName;
    @FXML
    TextField studentPassword;
    @FXML
    TextField moderatorPassword;
    @FXML
    DatePicker startDate;
    @FXML
    TextField startTime;
    @FXML
    CheckBox scheduleCheckbox;

    /**
     * Create the current date.
     */
    @FXML
    public void initialize() {
        startDate.setValue(LocalDate.now());
        startTime.setText(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE));
    }

    /**
     * Handle button action for button Mode from Light.
     */
    public void changeMode() {
        LocalData.switchColorScheme();
    }

    /**
     * Handles clicking the button Create from Light.
     */
    public void createRoom() {
        CreateRoomLogic.createRoom(lecturerName.getText(), roomName.getText(),
                studentPassword.getText(), moderatorPassword.getText(),
                scheduleCheckbox.isSelected(), startDate.getValue(), startTime.getText());
    }

    /**
     * Handle button action for going back to Entry page from Light.
     */
    public void goBack() {
        Views.goBack();
    }
}
