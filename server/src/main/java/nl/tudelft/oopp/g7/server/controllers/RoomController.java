package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.*;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/room")
public class RoomController {

    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomRepository roomRepository;

    private static final int GENERATED_PASSWORD_LENGTH = 16;

    public RoomController(JdbcTemplate jdbcTemplate) {
        this.roomRepository = new RoomRepository(jdbcTemplate);
    }

    /**
     * Endpoint to create a new room.
     * @param newRoom The {@link NewRoom} object containing the information about the new room.
     * @return A {@link ResponseEntity} containing the {@link Room} or null and a {@link HttpStatus} that is one of
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
     * Endpoint to join a room.
     * @param roomId The id of the room to join.
     * @param roomJoinRequest A {@link RoomJoinRequest} containing the information required to join the room.
     * @return A {@link ResponseEntity} containing a {@link RoomJoinInfo} or null and a {@link HttpStatus} that is one
     *      of BAD_REQUEST (400), UNAUTHORIZED (401), NOT_FOUND (404), or OK (200).
     */
    @PostMapping("/{room_id}/join")
    public ResponseEntity<RoomJoinInfo> joinRoom(@PathVariable("room_id") String roomId, @RequestBody RoomJoinRequest roomJoinRequest) {
        // Check if the room id field is set.
        if (roomId == null || roomId.equals("")) {
            // Inform that client that they did something wrong.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Room room = roomRepository.getRoomById(roomId);

        // If there are no rooms with the id.
        if (room == null) {
            // Inform the client that the room does not exist.
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (room.getModeratorPassword().equals(roomJoinRequest.getPassword())) {
            return new ResponseEntity<>(
                    new RoomJoinInfo(
                            room.getId(),
                            room.getName(),
                            // TODO: User authorization.
                            "",
                            UserRole.MODERATOR),
                    HttpStatus.OK);
        }

        if (room.getStudentPassword().equals(roomJoinRequest.getPassword())) {
            return new ResponseEntity<>(
                    new RoomJoinInfo(
                            room.getId(),
                            room.getName(),
                            // TODO: User authorization.
                            "",
                            UserRole.STUDENT),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Endpoint to change the speed of a room.
     * @param roomId The id of the room to edit the speed of.
     * @param speedAlterRequest A request containing a speed integer by which to edit the speed.
     * @return A {@link ResponseEntity} containing a {@link HttpStatus} that is one of BAD_REQUEST (400),
     *      NOT_FOUND (404), INTERNAL_SERVER_ERROR (500) or OK (200)
     */
    @PostMapping("/{room_id}/speed")
    public ResponseEntity<Void> setRoomSpeed(@PathVariable("room_id") String roomId, @RequestBody SpeedAlterRequest speedAlterRequest) {

        if (speedAlterRequest.getSpeed() != -1 && speedAlterRequest.getSpeed() != 1) {
            logger.error("Invalid speed request received with value \"{}\" for room \"{}\"", speedAlterRequest.getSpeed(), roomId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        int rowsChanged = roomRepository.editSpeedById(roomId, speedAlterRequest);

        // Check if there was any other amount of rows changed then the expected 1.
        if (rowsChanged != 1) {
            // If that is the case log an error and inform the client about the error.
            logger.error("The speed of the room with id \"{}\" could not be edited", roomId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Endpoint to get the speed of a room.
     * @param roomId The id of the room to edit the speed of.
     * @return A {@link ResponseEntity} containing a {@link SpeedAlterRequest} and a {@link HttpStatus} that is one of
     *      TODO: INTERNAL_SERVER_ERROR (500), NOT_FOUND (404)
     *      or OK (200)
     */
    @GetMapping("/{room_id}/speed")
    public ResponseEntity<SpeedAlterRequest> getRoomSpeed(@PathVariable("room_id") String roomId) {
        SpeedAlterRequest response = roomRepository.getSpeedById(roomId);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
