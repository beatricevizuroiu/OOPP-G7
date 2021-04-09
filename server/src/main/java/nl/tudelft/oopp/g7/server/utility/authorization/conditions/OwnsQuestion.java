package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.RoomRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;
import nl.tudelft.oopp.g7.server.utility.exceptions.UnauthorizedException;

public class OwnsQuestion extends AuthorizationCondition {
    private final int questionId;

    public OwnsQuestion(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public void check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository, RoomRepository roomRepository) throws UnauthorizedException {
        Question question = questionRepository.getQuestionById(roomId, questionId);

        if (!question.getAuthorId().equals(user.getId())) {
            throw new UnauthorizedException();
        }
    }
}
