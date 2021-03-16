package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class Room {
    private String id;
    private String studentPassword;
    private String moderatorPassword;
    private String name;
    private boolean isOpen;
    private boolean isOver;
    private Date startDate;

    public static Room fromResultSet(ResultSet rs) throws SQLException {
        if (rs.next())
            return new Room(
                    rs.getString("id"),
                    rs.getString("studentPassword"),
                    rs.getString("moderatorPassword"),
                    rs.getString("name"),
                    rs.getBoolean("open"),
                    rs.getBoolean("over"),
                    new Date(rs.getTimestamp("startDate").getTime())
            );
        else
            return null;
    }
}
