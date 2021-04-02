package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Poll info.
 */
@AllArgsConstructor @NoArgsConstructor
@Data
public class PollInfo {
    // The id of the poll.
    private int id;
    // The question being asked
    private String question;
    // Whether the poll is currently accepting answers.
    private boolean isAcceptingAnswers;
    // Whether the poll info has result information.
    private boolean hasResults;
    // The options the user can select and possibly their results.
    private PollOption[] options;


    /**
     * From result set poll info.
     *
     * @param rs     the rs
     * @param noNext the no next
     * @return the poll info
     * @throws SQLException the sql exception
     */
    public static PollInfo fromResultSet(ResultSet rs, boolean noNext) throws SQLException {
        if (noNext || rs.next()) {
            return new PollInfo(
                    rs.getInt("id"),
                    rs.getString("question"),
                    !rs.getBoolean("isOver"),
                    rs.getBoolean("publicResults"),
                    new PollOption[0]);
        }

        return null;
    }
}
