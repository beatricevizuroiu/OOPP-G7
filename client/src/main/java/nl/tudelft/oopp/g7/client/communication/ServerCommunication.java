package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.g7.common.Question;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

// Class for generalizing methods.
public class ServerCommunication {
    private static final Gson gson = new Gson();
    private static final String endBody = "http://localhost:8080/api/v1/room";

    /**
     * Retrieve a question specified by ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return Question that was specified
     */
    public static Question retrieveQuestionById(int roomID, int questionID) {
        // add the appropriate end-point
        URI uri = URI.create(endBody + roomID + "/question/" + questionID);

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
     * @return a list of Questions that include all questions on server
     */
    public static List<Question> retrieveAllQuestions(int roomID) {
        // add the appropriate end-point
        URI uri = URI.create(endBody + roomID +"/question/all");

        // retrieve all of the questions
        HttpResponse<String> response = HttpMethods.get(uri);

        // extract the questions
        String questions = response.body();

        // create correct generic type for GSON parsing
        Type questionListType = new TypeToken<List<Question>>() {}.getType();

        // parse JSON array into Question List
        return gson.fromJson(questions, questionListType);
    }

    public static List<Question> retrieveAllAnsweredQuestions(int roomID) {
        // get a list of all questions
        List<Question> questionList = retrieveAllQuestions(roomID);

        // remove all of the unanswered questions
        questionList.removeIf(q -> !q.isAnswered());

        // return the new question list
        return questionList;
    }


    /**
     * Deletes the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return A {@link HttpResponse<String>} containing the response received from server.
     */
    public static HttpResponse<String> deleteQuestion(int roomID, int questionID) {
        // add the appropriate end-point
        URI uri = URI.create(endBody + roomID + "/question/" + questionID);

        // delete the question and store the response
        // appropriate code handling is done within the method
        // FIXME: we could do the code handling in these methods
        return HttpMethods.delete(uri);
    }
}
