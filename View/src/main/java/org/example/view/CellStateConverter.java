package org.example.view;

import javafx.util.StringConverter;

public class CellStateConverter extends StringConverter<Boolean> {
    private static final String FULL_BLOCK = "█";

    @Override
    public String toString(Boolean object) {
        return object ? "1" : "0";
    }

    @Override
    public Boolean fromString(String string) {
        return "1".equalsIgnoreCase(string);
    }

    public String toColor(Boolean object) {
        return object ? "#00ff00" : "#777777";
    }

    public String toBlock(Boolean object) {
        return FULL_BLOCK;
    }
}
