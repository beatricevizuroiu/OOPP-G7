package nl.tudelft.oopp.g7.client.communication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpMethods {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    // TODO: Should we change this to async to not block the thread
    // TODO: Should we integrate send method into the requests?
    /**
     * Sends the given request to the server.
     * @param request HttpRequest object that contains appropriate information.
     */
    public static void send(HttpRequest request) {
        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Communication with server failed.");
        }

        System.out.println("Status Code = " + response.statusCode());
    }

    /**
     * Creates and sends a generic get (RETRIEVE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @return HttpRequest object
     */
    public static HttpRequest get(URI uri) {
        return HttpRequest.newBuilder().GET().uri(uri).build();
    }

    /**
     * Creates and sends a generic post (CREATE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @param body should be a JSON formatted String
     * @return HttpRequest object with the specified body and appropriate header.
     */
    public static HttpRequest post(URI uri, String body) {
        return HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body))
                                       .uri(uri).header("Content-Type", "application/json")
                                       .build();
    }

    /**
     * Creates and sends a generic put (UPDATE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @param body should be a JSON formatted String
     * @return HttpRequest object with the specified body and appropriate header.
     */
    public static HttpRequest put(URI uri, String body) {
        return HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(body))
                                       .uri(uri).header("Content-Type", "application/json")
                                       .build();
    }

    // TODO: Create an appropriate reply for 404 not-found

    /**
     * Creates and sends generic delete (DELETE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @return HttpRequest object with the specified body and appropriate header.
     */
    public static HttpRequest delete(URI uri) {
        return HttpRequest.newBuilder().DELETE()
                                       .uri(uri).header("Content-Type", "application/json")
                                       .build();
    }
}
