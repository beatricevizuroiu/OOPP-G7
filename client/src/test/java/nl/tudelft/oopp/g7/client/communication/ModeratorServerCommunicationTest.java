package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.*;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;
import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockserver.model.HttpRequest.request;


public class ModeratorServerCommunicationTest {
    private static List<Question> questionList;
    private static MockServerConfigurations expectations;
    private static ClientAndServer mockServer;
    private static String userID = "TEST";
    private static String roomID = "TestRoomID";
    private static String path = "/api/v1/room/" + roomID + "/";
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

    @BeforeAll
    static void setUpServer() {
        // initialize the expectation class
        expectations = new MockServerConfigurations();
        mockServer = ClientAndServer.startClientAndServer(8080);

        questionList = expectations.getQuestionList();
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }

    @Test
    void retrieveAllQuestions() {
        LocalData.setSortingOrder(SortingOrder.UPVOTES);
        List<Question> expectedQuestionList = questionList.stream().filter(q -> !q.isAnswered()).collect(Collectors.toList());

        // mock the endpoint
        expectations.createExpectationWithResponseBody(path + "question/all", "GET", gson.toJson(expectedQuestionList), 200);

        List<Question> questionList = ModeratorServerCommunication.retrieveAllQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        // get a sorted list
        expectedQuestionList = expectedQuestionList.stream().sorted(Comparator.comparing(Question::getUpvotes).reversed())
                .collect(Collectors.toList());

        assertEquals(expectedQuestionList, questionList);
    }

    @Test
    void answerQuestionWorks() {
        Answer answer = new Answer("This is an answer.");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/" + 1 + "/answer", "POST", gson.toJson(answer), 200);

        HttpResponse<String> response = ModeratorServerCommunication.answerQuestion(roomID, 1, answer);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 1 + "/answer")
                                .withBody(gson.toJson(answer)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void answerQuestionsNotWorks() {
        Answer answer = new Answer("This is an answer.");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/" + 5 + "/answer", "POST", gson.toJson(answer), 404);

        HttpResponse<String> response = ModeratorServerCommunication.answerQuestion(roomID, 5, answer);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 5 + "/answer")
                                .withBody(gson.toJson(answer)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }

    @Test
    void markAsAnswered() {
        // mock the endpoint
        expectations.createExpectationWithoutBody(path + "question/" + 1 + "/answer", "POST", 200);

        HttpResponse<String> response = ModeratorServerCommunication.markAsAnswered(roomID, 1);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 1 + "/answer")
                                .withBody(""),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void banUser() {
        BanReason banReason = new BanReason("");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "user/" + userID + "/ban", "POST", gson.toJson(banReason), 200);

        HttpResponse<String> response = ModeratorServerCommunication.banUser(roomID, userID, banReason);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "user/" + userID + "/ban")
                                .withBody(gson.toJson(banReason)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void createPoll() {
        String[] options = new String[1];
        options[0] = "Option 1";

        PollCreateRequest pollCreateRequest = new PollCreateRequest("Question", options, true);

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "poll", "POST", gson.toJson(pollCreateRequest), 200);

        HttpResponse<String> response = ModeratorServerCommunication.createPoll(roomID, "Question", options, true);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/poll")
                                .withBody(gson.toJson(pollCreateRequest)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void closePoll() {
        PollCloseRequest pollCloseRequest = new PollCloseRequest(true);

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "poll/close", "POST", gson.toJson(pollCloseRequest), 200);

        HttpResponse<String> response = ModeratorServerCommunication.closePoll(roomID, true);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/poll/close"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }
}