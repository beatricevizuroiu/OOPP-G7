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
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
        while (!mockServer.hasStopped(3,100L, TimeUnit.MILLISECONDS)){}
    }

    @Test
    void retrieveAllQuestions() {
        // mock the endpoint
        expectations.createExpectationAll();

        LocalData.setSortingOrder(SortingOrder.UPVOTES);

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
        List<Question> expectedList = expectations.getQuestionList();
        expectedList = expectedList.stream().sorted(Comparator.comparing(Question::getUpvotes).reversed())
                .collect(Collectors.toList());

        assertEquals(expectedList, questionList);
    }

    @Test
    void answerQuestionWorks() {
        // mock the endpoint
        expectations.createExpectationAnswerQuestionWorks(1);

        Answer answer = new Answer("This is an answer.");

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
        // mock the endpoint
        expectations.createExpectationAnswerQuestionNotWorks();

        Answer answer = new Answer("This is an answer.");

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
    void banUser() {
        // mock the endpoint
        expectations.createExpectationBanUser(userID);

        BanReason banReason = new BanReason("");

        HttpResponse<String> response = ModeratorServerCommunication.banUser(roomID, userID, banReason);

        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath(path + "user/" + userID + "/ban")
                                .withBody(gson.toJson(new BanReason(""))),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }
}