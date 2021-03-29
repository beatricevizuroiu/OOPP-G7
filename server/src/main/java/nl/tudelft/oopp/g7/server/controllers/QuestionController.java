package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UpvoteRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController()
@RequestMapping("/api/v1/room/{room_id}/question")
public class QuestionController {

    Logger logger = LoggerFactory.getLogger(QuestionController.class);

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AuthorizationHelper authorizationHelper;
    private final UpvoteRepository upvoteRepository;

    /**
     * Primary constructor for the QuestionController.
     */
    public QuestionController(QuestionRepository questionRepository, UserRepository userRepository, AuthorizationHelper authorizationHelper, UpvoteRepository upvoteRepository) {
        this.questionRepository = questionRepository;
        this.authorizationHelper = authorizationHelper;
        this.userRepository = userRepository;
        this.upvoteRepository = upvoteRepository;

        // Log QuestionController construction
        logger.trace("Constructed QuestionController");
    }

    /**
     * Endpoint for retrieving a question by ID.
     * @param id The ID of the question that should be retrieved.
     * @return A {@link ResponseEntity} containing a {@link Question} and a http status of 200 (OK)
     *         if there is no question found it will return an empty {@link ResponseEntity} with Http Status 404 (NOT_FOUND).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Log question request
        logger.debug("Question with id {} in room with id {} requested", id, roomId);
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
     * Endpoint for retrieving all Questions.
     * @return A {@link ResponseEntity} containing a {@link List} of {@link Question}s containing every Question in the database
     *          and a Http Status of 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions(@PathVariable("room_id") String roomId, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Log questions request
        logger.debug("All questions requested.");
        // Return every question in the database.
        return new ResponseEntity<>(questionRepository.getAllQuestionsInRoom(roomId), HttpStatus.OK);
    }

    /**
     * Endpoint for upvoting a Question.
     * @param id The id of the Question that will be upvoted.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was upvoted, 401 (BAD_REQUEST)
     *       if not all requirements for upvoting are met and 404 (NOT_FOUND) if no Question was upvoted.
     */
    @PostMapping("/{id}/upvote")
    public ResponseEntity<Void> upvoteQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new IsStudent(),
                        new NotBanned()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Try to increase the upvote number by 1 and store the number of effected rows.
        int rowsChanged = upvoteRepository.addUpvote(roomId, user.getId(), id);

        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Otherwise return http status code 404 (NOT_FOUND)
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint for removing an Upvote from a Question.
     * @param id The id of the Question that will be un-upvoted.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was un-upvoted, 401 (BAD_REQUEST)
     *       if not all requirements for un-upvoting are met and 404 (NOT_FOUND) if no Question was un-upvoted.
     */
    @DeleteMapping("/{id}/upvote")
    public ResponseEntity<Void> removeUpvoteQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new IsStudent(),
                        new NotBanned()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        // Try to increase the upvote number by 1 and store the number of effected rows.
        int rowsChanged = upvoteRepository.removeUpvote(roomId, user.getId(), id);

        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Otherwise return http status code 404 (NOT_FOUND)
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint to edit questions.
     * @param id The id of the Question to edit.
     * @param question The {@link QuestionText} that will replace the old one.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was edited and 404
     *       (NOT_FOUND) if no Question was edited.
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> editQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestBody QuestionText question, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new NotBanned(),
                        new OneOf(
                                new IsModerator(),
                                new OwnsQuestion(id)
                        )
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        // Try to edit the question body and store the number of effected rows.
        int rowsChanged = questionRepository.editQuestionWithId(roomId, id, question.getText());

        if (rowsChanged == 1) {
            // If there was return http status code 200 (OK)
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Otherwise return http status code 404 (NOT_FOUND)
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Endpoint to delete Questions.
     * @param id The id of the Question to delete.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was deleted and 404
     *      (NOT_FOUND) if no Question was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("room_id") String roomId, @PathVariable("id") int id, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new NotBanned(),
                        new OneOf(
                                new IsModerator(),
                                new OwnsQuestion(id)
                        )
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Log the deletion request
        logger.debug("Question " + id + " is being deleted.");
        // Try to delete the question with id and store the amount of rows changed.
        int rowsChanged = questionRepository.deleteQuestionWithId(roomId, id);

        // Check if there was a question deleted.
        if (rowsChanged == 1) {
            // If there was log the deletion and return http status code 200 (OK)
            logger.info("Question " + id + " has successfully been deleted.");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        // Otherwise log a warning and return http status code 404 (NOT_FOUND)
        logger.debug("Question " + id + " was requested to be deleted but does not exist!");
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    /**
     * Endpoint to create a new Question.
     * @param newQuestion The {@link QuestionText} object representing the new question.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was made and 500
     *       (INTERNAL_SERVER_ERROR) if no new Question could be made.
     */
    @PostMapping("/new")
    public ResponseEntity<Void> newQuestion(@PathVariable("room_id") String roomId, @RequestBody QuestionText newQuestion, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new NotBanned()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        if (user == null || newQuestion == null || newQuestion.getText() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Log the creation of a question
        logger.debug("A new question is being made.");

        // Create a new question in the database.
        int rowsChanged = questionRepository.createQuestion(roomId, newQuestion.getText(), user.getId());

        // Check whether the question was successfully created and log the result
        if (rowsChanged == 0) {
            logger.debug("A question was attempted to be made but it failed!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("A question was successfully made.");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to answer a Question.
     * @param id The id of the Question being answered.
     * @param answer The {@link QuestionText} to the Question.
     * @return A {@link ResponseEntity} containing a Http Status of 200 (OK) if a Question was answered and 404 (NOT_FOUND)
     *       if no Question could be edited.
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<Void> answerQuestion(@PathVariable("room_id") String roomId, @PathVariable int id, @RequestBody QuestionText answer, @RequestHeader("Authorization") String authorization, HttpServletRequest request) {
        if (roomId == null || roomId.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new IsModerator(),
                        new NotBanned()
                ))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (answer.getText() == null) {
            answer.setText("");
        }

        // Log the answering of a question
        logger.debug("Question " + id + " is being answered");
        
        // Update the question with the answer and store the amount of rows changed in a variable.
        int rowsChanged = questionRepository.answerQuestionWithId(roomId, id, answer.getText());

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
