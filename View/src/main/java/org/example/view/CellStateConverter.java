package org.example.view;

import javafx.util.StringConverter;

public class CellStateConverter extends StringConverter<Boolean> {
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

    public Boolean fromColor(String color) {
        return "#00ff00".equalsIgnoreCase(color);
    }
}