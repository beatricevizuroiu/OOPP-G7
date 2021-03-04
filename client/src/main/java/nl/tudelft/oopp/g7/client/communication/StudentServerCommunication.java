package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.g7.common.NewQuestion;
import nl.tudelft.oopp.g7.common.Question;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

public class StudentServerCommunication {
    // TODO: decide on how to handle rooms
    // opted for static for now, should be changed when multiple lectures are added
    private static final Gson gson = new Gson();
    // again opted for static final since end-point is fixed
    private static final String endBody = "http://localhost:8080/api/v1/question";

    /**
     * Sends a post request with appropriate NewQuestion body.
     * @param question NewQuestion object asked by student
     */
    public static void askQuestion(NewQuestion question) {
        // convert the body to JSON
        String body = gson.toJson(question);

        // add the appropriate end-point
        URI uri = URI.create(endBody + "/new");

        // send the POST request
        HttpMethods.post(uri, body);
    }

    /**
     * Retrieve a question specified by ID.
     * @param id ID of the question you
     * @return Question that was specified
     */
    public static Question retrieveQuestionById(int id) {
        // add the appropriate end-point
        URI uri = URI.create(endBody + "/" + id);

        // retrieve the specified question
        String question = HttpMethods.get(uri);

        // parse the JSON into Question
        return gson.fromJson(question, Question.class);
    }

    /**
     * Retrieves all questions from the server.
     * @return a list of Questions that include all questions on server
     */
    public static List<Question> retrieveAllQuestions() {
        // add the appropriate end-point
        URI uri = URI.create(endBody + "/all");

        // retrieve all of the questions
        String questions = HttpMethods.get(uri);

        // create correct generic type for GSON parsing
        Type questionListType = new TypeToken<List<Question>>() {}.getType();

        // parse JSON array into Question List
        return gson.fromJson(questions, questionListType);
    }

}
