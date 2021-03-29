package nl.tudelft.oopp.g7.server.controllers;

import nl.tudelft.oopp.g7.common.BanReason;
import nl.tudelft.oopp.g7.common.UserInfo;
import nl.tudelft.oopp.g7.common.UserRole;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UpvoteRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.authorization.AuthorizationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private UserController userController;
    private MockHttpServletRequest request_stud;
    private MockHttpServletRequest request_mod;

    private final String TEST_ROOM_ID = "SIfhfCMwN6np3WcMW27ka4hAwBtS1pRVetvH";

    private final String AUTHORIZATION_STUDENT = "Bearer Ftqp8J5Ub8PcUO0qJDXGuAooXZZfzZrZbfb51pCeYWDchzf6wyuwtFNzYeEeacE7k82Xn7y6ue9KWxPmP0eENubnz3PMelle4i9NLKb0RiQiVCDK8xdDjuu1uacyHdTC";
    private final String AUTHORIZATION_MODERATOR = "Bearer Dm1J7ZsghOtyvFnbMEMWrJDlWHteOGx3rr60stqn405f4sdgPqsj8wO9lWcGkrNGCYf5yH9Y1efaMgnD32hUwaSi3Jsi1mdtXUBK2U7C2HdqdAPdnuUqih2ihmjMk5lG";
    private final String AUTHORIZATION_EMPTY = "";
    private final String QUERY_CHECK_BANNED = "SELECT COUNT(ip) FROM bannedUsers WHERE ip=?";

    @BeforeEach
    void setUp() throws IOException {
        // Setup an in memory database with H2.
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");

        // Create a jdbc template with the datasource.
        jdbcTemplate = new JdbcTemplate(dataSource);

        // Read the file with test information and run the queries on the database.
        String sqlQueries = Files.readString(Path.of(new File("src/test/resources/test-question.sql").getPath()));
        jdbcTemplate.execute(sqlQueries);

        QuestionRepository questionRepository = new QuestionRepository(jdbcTemplate);
        UserRepository userRepository = new UserRepository(jdbcTemplate);
        BanRepository banRepository = new BanRepository(jdbcTemplate);
        UpvoteRepository upvoteRepository = new UpvoteRepository(jdbcTemplate);

        // Create our questionController with our in memory datasource.
        userController = new UserController(
                userRepository,
                banRepository,
                new AuthorizationHelper(
                        userRepository,
                        banRepository,
                        questionRepository),
                upvoteRepository);

        request_stud = new MockHttpServletRequest();
        request_stud.setRemoteAddr("127.10.0.1");

        request_mod = new MockHttpServletRequest();
        request_mod.setRemoteAddr("127.11.0.1");
    }

    @Test
    void getUserInfoById() {
        ResponseEntity<UserInfo> actual = userController.getUserInfoById(TEST_ROOM_ID, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", AUTHORIZATION_STUDENT, request_stud);

        UserInfo expected = new UserInfo(
                "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q",
                TEST_ROOM_ID,
                "Student name",
                UserRole.STUDENT
        );


        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void getAllUsersInRoom() {
        ResponseEntity<List<UserInfo>> actual = userController.getAllUsersInRoom(TEST_ROOM_ID, AUTHORIZATION_STUDENT, request_stud);

        List<UserInfo> expected = List.of(
                new UserInfo(
                        "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q",
                        TEST_ROOM_ID,
                        "Student name",
                        UserRole.STUDENT
                ),
                new UserInfo(
                        "eWMuhJfj41ShR1BdPGbLg2NKZ7FIEMb4VE6R",
                        TEST_ROOM_ID,
                        "Moderator name",
                        UserRole.MODERATOR
                )
        );

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void getUpvotesByUser() {
        ResponseEntity<List<Integer>> actual = userController.getUpvotesByUser(TEST_ROOM_ID, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", AUTHORIZATION_STUDENT, request_stud);

        List<Integer> expected = List.of(2);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void banUserById() {
        ResponseEntity<Void> actual = userController.banUserById(TEST_ROOM_ID, "RhNWf7SijmtQO8FIaaXNqKc13jvz4uuB4L9Q", new BanReason("You are banned!"), AUTHORIZATION_MODERATOR, request_mod);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(1, jdbcTemplate.query(QUERY_CHECK_BANNED,
            ps -> ps.setString(1, "127.10.0.1"),
            (ResultSetExtractor<Integer>) rs -> {
                rs.next();
                return rs.getInt(1);
            }));
    }
}