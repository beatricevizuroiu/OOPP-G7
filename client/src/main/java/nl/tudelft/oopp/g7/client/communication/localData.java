package nl.tudelft.oopp.g7.client.communication;

public class localData {
    private static String myNickname = "";
    private static String myRoomID = null;
    private static String myPassword = "";
    private static String myStudentPassword = "";
    private static String myModeratorPassword = null;

    public static void setNickname(String nickname) {
        myNickname = nickname;
    }

    public static void setRoomID(String roomID) {
        myRoomID = roomID;
    }

    public static void setPassword(String password) {
        myPassword = password;
    }

    public static void setStudentPassword(String password) {
        myStudentPassword = password;
    }

    public static void setModeratorPassword(String moderatorPassword) {
        myModeratorPassword = moderatorPassword;
    }

    public static String getNickname() {
        return myNickname;
    }

    public static String getRoomID() {
        return myRoomID;
    }

    public static String getPassword() {
        return myPassword;
    }

    public static String getStudentPassword() {
        return myStudentPassword;
    }

    public static String getModeratorPassword() {
        return myModeratorPassword;
    }
}
