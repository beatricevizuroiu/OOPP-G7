package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.exceptions.UnauthorizedException;

public class All extends AuthorizationCondition {
    private final AuthorizationCondition[] conditions;

    public All(AuthorizationCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public void check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository) throws UnauthorizedException {
        for (AuthorizationCondition condition : conditions) {
            condition.check(roomId, user, ip, banRepository, userRepository, questionRepository);
        }
    }
}
