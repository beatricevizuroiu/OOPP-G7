package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.Exceptions.UnauthorizedException;

public class BelongsToRoom extends AuthorizationCondition {
    @Override
    public void check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository) throws UnauthorizedException {
        if (!user.getRoomId().equals(roomId)) {
            throw new UnauthorizedException();
        }
    }
}
