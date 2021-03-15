package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor
@Data
public class Room {
    private String id;
    private String studentPassword;
    private String moderatorPassword;
    private String name;
    private boolean isOpen;
    private boolean isOver;
    private Date startDate;
}
