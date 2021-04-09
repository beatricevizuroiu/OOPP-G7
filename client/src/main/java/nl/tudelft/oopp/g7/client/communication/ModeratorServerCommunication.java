package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.*;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModeratorServerCommunication {
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    private static final String uriBody = LocalData.getServerUrl() + "/api/v1/room/";

    /**
     * Retrieve all questions from the server and sort them based on upvotes.
     * @param roomID ID of the room students belongs
     * @return A {@link List} of question containing all questions in sorted order.
     */
    public static List<Question> retrieveAllQuestions(String roomID) {
        // retrieve the question list from server
        List<Question> questions = ServerCommunication.retrieveAllUnansweredQuestions(roomID);

        if (LocalData.getSortingOrder() == SortingOrder.UPVOTES) {
            // sort the questions based on number of upvotes and return the list
            return questions.stream().sorted(Comparator.comparing(Question::getUpvotes).reversed())
                    .collect(Collectors.toList());
        }

        // sort the questions based on most recent and return the list
        return questions.stream().sorted(Comparator.comparing(Question::getPostedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Answer the question with the specified ID.
     * @param roomID ID of the room student belongs
     * @param questionID ID of the question
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> answerQuestion(String roomID, int questionID, Answer answer) {
        // convert the body to JSON
        String body = gson.toJson(answer);

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/question/" + questionID + "/answer");

        // answer the question and return the response
        return HttpMethods.post(uri, body);
    }

    public static HttpResponse<String> markAsAnswered(String roomID, int questionID) {
        return answerQuestion(roomID, questionID, new Answer(""));
    }

    /**
     * Bans the user with specified id.
     * @param roomID ID of the room student belongs.
     * @param userID ID of the student that will be banned.
     * @param banReason {@link BanReason} object that contains the reason for ban.
     * @return A HttpResponse containing the response received from server.
     */
    public static HttpResponse<String> banUser(String roomID, String userID, BanReason banReason) {
        // convert the body to JSON
        String body = gson.toJson(banReason);

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/user/" + userID + "/ban");

        // send the ban request and return the response
        return HttpMethods.post(uri, body);
    }


    /**
     * Create a Poll.
     * @param roomID The roomId of the Room to create a Poll in.
     * @param question The question text of the Poll.
     * @param options The answer options of the Poll.
     * @param publicResults True if the results of the Poll should be public while it is active.
     * @return The Http Response (No data).
     */
    public static HttpResponse<String> createPoll(String roomID, String question, String[] options, boolean publicResults) {
        // put the information in a PollCreateRequest and convert it to JSON
        String body = gson.toJson(new PollCreateRequest(question, options, publicResults));

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/poll");

        // send the request to the server
        return HttpMethods.post(uri, body);
    }

    /**
     * Close the open Poll in a Room.
     * @param roomID The roomId of the Room to close the Poll in.
     * @param publishResults Determines whether the results should be available to Students.
     * @return The Http Response (No data).
     */
    public static HttpResponse<String> closePoll(String roomID, boolean publishResults) {
        // convert the body to JSON
        String body = gson.toJson(new PollCloseRequest(publishResults));

        // add the appropriate end-point
        URI uri = URI.create(uriBody + roomID + "/poll/close");

        // send the request to the server
        return HttpMethods.post(uri, body);
    }
}
