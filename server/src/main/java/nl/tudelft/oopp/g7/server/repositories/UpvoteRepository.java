package nl.tudelft.oopp.g7.server.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class UpvoteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS upvotes("
            + "userID varchar(36) not NULL,"
            + "roomID varchar(36) not NULL,"
            + "questionID int not NULL,"
            + "PRIMARY KEY (userID, roomID, questionID),"
            + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (questionID) REFERENCES questions(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE);";

    private static final String QUERY_SELECT_UPVOTES_BY_USER_ID = "SELECT * FROM upvotes WHERE roomID=? AND userID=?;";
    private static final String QUERY_INSERT_UPVOTE = "INSERT INTO upvotes (userID, roomID, questionID) VALUES (?, ?, ?);";
    private static final String QUERY_DELETE_UPVOTE = "DELETE FROM upvotes WHERE userID=? AND roomID=? AND questionID=?;";


    /**
     * Primary constructor for the user repository.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public UpvoteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    public List<Integer> getUpvotesForUser(String roomId, String userId) {
        logger.debug("Retrieving upvotes from user with id: {} in room with id: {} from the database", userId, roomId);

        return jdbcTemplate.query(QUERY_SELECT_UPVOTES_BY_USER_ID,
                (ps) -> {
                    // Set the first variable in the PreparedStatement to the id of the room being requested.
                    ps.setString(1, roomId);
                    // Set the second variable in the PreparedStatement to the id of the user being requested.
                    ps.setString(2, userId);
                },

                // Send the ResultSet to the UserInfo class to create a UserInfo instance from it.
                (rs) -> {
                    List<Integer> questionIds = new ArrayList<>();
                    while (rs.next()) {
                        questionIds.add(rs.getInt("questionID"));
                    }

                    return questionIds;
                });
    }

    public int addUpvote(String roomId, String userId, int questionId) {
        logger.debug("Adding an upvote to question with id: {}, in room with id: {}, for user with id: {}", questionId, roomId, userId);

        return jdbcTemplate.update(QUERY_INSERT_UPVOTE,
                (ps) -> {
                    ps.setString(1, userId);
                    ps.setString(2, roomId);
                    ps.setInt(3, questionId);
                });
    }

    public int removeUpvote(String roomId, String userId, int questionId) {
        logger.debug("Removing an upvote to question with id: {}, in room with id: {}, for user with id: {}", questionId, roomId, userId);

        return jdbcTemplate.update(QUERY_DELETE_UPVOTE,
                (ps) -> {
                    ps.setString(1, userId);
                    ps.setString(2, roomId);
                    ps.setInt(3, questionId);
                });
    }
}
