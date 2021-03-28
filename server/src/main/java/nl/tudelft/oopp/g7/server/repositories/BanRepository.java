package nl.tudelft.oopp.g7.server.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class BanRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS bannedUsers("
            + "ip varchar(39) not NULL,"
            + "roomID varchar(36) not NULL,"
            + "reason text DEFAULT '' not NULL,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,"
            + "PRIMARY KEY (ip, roomID));";

    private static final String QUERY_COUNT_USER_BY_IP = "SELECT COUNT(ip) FROM bannedUsers WHERE ip=? AND roomID=?;";
    private static final String QUERY_BAN_USER = "INSERT INTO bannedUsers (ip, roomID, reason) VALUES (?, ?, ?);";

    /**
     * The primary constructor for the BanRepository class.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public BanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Checks whether an Ip is currently banned.
     * @param roomId The roomId of the Room in which the check should be performed.
     * @param ip The Ip to check.
     * @return Whether the Ip is banned in the form of a boolean.
     */
    public boolean checkBanned(String roomId, String ip) {
        //noinspection ConstantConditions
        int bannedAmount = jdbcTemplate.query(QUERY_COUNT_USER_BY_IP,
            (ps) -> {
                ps.setString(1, ip);
                ps.setString(2, roomId);
            },
            (rs) -> {
                rs.next();
                return rs.getInt(1);
            });

        return bannedAmount > 0;
    }

    /**
     * Ban an Ip from interacting with a Room.
     * @param roomId The roomId to ban the Ip from.
     * @param ip The Ip to ban from the Room.
     * @param reason The reason of banning this Ip.
     * @return The number of lines changed in the database.
     */
    public int banUser(String roomId, String ip, String reason) {
        return jdbcTemplate.update(QUERY_BAN_USER,
            (ps) -> {
                ps.setString(1, ip);
                ps.setString(2, roomId);
                ps.setString(3, reason);
            });
    }

}
