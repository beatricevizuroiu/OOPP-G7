package nl.tudelft.oopp.g7.client.communication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpMethods {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    // TODO: Should we change this to async to not block the thread
    /**
     * Sends the given request to the server.
     * @param request HttpRequest object that contains appropriate information.
     * @return response's status code
     */
    public static HttpResponse<String> send(HttpRequest request) {
        // object that will hold the response
        HttpResponse<String> response = null;

        try {
            // send the request through client and store the result in response
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { // any exception means there was a problem
            System.out.println("Communication with server failed.");
        }

        return response;
    }

    /**
     * Creates and sends a generic GET (RETRIEVE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @return Response object
     */
    public static HttpResponse<String> get(URI uri) {
        // create the GET request
        HttpRequest request =  HttpRequest.newBuilder().GET().uri(uri).build();
        // send the request through the http client and store the response
        HttpResponse<String> response = send(request);

        // if the status is anything other than 200 (OK), a problem should've occurred
        if (response.statusCode() != 200) {
            System.out.println("A problem occurred.");
        }

        return response;
    }

    /**
     * Creates and sends a generic POST (CREATE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @param body should be a JSON formatted String
     * @return Response object
     */
    public static HttpResponse<String> post(URI uri, String body) {
        // create a POST request object with a body of JSON
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body))
                                         .uri(uri).header("Content-Type", "application/json")
                                         .build();
        // send the request through the http client and store the response
        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            System.out.println("A problem occurred.");
        }

        return response;
    }

    /**
     * Creates and sends a generic put (UPDATE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @param body should be a JSON formatted String
     * @return Response object
     */
    public static HttpResponse<String> put(URI uri, String body) {
        // create a PUT request object with a body of JSON
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(body))
                                         .uri(uri).header("Content-Type", "application/json")
                                         .build();
        // send the request through the http client and store the response
        HttpResponse<String> response = send(request);

        if (response.statusCode() != 200) {
            System.out.println("A problem occurred.");
        }

        return response;
    }

    /**
     * Creates and sends generic delete (DELETE resource) request object to the specified end-point.
     * @param uri specified end-point
     * @return Response object
     */
    public static HttpResponse<String> delete(URI uri) {
        // create a DELETE request object, the deleted resource will have a JSON body
        HttpRequest request = HttpRequest.newBuilder().DELETE()
                                         .uri(uri).header("Content-Type", "application/json")
                                         .build();
        // send the request through the http client and store the response
        HttpResponse<String> response = send(request);

        if (response.statusCode() == 404) { // in the case of (NOT FOUND)
            System.out.println("Question doesn't exist.");
        } else if (response.statusCode() != 200) {
            System.out.println("A problem occurred.");
        }

        return response;
    }
}
