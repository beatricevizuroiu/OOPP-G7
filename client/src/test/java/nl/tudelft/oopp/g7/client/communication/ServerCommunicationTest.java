package nl.tudelft.oopp.g7.client.communication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class ServerCommunicationTest {

    @Test
    public void testRandomQuote() {
        assertNotNull(ServerCommunication.getQuote());
    }
}
