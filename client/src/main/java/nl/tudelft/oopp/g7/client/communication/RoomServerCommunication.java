package nl.tudelft.oopp.g7.client.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
     * @param roomID ID of the room user wants to join
     * @param roomJoinRequest contains necessary student/moderator password
     * @return a {@link RoomJoinInfo}
     */
    public static RoomJoinInfo joinRoom(int roomID, RoomJoinRequest roomJoinRequest) {
        // create the uri
        URI uri = URI.create(uriBody + roomID + "/join");

        // convert roomJoinRequest to JSON
        String jsonRoomJoinRequest = gson.toJson(roomJoinRequest);

        // send the request and store the resposne
        HttpResponse<String> response = HttpMethods.post(uri, jsonRoomJoinRequest);

        //return the RoomJoinInfo object
        return gson.fromJson(response.body(), RoomJoinInfo.class);
    }
}
