package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.common.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZoneId;
import java.util.*;

public class QuestionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(QuestionRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS questions("
            + "id serial PRIMARY KEY not NULL,"
            + "userID varchar(36) not NULL,"
            + "roomID varchar(36) not NULL,"
            + "text text not NULL,"
            + "answer text DEFAULT '' not NULL,"
            + "postedAt timestamp with time zone DEFAULT NOW(),"
            + "answered boolean DEFAULT FALSE not NUll,"
            + "edited boolean DEFAULT FALSE not NULL,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id) ON DELETE CASCADE,"
            + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE);";

    private static final String QUERY_SELECT_QUESTION_BY_ID = "SELECT q.id, q.userID, q.roomID, q.text, q.answer, q.postedAt, q.answered, q.edited,\n"
            + "       (SELECT COUNT(questionID) from upvotes WHERE questionID = q.id) as upvotes\n"
            + "FROM questions as q\n"
            + "WHERE q.id=? AND q.roomID=?;";
    private static final String QUERY_SELECT_ALL_QUESTIONS = "SELECT q.id, q.userID, q.roomID, q.text, q.answer, q.postedAt, q.answered, q.edited,\n"
            + "       (SELECT COUNT(questionID) from upvotes WHERE questionID = q.id) as upvotes\n"
            + "FROM questions as q\n"
            + "WHERE q.roomID=?;";
    private static final String QUERY_CREATE_QUESTION = "INSERT INTO questions (userID, roomID, text) VALUES (?, ?, ?);";
    private static final String QUERY_ANSWER_QUESTION = "UPDATE questions SET answer=?, answered=true WHERE id=? AND roomID=?;";
    private static final String QUERY_EDIT_QUESTION = "UPDATE questions SET text=?, userID=?, edited=true WHERE id=? AND roomID=?;";
    private static final String QUERY_DELETE_QUESTION = "DELETE FROM questions WHERE id=? AND roomID=?;";
    private static final String QUERY_MOST_RECENT_QUESTION_FROM_USER = "SELECT * FROM questions WHERE roomID=? AND userID=? ORDER BY postedAt DESC LIMIT 1;";

    /**
     * Primary constructor for the QuestionRepository class.
     * @param jdbcTemplate The {@link JdbcTemplate} that should handle the database queries.
     */
    public QuestionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        try {
            jdbcTemplate.execute(QUERY_CREATE_TABLE);
        } catch (DataAccessException e) {
            logger.error("Database creation failed!", e);
        }
    }

    /**
     * Get a Question from the database.
     * @param roomId The id of the Room the Question is in.
     * @param id The id of the Question.
     * @return The {@link Question} that was retrieved.
     */
    public Question getQuestionById(String roomId, int id) {
        logger.debug("Retrieving question with id: {} from the database", id);
        return jdbcTemplate.query(QUERY_SELECT_QUESTION_BY_ID,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the id of the question being requested.
                ps.setInt(1, id);
                // Set the second variable in the PreparedStatement to the room id.
                ps.setString(2, roomId);
            },

            // Send the ResultSet to the Question class to create a Question instance from it.
            (rs) -> {
                return Question.fromResultSet(rs, false);
            });
    }

    /**
     * Get all Questions within a specific Room.
     * @param roomId The id of the Room to list the Questions from.
     * @return The {@link List} of {@link Question}s from the Room.
     */
    public List<Question> getAllQuestionsInRoom(String roomId) {
        logger.debug("Retrieving all question in room with id: {}", roomId);
        return jdbcTemplate.query(QUERY_SELECT_ALL_QUESTIONS,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the room id.
                ps.setString(1, roomId);
            },
            (rs) -> {
                // Create a list to hold all questions.
                List<Question> questionList = new ArrayList<>();
                // Loop while the result set has entries.
                while (rs.next()) {
                    // Create a new question from the result set and add it to the list of questions.
                    questionList.add(Question.fromResultSet(rs, true));
                }
                // Return the list of questions.
                return questionList;
            });
    }

    /**
     * Edit a Question's body in the database.
     * @param roomId The id of the Room to edit the Question in.
     * @param id The id of the Question.
     * @param newBody The new body of the Question.
     * @return The amount of rows changed in the database.
     */
    public int editQuestionWithId(String roomId, int id, String newBody, String by) {
        logger.debug("Changing the text of question with id: {}, to \"{}\"", id, newBody);
        return jdbcTemplate.update(QUERY_EDIT_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the new text body of the question.
                ps.setString(1, newBody);

                // Set the second variable in the PreparedStatement to the id of the user updating the question.
                ps.setString(2, by);

                // Set the third variable in the PreparedStatement to the id of the question to update.
                ps.setInt(3, id);

                // Set the fourth variable in the PreparedStatement to the room id.
                ps.setString(4, roomId);
            });
    }

    /**
     * Delete a Question from the database.
     * @param roomId The id of the Room to delete the Question from.
     * @param id The id of the Question to delete.
     * @return The amount of rows changed in the database.
     */
    public int deleteQuestionWithId(String roomId, int id) {
        return jdbcTemplate.update(QUERY_DELETE_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the question id.
                ps.setInt(1, id);

                // Set the second variable in the PreparedStatement to the room id.
                ps.setString(2, roomId);
            });
    }

    /**
     * Create a new Question in the database.
     * @param roomId The id of the Room to create the Question in.
     * @param questionText The text for the new Question.
     * @return The amount of rows changed in the database.
     */
    public int createQuestion(String roomId, String questionText, String userId) {
        logger.debug("Storing a new question with text: \"{}\"", questionText);
        return jdbcTemplate.update(QUERY_CREATE_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the user id.
                ps.setString(1, userId);
                // Set the second variable in the PreparedStatement to the room id.
                ps.setString(2, roomId);

                // Set the third variable in the PreparedStatement to the text of the new question.
                ps.setString(3, questionText);
            });
    }

    /**
     * Store the answer to a Question in the database.
     * @param roomId The Room the Question is in.
     * @param id The id of the Question.
     * @param answer The answer to the Question.
     * @return The amount of rows that where changed in the database.
     */
    public int answerQuestionWithId(String roomId, int id, String answer) {
        logger.debug("Storing answer to question with id: {}, answer: {}", id, answer);
        return jdbcTemplate.update(QUERY_ANSWER_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the answer to the question.
                ps.setString(1, answer);

                // Set the second variable in the PreparedStatement to the id of the question to update.
                ps.setInt(2, id);

                // Set the third variable in the PreparedStatement to the room id.
                ps.setString(3, roomId);
            });
    }

    /**
     * Retrieve the amount of time passed since the last question was posted by the specified user.
     * @param roomId The room in which to check.
     * @param userId The id of the user to check.
     * @return The time in seconds since the last question was posted.
     */
    public long timeSinceLastQuestionByUser(String roomId, String userId) {
        //noinspection ConstantConditions
        return jdbcTemplate.query(QUERY_MOST_RECENT_QUESTION_FROM_USER,
            (ps) -> {
                ps.setString(1, roomId);
                ps.setString(2, userId);
            }, (rs) -> {
                if (rs.next()) {
                    return (new Date().getTime() - rs.getTimestamp("postedAt").getTime()) / 1000;
                }

                return Long.MAX_VALUE;
            });
    }
}
