package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class User {
    private String id;
    private String roomId;
    private String nickname;
    private String ip;
    private String authorizationToken;
    private UserRole role;
}
