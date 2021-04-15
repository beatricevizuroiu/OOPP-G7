package nl.tudelft.oopp.g7.client.logic;

import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;

public class LecturerViewLogic {

    /**
     * Logic for Speed indicator to show the lecturer if he is going too fast.
     */
    public static void speedIndicator(String roomID, Circle circle1, Circle circle2, Circle circle3, Circle circle4) {
        double speedInRoom = ServerCommunication.getSpeed(roomID);
        double peopleInRoom = ServerCommunication.retrieveAllUsers(roomID).size();

        speedInRoom = speedInRoom / peopleInRoom * 100;


        circle1.setFill(Color.valueOf("#ffffff"));
        circle2.setFill(Color.valueOf("#ffffff"));
        circle3.setFill(Color.valueOf("#ffffff"));
        circle4.setFill(Color.valueOf("#ffffff"));

        // lecturer is too slow
        if (speedInRoom <= -75) {
            circle3.setFill(Color.valueOf("#8D1BAA"));
            circle4.setFill(Color.valueOf("#8D1BAA"));
        }


        if (speedInRoom > -75 && speedInRoom < -25) {
            circle3.setFill(Color.valueOf("#8D1BAA"));
        }

        if (speedInRoom >= 25 && speedInRoom < 75) {
            circle2.setFill(Color.valueOf("#8D1BAA"));
        }

        // lecturer is too fast
        if (speedInRoom >= 75) {
            circle1.setFill(Color.valueOf("#8D1BAA"));
            circle2.setFill(Color.valueOf("#8D1BAA"));
        }
    }
}
