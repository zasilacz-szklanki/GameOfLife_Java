package org.example.Model;

//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class GameOfLifeRow extends GameOfLifeSegment {
    public GameOfLifeRow(List<List<GameOfLifeCell>> board, int index) {
        super(board, index, true);
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("\n");
//        for (GameOfLifeCell cell : segment) {
//            builder.append(cell.getCellValue() ? "alive" : "dead").append(" ");
//        }
//        builder.append("\n");
//        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(builder.toString()).toString();
//    }
}