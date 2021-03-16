package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerCommunicationTest {
    private static Question question;
    private static Question anotherQuestion;
    private static String roomID;

    private static String uriBody = "http://localhost:8080/api/v1/room/";
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

    private static boolean isConnected;

    @BeforeAll
    static void testConnection() {
        try {
            // test an endpoint to see if connection is alive
            URI uri = URI.create(uriBody);
            HttpMethods.get(uri);

            isConnected = true;

        } catch (NullPointerException e) { // if connection is dead we don't want to fail all tests
            isConnected = false;
        }
    }


    /**
     * Helper method for setting up the test suite.
     */
    @BeforeEach
    void setUp() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // create the room uri
        URI uriRoom = URI.create(uriBody + "create");

        // create NewRoom object
        NewRoom newRoom = new NewRoom("Test", "s", "m", new Date(0));

        // convert NewRoom into JSON and send to server
        HttpResponse<String> roomHttpResponse = HttpMethods.post(uriRoom, gson.toJson(newRoom));

        roomID = gson.fromJson(roomHttpResponse.body(), Room.class).getId();

        // if connection is alive create the questions with timestamps
        QuestionText questionText = new QuestionText("Current time: " + (new Date()).getTime());
        QuestionText anotherQuestionText = new QuestionText("Current time: " + (new Date()).getTime());

        // create the question uri
        URI uriQuestion = URI.create(uriBody + roomID + "/question/new");

        // convert to json and send / store the response
        HttpMethods.post(uriQuestion, gson.toJson(questionText));
        HttpMethods.post(uriQuestion, gson.toJson(anotherQuestionText));

        // extract questionText and anotherQuestionText
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);
        question = questions.get(questions.size() - 2);
        anotherQuestion = questions.get(questions.size() - 1);
    }

    /**
     * Helper method for cleaning up questions.
     */
    @AfterEach
    void cleanUp() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // extract questionText and anotherQuestionText
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);
        HttpMethods.delete(URI.create(uriBody + roomID + "/question/" + question.getId()));
        HttpMethods.delete(URI.create(uriBody + roomID + "/question/" + anotherQuestion.getId()));
    }

    @Test
    void testRetrieveAllQuestions() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);

        assertEquals(question, questions.get(questions.size() - 2));
        assertEquals(anotherQuestion, questions.get(questions.size() - 1));
    }

    @Test
    void testRetrieveQuestionByID() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // test whether the questions are the same
        assertEquals(ServerCommunication.retrieveQuestionById(roomID, question.getId()), question);
        assertEquals(ServerCommunication.retrieveQuestionById(roomID, anotherQuestion.getId()), anotherQuestion);
    }

    @Test
    void testRetrieveAllAnsweredQuestions() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // extract questionText and anotherQuestionText
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);

        URI answerBody = URI.create(uriBody + roomID + "/question/" + anotherQuestion.getId() + "/answer");
        HttpMethods.post(answerBody, gson.toJson(new QuestionText("Test answer")));

        // test whether the answered question has been received
        List<Question> answeredQuestions = ServerCommunication.retrieveAllAnsweredQuestions(roomID);
        assertNotEquals(questions, answeredQuestions);

        // test whether questions match
        assertEquals(anotherQuestion.getId(), answeredQuestions.get(0).getId());
    }

    @Test
    void testUpvoteQuestion() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // retrieve a question from the server
        // extract questionText and anotherQuestionText
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);
        Question question = questions.get(questions.size() - 2);

        // upvote a question
        HttpResponse<String> response = ServerCommunication.upvoteQuestion(roomID, question.getId());

        // test whether upvote is registered
        assertEquals(200, response.statusCode());
        assertEquals(1, ServerCommunication.retrieveQuestionById(roomID, question.getId()).getUpvotes());
    }

    @Test
    void testEditQuestion() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // edit the question body
        HttpResponse<String> response = ServerCommunication.editQuestion(roomID,
                                            anotherQuestion.getId(), new QuestionText("Edited body"));

        Question editedQuestion = ServerCommunication.retrieveQuestionById(roomID, anotherQuestion.getId());
        assertEquals(200, response.statusCode());
        assertNotEquals(anotherQuestion, editedQuestion);
        assertEquals("Edited body", editedQuestion.getText());
    }

    @Test
    void deleteQuestion() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // delete the last question
        HttpResponse<String> response = ServerCommunication.deleteQuestion(roomID, anotherQuestion.getId());

        // retrieve all questions and check whether the last question is still there
        List<Question> questionList = ServerCommunication.retrieveAllQuestions(roomID);
        assertEquals(200, response.statusCode());
        assertFalse(questionList.contains(anotherQuestion));
    }
}
