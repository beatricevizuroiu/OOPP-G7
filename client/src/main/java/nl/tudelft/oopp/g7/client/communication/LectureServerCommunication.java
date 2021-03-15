package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.net.http.HttpResponse;

public class LectureServerCommunication {
    private static final Gson gson = new Gson();
    private static final String endBody = "http://localhost:8080/api/v1/room";

    /**
     * Marks the question as answered.
     * @param roomID roomID ID of the room student belongs
     * @param questionID questionID ID of the question
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> markAsAnswered(int roomID, int questionID) {
        // send an empty question body to mark as answered and return the response
        return ModeratorServerCommunication.answerQuestion(roomID, questionID, new QuestionText(""));
    }
}
