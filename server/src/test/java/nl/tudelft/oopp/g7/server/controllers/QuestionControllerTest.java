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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionControllerTest {

    private DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private QuestionController questionController;

    @BeforeEach
    void setUp() throws IOException {
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
        jdbcTemplate = new JdbcTemplate(dataSource);

        String sqlQueries = Files.readString(Path.of(new File("src/test/resources/test-question.sql").getPath()));
        jdbcTemplate.execute(sqlQueries);

        questionController = new QuestionController(jdbcTemplate);
    }

    @Test
    void getQuestion() {
        Question expected = new Question(1, "This is a question", "", new Date(0), 0, false, false);
        Question actual = questionController.getQuestion(1).getBody();
        assertEquals(expected, actual);
    }

    @Test
    void getAllQuestions() {
        List<Question> expected = new ArrayList<>();
        expected.add(new Question(1, "This is a question", "", new Date(0), 0, false, false));
        expected.add(new Question(2, "This is a question", "This is an answer to the question", new Date(1614511580000L), 0, true, false));
        expected.add(new Question(3, "This is a question", "", new Date(0), 20, true, false));

        List<Question> actual = questionController.getAllQuestions();
        assertEquals(expected, actual);
    }

    @Test
    void newQuestion() {
        questionController.newQuestion(new NewQuestion("This is a new question"));
        String expected = "This is a new question";
        String actual = questionController.getQuestion(4).getBody().getText();

        assertEquals(expected, actual);
    }

    @Test
    void answerQuestion() {
        questionController.answerQuestion(1, new Answer("This an answer"));
        Question actual = questionController.getQuestion(1).getBody();
        String expectedAnswer = "This an answer";
        String actualAnswer = actual.getAnswer();

        assertEquals(expectedAnswer, actualAnswer);
        assertTrue(actual.isAnswered());
    }
}