package nl.tudelft.oopp.g7.client.communication;

import org.junit.jupiter.api.Test;

public class ServerCommunicationTest {
    @Test
    void test() {
        System.out.println(ServerCommunication.retrieveAllAnsweredQuestions(12));
    }
}
