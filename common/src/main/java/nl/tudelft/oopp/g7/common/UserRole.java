package nl.tudelft.oopp.g7.common;

import lombok.Getter;

public enum UserRole {
    STUDENT("/views/StudentHome.fxml", "/views/StudentHelp.fxml",
            "/views/StudentAnsweredQuestions.fxml", "/views/ListUsersStudent.fxml"),
    MODERATOR("/views/ModeratorHome.fxml", "/views/ModeratorHelp.fxml",
            "/views/ModeratorAnsweredQuestions.fxml", "/views/ListUsersModerator.fxml"),
    LECTURER("/views/LecturerHome.fxml", "/views/LecturerHelp.fxml",
            "/views/ModeratorAnsweredQuestions.fxml", "/views/ListUsersModerator.fxml");

    @Getter
    private final String homePage;
    @Getter
    private final String helpPage;
    @Getter
    private final String answeredQuestionsPage;
    @Getter
    private final String listUsersPage;

    UserRole(String homePage, String helpPage, String answeredQuestionsPage, String listUsersPage) {
        this.homePage = homePage;
        this.helpPage = helpPage;
        this.answeredQuestionsPage = answeredQuestionsPage;
        this.listUsersPage = listUsersPage;
    }
}
