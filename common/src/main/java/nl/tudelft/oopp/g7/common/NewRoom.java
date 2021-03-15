package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class NewRoom {
    private String name;
    private String studentPassword;
    private String moderatorPassword;
    private Date startDate;
}
