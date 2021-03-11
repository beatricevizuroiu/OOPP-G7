package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.g7.common.QuestionText;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HttpMethodsTest {
    private static String endBody = "https://httpbin.org/";
    private Gson gson = new Gson();

    @Test
    void getTest() {
        // create uri for get
        URI uri = URI.create(endBody + "/get");

        // get the response
        HttpResponse<String> response = HttpMethods.get(uri);

        // check if it received OK
        assertEquals(200, response.statusCode());
    }

    @Test
    void incorrectGetPathTest() {
        // create a non-existing path
        URI uri = URI.create(endBody + "/dummy");

        // get the response
        HttpResponse<String> response = HttpMethods.get(uri);

        // it should receive 404
        assertNotEquals(200, response.statusCode());
        assertEquals(404, response.statusCode());
    }

    @Test
    void postTest() {
        // create uri for get
        URI uri = URI.create(endBody + "/post");

        // create a dummy body for easy JSON conversion
        QuestionText newQuestion = new QuestionText("Test");

        // convert to json and send / store the response
        HttpResponse<String> response = HttpMethods.post(uri, gson.toJson(newQuestion));

        assertEquals(200, response.statusCode());
    }

    @Test
    void incorrectPostPathTest() {
        // create a non-existing path
        URI uri = URI.create(endBody + "/dummy");

        // create a dummy body for easy JSON conversion
        QuestionText newQuestion = new QuestionText("Test");

        // convert to json and send / store the response
        HttpResponse<String> response = HttpMethods.post(uri, gson.toJson(newQuestion));

        // it should receive 404
        assertNotEquals(200, response.statusCode());
        assertEquals(404, response.statusCode());
    }

    @Test
    void putTest() {
        // create uri for get
        URI uri = URI.create(endBody + "/put");

        // create a dummy body for easy JSON conversion
        QuestionText newQuestion = new QuestionText("Test");

        // convert to json and send / store the response
        HttpResponse<String> response = HttpMethods.put(uri, gson.toJson(newQuestion));

        assertEquals(200, response.statusCode());
    }


    @Test
    void incorrectPutPathTest() {
        // create a non-existing path
        URI uri = URI.create(endBody + "/dummy");

        // create a dummy body for easy JSON conversion
        QuestionText newQuestion = new QuestionText("Test");

        // convert to json and send / store the response
        HttpResponse<String> response = HttpMethods.put(uri, gson.toJson(newQuestion));

        // it should receive 404
        assertNotEquals(200, response.statusCode());
        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteTest() {
        // create uri for get
        URI uri = URI.create(endBody + "/delete");

        // get the response
        HttpResponse<String> response = HttpMethods.delete(uri);

        assertEquals(200, response.statusCode());
    }

    @Test
    void incorrectDeletePathTest() {
        // create a non-existing path
        URI uri = URI.create(endBody + "/dummy");

        // get the response
        HttpResponse<String> response = HttpMethods.delete(uri);

        // it should receive 404
        assertNotEquals(200, response.statusCode());
        assertEquals(404, response.statusCode());
    }

}
