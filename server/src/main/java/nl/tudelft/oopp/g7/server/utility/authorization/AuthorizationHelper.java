package nl.tudelft.oopp.g7.server.utility.authorization;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.RandomString;
import nl.tudelft.oopp.g7.server.utility.authorization.conditions.AuthorizationCondition;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {
    private final UserRepository userRepository;
    private final BanRepository banRepository;
    private final QuestionRepository questionRepository;

    public AuthorizationHelper(UserRepository userRepository, BanRepository banRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.banRepository = banRepository;
        this.questionRepository = questionRepository;
    }

    public String createAuthorizationToken() {
        String authorizationToken;

        RandomString randomString = new RandomString(128);

        do {
            authorizationToken = randomString.nextString();
        } while (userRepository.getUserByToken(authorizationToken) != null);

        return authorizationToken;
    }

    public boolean isAuthorized(String roomId, String authorizationHeader, String ip, AuthorizationCondition condition) {
        String token = authorizationHeader.split(" ")[1];
        User user = userRepository.getUserByToken(token);

        return condition.check(roomId, user, ip, banRepository, userRepository, questionRepository);
    }
}
