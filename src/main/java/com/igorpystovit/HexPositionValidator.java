package com.igorpystovit;

import com.igorpystovit.util.Pair;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HexPositionValidator {
    private int screenWidth = 1600;
    private int screenHeight = 800;

    public HexPositionValidator(int screenWidth,int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public boolean isScreenPositionValid(HexShape hexShape, int width, int height) {
        boolean yAxisCriteriaMet = false;
        boolean xAxisCriteriaMet = false;

        if (hexShape.containsInitPair()) {
            yAxisCriteriaMet = hexShape.getCoordinateMap().values()
                    .stream()
                    .mapToDouble(Pair::getRight)
                    .noneMatch(y -> (y <= 0) || (y >= height));

            xAxisCriteriaMet = hexShape.getCoordinateMap().values()
                    .stream()
                    .mapToDouble(Pair::getLeft)
                    .noneMatch(x -> (x <= 0) || (x >= width));
        }

        return yAxisCriteriaMet && xAxisCriteriaMet;
    }

}
