package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import com.igorpystovit.util.Pair;

public interface InitPointPositionResolver {
    void resolveConnectionsInitPoints(HexShape hexShape);
    Pair<Double> resolveConnectionInitPoint(int position,HexShape parentHex);
}
