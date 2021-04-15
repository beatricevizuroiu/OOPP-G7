package nl.tudelft.oopp.g7.client;

import nl.tudelft.oopp.g7.client.logic.LocalData;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            LocalData.setServerUrl(args[0]);
        }

        MainApp.main(new String[0]);
    }
}
