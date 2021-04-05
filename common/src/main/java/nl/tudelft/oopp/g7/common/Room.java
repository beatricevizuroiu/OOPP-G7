package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class Room {
    @NotNull @NotEmpty
    private String id;
    @NotNull
    private String studentPassword;
    @NotNull
    private String moderatorPassword;
    @NotNull @NotEmpty
    private String name;
    private boolean isOver;
    @NotNull
    private Date startDate;

    /**
     * Create a {@link Room} object from a {@link ResultSet}.
     * @param rs The result to create the {@link Room} from.
     * @return A new {@link Room} if the result set contained the needed information. Null otherwise.
     * @throws SQLException Thrown if something goes wrong getting the information out of the {@link ResultSet}.
     */
    public static Room fromResultSet(ResultSet rs) throws SQLException {
        if (rs.next())
            return new Room(
                    rs.getString("id"),
                    rs.getString("studentPassword"),
                    rs.getString("moderatorPassword"),
                    rs.getString("name"),
                    rs.getBoolean("over"),
                    new Date(rs.getTimestamp("startDate").getTime())
            );
        else
            return null;
    }
}
