package nl.tudelft.oopp.g7.server.utility.authorization;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.Exceptions.UnauthorizedException;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.AuthorizationCondition;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {
    private final UserRepository userRepository;
    private final BanRepository banRepository;
    private final QuestionRepository questionRepository;

    /**
     * The constructor for the AuthorizationHelper class.
     * @param userRepository The UserRepository instance that is currently in use.
     * @param banRepository The BanRepository instance that is currently in use.
     * @param questionRepository The QuestionRepository instance that is currently in use.
     */
    public AuthorizationHelper(UserRepository userRepository, BanRepository banRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Generates a random Authorization Token for a new user.
     * @return A new Authorization Token as a String.
     */
    public String createAuthorizationToken() {
        String authorizationToken;

        RandomString randomString = new RandomString(128);

        do {
            authorizationToken = randomString.nextString();
        } while (userRepository.getUserByToken(authorizationToken) != null);

        return authorizationToken;
    }

    /**
     * Splits an Authorization Header into the header and the token and returns the token for further use if possible.
     * @param header A raw Authorization Header.
     * @return A usable Authorization Token.
     */
    public String parseAuthorizationHeader(String header) {
        String[] authParts = header.split(" ");
        if (authParts.length < 2)
            return null;

        return authParts[1];
    }

    /**
     * Searches the database for the User associated with an Authorization Header.
     * @param header A raw Authorization Header.
     * @return A User.
     */
    public User getUserFromAuthorizationHeader(String header) {
        String token = parseAuthorizationHeader(header);
        if (token == null)
            return null;

        return userRepository.getUserByToken(token);
    }

    /**
     * Checks if a User meets a certain authorization requirement to perform an action.
     * @param roomId The roomId of the Room in which the User is acting.
     * @param authorizationHeader The Authorization Header associated with the User.
     * @param ip The public Ip address of the User.
     * @param condition The condition to check.
     * @return Whether the User meets the given condition in a boolean.
     */
    public void checkAuthorization(String roomId, String authorizationHeader, String ip, AuthorizationCondition condition) throws UnauthorizedException {
        User user = getUserFromAuthorizationHeader(authorizationHeader);
        if (user == null) {
            throw new UnauthorizedException();
        }

        condition.check(roomId, user, ip, banRepository, userRepository, questionRepository);
    }
}
