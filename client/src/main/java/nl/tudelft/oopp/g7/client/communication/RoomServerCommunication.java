package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.tudelft.oopp.g7.client.logic.LocalData;
import nl.tudelft.oopp.g7.common.NewRoom;
import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.common.RoomJoinInfo;
import nl.tudelft.oopp.g7.common.RoomJoinRequest;

import java.net.URI;
import java.net.http.HttpResponse;

public class RoomServerCommunication {
    private static Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
    private static final String uriBody = "http://localhost:8080/api/v1/room/";

    /**
     * Send new room request to the server and retrieve the new room.
     * @param newRoom contains parameters of the room you want to create
     * @return a {@link Room} with the specified parameters
     */
    public static Room createRoom(NewRoom newRoom) {
        // create the uri
        URI uri = URI.create(uriBody + "create");

        // convert newRoom to JSON
        String jsonNewRoom = gson.toJson(newRoom);

        // send the request and store the response
        HttpResponse<String> response = HttpMethods.post(uri, jsonNewRoom);

        // return the room object
        return gson.fromJson(response.body(), Room.class);
    }

    /**
     * Join a room if available.
     * @param roomID ID of the room user wants to join.
     * @param password The password the user wants to join with.
     * @param nickname The nickname the user wants to use.
     * @return A {@link RoomJoinInfo}
     */
    public static RoomJoinInfo joinRoom(String roomID, String password, String nickname) {
        // create the uri
        URI uri = URI.create(uriBody + roomID + "/join");

        // convert roomJoinRequest to JSON
        String jsonRoomJoinRequest = gson.toJson(new RoomJoinRequest(password, nickname));

        // send the request and store the response
        HttpResponse<String> response = HttpMethods.post(uri, jsonRoomJoinRequest);
        if (response.statusCode() != 200)
            return null;

        // extract the RoomJoinInfo object
        RoomJoinInfo roomJoinInfo = gson.fromJson(response.body(), RoomJoinInfo.class);

        // store the roomName
        LocalData.setRoomName(roomJoinInfo.getRoomName());

        // return the roomJoinInfo
        return roomJoinInfo;
    }
}
