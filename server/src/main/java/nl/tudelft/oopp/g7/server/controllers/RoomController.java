package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.*;
import nl.tudelft.oopp.g7.server.repositories.*;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api/v1/room")
public class RoomController {

    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final SpeedRepository speedRepository;
    private final PollRepository pollRepository;
    private final AuthorizationHelper authorizationHelper;

    private static final int GENERATED_PASSWORD_LENGTH = 16;

    /**
     * The primary constructor for the RoomController .
     */
    public RoomController(RoomRepository roomRepository, UserRepository userRepository, SpeedRepository speedRepository, PollRepository pollRepository, AuthorizationHelper authorizationHelper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.speedRepository = speedRepository;
        this.pollRepository = pollRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @GetMapping("/coffee")
    public ResponseEntity<Void> brewCoffee() {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    /**
     * Endpoint to create a new Room.
     * @param newRoom The {@link NewRoom} object containing the information about the new Room.
     * @return A {@link ResponseEntity} containing the {@link Room} if successfully made and a {@link HttpStatus} that is one of
     *      OK (200), BAD_REQUEST (400), or INTERNAL_SERVER_ERROR (500).
     */
    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody NewRoom newRoom) {
        logger.debug("A new room with name \"{}\" is being made.", newRoom.getName());
        // Check if the room name is not empty.
        if (newRoom.getName() == null || newRoom.getName().equals("")) {
            // Inform the client that the request was malformed.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Check if the start date is set.
        if (newRoom.getStartDate() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Check if student password is null.
        if (newRoom.getStudentPassword() == null) {
            // If it is set it to an empty string.
            newRoom.setStudentPassword("");
        }

        String studentPassword = parseStudentPassword(newRoom.getStudentPassword());
        String moderatorPassword = parseModeratorPassword(newRoom.getModeratorPassword());

        if (studentPassword.equals(moderatorPassword)) {
            // Client provided the same password for students and moderators.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // Create a new room with the information provided.
        Room room = new Room(
                // Create a new room id that does not yet exist.
                roomRepository.createNewId(),
                // Use the student password supplied by the user and use only the first 32 characters.
                studentPassword,
                // Use the moderator password supplied or create our own if it is not provided and limit it to 32 characters.
                moderatorPassword,
                // Use the user supplied name for the room.
                newRoom.getName(),
                // Make the room closed by default.
                false,
                // Make the room not over by default.
                false,
                // Set the start date to the date provided.
                newRoom.getStartDate()
        );

        // Create the room in the database.
        int rowsChanged = roomRepository.createRoom(room);

        // Check if there was any other amount of rows changed then the expected 1.
        if (rowsChanged != 1) {
            // If that is the case log an error and inform the client about the error.
            logger.error("The room with id \"{}\" could not be written to the database!", room.getId());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Inform the client that the room has been created and send them the room information.
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    /**
     * Endpoint to join a Room.
     * @param roomId The id of the Room to join.
     * @param roomJoinRequest A {@link RoomJoinRequest} containing the information required to join the Room.
     * @return A {@link ResponseEntity} containing a {@link RoomJoinInfo} if it was found and a {@link HttpStatus} that is one
     *      of BAD_REQUEST (400), UNAUTHORIZED (401), NOT_FOUND (404), or OK (200).
     */
    @PostMapping("/{room_id}/join")
    public ResponseEntity<RoomJoinInfo> joinRoom(@PathVariable("room_id") String roomId,
                                                 @RequestBody RoomJoinRequest roomJoinRequest,
                                                 @RequestHeader("Authorization") String authorization,
                                                 HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Room room = roomRepository.getRoomById(roomId);

        // If there are no rooms with the id.
        if (room == null) {
            // Inform the client that the room does not exist.
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (room.getModeratorPassword().equals(roomJoinRequest.getPassword())) {
            User user = new User(userRepository.createNewId(), roomId, roomJoinRequest.getNickname(), request.getRemoteAddr(), authorizationHelper.createAuthorizationToken(), UserRole.MODERATOR);
            userRepository.storeUser(user);

            return new ResponseEntity<>(
                    new RoomJoinInfo(
                            room.getId(),
                            user.getId(),
                            room.getName(),
                            user.getAuthorizationToken(),
                            user.getNickname(),
                            user.getRole()),
                    HttpStatus.OK);
        }

        if (room.getStudentPassword().equals(roomJoinRequest.getPassword())) {
            User user = new User(userRepository.createNewId(), roomId, roomJoinRequest.getNickname(), request.getRemoteAddr(), authorizationHelper.createAuthorizationToken(), UserRole.STUDENT);
            userRepository.storeUser(user);

            return new ResponseEntity<>(
                    new RoomJoinInfo(
                            room.getId(),
                            user.getId(),
                            room.getName(),
                            user.getAuthorizationToken(),
                            user.getNickname(),
                            user.getRole()),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Endpoint to change the Speed of a Room.
     * @param roomId The id of the Room to edit the Speed of.
     * @param speedAlterRequest A request containing a Speed integer by which to edit the Speed.
     * @return A {@link ResponseEntity} containing a {@link HttpStatus} that is one of BAD_REQUEST (400),
     *      NOT_FOUND (404), INTERNAL_SERVER_ERROR (500) or OK (200)
     */
    @PostMapping("/{room_id}/speed")
    public ResponseEntity<Void> setRoomSpeed(@PathVariable("room_id") String roomId,
                                             @RequestBody SpeedAlterRequest speedAlterRequest,
                                             @RequestHeader("Authorization") String authorization,
                                             HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new NotBanned(),
                        new IsStudent(),
                        new BelongsToRoom()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        int speed = speedAlterRequest.getSpeed();

        if (speed < -1 || speed > 1) {
            logger.debug("Invalid speed request received with value \"{}\" for room \"{}\"", speedAlterRequest.getSpeed(), roomId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        int rowsChanged = speedRepository.setSpeedForUserInRoom(roomId, user.getId(), speed);

        // Check if there was any other amount of rows changed then the expected 1.
        if (rowsChanged != 1) {
            // If that is the case log an error and inform the client about the error.
            logger.debug("The speed of the room with id \"{}\" could not be edited", roomId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to reset the Speed of a Room.
     * @param roomId The id of the Room to reset the Speed of.
     * @return A {@link ResponseEntity} containing a {@link HttpStatus} that is one of
     *      UNAUTHORIZED (401) or OK (200)
     */
    @DeleteMapping("/{room_id}/speed")
    public ResponseEntity<Void> resetRoomSpeed(@PathVariable("room_id") String roomId,
                                                          @RequestHeader("Authorization") String authorization,
                                                          HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new IsModerator(),
                        new NotBanned()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        speedRepository.resetSpeedForRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to get the Speed of a Room.
     * @param roomId The id of the Room to edit the Speed of.
     * @return A {@link ResponseEntity} containing a {@link SpeedAlterRequest} with the Room's Speed and a
     *       {@link HttpStatus} that is one of TODO: INTERNAL_SERVER_ERROR (500), NOT_FOUND (404)
     *      or OK (200)
     */
    @GetMapping("/{room_id}/speed")
    public ResponseEntity<SpeedAlterRequest> getRoomSpeed(@PathVariable("room_id") String roomId,
                                                          @RequestHeader("Authorization") String authorization,
                                                          HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new IsModerator()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SpeedAlterRequest response = new SpeedAlterRequest(speedRepository.getSpeedForRoom(roomId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get the {@link PollInfo} for the room with roomId.
     * @param roomId The id of the room to retrieve the poll for.
     * @param authorization The authorization header.
     * @param request The {@link HttpServletRequest} associated with the Http request.
     * @return A {@link ResponseEntity} containing the {@link PollInfo} and a http status of OK (200) or null and a non
     *          OK http status.
     */
    @GetMapping("/{room_id}/poll")
    public ResponseEntity<PollInfo> getPoll(@PathVariable("room_id") String roomId,
                                            @RequestHeader("Authorization") String authorization,
                                            HttpServletRequest request) {
        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        PollInfo pollInfo = pollRepository.getPoll(roomId, -1,user.getRole() == UserRole.MODERATOR);

        if (pollInfo == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(pollInfo, HttpStatus.OK);
    }

    /**
     * Create a new poll in the room specified.
     * @param roomId The id of the room to create the poll in.
     * @param pollCreateRequest The {@link PollCreateRequest} to create the poll from.
     * @param authorization The authorization header.
     * @param request The {@link HttpServletRequest} associated with the Http request.
     * @return A {@link ResponseEntity} containing the http status code indicating whether the request completed
     *          successfully or if there was an error.
     */
    @PostMapping("/{room_id}/poll")
    public ResponseEntity<Void> createPoll(@PathVariable("room_id") String roomId,
                                               @RequestBody PollCreateRequest pollCreateRequest,
                                               @RequestHeader("Authorization") String authorization,
                                               HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Make sure the poll create request is all in order.
        if (pollCreateRequest == null ||
                pollCreateRequest.getQuestion() == null ||
                pollCreateRequest.getOptions() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new IsModerator(),
                        new NotBanned()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        pollRepository.createPoll(roomId, pollCreateRequest.getQuestion(), pollCreateRequest.isHasPublicResults(), pollCreateRequest.getOptions());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Change the answer given to the current poll.
     * @param roomId The id of the room to answer the poll in.
     * @param pollAnswerRequest The {@link PollAnswerRequest} to answer the poll from.
     * @param authorization The authorization header.
     * @param request The {@link HttpServletRequest} associated with the Http request.
     * @return A {@link ResponseEntity} containing the http status code indicating whether the request completed
     *          successfully or if there was an error.
     */
    @PostMapping("/{room_id}/poll/answer")
    public ResponseEntity<Void> answerPoll(@PathVariable("room_id") String roomId,
                                           @RequestBody PollAnswerRequest pollAnswerRequest,
                                           @RequestHeader("Authorization") String authorization,
                                           HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new NotBanned()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = authorizationHelper.getUserFromAuthorizationHeader(authorization);

        PollInfo mostRecentPoll = pollRepository.getPoll(roomId, -1, false);

        boolean found = false;

        for (PollOption option : mostRecentPoll.getOptions()) {
            if (option.getId() == pollAnswerRequest.getOptionId()) {
                found = true;
                break;
            }
        }

        if (!found) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        pollRepository.updateResult(roomId, user.getId(),mostRecentPoll.getId(), pollAnswerRequest.getOptionId());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * Close the currently running poll.
     * @param roomId The id of the room to close the poll in.
     * @param pollCloseRequest The {@link PollCloseRequest} to close the poll from.
     * @param authorization The authorization header.
     * @param request The {@link HttpServletRequest} associated with the Http request.
     * @return A {@link ResponseEntity} containing the http status code indicating whether the request completed
     *          successfully or if there was an error.
     */
    @DeleteMapping("/{room_id}/poll")
    public ResponseEntity<Void> closePoll(@PathVariable("room_id") String roomId,
                                          @RequestBody PollCloseRequest pollCloseRequest,
                                          @RequestHeader("Authorization") String authorization,
                                          HttpServletRequest request) {

        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authorizationHelper.isAuthorized(
                roomId,
                authorization,
                request.getRemoteAddr(),
                new All(
                        new BelongsToRoom(),
                        new NotBanned(),
                        new IsModerator()
                )
        )) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        PollInfo mostRecentPoll = pollRepository.getMostRecentPollInRoom(roomId);

        pollRepository.endPoll(roomId, mostRecentPoll.getId(), pollCloseRequest.isPublishResults());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * A helper method to parse the student password.
     * @param studentPassword The student password to parse.
     * @return The parsed student password.
     */
    private String parseStudentPassword(String studentPassword) {
        return studentPassword.substring(0, Math.min(
                studentPassword.length(),
                32
        ));
    }

    /**
     * A helper method to parse the moderator password.
     * @param moderatorPassword The moderator password to parse.
     * @return The parsed moderator password.
     */
    private String parseModeratorPassword(String moderatorPassword) {
        return createOrUsePassword(
                moderatorPassword.substring(0, Math.min(
                        moderatorPassword.length(),
                        32
                )));
    }

    /**
     * A helper method to generate a random password if none is given.
     * @param password The optional password.
     * @return Either password if it is set or a random string with length GENERATED_PASSWORD_LENGTH.
     */
    private String createOrUsePassword(String password) {
        if (password != null && !password.equals(""))
            return password;

        return new RandomString(GENERATED_PASSWORD_LENGTH).nextString();
    }
}
