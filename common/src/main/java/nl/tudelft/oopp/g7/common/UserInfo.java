package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor @AllArgsConstructor
@Data
public class UserInfo {
    @NotNull @NotEmpty
    private String id;
    @NotNull @NotEmpty
    private String roomId;
    @NotNull @NotEmpty
    private String nickname;
    @NotNull
    private UserRole userRole;

    /**
     * Create a {@link UserInfo} object from a {@link ResultSet}.
     * @param rs The result to create the {@link UserInfo} from.
     * @return A new {@link UserInfo} if the result set contained the needed information. Null otherwise.
     * @throws SQLException Thrown if something goes wrong getting the information out of the {@link ResultSet}.
     */
    public static UserInfo fromResultSet(ResultSet rs, boolean noNext) throws SQLException {
        if (noNext || rs.next())
            return new UserInfo(
                    rs.getString("id"),
                    rs.getString("roomId"),
                    rs.getString("nickname"),
                    UserRole.valueOf(rs.getString("userRole"))
            );
        else
            return null;
    }
}
