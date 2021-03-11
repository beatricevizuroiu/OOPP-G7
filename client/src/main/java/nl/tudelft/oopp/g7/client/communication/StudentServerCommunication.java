package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.Question;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

public class StudentServerCommunication {
    // TODO: decide on how to handle rooms
    // opted for static for now, should be changed when multiple lectures are added
    private static final Gson gson = new Gson();
    // again opted for static final since end-point is fixed
    private static final String endBody = "http://localhost:8080/api/v1";

    /**
     * Sends a post request with appropriate NewQuestion body.
     * @param question QuestionText asked by student
     * @return response body of the post request
     */
    public static HttpResponse<String> askQuestion(QuestionText question) {
        // convert the body to JSON
        String body = gson.toJson(question);

        // add the appropriate end-point
        URI uri = URI.create(endBody + "/question/new");

        // send the POST request and return the response
        return HttpMethods.post(uri, body);
    }

    /**
     * Retrieve a question specified by ID.
     * @param id ID of the question you
     * @return Question that was specified
     */
    public static Question retrieveQuestionById(int id) {
        // add the appropriate end-point
        URI uri = URI.create(endBody + "/question/" + id);

        // retrieve the specified question's response
        HttpResponse<String> response = HttpMethods.get(uri);

        // extract the questions
        String question = response.body();

        // parse the JSON into Question
        return gson.fromJson(question, Question.class);
    }

    /**
     * Retrieves all questions from the server.
     * @return a list of Questions that include all questions on server
     */
    public static List<Question> retrieveAllQuestions() {
        // add the appropriate end-point
        URI uri = URI.create(endBody + "/question/all");

        // retrieve all of the questions
        HttpResponse<String> response = HttpMethods.get(uri);

        // extract the questions
        String questions = response.body();

        // create correct generic type for GSON parsing
        Type questionListType = new TypeToken<List<Question>>() {}.getType();

        // parse JSON array into Question List
        return gson.fromJson(questions, questionListType);
    }

}
