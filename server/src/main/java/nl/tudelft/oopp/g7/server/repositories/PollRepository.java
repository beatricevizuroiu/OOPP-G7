package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.common.PollInfo;
import nl.tudelft.oopp.g7.common.PollOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class PollRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(PollRepository.class);

    private static final String QUERY_CREATE_TABLE_POLLS = "CREATE TABLE IF NOT EXISTS polls("
            + "id serial PRIMARY KEY not NULL,"
            + "roomID varchar(36) not NULL,"
            + "question text not NULL,"
            + "createdAt timestamp with time zone DEFAULT NOW(),"
            + "publicResults boolean DEFAULT FALSE not NUll,"
            + "isOver boolean DEFAULT FALSE not NULL,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE);";

    private static final String QUERY_CREATE_TABLE_OPTIONS = "CREATE TABLE IF NOT EXISTS pollOptions("
            + "id serial PRIMARY KEY not NULL,"
            + "pollID int not NULL,"
            + "roomID varchar(36) not NULL,"
            + "text text not NULL,"
            + "FOREIGN KEY (pollID) REFERENCES polls(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE);";

    private static final String QUERY_CREATE_TABLE_RESULTS = "CREATE TABLE IF NOT EXISTS pollResults("
            + "pollID int not NULL,"
            + "roomID varchar(36) not NULL,"
            + "userID varchar(36) not NULL,"
            + "optionID int not NULL,"
            + "PRIMARY KEY (pollID, roomID, userID),"
            + "FOREIGN KEY (pollID) REFERENCES polls(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (optionID) REFERENCES pollOptions(id) ON DELETE CASCADE);";

    private static final String QUERY_CREATE_POLL = "INSERT INTO polls (roomID, question, publicResults) VALUES (?, ?, ?) RETURNING id;";
    private static final String QUERY_CREATE_POLL_OPTION = "INSERT INTO pollOptions (pollID, roomID, text) VALUES (?, ?, ?);";
    private static final String QUERY_DELETE_POLL_RESULT_FOR_USER = "DELETE FROM pollResults WHERE pollID=? AND roomID=? and userID=?";
    private static final String QUERY_INSERT_POLL_RESULT_FOR_USER = "INSERT INTO pollResults (pollID, roomID, userID, optionID) VALUES (?, ?, ?, ?);";
    private static final String QUERY_END_POLL = "UPDATE polls SET isOver = TRUE, publicResults = ? WHERE roomID=? and id=?;";
    private static final String QUERY_REOPEN_POLL = "UPDATE polls SET isOver = FALSE WHERE roomID=? and id=?;";
    private static final String QUERY_GET_POLL_WITH_ID = "SELECT * FROM polls WHERE roomID=? AND id=?";
    private static final String QUERY_GET_POLL_MOST_RECENT = "SELECT * FROM polls WHERE roomID=? ORDER BY createdAt DESC LIMIT 1;";
    private static final String QUERY_GET_POLL_OPTIONS = "SELECT * FROM pollOptions WHERE roomID=? AND pollID=?";
    private static final String QUERY_COUNT_ANSWERS_TO_RESULT = "SELECT COUNT(optionID) FROM pollResults WHERE pollID=? AND roomID=? and optionID=?";

    /**
     * Primary constructor for the PollRepository class.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public PollRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE_POLLS);
            jdbcTemplate.execute(QUERY_CREATE_TABLE_OPTIONS);
            jdbcTemplate.execute(QUERY_CREATE_TABLE_RESULTS);

        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Create a new poll in the specified from with the specified options.
     * @param roomId The id of the room the poll belongs to.
     * @param question The question that is being asked in the poll.
     * @param publicResults Whether the results of the poll should be public during voting.
     * @param options The options that users can choose from during the poll.
     * @return The id of the poll.
     */
    public int createPoll(String roomId, String question, boolean publicResults, String[] options) {
        logger.debug("Creating poll in database for room with id {}, and question: {}", roomId, question);
        //noinspection ConstantConditions
        int pollId = jdbcTemplate.query(QUERY_CREATE_POLL,
            (ps) -> {
                ps.setString(1, roomId);
                ps.setString(2, question);
                ps.setBoolean(3, publicResults);
            },
            (rs) -> {
                rs.next();
                return rs.getInt("id");
            });

        for (String option : options) {
            jdbcTemplate.update(QUERY_CREATE_POLL_OPTION, (ps) -> {
                ps.setInt(1, pollId);
                ps.setString(2, roomId);
                ps.setString(3, option);
            });
        }

        return pollId;
    }

    /**
     * Mark a poll is finished in the database.
     * @param roomId The id of the room that the poll belongs to.
     * @param pollId The id of the poll.
     */
    public int endPoll(String roomId, int pollId, boolean publishResults) {
        logger.debug("Closing poll in room with id: {}, and with poll id: {}", roomId, pollId);
        return jdbcTemplate.update(QUERY_END_POLL, (ps) -> {
            ps.setBoolean(1, publishResults);
            ps.setString(2, roomId);
            ps.setInt(3, pollId);
        });
    }

    /**
     * Reopen a Poll in a Room.
     * @param roomId The roomId of the Room the Poll is in
     * @param pollId The pollId of the Poll to re-open
     * @return The amount of lines changed
     */
    public int reopenPoll(String roomId, int pollId) {
        logger.debug("Closing poll in room with id: {}, and with poll id: {}", roomId, pollId);
        return jdbcTemplate.update(QUERY_REOPEN_POLL, (ps) -> {
            ps.setString(1, roomId);
            ps.setInt(2, pollId);
        });
    }

    /**
     * Change the option a user voted on in a poll.
     * @param roomId The room the user and poll belong to.
     * @param userId The id of the user.
     * @param pollId The id of the poll.
     * @param optionId The id of the option the user voted for.
     */
    public void updateResult(String roomId, String userId, int pollId, int optionId) {
        logger.debug("Changing poll answer in the database for user with id: {}, in room with id: {}", userId, roomId);
        jdbcTemplate.update(QUERY_DELETE_POLL_RESULT_FOR_USER, (ps) -> {
            ps.setInt(1, pollId);
            ps.setString(2, roomId);
            ps.setString(3, userId);
        });

        jdbcTemplate.update(QUERY_INSERT_POLL_RESULT_FOR_USER, (ps) -> {
            ps.setInt(1, pollId);
            ps.setString(2, roomId);
            ps.setString(3, userId);
            ps.setInt(4, optionId);
        });
    }

    /**
     * Get a poll for a specific room.
     * @param roomId The id of the room.
     * @param pollId The id of the poll. Setting this to -1 will get the most recent poll.
     * @param alwaysHaveAnswers Whether the {@link PollInfo} object should always contain answers.
     * @return A {@link PollInfo} object containing the poll.
     */
    public PollInfo getPoll(String roomId, int pollId, boolean alwaysHaveAnswers) {
        logger.debug("Getting poll from database with poll id: {}, for room id: {}", pollId, roomId);
        PollInfo pollInfo;

        if (pollId == -1) {
            pollInfo = getMostRecentPollInRoom(roomId);
        } else {
            pollInfo = jdbcTemplate.query(QUERY_GET_POLL_WITH_ID,
                (ps) -> {
                    ps.setString(1, roomId);
                    ps.setInt(2, pollId);
                },
                (rs) -> {
                    return PollInfo.fromResultSet(rs, false);
                });
        }

        if (pollInfo == null)
            return null;

        if (alwaysHaveAnswers) {
            pollInfo.setHasResults(true);
        }

        List<PollOption> pollOptions = getOptionsForPoll(roomId, pollInfo);

        if (pollOptions == null)
            return null;

        pollInfo.setOptions(pollOptions.toArray(new PollOption[0]));

        return pollInfo;
    }

    /**
     * Get the most recent poll in the room specified.
     * <br><br>
     * WARNING: This method does not retrieve a full {@link PollInfo} object. Only the bare minimum needed for calling
     * other methods.
     * @param roomId The room id of the room to retrieve the poll from.
     * @return PollInfo
     */
    public PollInfo getMostRecentPollInRoom(String roomId) {
        logger.debug("Getting most recent poll from database in room with id {}", roomId);
        return jdbcTemplate.query(QUERY_GET_POLL_MOST_RECENT,
            (ps) -> {
                ps.setString(1, roomId);
            },
            (rs) -> {
                return PollInfo.fromResultSet(rs, false);
            });
    }

    /**
     * Retrieve the options for a poll with the specified id.
     * @param roomId The id of the room that contains the poll.
     * @param pollInfo The {@link PollInfo} object describing the poll.
     * @return A {@link List} of {@link PollOption}s that are assigned to the poll.
     */
    private List<PollOption> getOptionsForPoll(String roomId, PollInfo pollInfo) {
        logger.debug("Retrieving options for poll with id {}, in room with id {}", pollInfo.getId(), roomId);
        return jdbcTemplate.query(QUERY_GET_POLL_OPTIONS,
            (ps) -> {
                ps.setString(1, roomId);
                ps.setInt(2, pollInfo.getId());
            },
            (rs) -> {
                List<PollOption> options = new ArrayList<>();
                while (rs.next()) {
                    PollOption option = PollOption.fromResultSet(rs, true);
                    if (pollInfo.isHasResults()) {
                        option.setResultCount(getResultCountForOption(roomId, pollInfo.getId(), option));
                    }

                    options.add(option);
                }

                return options;
            });
    }

    /**
     * Retrieve the amount of votes that are assigned to the {@link PollOption} provided.
     * @param roomId The room id that the poll belongs to.
     * @param pollId The id of the poll that the {@link PollOption} belongs to.
     * @param option The {@link PollOption} to check the votes for.
     * @return An integer representing the amount of votes that are assigned to the {@link PollOption}.
     */
    private Integer getResultCountForOption(String roomId, int pollId, PollOption option) {
        logger.debug("Retrieving the amount of votes for option with id {}, in room with id {}, for poll with id {}", option.getId(), roomId, pollId);
        return jdbcTemplate.query(QUERY_COUNT_ANSWERS_TO_RESULT,
            (ps) -> {
                ps.setInt(1, pollId);
                ps.setString(2, roomId);
                ps.setInt(3, option.getId());
            },
            (rs2) -> {
                rs2.next();
                return rs2.getInt(1);
            });
    }
}
