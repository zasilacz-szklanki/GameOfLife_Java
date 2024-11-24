package org.example.Model;

//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class GameOfLifeColumn extends GameOfLifeSegment {
    public GameOfLifeColumn(List<List<GameOfLifeCell>> board, int index) {
        super(board, index, false);
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("\n");
//        for (GameOfLifeCell cell : segment) {
//            builder.append(cell.getCellValue() ? "alive" : "dead").append("\n");
//        }
//        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(builder.toString()).toString();
//    }
}
