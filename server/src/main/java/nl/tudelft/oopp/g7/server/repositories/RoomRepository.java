package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.common.Room;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;

public class RoomRepository {

    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(RoomRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS rooms("
            + "id varchar(36) PRIMARY KEY not NULL,"
            + "studentPassword varchar(32) DEFAULT '' not NULL,"
            + "moderatorPassword varchar(32) not NULL,"
            + "name text not NULL,"
            + "open boolean DEFAULT FALSE not NULL,"
            + "over boolean DEFAULT FALSE not NULL,"
            + "startDate timestamp with time zone not NUlL);";

    private static final String QUERY_COUNT_ROOMS_WITH_ID = "SELECT count(id) FROM rooms WHERE id=?";
    private static final String QUERY_CREATE_ROOM = "INSERT INTO rooms (id, studentPassword, moderatorPassword, name, open, over, startDate) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String QUERY_GET_ROOM_WITH_ID = "SELECT * FROM rooms WHERE id=?";

    /**
     * Primary constructor for the room repository.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Generate a new room id that is not already in use.
     * @return The new id as a string.
     */
    public String createNewId() {
        String id;

        // Create a new random string generator with length 36.
        RandomString randomString = new RandomString(36);

        do {
            // Create a new random id.
            id = randomString.nextString();
            // Check if the id is already in the database. If it is create a new one and try again.
        } while (countRoomsWithId(id) >= 1);

        return id;
    }


    /**
     * Count the amount of rooms that have a certain id in the database.
     * @param roomId The id to count
     * @return Expected values of 0 or 1, if it is more something is wrong.
     */
    public int countRoomsWithId(String roomId) {
        // Stop intellij from complaining about the query statement.
        //noinspection ConstantConditions
        return jdbcTemplate.query(QUERY_COUNT_ROOMS_WITH_ID,
            (ps) -> ps.setString(1, roomId),
            (rs) -> {
                rs.next();
                return rs.getInt(1);
            });
    }

    /**
     * Store a new room into the database.
     * @param room The {@link Room} to store.
     * @return The amount of rows that where changed.
     */
    public int createRoom(Room room) {
        //set everything in a prepared statement and run it
        return jdbcTemplate.update(QUERY_CREATE_ROOM,
            (ps) -> {
                // Set the id of the room in the PreparedStatement.
                ps.setString(1, room.getId());
                // Set the student password of the room in the PreparedStatement.
                ps.setString(2, room.getStudentPassword());
                // Set the moderator password of the room in the PreparedStatement.
                ps.setString(3, room.getModeratorPassword());
                // Set the name of the room in the PreparedStatement.
                ps.setString(4, room.getName());
                // Set whether or not the room is open in the PreparedStatement.
                ps.setBoolean(5, room.isOpen());
                // Set whether or not the room is over in the PreparedStatement.
                ps.setBoolean(6, room.isOver());
                // Set the start date of the room in the PreparedStatement.
                ps.setDate(7, new Date(room.getStartDate().getTime()));
            });
    }

    /**
     * Retrieve a {@link Room} from the database.
     * @param roomId The id of the room to retrieve.
     * @return The {@link Room} that was retrieved.
     */
    public Room getRoomById(String roomId) {
        return jdbcTemplate.query(QUERY_GET_ROOM_WITH_ID,
            (ps) -> ps.setString(1, roomId), Room::fromResultSet);
    }
}
