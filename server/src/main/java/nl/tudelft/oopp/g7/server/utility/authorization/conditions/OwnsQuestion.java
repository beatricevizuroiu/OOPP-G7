package nl.tudelft.oopp.g7.server.utility.authorization.conditions;

import nl.tudelft.oopp.g7.common.User;
import nl.tudelft.oopp.g7.common.Question;
import nl.tudelft.oopp.g7.server.repositories.BanRepository;
import nl.tudelft.oopp.g7.server.repositories.QuestionRepository;
import nl.tudelft.oopp.g7.server.repositories.UserRepository;

public class OwnsQuestion extends AuthorizationCondition {
    private final int questionId;

    public OwnsQuestion(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean check(String roomId, User user, String ip, BanRepository banRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        Question question = questionRepository.getQuestionById(roomId, questionId);
        return question.getAuthorId().equals(user.getId());
    }
}
