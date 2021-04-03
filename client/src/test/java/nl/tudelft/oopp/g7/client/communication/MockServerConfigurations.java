package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.oopp.g7.common.*;
import org.mockserver.client.MockServerClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * The type Mock server configurations.
 */
@AllArgsConstructor
@Data
public class MockServerConfigurations {
    private Question question;
    private Question questionWithAnswer;
    private Question questionWithUpvote;

    private static String roomID = "TestRoomID";
    private static String path = "/api/v1/room/" + roomID + "/";
    private static final Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

    /**
     * Instantiates a new Mock server configurations.
     */
    public MockServerConfigurations() {
        question = new Question(1, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 0, false, false);
        questionWithAnswer = new Question(2, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "This is an answer to the question", new Date(1614511580000L), 1, true, false);
        questionWithUpvote = new Question(3, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", "This is a question", "", new Date(0), 10, true, false);
    }

    /**
     * Gets question list.
     *
     * @return the question list
     */
    public List<Question> getQuestionList() {
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        questionList.add(questionWithAnswer);
        questionList.add(questionWithUpvote);

        return questionList;
    }

    /**
     * Create expectation all.
     */
    public void createExpectationAll() {
        new MockServerClient("localhost", 8080)
                .when(
                    request()
                        .withMethod("GET")
                        .withPath(path + "question/all")
            )
                .respond(
                    response()
                        .withStatusCode(200)
                        .withBody(gson.toJson(getQuestionList()))

            );
    }

    /**
     * Create expectation question id.
     *
     * @param questionID the question id
     */
    public void createExpectationQuestionID(int questionID) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath(path + "question/" + questionID)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(question))
                );
    }

    /**
     * Create expectation question id not works.
     */
    public void createExpectationQuestionIDNotWorks() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath(path + "question/" + 5)
                )
                .respond(
                        response()
                                .withStatusCode(404)
                                .withBody("")
                );
    }

    /**
     * Create expectation ask question.
     */
    public void createExpectationAskQuestion() {
        QuestionText text = new QuestionText("New Question");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/new")
                                .withBody(gson.toJson(text))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation ask question server error.
     */
    public void createExpectationAskQuestionServerError() {
        QuestionText text = new QuestionText("Problematic");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/new")
                                .withBody(gson.toJson(text))
                )
                .respond(
                        response()
                                .withStatusCode(500)
                );
    }

    /**
     * Create expectation upvote works.
     */
    public void createExpectationUpvoteWorks() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 1 + "/upvote")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation upvote not works.
     */
    public void createExpectationUpvoteNotWorks() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 2 + "/upvote")
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );
    }

    /**
     * Create expectation edit question works.
     *
     * @param questionID the question id
     */
    public void createExpectationEditQuestionWorks(int questionID) {
        QuestionText text = new QuestionText("Edited text");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + questionID)
                                .withBody(gson.toJson(text))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation edit question not works.
     */
    public void createExpectationEditQuestionNotWorks() {
        QuestionText text = new QuestionText("Edited text");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 5)
                                .withBody(gson.toJson(text))
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );
    }

    /**
     * Create expectation delete question works.
     *
     * @param questionID the question id
     */
    public void createExpectationDeleteQuestionWorks(int questionID) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("DELETE")
                                .withPath(path + "question/" + questionID)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation delete question not works.
     */
    public void createExpectationDeleteQuestionNotWorks() {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("DELETE")
                                .withPath(path + "question/" + 5)
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );
    }

    /**
     * Create expectation get speed.
     */
    public void createExpectationGetSpeed() {
        SpeedAlterRequest response = new SpeedAlterRequest(1);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath(path + "speed")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(response))
                );
    }

    /**
     * Create expectation set speed works.
     */
    public void createExpectationSetSpeedWorks() {
        SpeedAlterRequest request = new SpeedAlterRequest(1);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "speed")
                                .withBody(gson.toJson(request))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation set speed bad request.
     */
    public void createExpectationSetSpeedBadRequest() {
        SpeedAlterRequest request = new SpeedAlterRequest(2);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "speed")
                                .withBody(gson.toJson(request))
                )
                .respond(
                        response()
                                .withStatusCode(400)
                );
    }

    /**
     * Create expectation answer question works.
     *
     * @param questionID the question id
     */
    public void createExpectationAnswerQuestionWorks(int questionID) {
        QuestionText answer = new QuestionText("This is an answer.");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + questionID + "/answer")
                                .withBody(gson.toJson(answer))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }

    /**
     * Create expectation answer question not works.
     */
    public void createExpectationAnswerQuestionNotWorks() {
        QuestionText answer = new QuestionText("This is an answer.");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "question/" + 5)
                                .withBody(gson.toJson(answer))
                )
                .respond(
                        response()
                                .withStatusCode(404)
                );
    }

    /**
     * Create expectation create room works.
     */
    public void createExpectationCreateRoomWorks() {
        NewRoom newRoom = new NewRoom("Test Room", "s", "m", new Date(0));
        Room room = new Room("1", "s", "m", "Test Room", true, false, new Date(0));

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/create")
                                .withBody(gson.toJson(newRoom))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(room))
                );
    }

    /**
     * Create expectation create room bad request.
     */
    public void createExpectationCreateRoomBadRequest() {
        // if same password give bad request error
        NewRoom newRoom = new NewRoom("Test Room", "m", "m", new Date(0));

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/api/v1/room/create")
                                .withBody(gson.toJson(newRoom))
                )
                .respond(
                        response()
                                .withStatusCode(400)
                                .withBody("")
                );
    }

    /**
     * Create expectation join room works.
     */
    public void createExpectationJoinRoomWorks() {
        RoomJoinRequest request = new RoomJoinRequest("s", "test");

        RoomJoinInfo info = new RoomJoinInfo(roomID, "id", "name", "auth", "test", UserRole.STUDENT);

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "join")
                                .withBody(gson.toJson(request))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(info))
                );
    }

    /**
     * Create expectation join room unauthorized.
     */
    public void createExpectationJoinRoomUnauthorized() {
        RoomJoinRequest request = new RoomJoinRequest("notModPass", "test");

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "join")
                                .withBody(gson.toJson(request))
                )
                .respond(
                        response()
                                .withStatusCode(401)
                                .withBody("")
                );
    }

    public void createExpectationRetrieveUserInfo() {
        List<UserInfo> userInfoList = new ArrayList<>();
        userInfoList.add(new UserInfo("1", "TestRoomID", "test", UserRole.STUDENT));

        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath(path + "user/all")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(gson.toJson(userInfoList))
                );
    }

    public void createExpectationBanUser(String userID) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath(path + "user/" + userID + "/ban")
                                .withBody(gson.toJson(new BanReason("")))
                )
                .respond(
                        response()
                                .withStatusCode(200)
                );
    }
}
