package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Answer;
import nl.tudelft.oopp.g7.common.NewQuestion;
import nl.tudelft.oopp.g7.common.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionControllerTest {

    private DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private QuestionController questionController;

    @BeforeEach
    void setUp() throws IOException {
        // Setup an in memory database with H2.
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");

        // Create a jdbc template with the datasource.
        jdbcTemplate = new JdbcTemplate(dataSource);

        // Read the file with test information and run the queries on the database.
        String sqlQueries = Files.readString(Path.of(new File("src/test/resources/test-question.sql").getPath()));
        jdbcTemplate.execute(sqlQueries);

        // Create our questionController with our in memory datasource.
        questionController = new QuestionController(jdbcTemplate);
    }

    @Test
    void getQuestion() {
        // Create the question we expect to see.
        Question expected = new Question(1, "This is a question", "", new Date(0), 0, false, false);

        // Get the question from the controller.
        Question actual = questionController.getQuestion(1).getBody();

        // Check to see if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void getAllQuestions() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "This is a question", "", new Date(0), 20, true, false));

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions();

        // Check if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void deleteQuestion() {
        // Create the list of questions we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(3, "This is a question", "", new Date(0), 20, true, false));

        // Delete question with id 2.
        questionController.deleteQuestion(2);

        // Get the list of question left.
        List<Question> actual = questionController.getAllQuestions();

        // Check if that list is the same as what we expect to see.
        assertEquals(expected, actual);
    }

    @Test
    void newQuestion() {
        // Create a new question.
        questionController.newQuestion(new NewQuestion("This is a new question"));

        String expected = "This is a new question";

        // Get the text of the new question from the controller.
        String actual = questionController.getQuestion(4).getBody().getText();

        // Check if the question has te expected text.
        assertEquals(expected, actual);
    }

    @Test
    void answerQuestion() {
        // Answer question 1.
        questionController.answerQuestion(1, new Answer("This an answer"));

        // Get the question with id 1.
        Question actual = questionController.getQuestion(1).getBody();

        // Store the expected and actual answers.
        String expectedAnswer = "This an answer";
        String actualAnswer = actual.getAnswer();

        // Check if the answers are equal.
        assertEquals(expectedAnswer, actualAnswer);

        // Check if the question is marked as answered.
        assertTrue(actual.isAnswered());
    }
}