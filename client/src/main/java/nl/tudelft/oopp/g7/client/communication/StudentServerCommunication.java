package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.QuestionText;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentServerCommunication {
    private static final Gson gson = new Gson();
    private static final String endBody = "http://localhost:8080/api/v1/room";

    /**
     * Sends a post request with appropriate NewQuestion body.
     * @param roomID ID of the room students belongs
     * @param question QuestionText asked by student
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> askQuestion(String roomID, QuestionText question) {
        // convert the body to JSON
        String body = gson.toJson(question);

        // add the appropriate end-point
        URI uri = URI.create(endBody + roomID + "/question/new");

        // send the POST request and return the response
        return HttpMethods.post(uri, body);
    }

    /**
     * Retrieve all questions from the server and sort them based on most recent.
     * @param roomID ID of the room students belongs
     * @return A {@link List} of question containing all questions in sorted order.
     */
    public static List<Question> retrieveAllQuestions(String roomID) {
        // retrieve the question list from server
        List<Question> questions = ServerCommunication.retrieveAllQuestions(roomID);

        // sort the questions based on most recent and return the list
        return questions.stream().sorted(Comparator.comparing(Question::getPostedAt))
                        .collect(Collectors.toList());
    }

    /**
     * Edit a question if it is owned by the student.
     * @param roomID ID of the room students belongs
     * @param questionID ID of the question
     * @param ownedQuestions a List containing questions owned (asked) by the student
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> editQuestion(String roomID, int questionID, QuestionText questionText,
                                                                                List<Question> ownedQuestions) {
        // check if the question is sent by the student
        // FIXME: ideally handle it elsewhere
        if (!isOwned(questionID, ownedQuestions)) {
            System.out.println("The question does not belong to student.");
            return null;
        }

        // edit the question and return the response
        return ServerCommunication.editQuestion(roomID, questionID, questionText);
    }

    /**
     * Delete a question if it is owned by the student.
     * @param roomID ID of the room students belongs
     * @param questionID ID of the question
     * @param ownedQuestions a List containing questions owned (asked) by the student
     * @return A {@link HttpResponse} containing the response received from server.
     */
    public static HttpResponse<String> deleteQuestion(String roomID, int questionID, List<Question> ownedQuestions) {
        // check if the question is sent by the student
        // FIXME: ideally handle it elsewhere
        if (!isOwned(questionID, ownedQuestions)) {
            System.out.println("The question does not belong to student.");
            return null;
        }

        // delete the question and return the response
        return ServerCommunication.deleteQuestion(roomID, questionID);
    }

    /**
     * Checks whether the question is sent by the student.
     * @param questionID ID of the question
     * @param ownedQuestions a List containing questions owned (asked) by the student
     * @return a boolean that tells whether the question with specified ID is asked by the student
     */
    public static boolean isOwned(int questionID, List<Question> ownedQuestions) {
        return ownedQuestions.stream().map(Question::getId)
                             .collect(Collectors.toList()).contains(questionID);
    }
}
