package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class NewRoom {
    @NotNull @NotEmpty
    private String name;
    @NotNull
    private String studentPassword = "";
    @NotNull
    private String moderatorPassword;
    @NotNull
    private Date startDate;
}
