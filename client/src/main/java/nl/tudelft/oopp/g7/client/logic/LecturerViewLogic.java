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

        if(peopleInRoom <= 150) {
            speedInRoom = (speedInRoom * 13.43 / peopleInRoom) / 100;
        } else{
            speedInRoom = (speedInRoom / 13.43 / peopleInRoom) * 100;
        }

        // lecturer is going normal
        if (speedInRoom == 0) {
            circle1.setFill(Color.valueOf("#ffffff"));
            circle2.setFill(Color.valueOf("#ffffff"));
            circle3.setFill(Color.valueOf("#ffffff"));
            circle4.setFill(Color.valueOf("#ffffff"));
        }

        // lecturer is too slow
        if (speedInRoom == -1) {
            circle1.setFill(Color.valueOf("#ffffff"));
            circle2.setFill(Color.valueOf("#ffffff"));
            circle3.setFill(Color.valueOf("#8D1BAA"));
            circle4.setFill(Color.valueOf("#8D1BAA"));
            PauseTransition transition = new PauseTransition(Duration.seconds(30));
            transition.setOnFinished(event -> {
                        circle3.setFill(Color.valueOf("#ffffff"));
                        circle4.setFill(Color.valueOf("#ffffff"));
                    }
            );
        }


        if (speedInRoom > -1 && speedInRoom < 0) {
            circle1.setFill(Color.valueOf("#ffffff"));
            circle2.setFill(Color.valueOf("#ffffff"));
            circle3.setFill(Color.valueOf("#8D1BAA"));
            circle4.setFill(Color.valueOf("#ffffff"));
            PauseTransition transition = new PauseTransition(Duration.seconds(30));
            transition.setOnFinished(event -> {
                        circle3.setFill(Color.valueOf("#ffffff"));
                    }
            );
        }

        if (speedInRoom > 0 && speedInRoom < 1) {
            circle1.setFill(Color.valueOf("#ffffff"));
            circle2.setFill(Color.valueOf("#8D1BAA"));
            circle3.setFill(Color.valueOf("#ffffff"));
            circle4.setFill(Color.valueOf("#ffffff"));
            PauseTransition transition = new PauseTransition(Duration.seconds(30));
            transition.setOnFinished(event -> {
                        circle2.setFill(Color.valueOf("#ffffff"));
                    }
            );
        }

        // lecturer is too fast
        if (speedInRoom == 1) {
            circle1.setFill(Color.valueOf("#8D1BAA"));
            circle2.setFill(Color.valueOf("#8D1BAA"));
            circle3.setFill(Color.valueOf("#ffffff"));
            circle4.setFill(Color.valueOf("#ffffff"));
            PauseTransition transition = new PauseTransition(Duration.seconds(30));
            transition.setOnFinished(event -> {
                        circle1.setFill(Color.valueOf("#ffffff"));
                        circle2.setFill(Color.valueOf("#ffffff"));
                    }
            );
        }
    }
}
