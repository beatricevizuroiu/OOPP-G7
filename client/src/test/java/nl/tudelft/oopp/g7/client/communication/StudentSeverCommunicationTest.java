package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.SortingOrder;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockserver.model.HttpRequest.request;

/**
 * Testing suite for the server-client communication channel.
 * The server must be online for the tests to pass
 */
public class StudentSeverCommunicationTest {
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
        while (!mockServer.hasStopped(3,100L, TimeUnit.MILLISECONDS)){

        }
    }

    @Test
    void retrieveAllQuestions() {
        // mock the endpoint
        expectations.createExpectationAll();

        LocalData.setSortingOrder(SortingOrder.NEW);

        List<Question> questionList = StudentServerCommunication.retrieveAllQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        // get a sorted list
        List<Question> expectedList = expectations.getQuestionList();
        expectedList = expectedList.stream().sorted(Comparator.comparing(Question::getPostedAt).reversed())
                .collect(Collectors.toList());

        assertEquals(expectedList, questionList);
    }

    @Test
    void testAskQuestion() {
        // mock the endpoint
        expectations.createExpectationAskQuestion();

        QuestionText text = new QuestionText("New Question");

        HttpResponse<String> response = StudentServerCommunication.askQuestion(roomID, text);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/question/new")
                                .withBody(gson.toJson(text)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void testAskQuestionServerError() {
        // mock the endpoint
        expectations.createExpectationAskQuestionServerError();

        QuestionText text = new QuestionText("Problematic");

        HttpResponse<String> response = StudentServerCommunication.askQuestion(roomID, text);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/question/new")
                                .withBody(gson.toJson(text)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(500, response.statusCode());
    }
}
