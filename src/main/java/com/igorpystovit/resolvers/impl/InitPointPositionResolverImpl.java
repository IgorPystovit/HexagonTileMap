package com.igorpystovit.resolvers.impl;

import com.igorpystovit.HexShape;
import com.igorpystovit.resolvers.InitPointPositionResolver;
import com.igorpystovit.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class InitPointPositionResolverImpl implements InitPointPositionResolver {
    public void resolveConnectionsInitPoints(HexShape hexShape) {
        Map<Integer, UnaryOperator<Pair<Double>>> strategyMap = getStrategyMap();
        Map<Integer, HexShape> connections = hexShape.getConnections();

        if (!connections.isEmpty() || (hexShape.isRoot())) {
            connections
                    .forEach((position, shape) -> {
                        if (!shape.containsInitPair()) {
                            UnaryOperator<Pair<Double>> strategy = strategyMap.get(position);
                            shape.setInitPair(strategy.apply(hexShape.getCoordinateMap().get(position)));
                        }
                    });
        }
    }

    public Pair<Double> resolveConnectionInitPoint(int connectionPosition,HexShape parentHex) {
        return getStrategyMap()
                .get(connectionPosition)
                .apply(parentHex.getCoordinateMap().get(connectionPosition));
    }

    private Map<Integer, UnaryOperator<Pair<Double>>> getStrategyMap() {
        Map<Integer, UnaryOperator<Pair<Double>>> strategyMap = new LinkedHashMap<>();
        strategyMap.put(1, pair -> new Pair<>(pair.getLeft(), pair.getRight() - (HexShape.SPOT_SIZE * 2)));
        strategyMap.put(2, pair -> new Pair<>(pair.getLeft() + (HexShape.SPOT_SIZE / 2.0), pair.getRight() - HexShape.SPOT_SIZE));
        strategyMap.put(3, pair -> new Pair<>(pair.getLeft(), pair.getRight()));
        strategyMap.put(4, pair -> new Pair<>(pair.getLeft() - HexShape.SPOT_SIZE, pair.getRight()));
        strategyMap.put(5, pair -> new Pair<>(pair.getLeft() - (HexShape.SPOT_SIZE + (HexShape.SPOT_SIZE / 2.0)), pair.getRight() - HexShape.SPOT_SIZE));
        strategyMap.put(6, pair -> new Pair<>(pair.getLeft() - HexShape.SPOT_SIZE, pair.getRight() - (HexShape.SPOT_SIZE * 2)));
        return strategyMap;
    }
}
