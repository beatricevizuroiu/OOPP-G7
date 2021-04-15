package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.g7.client.Views;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.util.Util;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.common.RoomJoinInfo;
import nl.tudelft.oopp.g7.common.SortingOrder;

import java.time.LocalDate;
import java.util.Date;

public class CreateRoomLogic {
    /**
     * Create a pop-up for confirming creation of a room.
     * @param lecturerName TextField in which lecturer's name is written
     * @param roomName     TextField in which room's name is written
     * @return a boolean confirming whether the user pressed OK or cancel
     */
    public static boolean createRoomConfirmation(String lecturerName, String roomName) {
        return Util.getConfirmation("Create Room", null, """
                You are creating the room for the Course: %s
                This course is held by the lecturer: %s.""".formatted(
                roomName, lecturerName));
    }

    /**
     * Get the Date and Time from the datepicker.
     * @param startDate Date the Room should open at.
     * @param startTime Time the Room should open at.
     * @return The {@link Date} the Room should open at.
     */
    public static Date getTimeAndDateFromUI(LocalDate startDate, String startTime) {
        Date time = parseStringToTime(startTime);

        if (startDate == null || time == null)
            return null;

        return new Date(startDate.getYear() - 1900, startDate.getMonth().ordinal(), startDate.getDayOfMonth(), time.getHours(), time.getMinutes(), 0);
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

    public static void createRoom(String lecturerName, String roomName, String studentPassword, String moderatorPassword, boolean isScheduled, LocalDate startDate, String startTime) {
        // if the user presses OK, the go to Lecturer View
        if (CreateRoomLogic.createRoomConfirmation(lecturerName, roomName)) {

            Date startDateTime = CreateRoomLogic.getTimeAndDateFromUI(startDate, startTime);

            if (isScheduled && startDateTime == null) {
                Util.createAlert(Alert.AlertType.ERROR, "Invalid date/time", null, "The date and or time you have entered are invalid.")
                        .showAndWait();
                return;
            }

            if (startDateTime == null)
                startDateTime = new Date();

            // Put all information for a new room in a NewRoom object and send it to the server to have it create the room
            NewRoom newRoom = new NewRoom(roomName, studentPassword, moderatorPassword, startDateTime);
            Room room = RoomServerCommunication.createRoom(newRoom);
            if (room == null) {
                Util.createAlert(Alert.AlertType.ERROR, "Could not create room.", null, "Could not create room.")
                        .showAndWait();
                return;
            }

            // Store all relevant room information for future reference
            LocalData.storeAll(room);

            if (isScheduled) {
                ModeratorViewLogic.displayLinkAndPasswords();
                return;
            }

            RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(room.getId(), room.getModeratorPassword(), lecturerName);

            if (roomJoinInfo == null) {
                Util.createAlert(Alert.AlertType.ERROR, "Could not join room.", null, "Could not join room.")
                        .showAndWait();
                return;
            }

            LocalData.storeAll(roomJoinInfo);

            Views.navigateTo(Views.HOME);

        }
    }
}
