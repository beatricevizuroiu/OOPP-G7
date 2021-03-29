package nl.tudelft.oopp.g7.client.logic;

import lombok.Getter;
import lombok.Setter;

public class LocalData {
    @Setter @Getter
    private static String nickname = "";
    @Setter @Getter
    private static String roomID = null;
    @Setter @Getter
    private static String studentPassword = "";
    @Setter @Getter
    private static String moderatorPassword = null;
    @Setter @Getter
    private static String token = "";
}
