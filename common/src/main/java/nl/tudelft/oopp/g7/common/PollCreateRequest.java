package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor
@Data
public class PollCreateRequest {
    // The question being asked.
    @NotNull @NotEmpty
    private String question;
    // The answer options for the question.
    @NotNull @NotEmpty
    private String[] options;
    // Whether the results of the poll should be public during the voting. (Numbers only, no individual votes)
    private boolean hasPublicResults;

}
