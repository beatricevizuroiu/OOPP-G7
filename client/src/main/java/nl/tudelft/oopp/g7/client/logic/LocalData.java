package nl.tudelft.oopp.g7.client.logic;

import lombok.Getter;
import lombok.Setter;
import nl.tudelft.oopp.g7.client.MainApp;
import nl.tudelft.oopp.g7.common.SortingOrder;
import nl.tudelft.oopp.g7.common.UserInfo;

import java.util.HashMap;
import java.util.*;

public class LocalData {
    @Setter @Getter
    private static String nickname = "";
    @Setter @Getter
    private static String roomID = null;
    @Setter @Getter
    private static String userID = null;
    @Setter @Getter
    private static String studentPassword = "";
    @Setter @Getter
    private static String moderatorPassword = null;
    @Setter @Getter
    private static String token = "";
    @Setter @Getter
    private static String roomName = "";
    @Setter @Getter
    private static boolean isLecturer = false;
    @Setter @Getter
    private static SortingOrder sortingOrder = SortingOrder.NEW;
    @Setter @Getter
    private static String serverUrl = "http://localhost:8080";
    @Getter
    private static ColorScheme colorScheme = ColorScheme.LIGHT;
    public static HashMap<String, UserInfo> userMap = new HashMap<>();
    public static Set<Integer> upvotedQuestions = new HashSet<>();

    public static void switchColorScheme() {
        switch (colorScheme) {
            case DARK -> colorScheme = ColorScheme.LIGHT;
            case LIGHT -> colorScheme = ColorScheme.DARK;
        }

        System.out.printf("Changed color scheme to: %s%n", colorScheme);

        MainApp.updateStyleSheets();
    }

    public static void setColorScheme(ColorScheme colorScheme) {
        LocalData.colorScheme = colorScheme;

        MainApp.updateStyleSheets();
    }

    public enum ColorScheme {
        LIGHT("/style/lightTheme.css"),
        DARK("/style/darkTheme.css");

        @Getter
        private final String stylesheet;

        ColorScheme(String stylesheet) {
            this.stylesheet = stylesheet;
        }
    }
}
