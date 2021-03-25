package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.web.MockHttpServletRequest;

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
    private MockHttpServletRequest request;

    private final String TEST_ROOM_ID = "SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH";
    private final String AUTHORIZATION = "Bearer Ftqp8J5Ub8PcUO0qJDXGuAooXZZfzZrZbfb51pCeYWDchzf6wyuwtFNzYeEeacE7k82Xn7y6ue9KWxPmP0eENubnz3PMelle4i9NLKb0RiQiVCDK8xdDjuu1uacyHdTC";

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

        QuestionRepository questionRepository = new QuestionRepository(jdbcTemplate);
        UserRepository userRepository = new UserRepository(jdbcTemplate);
        BanRepository banRepository = new BanRepository(jdbcTemplate);

        // Create our questionController with our in memory datasource.
        questionController = new QuestionController(
                questionRepository,
                new AuthorizationHelper(
                        userRepository,
                        banRepository,
                        questionRepository
                ));

        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.10.0.1");
    }

    @Test
    void getQuestion() {
        // Create the question we expect to see.
        Question expected = new Question(1, "dummy", "This is a question", "", new Date(0), 0, false, false);

        // Get the question from the controller.
        Question actual = questionController.getQuestion(TEST_ROOM_ID,1, AUTHORIZATION, request).getBody();

        // Check to see if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void getAllQuestions() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "dummy", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "dummy", "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "dummy", "This is a question", "", new Date(0), 20, true, false));

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION, request);

        // Check if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void upvoteQuestion() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "dummy", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "dummy", "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "dummy", "This is a question", "", new Date(0), 21, true, false));

        // Upvote the question with id 3.
        questionController.upvoteQuestion(TEST_ROOM_ID, 3, AUTHORIZATION, request);

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION, request);

        // Check if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void editQuestion() {
        // Create the list of questions we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "dummy", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "dummy", "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "dummy", "This is updated question body.", "", new Date(0), 20, true, true));

        // Edit the question with id 3.
        questionController.editQuestion(TEST_ROOM_ID, 3, new QuestionText("This is updated question body."), AUTHORIZATION, request);

        // Get the list of questions left.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION, request);

        // Check if that list is the same as what we expect to see.
        assertEquals(expected, actual);
    }

    @Test
    void deleteQuestion() {
        // Create the list of questions we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "dummy", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(3, "dummy", "This is a question", "", new Date(0), 20, true, false));

        // Delete question with id 2.
        questionController.deleteQuestion(TEST_ROOM_ID, 2, AUTHORIZATION, request);

        // Get the list of question left.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION, request);

        // Check if that list is the same as what we expect to see.
        assertEquals(expected, actual);
    }

    @Test
    void newQuestion() {
        // Create a new question.
        questionController.newQuestion(TEST_ROOM_ID, new QuestionText("This is a new question"), AUTHORIZATION, request);

        String expected = "This is a new question";

        // Get the text of the new question from the controller.
        String actual = questionController.getQuestion(TEST_ROOM_ID, 4, AUTHORIZATION, request).getBody().getText();

        // Check if the question has te expected text.
        assertEquals(expected, actual);
    }

    @Test
    void answerQuestion() {
        // Answer question 1.
        questionController.answerQuestion(TEST_ROOM_ID, 1, new QuestionText("This an answer"), AUTHORIZATION, request);

        // Get the question with id 1.
        Question actual = questionController.getQuestion(TEST_ROOM_ID, 1, AUTHORIZATION, request).getBody();

        // Store the expected and actual answers.
        String expectedAnswer = "This an answer";
        String actualAnswer = actual.getAnswer();

        // Check if the answers are equal.
        assertEquals(expectedAnswer, actualAnswer);

        // Check if the question is marked as answered.
        assertTrue(actual.isAnswered());
    }
}