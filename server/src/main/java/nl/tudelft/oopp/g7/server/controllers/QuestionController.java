package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController()
@RequestMapping("/api/v1/room/{room_id}/question")
public class QuestionController {

    Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionRepository questionRepository;

    /**
     * Construct the database table if not yet present.
     * @param jdbcTemplate The SQL query for creating the table(s) that should be made.
     */
    public QuestionController(JdbcTemplate jdbcTemplate) {
        this.questionRepository = new QuestionRepository(jdbcTemplate);

        // Log QuestionController construction
        logger.trace("Constructed QuestionController");
    }

    /**
     * Endpoint for retrieving a question by ID.
     * @param id The ID of the question that should be retrieved.
     * @return A {@link ResponseEntity} containing a {@link Question} and a http status of 200 (OK)
     *         if there is no question found it will return an empty {@link ResponseEntity} with http status 404 (NOT_FOUND).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id) {
        // Log question request
        logger.debug("Question with id " + id + " requested");
        // Get the question with the id from the database.
        Question question = questionRepository.getQuestionById(roomId, id);

        // Check if there was no question found.
        if (question == null) {
            // If there was not log a warning and return http status code 404 (NOT_FOUND).
            logger.debug("Question " + id + " was requested but does not exist!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Otherwise log a success and return the question with http status code 200 (OK).
        logger.debug("Question " + id + " successfully returned.");
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving all questions.
     * @return A {@link List} of {@link Question}s containing every question in the database.
     */
    @GetMapping("/all")
    public List<Question> getAllQuestions(@PathVariable("room_id") String roomId) {
        // Log questions request
        logger.debug("All questions requested.");
        // Return every question in the database.
        return questionRepository.getAllQuestionsInRoom(roomId);
    }

    /**
     * Endpoint for upvoting a question.
     * @param id The id of the question that will be upvoted.
     * @return A {@link ResponseEntity} containing NULL and a status code of 200 (OK) if a question was upvoted and 404
     *       (NOT_FOUND) if no question was upvoted.
     */
    @PutMapping("/{id}/upvote")
    public ResponseEntity<Void> upvoteQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id) {
        // Try to increase the upvote number by 1 and store the number of effected rows.
        int rowsChanged = questionRepository.upvoteQuestionWithId(roomId, id);

        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            // Otherwise return http status code 404 (NOT_FOUND)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to edit questions.
     * @param id The id of the question to edit.
     * @param question The {@link QuestionText} that will replace the old one.
     * @return A {@link ResponseEntity} containing NULL and a status code of 200 (OK) if a question was edited and 404
     *       (NOT_FOUND) if no question was edited.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> editQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestBody QuestionText question) {
        // Try to edit the question body and store the number of effected rows.
        int rowsChanged = questionRepository.editQuestionWithId(roomId, id, question);

        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            // Otherwise return http status code 404 (NOT_FOUND)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to delete questions.
     * @param id The id of the question to delete.
     * @return A {@link ResponseEntity} containing NULL and a status code of 200 (OK) if a question was delete and 404
     *      (NOT_FOUND) if no question was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id) {
        // Log the deletion request
        logger.debug("Question " + id + " is being deleted.");
        // Try to delete the question with id and store the amount of rows changed.
        int rowsChanged = questionRepository.deleteQuestionWithId(roomId, id);

        // Check if there was a question deleted.
        if (rowsChanged == 1) {
            // If there was log the deletion and return http status code 200 (OK)
            logger.info("Question " + id + " has successfully been deleted.");
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            // Otherwise log a warning and return http status code 404 (NOT_FOUND)
            logger.debug("Question " + id + " was requested to be deleted but does not exist!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to create a new question.
     * @param newQuestion The {@link QuestionText} object representing the new question.
     */
    @PostMapping("/new")
    public void newQuestion(@PathVariable("room_id") String roomId, @RequestBody QuestionText newQuestion) {
        // Log the creation of a question
        logger.debug("A new question is being made.");

        // Create a new question in the database.
        int rowsChanged = questionRepository.createQuestion(roomId, newQuestion);

        // Check whether the question was successfully created and log the result
        if (rowsChanged == 0) {
            logger.debug("A question was attempted to be made but it failed!");
        } else {
            logger.info("A question was successfully made.");
        }
    }

    /**
     * Endpoint to answer a question.
     * @param id The id of the question being answered.
     * @param answer The {@link QuestionText} to the question.
     * @return A {@link ResponseEntity} containing NULL and a http status of 200 (OK) if a row is changed and 404 (NOT_FOUND) if no rows changed.
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<Void> answerQuestion(@PathVariable("room_id") String roomId, @PathVariable int id, @RequestBody QuestionText answer) {
        // Log the answering of a question
        logger.debug("Question " + id + " is being answered");
        
        // Update the question with the answer and store the amount of rows changed in a variable.
        int rowsChanged = questionRepository.answerQuestionWithId(roomId, id, answer);

        // Check if there where no rows updated.
        if (rowsChanged == 0) {
            // If that is the case log a warning and respond with http status code 404 (NOT_FOUND).
            logger.debug("Question " + id + " was requested to be answered but could not be edited!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Otherwise log a success and respond with http status code 200 (OK).
        logger.debug("Question " + id + " answered successfully.");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
