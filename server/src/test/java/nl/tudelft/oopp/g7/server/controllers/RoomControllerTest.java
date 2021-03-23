package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class RoomControllerTest {
    private DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private RoomController roomController;

    private final String TEST_ROOM_ID = "SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH";
    private final String TEST_ROOM_NAME = "Test room";
    private final String TEST_ROOM_STUDENT_PASSWORD = "";
    private final String TEST_ROOM_MODERATOR_PASSWORD = "NQj7RWvT4yQKUJsE";

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

        // Create our questionController with our in memory datasource.
        roomController = new RoomController(jdbcTemplate);
    }

    @Test
    void createRoom() {
        ResponseEntity<Room> response = roomController.createRoom(new NewRoom("This room is a test", "", "Sup3rS3cr3tP4ssw0rd", new Date(10000)));

        // Check if the request completed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Room actual = response.getBody();

        // Check if actual is not null.
        assertNotNull(actual);

        // Check if the values are as expected.
        assertEquals("This room is a test", actual.getName());
        assertEquals("", actual.getStudentPassword());
        assertEquals("Sup3rS3cr3tP4ssw0rd", actual.getModeratorPassword());
        assertEquals(new Date(10000), actual.getStartDate());
    }

    @Test
    void joinRoomStudent() {
        ResponseEntity<RoomJoinInfo> response = roomController.joinRoom(TEST_ROOM_ID, new RoomJoinRequest(TEST_ROOM_STUDENT_PASSWORD));

        // Check if the request completed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        RoomJoinInfo actual = response.getBody();

        // Check if the actual is not null.
        assertNotNull(actual);

        // Check if the values are as expected.
        assertEquals(TEST_ROOM_ID, actual.getRoomId());
        assertEquals(TEST_ROOM_NAME, actual.getRoomName());
        assertEquals(UserRole.STUDENT, actual.getRole());
    }

    @Test
    void joinRoomModerator() {
        ResponseEntity<RoomJoinInfo> response = roomController.joinRoom(TEST_ROOM_ID, new RoomJoinRequest(TEST_ROOM_MODERATOR_PASSWORD));

        // Check if the request completed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        RoomJoinInfo actual = response.getBody();

        // Check if the actual is not null.
        assertNotNull(actual);

        // Check if the values are as expected.
        assertEquals(TEST_ROOM_ID, actual.getRoomId());
        assertEquals(TEST_ROOM_NAME, actual.getRoomName());
        assertEquals(UserRole.MODERATOR, actual.getRole());
    }

    @Test
    void speedUpTest() {
        ResponseEntity<Void> response = roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(1));

        // Check if the request completed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void speedDownTest() {
        ResponseEntity<Void> response = roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(-1));

        // Check if the request completed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void badSpeedRequest() {
        ResponseEntity<Void> response = roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(2));

        // Check if the request failed successfully.
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getSpeedTest() {
        ResponseEntity<SpeedAlterRequest> response = roomController.getRoomSpeed(TEST_ROOM_ID);

        assertEquals(response.getBody().getSpeed(), 0);

        roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(1));
        response = roomController.getRoomSpeed(TEST_ROOM_ID);

        assertEquals(response.getBody().getSpeed(), 1);

        roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(-1));
        roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(-1));
        roomController.setRoomSpeed(TEST_ROOM_ID, new SpeedAlterRequest(-1));
        response = roomController.getRoomSpeed((TEST_ROOM_ID));

        assertEquals(response.getBody().getSpeed(), -2);
    }
}
