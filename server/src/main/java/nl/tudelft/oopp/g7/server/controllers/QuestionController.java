package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Answer;
import nl.tudelft.oopp.g7.common.NewQuestion;
import nl.tudelft.oopp.g7.common.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller()
@RequestMapping("/api/v1/question")
public class QuestionController {

    //TODO: Probably want to move the database table creation somewhere else.
    //TODO: Once switched to postgresql make postedAt default to the current date.
    private static final String QUERY_CREATE_TABLE = "CREATE TABLE QUESTIONS("
            + "id int PRIMARY KEY AUTO_INCREMENT not NULL,"
            + "text text not NULL,"
            + "answer text DEFAULT '' not NULL,"
            + "postedAt date," // DEFAULT GETDATE()
            + "upvotes int not NULL DEFAULT 0,"
            + "answered boolean DEFAULT FALSE not NUll,"
            + "edited boolean DEFAULT FALSE not NULL);";

    private final JdbcTemplate jdbcTemplate;

    public QuestionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute(QUERY_CREATE_TABLE);
    }

    /**
     * Endpoint for retrieving a question by ID.
     * @param id The ID of the question that should be retrieved.
     * @return A {@link ResponseEntity} containing a {@link Question} and a http status of 200 (OK)
     *         if there is no question found it will return an empty {@link ResponseEntity} with http status 404 (NOT_FOUND).
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Question> getQuestion(@PathVariable("id") int id) {
        Question question = jdbcTemplate.query("SELECT * FROM questions WHERE id=" + id, (rs) -> {
            return Question.fromResultSet(rs, false);
        });
        if (question == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving all questions.
     * @return A {@link List} of {@link Question}s containing every question in the database.
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Question> getAllQuestions() {
        return jdbcTemplate.query("SELECT * FROM questions", (rs) -> {
            List<Question> questionList = new ArrayList<>();
            while (rs.next()) {
                questionList.add(Question.fromResultSet(rs, true));
            }
            return questionList;
        });
    }

    /**
     * Endpoint to create a new question.
     * @param newQuestion The {@link NewQuestion} object representing the new question.
     */
    @PostMapping("/new")
    @ResponseBody
    public void newQuestion(@RequestBody NewQuestion newQuestion) {
        jdbcTemplate.update(String.format("INSERT INTO questions (text) VALUES ('%s')", newQuestion.getText()));
    }

    /**
     * Endpoint to answer a question.
     * @param id The id of the question being answered.
     * @param answer The {@link Answer} to the question.
     * @return A {@link ResponseEntity} containing NULL and a http status of 200 (OK) if a row is changed and 404 (NOT_FOUND) if no rows changed.
     */
    @PostMapping("/{id}/answer")
    @ResponseBody
    public ResponseEntity<Void> answerQuestion(@PathVariable String id, @RequestBody Answer answer) {
        int rowsChanged = jdbcTemplate.update(String.format("UPDATE questions SET answer='%s', answered=true WHERE id=%s", answer.getAnswer(), id));
        if (rowsChanged == 0)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
