package nl.tudelft.oopp.g7.client.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import nl.tudelft.oopp.g7.client.logic.LocalData;

import java.io.IOException;
import java.net.URL;

public class EntryRoomDisplay extends Application {

    private static Stage currentStage;
    private static Scene currentScene;
    @Getter
    private static boolean isDarkMode;

    @Override
    public void start(Stage primaryStage) throws IOException{
        currentStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();

        URL xmlUrl = getClass().getResource("/entryPage.fxml");

        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setTitle("RaisedHand");
        currentStage.getIcons().add(new Image("/icons/hand.png"));

        currentScene = new Scene(root);

        currentStage.setScene(currentScene);
        currentStage.show();
    }

    /**
     * Get current scene scene.
     *
     * @return the scene
     */
    public static Scene getCurrentScene(){
        return currentScene;
    }

    /**
     * Set current scene.
     *
     * @param newSceneName the new scene name
     */
    public static void setCurrentScene(String newSceneName){
        try{
            Parent parent = FXMLLoader.load(EntryRoomDisplay.class.getResource(newSceneName));
            Scene newScene = new Scene(parent);

            currentStage.setScene((newScene));
            EntryRoomDisplay.currentScene = newScene;

            if (LocalData.getRoomName() != null && !"".equals(LocalData.getRoomName())) {
                currentStage.setTitle("RaisedHand: " + LocalData.getRoomName());
            } else {
                currentStage.setTitle("RaisedHand");
            }

            currentStage.show();

            if (newSceneName.contains("DARKMODE")) {
                isDarkMode = true;
            } else {
                isDarkMode = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get current stage stage.
     *
     * @return the stage
     */
    public static Stage getCurrentStage(){
        return currentStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
