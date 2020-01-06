package com.igorpystovit.validator;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.util.Pair;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HexPositionValidator {

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

    public static int validatePosition(int position) {
        if (position > 6) {
            position -= 6;
        } else if (position <= 0) {
            position = 6;
        }
        return position;
    }
}
