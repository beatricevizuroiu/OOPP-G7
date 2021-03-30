package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class RoomJoinInfo {
    private String roomId;
    private String userId;
    private String roomName;
    private String authorization;
    private String nickname;
    private UserRole role;
}
