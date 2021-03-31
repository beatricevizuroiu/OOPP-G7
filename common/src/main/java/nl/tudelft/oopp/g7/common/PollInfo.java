package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PollInfo {
    // The id of the poll.
    @Positive
    private int id;
    // The question being asked
    @NotNull @NotEmpty
    private String question;
    // Whether the poll is currently accepting answers.
    private boolean isAcceptingAnswers;
    // Whether the poll info has result information.
    private boolean hasResults;
    // The options the user can select and possibly their results.
    @NotNull @NotEmpty
    private PollOption[] options;

    /**
     * Construct PollInfo from a resultSet.
     * @param rs The resultSet.
     * @return PollInfo.
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
