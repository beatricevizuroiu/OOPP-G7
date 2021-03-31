package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor @NoArgsConstructor
@Data
public class User {
    @NotNull @NotEmpty
    private String id;
    @NotNull @NotEmpty
    private String roomId;
    @NotNull @NotEmpty
    private String nickname;
    @NotNull @NotEmpty
    private String ip;
    @NotNull @NotEmpty
    private String authorizationToken;
    @NotNull
    private UserRole role;

    /**
     * Create a {@link User} object from a {@link ResultSet}.
     * @param rs The result to create the {@link User} from.
     * @return A new {@link User} if the result set contained the needed information. Null otherwise.
     * @throws SQLException Thrown if something goes wrong getting the information out of the {@link ResultSet}.
     */
    public static User fromResultSet(ResultSet rs, boolean noNext) throws SQLException {
        if (noNext || rs.next())
            return new User(
                    rs.getString("id"),
                    rs.getString("roomId"),
                    rs.getString("nickname"),
                    rs.getString("ip"),
                    rs.getString("token"),
                    UserRole.valueOf(rs.getString("userRole"))
            );
        else
            return null;
    }
}
