package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import com.igorpystovit.Hexagon;
import javafx.scene.shape.Polygon;

import java.util.Collection;
import java.util.Map;

public interface DesignResolver {
    void highlightHexes(Collection<HexShape> hexes);

    void resetToDefault();

    void playTransitionOn(HexShape hex);

    Map<HexShape, Polygon> getHexPolygonMap();

    void setManagedHexagon(Hexagon hexagon);
}
