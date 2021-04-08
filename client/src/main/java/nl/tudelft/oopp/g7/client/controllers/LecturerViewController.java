package nl.tudelft.oopp.g7.client.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.g7.client.communication.ModeratorServerCommunication;
import nl.tudelft.oopp.g7.client.communication.ServerCommunication;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.logic.ModeratorViewLogic;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;
import nl.tudelft.oopp.g7.common.UserInfo;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LecturerViewController {
    private final String roomID;
    private final String nickname;
    private final String moderatorPassword;
    private final String studentPassword;
    private HashMap<String, UserInfo> userMap = new HashMap<>();

    @FXML
    public ScrollPane questionList;
    @FXML
    private VBox questionContainer;
    @FXML
    private TextArea answerBox;
    @FXML
    private Button postAnswerButton;
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle2;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle4;

    /**
     * The constructor for LecturerViewController.
     */
    public LecturerViewController() {
        roomID = LocalData.getRoomID();
        nickname = LocalData.getNickname();
        moderatorPassword = LocalData.getModeratorPassword();
        studentPassword = LocalData.getStudentPassword();
    }

    /**
     * Start-up routine.
     */
    public void initialize() {
        // Start a timer and create a separate thread on it to automatically call retrieveQuestions() and speedIndicator()
        Timer timer = new Timer(true);

        LecturerViewController reference = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(reference::retrieveQuestions);
                Platform.runLater(reference::speedIndicator);
            }
        }, 0L, 5000L);
    }

    /**
     * Update the list of questions sorted by most upvotes.
     */
    public void retrieveQuestions() {
        // Retrieve all of the questions and then put them into question pane
        ModeratorViewLogic.retrieveAllQuestions(roomID, answerBox, postAnswerButton, questionContainer, questionList);
    }

    /**
     * Display link and passwords.
     */
    public void displayLinkAndPassword() {
        ModeratorViewLogic.displayLinkAndPasswords();
    }


    /**
     * Handle button action for button Mode from Light to Dark.
     *
     * @param event the event
     */
    public void handleButtonMode(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/LecturerViewUI(DARKMODE).fxml");
    }

    /**
     * Handle button action for button Mode from Dark to Light.
     *
     * @param event the event
     */
    public void handleButtonMode2(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Mode is clicked, change Scene to Join Room
        EntryRoomDisplay.setCurrentScene("/LecturerViewUI.fxml");
    }

    /**
     * Handle button action for Help Button Light Mode.
     *
     * @param event the event
     */
    public void handleHelpButtonLight(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer.fxml");
    }

    /**
     * Handle button action for Help Button Dark Mode.
     *
     * @param event the event
     */
    public void handleHelpButtonDark(ActionEvent event) {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Help is clicked, change to Help scene
        EntryRoomDisplay.setCurrentScene("/HelpFileLecturer(DARKMODE).fxml");
    }

    /**
     * Handle button action for Answered Questions Button light Mode.
     */
    public void answeredQuestionList() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (lightmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator.fxml");
    }

    /**
     * Handle button action for Answered Questions Button Dark Mode.
     */
    public void answeredQuestionListDark() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if Answered questions is clicked, change to Answered Questions (darkmode) scene
        EntryRoomDisplay.setCurrentScene("/AnsweredQuestionsModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for List Users Button light Mode.
     */
    public void listofUsers() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsersModerator.fxml");
    }

    /**
     * Handle button action for List Users Button dark Mode.
     */
    public void listofUsersDark() {
        Scene scene = EntryRoomDisplay.getCurrentScene();
        Stage stage = EntryRoomDisplay.getCurrentStage();

        // if list of Users is clicked, change to List of Users scene
        EntryRoomDisplay.setCurrentScene("/ListUsersModerator(DARKMODE).fxml");
    }

    /**
     * Handle button action for deleting a question.
     */
    public void deleteQuestion() {
//        ModeratorViewLogic.deleteQuestion(roomID, questionId, questionContainer, questionList);
    }

    /**
     * Handle button action for editing a question.
     */
    public void editQuestion() {
//        ModeratorViewLogic.editQuestion(roomID, questionId, questionContainer, questionList);
    }

    /**
     * Handle button action for banning a user.
     */
    public void banUser() {
//        ModeratorViewLogic.banUser(roomID, userID);
    }


    /**
     * Handle button action for answering a question.
     */
    public void answerQuestion() {
        /*HttpResponse<String> response = ModeratorServerCommunication.answerQuestion(roomID, new QuestionText(answerBox.getText()));
        answerBox.setText("");
        retrieveQuestions();*/
    }

    /**
     * Handle button action for creating a poll.
     */
    public void createPoll() {
        //TODO
    }

    /**
     * Handle button action for exporting questions.
     */
    public void exportQuestions() {
        ModeratorViewLogic.exportQuestions(roomID);
    }

    /**
     * Handle button action for creating a  link.
     */
    public void createLink() {
        //TODO
    }

    public void speedIndicator() {
        double speedInRoom = ServerCommunication.getSpeed(roomID);
        double peopleInRoom = ServerCommunication.retrieveAllUsers(roomID).size();
        speedInRoom = (speedInRoom * 13.43 * peopleInRoom) / 100;

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
