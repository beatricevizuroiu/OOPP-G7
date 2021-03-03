package nl.tudelft.oopp.g7.client.communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StudentServerCommunication {

    // opted for static for now, should be changed when multiple lectures are added
    private static HttpClient httpClient = HttpClient.newBuilder().build();

    public static void askQuestion(String questionText) {
        String body = "{\"text\": \"" + questionText + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                                         .POST(HttpRequest.BodyPublishers.ofString(body))
                                         .uri(URI.create("http://localhost:8080/api/v1/question/new"))
                                         .header("Content-Type", "application/json")
                                         .build();

        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Communication with server failed.");
        }

        System.out.println("Status Code = " + response.statusCode());
    }

}
