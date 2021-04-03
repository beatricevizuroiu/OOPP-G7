package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Poll option.
 */
@AllArgsConstructor @NoArgsConstructor
@Data
public class PollOption {
    @Positive
    private int id;
    @NotNull @NotEmpty
    private String text;
    @PositiveOrZero
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
