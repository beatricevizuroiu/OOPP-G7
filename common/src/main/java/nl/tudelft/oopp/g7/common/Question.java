package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor @Data
public class Question {
    private int id;
    private String text;
    private String answer;
    private Date postedAt;
    private int upvotes;
    private boolean answered;
    private boolean edited;
}
