package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.PollAnswerRequest;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.SortingOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;

/**
 * Testing suite for the server-client communication channel.
 * The server must be online for the tests to pass
 */
public class StudentSeverCommunicationTest {
    private static List<Question> questionList;
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

        questionList = expectations.getQuestionList();
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }

    @Test
    void retrieveAllQuestions() {
        LocalData.setSortingOrder(SortingOrder.NEW);
        List<Question> expectedQuestionList = questionList.stream().filter(q -> !q.isAnswered()).collect(Collectors.toList());

        // mock the endpoint
        expectations.createExpectationWithResponseBody(path + "question/all", "GET", gson.toJson(expectedQuestionList), 200);


        List<Question> questionList = StudentServerCommunication.retrieveAllQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        expectedQuestionList = expectedQuestionList.stream().sorted(Comparator.comparing(Question::getPostedAt).reversed())
                .collect(Collectors.toList());

        assertEquals(expectedQuestionList, questionList);
    }

    @Test
    void testAskQuestion() {
        QuestionText text = new QuestionText("New Question");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/new", "POST", gson.toJson(text), 200);

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
        QuestionText text = new QuestionText("Problematic");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/new", "POST", gson.toJson(text), 500);

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

    @Test
    void answerPoll() {
        PollAnswerRequest pollAnswerRequest = new PollAnswerRequest(1);

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "poll/answer", "POST", gson.toJson(pollAnswerRequest), 200);

        HttpResponse<String> response = StudentServerCommunication.answerPoll(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/TestRoomID/poll/answer")
                                .withBody(gson.toJson(pollAnswerRequest)),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }
}
