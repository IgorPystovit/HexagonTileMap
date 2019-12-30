package com.igorpystovit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class InitPointPositionResolver {

    private Map<Integer, UnaryOperator<Pair>> getStrategyMap(){
        Map<Integer, UnaryOperator<Pair>> strategyMap = new LinkedHashMap<>();
        strategyMap.put(1,pair -> new Pair(pair.getX(), pair.getY() - (HexShape.SPOT_SIZE * 2)));
        strategyMap.put(2,pair -> new Pair(pair.getX() + (HexShape.SPOT_SIZE / 2.0), pair.getY() - HexShape.SPOT_SIZE));
        strategyMap.put(3,pair -> new Pair(pair.getX(),pair.getY()));
        strategyMap.put(4,pair -> new Pair(pair.getX() - HexShape.SPOT_SIZE,pair.getY()));
        strategyMap.put(5,pair -> new Pair(pair.getX() - (HexShape.SPOT_SIZE + (HexShape.SPOT_SIZE / 2.0)) ,pair.getY() - HexShape.SPOT_SIZE));
        strategyMap.put(6,pair -> new Pair(pair.getX() - HexShape.SPOT_SIZE ,pair.getY() - (HexShape.SPOT_SIZE * 2)));
        return strategyMap;
    }

    public void resolveNear(HexShape hexShape){
        Map<Integer, UnaryOperator<Pair>> strategyNap = getStrategyMap();
        hexShape.getNearShapes()
                .forEach((position, shape) -> {
                    if (!shape.containsInitPair()){
                        UnaryOperator<Pair> strategy = strategyNap.get(position);
                        shape.getCoordinateMap().put(1, strategy.apply(hexShape.getCoordinates().get(position)));
                    }
                });
    }

}
