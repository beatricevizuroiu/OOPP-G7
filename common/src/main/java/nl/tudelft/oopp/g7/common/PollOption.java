package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PollOption {
    private int id;
    private String text;
    private int resultCount;

    /**
     * Construct PollOption from a resultSet.
     * @param rs The ResultSet to use.
     * @return PollOption.
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
