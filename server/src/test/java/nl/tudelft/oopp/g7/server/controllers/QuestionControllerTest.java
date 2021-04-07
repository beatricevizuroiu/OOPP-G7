package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Answer;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UpvoteRepository;
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

//TODO: Fix SQL!

class QuestionControllerTest {

    private DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private QuestionController questionController;
    private MockHttpServletRequest request_stud;
    private MockHttpServletRequest request_mod;

    private final String TEST_ROOM_ID = "SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH";

    private final String AUTHORIZATION_STUDENT = "Bearer Ftqp8J5Ub8PcUO0qJDXGuAooXZZfzZrZbfb51pCeYWDchzf6wyuwtFNzYeEeacE7k82Xn7y6ue9KWxPmP0eENubnz3PMelle4i9NLKb0RiQiVCDK8xdDjuu1uacyHdTC";
    private final String AUTHORIZATION_MODERATOR = "Bearer Dm1J7ZsghOtyvFnbMEMWrJDlWHteOGx3rr60stqn405f4sdgPqsj8wO9lWcGkrNGCYf5yH9Y1efaMgnD32hUwaSi3Jsi1mdtXUBK2U7C2HdqdAPdnuUqih2ihmjMk5lG";
    private final String AUTHORIZATION_EMPTY = "";

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
        UpvoteRepository upvoteRepository = new UpvoteRepository(jdbcTemplate);

        // Create our questionController with our in memory datasource.
        questionController = new QuestionController(
                questionRepository,
                userRepository,
                new AuthorizationHelper(
                        userRepository,
                        banRepository,
                        questionRepository),
                upvoteRepository);

        request_stud = new MockHttpServletRequest();
        request_stud.setRemoteAddr("127.10.0.1");

        request_mod = new MockHttpServletRequest();
        request_mod.setRemoteAddr("127.11.0.1");
    }

    @Test
    void getQuestion() {
        // Create the question we expect to see.
        Question expected = new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false);

        // Get the question from the controller.
        Question actual = questionController.getQuestion(TEST_ROOM_ID,1, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check to see if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void getAllQuestions() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "This is an answer to the question", new Date(1614511580000L), 1, true, false));
        expected.add(new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 1, true, false));

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void upvoteQuestion() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "This is an answer to the question", new Date(1614511580000L), 1, true, false));
        expected.add(new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 2, true, false));

        // Upvote the question with id 3.
        questionController.upvoteQuestion(TEST_ROOM_ID, 3, AUTHORIZATION_STUDENT, request_stud);

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check if they are the same.
        assertEquals(expected, actual);
    }

    @Test
    void editQuestion() {
        // Create the list of questions we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "This is an answer to the question", new Date(1614511580000L), 1, true, false));
        expected.add(new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is updated question body.", "", new Date(0), 1, true, true));

        // Edit the question with id 3.
        questionController.editQuestion(TEST_ROOM_ID, 3, new QuestionText("This is updated question body."), AUTHORIZATION_STUDENT, request_stud);

        // Get the list of questions left.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check if that list is the same as what we expect to see.
        assertEquals(expected, actual);
    }

    @Test
    void deleteQuestion() {
        // Create the list of questions we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 1, true, false));

        // Delete question with id 2.
        questionController.deleteQuestion(TEST_ROOM_ID, 2, AUTHORIZATION_STUDENT, request_stud);

        // Get the list of question left.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check if that list is the same as what we expect to see.
        assertEquals(expected, actual);
    }

    @Test
    void newQuestion() {
        // Create a new question.
        questionController.newQuestion(TEST_ROOM_ID, new QuestionText("This is a new question"), AUTHORIZATION_STUDENT, request_stud);

        String expected = "This is a new question";

        // Get the text of the new question from the controller.
        String actual = questionController.getQuestion(TEST_ROOM_ID, 4, AUTHORIZATION_STUDENT, request_stud).getBody().getText();

        // Check if the question has te expected text.
        assertEquals(expected, actual);
    }

    @Test
    void answerQuestion() {
        // Answer question 1.
        questionController.answerQuestion(TEST_ROOM_ID, 1, new Answer("This an answer"), AUTHORIZATION_MODERATOR, request_mod);

        // Get the question with id 1.
        Question actual = questionController.getQuestion(TEST_ROOM_ID, 1, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Store the expected and actual answers.
        String expectedAnswer = "This an answer";
        String actualAnswer = actual.getAnswer();

        // Check if the answers are equal.
        assertEquals(expectedAnswer, actualAnswer);

        // Check if the question is marked as answered.
        assertTrue(actual.isAnswered());
    }

    @Test
    void removeUpvoteQuestion() {
        // Create the list of question we expect to have.
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 1, true, false));

        // Upvote the question with id 3.
        questionController.removeUpvoteQuestion(TEST_ROOM_ID, 2, AUTHORIZATION_STUDENT, request_stud);

        // Get all questions from the controller.
        List<Question> actual = questionController.getAllQuestions(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud).getBody();

        // Check if they are the same.
        assertEquals(expected, actual);
    }
}