package com.igorpystovit.resolver.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.resolver.api.InitPointPositionResolver;
import com.igorpystovit.util.Pair;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Resolver that provides right placing for {@link HexShape} init point
 * regarding the position of parent hex
 *
 * For such calculation it uses strategy map
 * that contains certain strategy for certain child hex position
 *
 * */
@Component
public class InitPointPositionResolverImpl implements InitPointPositionResolver {

    public Pair<Double> resolveConnectionInitPoint(int connectionPosition, HexShape parentHex) {
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
