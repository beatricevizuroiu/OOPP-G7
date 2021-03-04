package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Answer;
import nl.tudelft.oopp.g7.common.NewQuestion;
import nl.tudelft.oopp.g7.common.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("/api/v1/question")
public class QuestionController {

    //TODO: Probably want to move the database table creation somewhere else.
    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS questions("
            + "id serial PRIMARY KEY not NULL,"
            + "text text not NULL,"
            + "answer text DEFAULT '' not NULL,"
            + "postedAt timestamp with time zone DEFAULT NOW(),"
            + "upvotes int not NULL DEFAULT 0,"
            + "answered boolean DEFAULT FALSE not NUll,"
            + "edited boolean DEFAULT FALSE not NULL);";

    private static final String QUERY_SELECT_QUESTION_BY_ID = "SELECT * FROM questions WHERE id=?;";
    private static final String QUERY_SELECT_ALL_QUESTIONS = "SELECT * FROM questions";
    private static final String QUERY_CREATE_QUESTION = "INSERT INTO questions (text) VALUES (?)";
    private static final String QUERY_ANSWER_QUESTION = "UPDATE questions SET answer=?, answered=true WHERE id=?";
    private static final String QUERY_DELETE_QUESTION = "DELETE FROM questions WHERE id=?";

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
    public ResponseEntity<Question> getQuestion(@PathVariable("id") int id) {
        // Get the question with the id from the database.
        Question question = jdbcTemplate.query(QUERY_SELECT_QUESTION_BY_ID,
            // Set the first variable in the PreparedStatement to the id of the question being requested.
            (ps) -> ps.setInt(1, id),
            // Send the ResultSet to the Question class to create a Question instance from it.
            (rs) -> {
                return Question.fromResultSet(rs, false);
            });

        // Check if there was no question found.
        if (question == null) {
            // If there was not return http status code 404 (NOT_FOUND).
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Otherwise return the question with http status code 200 (OK).
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving all questions.
     * @return A {@link List} of {@link Question}s containing every question in the database.
     */
    @GetMapping("/all")
    public List<Question> getAllQuestions() {
        // Return every question in the database.
        return jdbcTemplate.query(QUERY_SELECT_ALL_QUESTIONS, (rs) -> {
            // Create a list to hold all questions.
            List<Question> questionList = new ArrayList<>();
            // Loop while the result set has entries.
            while (rs.next()) {
                // Create a new question from the result set and add it to the list of questions.
                questionList.add(Question.fromResultSet(rs, true));
            }
            // Return the list of questions.
            return questionList;
        });
    }

    /**
     * Endpoint to delete questions.
     * @param id The id of the question to delete.
     * @return A {@link ResponseEntity} containing NULL and a status code of 200 (OK) if a question was delete and 404
     *      (NOT_FOUND) if no question was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") int id) {
        // Try to delete the question with id and store the amount of rows changed.
        int rowsChanged = jdbcTemplate.update(QUERY_DELETE_QUESTION, (ps) -> ps.setInt(1, id));

        // Check if there was a question deleted.
        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            // Otherwise return http status code 404 (NOT_FOUND)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to create a new question.
     * @param newQuestion The {@link NewQuestion} object representing the new question.
     */
    @PostMapping("/new")
    public void newQuestion(@RequestBody NewQuestion newQuestion) {
        // Create a new question in the database.
        jdbcTemplate.update(QUERY_CREATE_QUESTION,
            // Set the first variable in the PreparedStatement to the text of the new question.
            (ps) -> ps.setString(1, newQuestion.getText())
        );
    }

    /**
     * Endpoint to answer a question.
     * @param id The id of the question being answered.
     * @param answer The {@link Answer} to the question.
     * @return A {@link ResponseEntity} containing NULL and a http status of 200 (OK) if a row is changed and 404 (NOT_FOUND) if no rows changed.
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<Void> answerQuestion(@PathVariable int id, @RequestBody Answer answer) {
        // Update the question with the answer and store the amount of rows changed in a variable.
        int rowsChanged = jdbcTemplate.update(QUERY_ANSWER_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the answer to the question.
                ps.setString(1, answer.getAnswer());
                // Set the second variable in the PreparedStatement to the id of the question to update.
                ps.setInt(2, id);
            });
        // Check if there where no rows updated.
        if (rowsChanged == 0) {
            // If that is the case respond with http status code 404 (NOT_FOUND).
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Otherwise respond with http status code 200 (OK).
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
