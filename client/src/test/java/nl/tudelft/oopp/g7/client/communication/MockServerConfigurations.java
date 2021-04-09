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
     * Create expectation without body.
     *
     * @param path       the path
     * @param method     the method
     * @param statusCode the status code
     */
    public void createExpectationWithoutBody(String path, String method, int statusCode) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                            .withMethod(method)
                            .withPath(path)
                )
                .respond(
                        response()
                            .withStatusCode(statusCode)
                );
    }

    /**
     * Create an expected server response with a request body.
     * @param path Request path.
     * @param method The method for the request.
     * @param requestBody The body of the expected request.
     * @param statusCode The expected status code.
     */
    public void createExpectationWithRequestBody(String path, String method, String requestBody, int statusCode) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod(method)
                                .withPath(path)
                                .withBody(requestBody)
                )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                );
    }

    /**
     * Create an expected server response with a response body.
     * @param path Request path.
     * @param method The method for the request.
     * @param responseBody The body of the expected response.
     * @param statusCode The expected status code.
     */
    public void createExpectationWithResponseBody(String path, String method, String responseBody, int statusCode) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod(method)
                                .withPath(path)
                )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(responseBody)
                );
    }

    /**
     * Create an expected server response with a request and response body.
     * @param path The Request path
     * @param method The method for the request
     * @param requestBody The body of the request
     * @param responseBody The body of the response
     * @param statusCode The expected status code
     */
    public void createExpectationWithBothBodies(String path, String method, String requestBody, String responseBody, int statusCode) {
        new MockServerClient("localhost", 8080)
                .when(
                        request()
                                .withMethod(method)
                                .withPath(path)
                                .withBody(requestBody)
                )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(responseBody)
                );
    }
}
