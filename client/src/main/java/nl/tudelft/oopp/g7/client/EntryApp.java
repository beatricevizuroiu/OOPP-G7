package nl.tudelft.oopp.g7.client;

import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.client.views.EntryRoomDisplay;

public class EntryApp {
    /**
     * Entry point.
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            LocalData.setServerUrl(args[0]);
        }

        EntryRoomDisplay.main(new String[0]);
    }
}
