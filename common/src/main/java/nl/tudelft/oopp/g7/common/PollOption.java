package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Poll option.
 */
@AllArgsConstructor @NoArgsConstructor
@Data
public class PollOption {
    private int id;
    private String text;
    private int resultCount;

    /**
     * From result set poll option.
     *
     * @param rs     the rs
     * @param noNext the no next
     * @return the poll option
     * @throws SQLException the sql exception
     */
    public static PollOption fromResultSet(ResultSet rs, boolean noNext) throws SQLException {
        if (noNext || rs.next()) {
            return new PollOption(
                    rs.getInt("id"),
                    rs.getString("text"),
                    0
            );
        }

        return null;
    }
}
