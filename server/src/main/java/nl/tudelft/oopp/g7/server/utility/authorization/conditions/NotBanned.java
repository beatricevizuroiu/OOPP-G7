package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.exceptions.UnauthorizedException;

public class NotBanned extends AuthorizationCondition {

    @Override
    public void check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository, RoomRepository roomRepository) throws UnauthorizedException {
        if (banRepository.checkBanned(roomId, ip)) {
            throw new UnauthorizedException();
        }
    }
}
