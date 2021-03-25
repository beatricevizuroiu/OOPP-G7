package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;

public class OneOf extends AuthorizationCondition {
    private final AuthorizationCondition[] conditions;

    public OneOf(AuthorizationCondition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        for (AuthorizationCondition condition : conditions) {
            if (condition.check(roomId, user, ip, banRepository, userRepository, questionRepository))
                return true;
        }
        return false;
    }
}
