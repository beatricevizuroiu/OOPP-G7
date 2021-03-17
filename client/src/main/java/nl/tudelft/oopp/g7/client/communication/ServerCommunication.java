package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

// Class for generalizing methods.
public class ServerCommunication {
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    private static final String uriBody = "http://localhost:8080/api/v1/room/";

    /**
     * Retrieve a question specified by ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return {@link Question} that was specified
     */
    public static Question retrieveQuestionById(String roomID, int questionID) {
        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/" + questionID);

        // retrieve the specified question's response
        HttpResponse<String> response = HttpMethods.get(uri);

        // extract the questions
        String question = response.body();

        // parse the JSON into Question
        return gson.fromJson(question, Question.class);
    }

    /**
     * Retrieves all questions from the server.
     * @param roomID ID of the room student belongs
     * @return a {@link List} of Questions that include all questions on server
     */
    public static List<Question> retrieveAllQuestions(String roomID) {
        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/all");

        // retrieve all of the questions
        HttpResponse<String> response = HttpMethods.get(uri);

        // extract the questions
        String questions = response.body();

        // create correct generic type for GSON parsing
        Type questionListType = new TypeToken<List<Question>>() {}.getType();

        // parse JSON array into Question List
        return gson.fromJson(questions, questionListType);
    }

    /**
     * Retrieves all questions and filters them to only answered questions.
     * @param roomID ID of the room student belongs
     * @return a {@link List} of Questions that includes only answered questions on the server
     */
    public static List<Question> retrieveAllAnsweredQuestions(String roomID) {
        // get a list of all questions
        List<Question> questionList = retrieveAllQuestions(roomID);

        // remove all of the unanswered questions
        questionList.removeIf(q -> !q.isAnswered());

        // return the new question list
        return questionList;
    }


    /**
     * Upvote the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> upvoteQuestion(String roomID, int questionID) {
        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/" + questionID + "/upvote");

        // send the upvote request and return the response
        return HttpMethods.put(uri, "");
    }

    /**
     * Edit the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @param questionText new text body of the question
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> editQuestion(String roomID, int questionID, QuestionText questionText) {
        // convert the body to JSON
        String body = gson.toJson(questionText);

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/" + questionID);

        // send the PUT request and return the response
        return HttpMethods.put(uri, body);
    }

    /**
     * Delete the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> deleteQuestion(String roomID, int questionID) {
        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/" + questionID);

        // delete the question and store the response
        // appropriate code handling is done within the method
        // FIXME: we could do the code handling in these methods
        return HttpMethods.delete(uri);
    }
}
