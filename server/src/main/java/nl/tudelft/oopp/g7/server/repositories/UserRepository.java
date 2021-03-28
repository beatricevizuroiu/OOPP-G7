package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.common.UserRole;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users("
            + "id varchar(36) PRIMARY KEY not NULL,"
            + "roomID varchar(36) not NULL,"
            + "nickname text DEFAULT '' not NULL,"
            + "ip varchar(39) not NULL,"
            + "userRole varchar(32) not NULL,"
            + "token varchar(128) not NULL,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE);";

    private static final String QUERY_COUNT_USERS_WITH_ID = "SELECT count(id) FROM users WHERE id=?";
    private static final String QUERY_STORE_USER = "INSERT INTO users (id, roomID, nickname, ip, userRole, token) VALUES (?,?,?,?,?,?);";
    private static final String QUERY_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=?;";
    private static final String QUERY_SELECT_ALL_USERS = "SELECT * FROM users WHERE roomID=?;";
    private static final String QUERY_SELECT_USER_BY_TOKEN = "SELECT * FROM users WHERE token=?;";

    /**
     * Primary constructor for the UserRepository class.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Generate a new userId that is not yet in use.
     * @return The new userId as a string.
     */
    public String createNewId() {
        String id;

        // Create a new random string generator with length 36.
        RandomString randomString = new RandomString(36);

        do {
            // Create a new random id.
            id = randomString.nextString();
            // Check if the id is already in the database. If it is create a new one and try again.
        } while (countUsersWithId(id) >= 1);

        return id;
    }


    /**
     * Count the amount of Users that have a certain id in the database.
     * @param userId The id to count
     * @return Expected values of 0 or 1, if it is more something is wrong.
     */
    public int countUsersWithId(String userId) {
        // Stop intellij from complaining about the query statement.
        //noinspection ConstantConditions
        return jdbcTemplate.query(QUERY_COUNT_USERS_WITH_ID,
            (ps) -> ps.setString(1, userId),
            (rs) -> {
                rs.next();
                return rs.getInt(1);
            });
    }

    /**
     * Get the UserInfo of a User by userId.
     * @param userId The userId of the User to get the UserInfo of.
     * @return The UserInfo of the User in question.
     */
    public UserInfo getUserInfoById(String userId) {
        logger.debug("Retrieving user with id: {} from the database", userId);
        return jdbcTemplate.query(QUERY_SELECT_USER_BY_ID,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the id of the user being requested.
                ps.setString(1, userId);
            },

            // Send the ResultSet to the UserInfo class to create a UserInfo instance from it.
            (rs) -> {
                return UserInfo.fromResultSet(rs, false);
            });
    }

    /**
     * Get a User by userId.
     * @param userId The userId of the User to get.
     * @return The requested User.
     */
    public User getUserById(String userId) {
        logger.debug("Retrieving user with id: {} from the database", userId);
        return jdbcTemplate.query(QUERY_SELECT_USER_BY_ID,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the id of the user being requested.
                ps.setString(1, userId);
            },

            // Send the ResultSet to the UserInfo class to create a UserInfo instance from it.
            (rs) -> {
                return User.fromResultSet(rs, false);
            });
    }

    /**
     * Get all Users in a Room.
     * @param roomId The roomId of the Room to get all Users from.
     * @return A list of UserInfo of all the Users in the Room.
     */
    public List<UserInfo> getAllUsersInRoom(String roomId) {
        logger.debug("Retrieving all question in room with id: {}", roomId);
        return jdbcTemplate.query(QUERY_SELECT_ALL_USERS,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the room id.
                ps.setString(1, roomId);
            },
            (rs) -> {
                // Create a list to hold all questions.
                List<UserInfo> userList = new ArrayList<>();
                // Loop while the result set has entries.
                while (rs.next()) {
                    // Create a new question from the result set and add it to the list of questions.
                    userList.add(UserInfo.fromResultSet(rs, true));
                }
                // Return the list of questions.
                return userList;
            });
    }

    /**
     * Get a User by their Authorization Token.
     * @param token The Authorization Token of the User.
     * @return The requested User.
     */
    public User getUserByToken(String token) {
        logger.debug("Retrieving user with token: {} from the database", token);
        return jdbcTemplate.query(QUERY_SELECT_USER_BY_TOKEN,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the id of the user being requested.
                ps.setString(1, token);
            },

            // Send the ResultSet to the UserInfo class to create a UserInfo instance from it.
            (rs) -> {
                return User.fromResultSet(rs, false);
            });
    }

    /**
     * Store a User in the database.
     * @param user The User to store.
     * @return The number of rows changed in the database.
     */
    public int storeUser(User user) {
        logger.debug("Storing user with id: {} from the database", user.getId());
        return jdbcTemplate.update(QUERY_STORE_USER,
            (ps) -> {
                // Set the userID in the PreparedStatement.
                ps.setString(1, user.getId());
                // Set the roomId of the user in the PreparedStatement.
                ps.setString(2, user.getRoomId());
                // Set the nickname of the user in the PreparedStatement.
                ps.setString(3, user.getNickname());
                // Set the ip of the user in the PreparedStatement.
                ps.setString(4, user.getIp());
                // Set whether or not the user is a moderator in the PreparedStatement.
                ps.setString(5, user.getRole().toString());
                // Set the authorizationToken of the user in the PreparedStatement.
                ps.setString(6, user.getAuthorizationToken());
            });
    }
}
