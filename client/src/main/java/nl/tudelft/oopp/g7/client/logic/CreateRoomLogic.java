package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.common.SortingOrder;

import java.time.LocalDate;
import java.util.Date;

public class CreateRoomLogic {
    /**
     * Create a pop-up for confirming creation of a room.
     * @param lecturerName TextField in which lecturer's name is written
     * @param roomName TextField in which room's name is written
     * @return a boolean confirming whether the user pressed OK or cancel
     */
    public static boolean createRoomConfirmation(TextField lecturerName, TextField roomName) {
        // show confirmation type pop-up with what you entered as text
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // title of pop-up
        alert.setTitle("Create Room");
        alert.setHeaderText(null);

        // body of pop-up with what the user entered
        alert.setContentText(String.format(
                        "You are creating the room for the Course: %s\n"
                        + "This course is held by the lecturer: %s.",
                roomName.getText(), lecturerName.getText()));

        alert.getDialogPane().setPrefHeight(150);

        // set types of buttons for the pop-up
        ButtonType okButton = new ButtonType ("OK");
        ButtonType cancelButton = new ButtonType ("Cancel");

        alert.getButtonTypes().setAll(okButton, cancelButton);

        // wait for the alert to appear
        alert.showAndWait();

        // return whether the user pressed OK or not
        return alert.getResult() == okButton;
    }

    /**
     * Stores the room/user information into a local static class.
     * @param lecturerName TextField in which lecturer's name is written
     * @param room object containing information about the newly created room
     */
    public static void createRoomStoreLocalData(TextField lecturerName, Room room) {
        LocalData.setNickname(lecturerName.getText());
        LocalData.setRoomID(room.getId());
        LocalData.setStudentPassword(room.getStudentPassword());
        LocalData.setModeratorPassword(room.getModeratorPassword());
        LocalData.setSortingOrder(SortingOrder.UPVOTES);
        LocalData.setLecturer(true);
    }

    /**
     * Get the Date and Time from the datepicker.
     * @param startDate Date the Room should open at.
     * @param startTime Time the Room should open at.
     * @return The {@link Date} the Room should open at.
     */
    public static Date getTimeAndDateFromUI(DatePicker startDate, TextField startTime) {
        LocalDate date = startDate.getValue();
        Date time = parseStringToTime(startTime.getText());

        if (date == null || time == null)
            return null;

        return new Date(date.getYear() - 1900, date.getMonth().ordinal(), date.getDayOfMonth(), time.getHours(), time.getMinutes(), 0);
    }

    private static Date parseStringToTime(String timeString) {
        if (timeString == null)
            return null;

        String[] parts = timeString.split(":");

        if (parts.length != 2)
            return null;

        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hours < 0 || hours >= 24)
                return null;

            if (minutes < 0 || minutes >= 60)
                return null;

            return new Date(0, 0, 0, hours, minutes, 0);

        } catch (NumberFormatException ignored) {
            return null;
        }

    }
}
