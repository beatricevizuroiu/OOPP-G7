package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.SpeedAlterRequest;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;

public class ServerCommunicationTest {
    private static MockServerConfigurations expectations;
    private static ClientAndServer mockServer;
    private static String roomID = "TestRoomID";
    private static String path = "/api/v1/room/" + roomID + "/";
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
        while (!mockServer.hasStopped(3,100L, TimeUnit.MILLISECONDS)){}
    }

    @Test
    void testRetrieveAllQuestions() {
        // mock the /all endpoint
        expectations.createExpectationAll();

        // retrieve the list of questions
        List<Question> questionList = ServerCommunication.retrieveAllQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        // get the actual question list and check
        assertEquals(expectations.getQuestionList(), questionList);
    }

    @Test
    void testRetrieveQuestionByID() {
        // mock the endpoint
        expectations.createExpectationQuestionID(1);

        // retrieve all answered questions
        Question question = ServerCommunication.retrieveQuestionById(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/" + 1),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectations.getQuestion(), question);
    }
    
    @Test
    void testRetrieveQuestionByIDNotWorks() {
        // mock the endpoint
        expectations.createExpectationQuestionIDNotWorks();

        // retrieve all answered questions
        Question question = ServerCommunication.retrieveQuestionById(roomID, 5);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/" + 5),
                        VerificationTimes.atLeast(1)
                );

        assertNull(question);
     }


    @Test
    void testRetrieveAllAnsweredQuestions() {
        // mock the endpoint
        expectations.createExpectationAll();

        List<Question> questionList = ServerCommunication.retrieveAllAnsweredQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectations.getQuestionWithAnswer(), questionList.get(0));
    }

    @Test
    void testUpvoteQuestion() {
        // mock the endpoint
        expectations.createExpectationUpvoteWorks();

        HttpResponse<String> response = ServerCommunication.upvoteQuestion(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/question/1/upvote"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void testUpvoteQuestionNotWorks() {
        // mock the endpoint
        expectations.createExpectationUpvoteNotWorks();

        HttpResponse<String> response = ServerCommunication.upvoteQuestion(roomID, 2);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/question/2/upvote"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }

    @Test
    void testEditQuestionWorks() {
        // mock the endpoint
        expectations.createExpectationEditQuestionWorks(1);

        // create the edited body
        QuestionText text = new QuestionText("Edited text");

        HttpResponse<String> response = ServerCommunication.editQuestion(roomID, 1, text);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 1)
                                .withBody(gson.toJson(text)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void testEditQuestionNotWorks() {
        // mock the endpoint
        expectations.createExpectationEditQuestionNotWorks();

        // create the edited body
        QuestionText text = new QuestionText("Edited text");

        HttpResponse<String> response = ServerCommunication.editQuestion(roomID, 5, text);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 5)
                                .withBody(gson.toJson(text)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }


    @Test
    void deleteQuestionWorks() {
        // mock the endpoint
        expectations.createExpectationDeleteQuestionWorks(1);

        HttpResponse<String> response = ServerCommunication.deleteQuestion(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("DELETE")
                                .withPath("/api/v1/room/TestRoomID/question/1"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteQuestionNotWorks() {
        // mock the endpoint
        expectations.createExpectationDeleteQuestionNotWorks();

        HttpResponse<String> response = ServerCommunication.deleteQuestion(roomID, 5);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("DELETE")
                                .withPath("/api/v1/room/TestRoomID/question/5"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }

    @Test
    void getSpeed() {
        // mock the endpoint
        expectations.createExpectationGetSpeed();

        int speed = ServerCommunication.getSpeed(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/speed"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(1, speed);
    }

    @Test
    void setSpeedWorks() {
        // mock the endpoint
        expectations.createExpectationSetSpeedWorks();

        SpeedAlterRequest request = new SpeedAlterRequest(1);

        HttpResponse<String> response = ServerCommunication.setSpeed(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/speed")
                                .withBody(gson.toJson(request)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void setSpeedBadRequest() {
        // mock the endpoint
        expectations.createExpectationSetSpeedBadRequest();

        SpeedAlterRequest request = new SpeedAlterRequest(5);

        HttpResponse<String> response = ServerCommunication.setSpeed(roomID, 5);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/speed")
                                .withBody(gson.toJson(request)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }
}
