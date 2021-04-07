package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.common.*;
import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;

public class ServerCommunicationTest {
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
    void testRetrieveAllQuestions() {
        // mock the /all endpoint
        expectations.createExpectationWithResponseBody(path + "question/all", "GET", gson.toJson(questionList), 200);

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
        expectations.createExpectationWithResponseBody(path + "question/" + 1, "GET", gson.toJson(expectations.getQuestion()), 200);

        // retrieve the question
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
        expectations.createExpectationWithResponseBody(path + "question/" + 5, "GET", "", 404);

        // retrieve the question
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
    void testRetrieveAllUsers() {
        // create the expected list
        List<UserInfo> expectedUserInfoList = new ArrayList<>();
        expectedUserInfoList.add(new UserInfo("1", "TestRoomID", "test", UserRole.STUDENT));

        // mock the endpoint
        expectations.createExpectationWithResponseBody(path + "user/all", "GET", gson.toJson(expectedUserInfoList), 200);

        // retrieve all user information
        List<UserInfo> userInfoList = ServerCommunication.retrieveAllUsers(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/user/all"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedUserInfoList, userInfoList);
    }

    @Test
    void testRetrieveUserByID() {
        // create the expected list
        UserInfo expectedUserInfo = new UserInfo("1", "TestRoomID", "test", UserRole.STUDENT);

        // mock the endpoint
        expectations.createExpectationWithResponseBody(path + "user/" + 1, "GET", gson.toJson(expectedUserInfo), 200);

        // retrieve all user information
        UserInfo userInfo = ServerCommunication.retrieveUserById(roomID, "1");

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/user/1"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedUserInfo, userInfo);
    }

    @Test
    void testRetrieveAllAnsweredQuestions() {
        // mock the /all endpoint
        expectations.createExpectationWithResponseBody(path + "question/all", "GET", gson.toJson(questionList), 200);

        List<Question> expectedQuestionList = questionList.stream().filter(Question::isAnswered).collect(Collectors.toList());
        List<Question> questionList = ServerCommunication.retrieveAllAnsweredQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedQuestionList, questionList);
    }

    @Test
    void testRetrieveAllUnansweredQuestions() {
        // mock the /all endpoint
        expectations.createExpectationWithResponseBody(path + "question/all", "GET", gson.toJson(questionList), 200);

        List<Question> expectedQuestionList = questionList.stream().filter(q -> !q.isAnswered()).collect(Collectors.toList());
        List<Question> questionList = ServerCommunication.retrieveAllUnansweredQuestions(roomID);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("GET")
                                .withPath("/api/v1/room/TestRoomID/question/all"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(expectedQuestionList, questionList);
    }

    @Test
    void testUpvoteQuestion() {
        // mock the endpoint
        expectations.createExpectationWithoutBody(path + "question/" + 1 + "/upvote", "POST", 200);

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
        expectations.createExpectationWithoutBody(path + "question/" + 2 + "/upvote", "POST", 404);

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
        // create the edited body
        QuestionText text = new QuestionText("Edited text");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/" + 1, "POST", gson.toJson(text), 200);

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
        // create the edited body
        QuestionText text = new QuestionText("Edited text");

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "question/" + 5, "POST", gson.toJson(text), 404);

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
        expectations.createExpectationWithoutBody(path + "question/" + 1, "DELETE",  200);

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
        expectations.createExpectationWithoutBody(path + "question/" + 5, "DELETE",  404);

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
        SpeedAlterRequest response = new SpeedAlterRequest(1);

        // mock the endpoint
        expectations.createExpectationWithResponseBody(path + "speed", "GET", gson.toJson(response), 200);

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
        SpeedAlterRequest request = new SpeedAlterRequest(1);

        // mock the endpoint
        expectations.createExpectationWithRequestBody(path + "speed", "POST", gson.toJson(request), 200);

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
    void removeUpvoteQuestionWorks() {
        // mock the endpoint
        expectations.createExpectationWithoutBody(path + "question/" + 1 + "/upvote", "DELETE", 200);

        HttpResponse<String> response = ServerCommunication.removeUpvoteQuestion(roomID, 1);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("DELETE")
                                .withPath("/api/v1/room/TestRoomID/question/1/upvote"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(200, response.statusCode());
    }

    @Test
    void removeUpvoteQuestionNotFound() {
        // mock the endpoint
        expectations.createExpectationWithoutBody(path + "question/" + 5 + "/upvote", "DELETE", 404);

        HttpResponse<String> response = ServerCommunication.removeUpvoteQuestion(roomID, 5);

        // check whether the request has been received by the server
        new MockServerClient("localhost", 8080)
                .verify(
                        request()
                                .withMethod("DELETE")
                                .withPath("/api/v1/room/TestRoomID/question/5/upvote"),
                        VerificationTimes.atLeast(1)
                );

        assertEquals(404, response.statusCode());
    }
}
