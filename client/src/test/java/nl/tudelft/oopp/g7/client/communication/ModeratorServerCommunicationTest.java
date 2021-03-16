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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModeratorServerCommunicationTest {
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
     * Helper method for cleaning up questions
     */
    @AfterEach
    void cleanUp() {
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

        // upvote a question for testing
        URI upvoteBody = URI.create(uriBody + roomID + "/question/" + anotherQuestion.getId() + "/upvote");
        HttpMethods.put(upvoteBody, "");

        // retrieve all questions
        List<Question> questions = StudentServerCommunication.retrieveAllQuestions(roomID);

        // check whether the order is correct (high upvote should be earlier in the list)
        assertEquals(anotherQuestion.getId(), questions.get(questions.size() - 2).getId());
        assertEquals(question.getId(), questions.get(questions.size() - 1).getId());
    }

    @Test
    void testAnswerQuestion() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        ModeratorServerCommunication.answerQuestion(roomID, question.getId(), new QuestionText("Test Answer"));

        assertTrue(ServerCommunication.retrieveQuestionById(roomID, question.getId()).isAnswered());
        assertEquals("Test Answer",
                     ServerCommunication.retrieveQuestionById(roomID, question.getId()).getAnswer());
    }
}
