package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.Answer;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UpvoteRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.Config;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/room/{room_id}/question")
@Validated
public class QuestionController {

    private static final Logger logger = LogManager.getLogger("serverLog");
    private static final Logger eventLogger = LogManager.getLogger("eventLog");

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
    public ResponseEntity<Question> getQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                @PathVariable("id") @Positive int id,
                                                @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom()
            ));

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
        eventLogger.info("\"{}\" requested question with id \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    /**
     * Endpoint for retrieving all Questions.
     * @return A {@link ResponseEntity} containing a {@link List} of {@link Question}s containing every Question in the database
     *          and a Http Status of 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                          @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                          HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom()
            ));

        // Log questions request
        logger.debug("All questions requested.");
        eventLogger.info("\"{}\" requested all questions in room \"{}\"", request.getRemoteAddr(), roomId);

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
    public ResponseEntity<Void> upvoteQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                               @PathVariable("id") int id,
                                               @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                               HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom(),
                new IsStudent(),
                new NotBanned(),
                    new NotClosed()
            ));

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        // Try to upvote the question as the user.
        int rowsChanged = upvoteRepository.addUpvote(roomId, user.getId(), id);

        if (rowsChanged == 1) {

            eventLogger.info("\"{}\" upvoted question with id \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

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
    public ResponseEntity<Void> removeUpvoteQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                                     @PathVariable("id") int id,
                                                     @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                                     HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom(),
                new IsStudent(),
                new NotBanned(),
                    new NotClosed()
            ));

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        // Try to remove the upvote by the user.
        int rowsChanged = upvoteRepository.removeUpvote(roomId, user.getId(), id);

        if (rowsChanged == 1) {

            eventLogger.info("\"{}\" undid their upvote to question \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

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
    public ResponseEntity<Void> editQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                             @PathVariable("id") int id,
                                             @RequestBody @NotNull @Valid QuestionText question,
                                             @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                             HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
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
            ));

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);


        // Try to edit the question body and store the number of effected rows.
        int rowsChanged = questionRepository.editQuestionWithId(roomId, id, question.getText(), user.getId());

        if (rowsChanged == 1) {

            eventLogger.info("\"{}\" edited question with id \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

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
    public ResponseEntity<Void> deleteQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                               @PathVariable("id") int id,
                                               @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                               HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
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
            ));

        // Log the deletion request
        logger.debug("Question " + id + " is being deleted.");
        // Try to delete the question with id and store the amount of rows changed.
        int rowsChanged = questionRepository.deleteQuestionWithId(roomId, id);

        // Check if there was a question deleted.
        if (rowsChanged == 1) {

            eventLogger.info("\"{}\" deleted question with id \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

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
    public ResponseEntity<Void> newQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                            @RequestBody @NotNull @Valid QuestionText newQuestion,
                                            @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                            HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new BelongsToRoom(),
                new NotBanned(),
                    new NotClosed()
            ));

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        long lastQuestionAsked = questionRepository.timeSinceLastQuestionByUser(roomId, user.getId());
        if (Config.RATE_LIMIT > lastQuestionAsked) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Ratelimit-Expires", Long.toString(Config.RATE_LIMIT - lastQuestionAsked));
            return new ResponseEntity<>(headers, HttpStatus.TOO_MANY_REQUESTS);
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

        eventLogger.info("\"{}\" asked a new question in room \"{}\"", request.getRemoteAddr(), roomId);
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
    public ResponseEntity<Void> answerQuestion(@PathVariable("room_id") @NotNull @NotEmpty String roomId,
                                               @PathVariable int id,
                                               @RequestBody @NotNull @Valid Answer answer,
                                               @RequestHeader("Authorization") @Pattern(regexp = "Bearer [a-zA-Z0-9]{128}") String authorization,
                                               HttpServletRequest request) {

        authorizationHelper.checkAuthorization(
            roomId,
            authorization,
            request.getRemoteAddr(),
            new All(
                new IsModerator(),
                new NotBanned()
            ));

        if (answer.getAnswer() == null) {
            answer.setAnswer("");
        }

        // Log the answering of a question
        logger.debug("Question " + id + " is being answered");
        
        // Update the question with the answer and store the amount of rows changed in a variable.
        int rowsChanged = questionRepository.answerQuestionWithId(roomId, id, answer.getAnswer());

        // Check if there where no rows updated.
        if (rowsChanged == 0) {
            // If that is the case log a warning and respond with http status code 404 (NOT_FOUND).
            logger.debug("Question " + id + " was requested to be answered but could not be edited!");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        eventLogger.info("\"{}\" answered question with id \"{}\" in room \"{}\"", request.getRemoteAddr(), id, roomId);

        // Otherwise log a success and respond with http status code 200 (OK).
        logger.debug("Question " + id + " answered successfully.");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
