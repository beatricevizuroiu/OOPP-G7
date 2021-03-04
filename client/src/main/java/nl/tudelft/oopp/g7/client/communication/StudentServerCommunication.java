package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.g7.common.NewQuestion;

import java.net.URI;
import java.net.http.HttpRequest;

public class StudentServerCommunication {
    // TODO: decide on how to handle rooms
    // opted for static for now, should be changed when multiple lectures are added
    private static final Gson gson = new Gson();
    // again opted for static final since end-point is fixed
    private static final String endBody = "http://localhost:8080/api/v1/question";

    /**
     * Sends a post request with appropriate NewQuestion body
     * @param question NewQuestion object asked by student
     */
    public static void askQuestion(NewQuestion question) {
        // convert the body to JSON
        String body = gson.toJson(question);
        URI uri = URI.create(endBody + "/new");

        // create a request object
        HttpRequest request = HttpMethods.post(uri, body);

        // send the request
        HttpMethods.send(request);
    }

}
