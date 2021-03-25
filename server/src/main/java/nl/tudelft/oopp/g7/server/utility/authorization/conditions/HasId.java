package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;

public class HasId extends AuthorizationCondition {
    private final String id;

    public HasId(String id) {
        this.id = id;
    }

    @Override
    public boolean check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        return user.getId().equals(id);
    }
}
