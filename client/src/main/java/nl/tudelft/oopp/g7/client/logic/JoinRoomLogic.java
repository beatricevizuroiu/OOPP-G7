package nl.tudelft.oopp.g7.client.logic;

import javafx.scene.control.Alert;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.client.communication.RoomServerCommunication;
import nl.tudelft.oopp.g7.client.util.Util;
import nl.tudelft.oopp.g7.common.RoomJoinInfo;
import nl.tudelft.oopp.g7.common.UserRole;

public class JoinRoomLogic {

    /**
     * Starts the process of joining a room. It will ask the user for confirmation and then it will join the room.
     * @param nickname The nickname to join the room with.
     * @param password The password to join the room with.
     * @param roomId The id of the room to join.
     */
    public static void joinRoom(String nickname, String password, String roomId) {
        // if the user presses OK, the go to Student View
        if (JoinRoomLogic.joinRoomConfirmation(nickname, roomId)) {

            RoomJoinInfo roomJoinInfo = RoomServerCommunication.joinRoom(roomId, password, nickname);

            if (roomJoinInfo == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not join room.");
                alert.setHeaderText("Could not join room.");
                alert.setContentText("It was not possible to join the room specified. The room might not exist, not have opened, or your password may be incorrect.");

                alert.showAndWait();
                return;
            }

            // Store all the entered information
            JoinRoomLogic.joinRoomStoreLocalData(roomJoinInfo);

            // Send password to the server to determine whether the user is a Student or a Moderator and send the user to the right UI
            if (roomJoinInfo.getRole() == UserRole.STUDENT) {
                MainApp.setCurrentScene("/views/StudentViewUI.fxml");
            } else {
                MainApp.setCurrentScene("/views/TAViewUI.fxml");
            }
        }
    }

    /**
     * Create pop-up confirming joining a room.
     * @param nickname The nickname that the user has set.
     * @param roomId   The room id that we are trying to join.
     * @return a boolean confirming whether the user pressed OK or cancel
     */
    private static boolean joinRoomConfirmation(String nickname, String roomId) {
        String alertMessage = "You are joining room: %s".formatted(roomId);
        if (nickname != null && !nickname.equals(""))
            alertMessage = "You are joining room: %s. %nAs: %s".formatted(roomId, nickname);

        return Util.getConfirmation("Join Room", null, alertMessage);
    }

    /**
     * Stores the room/user information into a local static class.
     * @param roomJoinInfo The info of the room we just joined.
     */
    public static void joinRoomStoreLocalData(RoomJoinInfo roomJoinInfo) {
        LocalData.setNickname(roomJoinInfo.getNickname());
        LocalData.setRoomID(roomJoinInfo.getRoomId());
        LocalData.setToken(roomJoinInfo.getAuthorization());
        LocalData.setUserID(roomJoinInfo.getUserId());
    }
}
