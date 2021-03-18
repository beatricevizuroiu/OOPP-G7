package nl.tudelft.oopp.g7.server.repositories;

import nl.tudelft.oopp.g7.common.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(QuestionRepository.class);

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS questions("
            + "id serial PRIMARY KEY not NULL,"
            + "userID int not NULL,"
            + "roomID varchar(36) not NULL,"
            + "text text not NULL,"
            + "answer text DEFAULT '' not NULL,"
            + "postedAt timestamp with time zone DEFAULT NOW(),"
            + "upvotes int not NULL DEFAULT 0,"
            + "answered boolean DEFAULT FALSE not NUll,"
            + "edited boolean DEFAULT FALSE not NULL,"
            + "FOREIGN KEY (roomID) REFERENCES rooms(id));";

    private static final String QUERY_SELECT_QUESTION_BY_ID = "SELECT * FROM questions WHERE id=? AND roomID=?;";
    private static final String QUERY_SELECT_ALL_QUESTIONS = "SELECT * FROM questions WHERE roomID=?;";
    private static final String QUERY_CREATE_QUESTION = "INSERT INTO questions (userID, roomID, text) VALUES (?, ?, ?);";
    private static final String QUERY_ANSWER_QUESTION = "UPDATE questions SET answer=?, answered=true WHERE id=? AND roomID=?;";
    private static final String QUERY_EDIT_QUESTION = "UPDATE questions SET text=?, edited=true WHERE id=? AND roomID=?;";
    private static final String QUERY_UPVOTE_QUESTION = "UPDATE questions SET upvotes = upvotes + 1 WHERE id=? AND roomID=?;";
    private static final String QUERY_DELETE_QUESTION = "DELETE FROM questions WHERE id=? AND roomID=?;";

    /**
     * Primary constructor for the question repository.
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
     * Get a single question from the database.
     * @param roomId The id of the room of the question.
     * @param id The id of the question.
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
     * Get all question within a specific room from the database.
     * @param roomId The id of the room to list the questions from.
     * @return The {@link List} of {@link Question}s that are in the room.
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
     * Add an upvote to a question in the database.
     * @param roomId The id of the room the question is in.
     * @param id The id of the question to upvote.
     * @return The amount of rows changed by the query.
     */
    public int upvoteQuestionWithId(String roomId, int id) {
        logger.debug("Adding an upvote to question with id: {}", id);
        return jdbcTemplate.update(QUERY_UPVOTE_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the question id.
                ps.setInt(1, id);

                // Set the second variable in the PreparedStatement to the room id.
                ps.setString(2, roomId);
            });
    }

    /**
     * Edit a question's body in the database.
     * @param roomId The id of the room to edit the question in.
     * @param id The id of the question.
     * @param newBody The new body of the question.
     * @return The amount of rows changed by the query.
     */
    public int editQuestionWithId(String roomId, int id, String newBody) {
        logger.debug("Changing the text of question with id: {}, to \"{}\"", id, newBody);
        return jdbcTemplate.update(QUERY_EDIT_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the new text body of the question.
                ps.setString(1, newBody);

                // Set the second variable in the PreparedStatement to the id of the question to update.
                ps.setInt(2, id);

                // Set the third variable in the PreparedStatement to the room id.
                ps.setString(3, roomId);
            });
    }

    /**
     * Delete a question from the database.
     * @param roomId The id of the room to delete the question from.
     * @param id The id of the question to delete.
     * @return The amount of rows changed by the query.
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
     * Create a new question in the database.
     * @param roomId The id of the room to create the question in.
     * @param questionText The text for the new question.
     * @return The amount of rows changed by the query.
     */
    public int createQuestion(String roomId, String questionText) {
        logger.debug("Storing a new question with text: \"{}\"", questionText);
        return jdbcTemplate.update(QUERY_CREATE_QUESTION,
            (ps) -> {
                // Set the first variable in the PreparedStatement to the user id.
                // TODO: This is currently just a placeholder user ID.
                ps.setInt(1, 0);

                // Set the second variable in the PreparedStatement to the room id.
                ps.setString(2, roomId);

                // Set the third variable in the PreparedStatement to the text of the new question.
                ps.setString(3, questionText);
            });
    }

    /**
     * Store the answer to a question into the database.
     * @param roomId The room the question is in.
     * @param id The id of the question.
     * @param answer The answer to the question.
     * @return The amount of rows that where changed by the query.
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
}
