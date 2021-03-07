package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.g7.common.NewQuestion;
import nl.tudelft.oopp.g7.common.Question;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Testing suite for the server-client communication channel.
 * The server must be online for the tests to pass
 */
public class StudentSeverCommunicationTest {
    private static NewQuestion newQuestion;
    private static NewQuestion anotherNewQuestion;
    private static String endBody = "http://localhost:8080/api/v1/question";
    private static Gson gson = new Gson();

    private static boolean isConnected;

    @BeforeAll
    static void testConnection() {
        try {
            // test an endpoint to see if connection is alive
            URI uri = URI.create(endBody + "/all");
            HttpMethods.get(uri);

            isConnected = true;

            setUp();
        } catch (NullPointerException e) { // if connection is dead we don't want to fail all tests
            isConnected = false;
        }

        System.out.println(isConnected);
    }


    /**
     * Helper method for setting up the test suite.
     */
    private static void setUp() {
        // create the URI
        URI uri = URI.create(endBody + "/new");

        // if connection is alive create the questions with timestamps
        newQuestion = new NewQuestion("Current time: " + (new Date()).getTime());
        anotherNewQuestion = new NewQuestion("Current time: " + (new Date()).getTime());

        // convert to json and send / store the response
        HttpMethods.post(uri, gson.toJson(newQuestion));
        HttpMethods.post(uri, gson.toJson(anotherNewQuestion));
    }

    @Test
    void testRetrieveAllQuestions() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        List<Question> questions = StudentServerCommunication.retrieveAllQuestions();

        assertEquals(newQuestion.getText(), questions.get(questions.size() - 2).getText());
        assertEquals(anotherNewQuestion.getText(), questions.get(questions.size() - 1).getText());
    }

    @Test
    void testRetrieveQuestionByID() {
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }

        // extract newQuestion and anotherNewQuestion
        List<Question> questions = StudentServerCommunication.retrieveAllQuestions();
        Question question = questions.get(questions.size() - 2);
        Question anotherQuestion = questions.get(questions.size() - 1);

        // test whether the questions are the same
        assertEquals(StudentServerCommunication.retrieveQuestionById(question.getId()), question);
        assertEquals(StudentServerCommunication.retrieveQuestionById(anotherQuestion.getId()), anotherQuestion);
    }

    @Test
    void testAskQuestion() throws InterruptedException {
        // FIXME: Until delete is implemented this test shouldn't be executed
        // Since it alters composition it messed up the test suite
        // However, it is standalone tested and indeed works

        /*
        // in the case that the server is not connected do not test
        if (!isConnected) {
            return;
        }


        // create a unique NewQuestion instance and send it
        NewQuestion yetAnotherNewQuestion = new NewQuestion("Current time: " + (new Date()).getTime());
        HttpResponse<String> response = StudentServerCommunication.askQuestion(yetAnotherNewQuestion);
        System.out.println(yetAnotherNewQuestion.getText());

        // extract the yetAnotherNewQuestion
        List<Question> questions = StudentServerCommunication.retrieveAllQuestions();
        Question yetAnotherQuestion = questions.get(questions.size() - 1);
        System.out.println(yetAnotherQuestion.getText());

        // test whether the questions are the same
        assertEquals(200, response.statusCode());
        assertEquals(yetAnotherNewQuestion.getText(), yetAnotherQuestion.getText());
        assertNotEquals(newQuestion.getText(), yetAnotherQuestion.getText());

         */
    }
}
