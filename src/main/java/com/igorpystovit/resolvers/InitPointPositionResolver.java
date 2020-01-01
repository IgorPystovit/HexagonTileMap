package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import com.igorpystovit.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

public class InitPointPositionResolver {
    @Getter
    @Setter
    private Set<HexShape> isolatedHexes = new HashSet<>();

    public void resolveConnectionsInitPoints(HexShape hexShape){
        Map<Integer, UnaryOperator<Pair>> strategyMap = getStrategyMap();
        Map<Integer,HexShape> connections = hexShape.getConnections();

        if (!connections.isEmpty() || (hexShape.isRoot())){
            connections
                    .forEach((position, shape) -> {
                        if (!shape.containsInitPair()){
                            UnaryOperator<Pair> strategy = strategyMap.get(position);
                            shape.setInitPair(strategy.apply(hexShape.getCoordinateMap().get(position)));
                        }
                    });
        }
    }


    private Map<Integer, UnaryOperator<Pair>> getStrategyMap(){
        Map<Integer, UnaryOperator<Pair>> strategyMap = new LinkedHashMap<>();
        strategyMap.put(1,pair -> new Pair(pair.getX() , pair.getY() - (HexShape.SPOT_SIZE * 2)));
        strategyMap.put(2,pair -> new Pair(pair.getX() + (HexShape.SPOT_SIZE / 2.0), pair.getY() - HexShape.SPOT_SIZE));
        strategyMap.put(3,pair -> new Pair(pair.getX(),pair.getY()));
        strategyMap.put(4,pair -> new Pair(pair.getX() - HexShape.SPOT_SIZE ,pair.getY()));
        strategyMap.put(5,pair -> new Pair(pair.getX() - (HexShape.SPOT_SIZE + (HexShape.SPOT_SIZE / 2.0)) ,pair.getY() - HexShape.SPOT_SIZE));
        strategyMap.put(6,pair -> new Pair(pair.getX() - HexShape.SPOT_SIZE,pair.getY() - (HexShape.SPOT_SIZE * 2)));
        return strategyMap;
    }
}
