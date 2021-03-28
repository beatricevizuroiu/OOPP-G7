package nl.tudelft.oopp.g7.server.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpeedRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS speeds("
            + "userID varchar(36) not NULL,"
            + "roomID varchar(36) not NULL,"
            + "speed int not NULL,"
            + "PRIMARY KEY (userID, roomID),"
            + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE);";

    private static final String QUERY_SELECT_SPEED_FOR_ROOM = "SELECT SUM(speed) FROM speeds WHERE roomID=?;";
    private static final String QUERY_DELETE_SPEED_FOR_ROOM = "DELETE FROM speeds WHERE roomID=?;";
    private static final String QUERY_DELETE_SPEED_FOR_ROOM_FROM_USER = "DELETE FROM speeds WHERE userID=? AND roomID=?;";
    private static final String QUERY_INSERT_SPEED_FOR_ROOM = "INSERT INTO speeds (userID, roomID, speed) VALUES (?, ?, ?);";

    /**
     * Primary constructor for the UserRepository class.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public SpeedRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Get the current (lecturer) Speed in a Room.
     * @param roomId The roomId to get the speed of.
     * @return The current Speed value.
     */
    public int getSpeedForRoom(String roomId) {
        logger.debug("Retrieving speed for room with id: {} from the database", roomId);

        //noinspection ConstantConditions
        return jdbcTemplate.query(QUERY_SELECT_SPEED_FOR_ROOM,
                (ps) -> ps.setString(1, roomId),
                (rs) -> {
                    rs.next();
                    return rs.getInt(1);
                });
    }

    /**
     * Resets the current Speed in a Room to 0.
     * @param roomId The roomId of the Room to reset the Speed of.
     * @return The number of rows changed in the database.
     */
    public int resetSpeedForRoom(String roomId) {
        logger.debug("Resetting the speed of room with id: {}", roomId);

        return jdbcTemplate.update(QUERY_DELETE_SPEED_FOR_ROOM,
                (ps) -> {
                    ps.setString(1, roomId);
                });
    }

    /**
     * Sets the Speed for a User in a Room.
     * @param roomId The roomId the User is in.
     * @param userId The userId of the User.
     * @param speed The Speed to set (-1, 0 or 1).
     * @return The number of rows changed in the database.
     */
    public int setSpeedForUserInRoom(String roomId, String userId, int speed) {
        logger.debug("Setting the speed for user with id: {}, in room with id: {}, to {}", userId, roomId, speed);

        jdbcTemplate.update(QUERY_DELETE_SPEED_FOR_ROOM_FROM_USER,
                (ps) -> {
                    ps.setString(1, userId);
                    ps.setString(2, roomId);
                });

        return jdbcTemplate.update(QUERY_INSERT_SPEED_FOR_ROOM,
                (ps) -> {
                    ps.setString(1, userId);
                    ps.setString(2, roomId);
                    ps.setInt(3, speed);
                });
    }
}
