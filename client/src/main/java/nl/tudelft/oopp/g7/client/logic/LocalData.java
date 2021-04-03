package nl.tudelft.oopp.g7.client.logic;

import lombok.Getter;
import lombok.Setter;
import nl.tudelft.oopp.g7.common.SortingOrder;
import nl.tudelft.oopp.g7.common.UserInfo;

import java.util.HashMap;

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
    public static HashMap<String, UserInfo> userMap = new HashMap<>();
}
