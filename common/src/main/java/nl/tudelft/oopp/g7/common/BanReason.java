package nl.tudelft.oopp.g7.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor @NoArgsConstructor
@Data
public class BanReason {
    @NotNull
    private String reason;
}
