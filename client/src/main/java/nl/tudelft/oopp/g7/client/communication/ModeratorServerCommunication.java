package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModeratorServerCommunication {
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    private static final String endBody = "http://localhost:8080/api/v1/room/";

    /**
     * Retrieve all questions from the server and sort them based on upvotes.
     * @param roomID ID of the room students belongs
     * @return A {@link List} of question containing all questions in sorted order.
     */
    public static List<Question> retrieveAllQuestions(String roomID) {
        // retrieve the question list from server
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);

        // sort the questions based on number of upvotes and return the list
        return questions.stream().sorted(Comparator.comparing(Question::getUpvotes).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Answer the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @param questionText answer TA wants to give
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> answerQuestion(String roomID, int questionID, QuestionText questionText) {
        // convert the body to JSON
        String body = gson.toJson(questionText);

        // add the appropriate end-point
        URI uri = URI.create(endBody + roomID + "/question/" + questionID + "/answer");

        // answer the question and return the response
        return HttpMethods.post(uri, body);
    }

    public static HttpResponse<String> markAsAnswered(String roomID, int questionID) {
        return answerQuestion(roomID, questionID, new QuestionText(""));
    }
}
