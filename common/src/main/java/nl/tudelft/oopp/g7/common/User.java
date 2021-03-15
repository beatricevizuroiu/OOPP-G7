package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Data
public class User {
    private String userName;
    private String role;
    private List<Question> ownedQuestions;
}
