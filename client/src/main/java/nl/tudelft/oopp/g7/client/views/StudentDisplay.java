package nl.tudelft.oopp.g7.client.views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import nl.tudelft.oopp.g7.client.controllers.StudentController;

import java.io.IOException;
import java.net.URL;

public class StudentDisplay extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/studentUI.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        //Code for the automatic update sourced from https://riptutorial.com/javafx/example/7291/updating-the-ui-using-platform-runlater
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        //update action
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException ex) {
                        System.err.println("Sleeping thread was interrupted, this should never occur");
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }
        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
