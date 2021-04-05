package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.common.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockserver.model.HttpRequest.request;

public class RoomServerCommunicationTest {
    private static MockServerConfigurations expectations;
    private static ClientAndServer mockServer;
    private static String path = "/api/v1/room/";
    private static String roomID = "TestRoomID";
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

    @BeforeAll
    static void setUpServer() {
        // initialize the expectation class
        expectations = new MockServerConfigurations();
        mockServer = ClientAndServer.startClientAndServer(8080);
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }

    @Test
    void createRoomWorks() {
        // mock the endpoint
        expectations.createExpectationCreateRoomWorks();

        NewRoom newRoom = new NewRoom("Test Room", "s", "m", new Date(0));

        Room expectedRoom = new Room("1", "s", "m", "Test Room", false, new Date(0));

        Room room = RoomServerCommunication.createRoom(newRoom);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/create")
                                .withBody(gson.toJson(newRoom)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedRoom, room);
    }

    @Test
    void createRoomWorksBadRequest() {
        // mock the endpoint
        expectations.createExpectationCreateRoomBadRequest();

        NewRoom newRoom = new NewRoom("Test Room", "s", "s", new Date(0));

        Room room = RoomServerCommunication.createRoom(newRoom);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/create")
                                .withBody(gson.toJson(newRoom)),
                        VerificationTimes.atLeast(1)
                );

        assertNull(room);
    }

    @Test
    void joinRoomWorks() {
        // mock the endpoint
        expectations.createExpectationJoinRoomWorks();

        RoomJoinRequest request = new RoomJoinRequest("s", "test");

        RoomJoinInfo expectedInfo = new RoomJoinInfo(roomID, "id", "name", "auth", "test", UserRole.STUDENT);
        RoomJoinInfo info = RoomServerCommunication.joinRoom(roomID, "s", "test");

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + roomID + "/join")
                                .withBody(gson.toJson(request)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedInfo, info);
    }

    @Test
    void joinRoomUnauthorized() {
        // mock the endpoint
        expectations.createExpectationJoinRoomUnauthorized();

        RoomJoinRequest request = new RoomJoinRequest("notModPass", "test");

        RoomJoinInfo info = RoomServerCommunication.joinRoom(roomID, "notModPass", "test");
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + roomID + "/join")
                                .withBody(gson.toJson(request)),
                        VerificationTimes.atLeast(1)
                );

        assertNull(info);
    }
}
