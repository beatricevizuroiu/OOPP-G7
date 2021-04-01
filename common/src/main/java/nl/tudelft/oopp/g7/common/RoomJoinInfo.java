package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor
@Data
public class RoomJoinInfo {
    @NotNull @NotEmpty
    private String roomId;
    @NotNull @NotEmpty
    private String userId;
    @NotNull @NotEmpty
    private String roomName;
    @NotNull @NotEmpty
    private String authorization;
    @NotNull
    private String nickname;
    @NotNull @NotEmpty
    private UserRole role;
}
