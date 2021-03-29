package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class Question {
    private int id;
    private String authorId;
    private String text;
    private String answer;
    private Date postedAt;
    private int upvotes;
    private boolean answered;
    private boolean edited;

    /**
     * Create a {@link Question} from a {@link ResultSet} if the result set contains the required information.
     * @param rs The {@link ResultSet} to take the information from.
     * @param noNext Whether to call {@link ResultSet#next()} on the {@link ResultSet}.
     * @return Either a {@link Question} object populated from the {@link ResultSet} or null if the {@link ResultSet} was empty.
     * @throws SQLException Thrown if something goes wrong getting the information out of the {@link ResultSet}.
     */
    public static Question fromResultSet(ResultSet rs, boolean noNext) throws SQLException {
        if (noNext || rs.next())
            return new Question(
                    rs.getInt("id"),
                    rs.getString("userId"),
                    rs.getString("text"),
                    rs.getString("answer"),
                    new Date(rs.getTimestamp("postedAt").getTime()),
                    rs.getInt("upvotes"),
                    rs.getBoolean("answered"),
                    rs.getBoolean("edited")
            );
        else
            return null;
    }
}
