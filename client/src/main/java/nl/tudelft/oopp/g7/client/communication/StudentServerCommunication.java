package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.PollAnswerRequest;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;
import nl.tudelft.oopp.g7.common.SortingOrder;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentServerCommunication {
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    private static final String uriBody = LocalData.getServerUrl() + "/api/v1/room/";

    /**
     * Sends a post request with appropriate NewQuestion body.
     * @param roomID ID of the room students belongs
     * @param question QuestionText asked by student
     * @return A {@link HttpResponse} containing the response received from server
     */
    public static HttpResponse<String> askQuestion(String roomID, QuestionText question) {
        // convert the body to JSON
        String body = gson.toJson(question);

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/new");

        // send the POST request and return the response
        return HttpMethods.post(uri, body);
    }

    /**
     * Retrieve all questions from the server and sort them based on most recent.
     * @param roomID ID of the room students belongs
     * @return A {@link List} of question containing all questions in sorted order
     */
    public static List<Question> retrieveAllQuestions(String roomID) {
        // retrieve the question list from server
        List<Question> questions = ServerCommunication.retrieveAllUnansweredQuestions(roomID);

        if (LocalData.getSortingOrder() == SortingOrder.NEW) {
            // sort the questions based on most recent and return the list
            return questions.stream().sorted(Comparator.comparing(Question::getPostedAt).reversed())
                    .collect(Collectors.toList());
        }

        // sort the questions based on number of upvotes and return the list
        return questions.stream().sorted(Comparator.comparing(Question::getUpvotes).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Send an answer to a Poll.
     * @param roomId The roomId of the Poll
     * @param optionId The optionId of the answer to send
     * @return The HttpResponse (No data)
     */
    public static HttpResponse<String> answerPoll(String roomId, int optionId) {
        // convert the option number to JSON
        String body = gson.toJson(new PollAnswerRequest(optionId));

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomId + "/poll/answer");

        // send the request to the server
        return HttpMethods.post(uri, body);
    }
}
