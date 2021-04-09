package nl.tudelft.oopp.g7.common;

import lombok.Data;

@Data
public class OptionsPosition {
    private int position;

    public OptionsPosition() {
        this.position = 0;
    }

    public void increment() {
        position++;
    }

    public void decrement() {
        position--;
    }
}
