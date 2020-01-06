package com.igorpystovit.resolver.api;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.util.Pair;

public interface InitPointPositionResolver {
    Pair<Double> resolveConnectionInitPoint(int position, HexShape parentHex);
}
