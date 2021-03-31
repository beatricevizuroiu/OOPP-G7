package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PollCreateRequest {
    // The question being asked.
    private String question;
    // The answer options for the question.
    private String[] options;
    // Whether the results of the poll should be public during the voting. (Numbers only, no individual votes)
    private boolean hasPublicResults;

}
