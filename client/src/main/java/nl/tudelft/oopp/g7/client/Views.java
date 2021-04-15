package nl.tudelft.oopp.g7.client;

import nl.tudelft.oopp.g7.client.logic.LocalData;

import java.util.Arrays;
import java.util.Stack;

public enum Views {
    ENTRY,
    JOIN,
    CREATE,
    HOME,
    HELP,
    ANSWERED_QUESTIONS,
    USER_LIST,
    CREATE_POLL;

    private static final Stack<Views> history = new Stack<>();
    private static Views currentView = ENTRY;

    public static void goBack() {
        Views target = history.pop();
        goToView(target);
        currentView = target;

        System.out.printf("HISTORY: %s%n", Arrays.toString(history.toArray()));
    }

    public static void navigateTo(Views view) {
        history.push(currentView);
        currentView = view;
        goToView(view);

        System.out.printf("HISTORY: %s%n", Arrays.toString(history.toArray()));
    }

    private static void goToView(Views view) {
        String target = "";
        switch (view) {
            case ENTRY -> target = "/views/Entry.fxml";
            case JOIN -> target = "/views/Join.fxml";
            case CREATE -> target = "/views/Create.fxml";
            case HOME -> target = LocalData.getRole().getHomePage();
            case HELP -> target = LocalData.getRole().getHelpPage();
            case ANSWERED_QUESTIONS ->  target = LocalData.getRole().getAnsweredQuestionsPage();
            case USER_LIST -> target = LocalData.getRole().getListUsersPage();
        }

        MainApp.setCurrentScene(target);
    }

    public static void reloadCurrentView() {
        goToView(currentView);
    }
}
