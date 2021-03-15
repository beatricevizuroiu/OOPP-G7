package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/room")
public class RoomController {

    Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomRepository roomRepository;

    private static final int GENERATED_PASSWORD_LENGTH = 16;

    public RoomController(JdbcTemplate jdbcTemplate) {
        this.roomRepository = new RoomRepository(jdbcTemplate);
    }

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

        // Create a new room with the information provided.
        Room room = new Room(
                // Create a new room id that does not yet exist.
                roomRepository.createNewId(),
                // Use the student password supplied by the user and use only the first 32 characters.
                newRoom.getStudentPassword()
                        .substring(
                                0,
                                Math.min(
                                        newRoom.getStudentPassword().length(),
                                        32
                                )),
                // Use the moderator password supplied or create our own if it is not provided and limit it to 32 characters.
                createOrUsePassword(
                        newRoom.getModeratorPassword()
                                .substring(
                                        0,
                                        Math.min(
                                                newRoom.getModeratorPassword().length(),
                                                32
                                        ))),
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

    private String createOrUsePassword(String password) {
        if (password != null && !password.equals(""))
            return password;

        return new RandomString(GENERATED_PASSWORD_LENGTH).nextString();
    }
}
