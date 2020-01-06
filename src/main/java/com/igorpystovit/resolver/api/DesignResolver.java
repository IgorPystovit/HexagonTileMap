package com.igorpystovit.resolver.api;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import javafx.scene.shape.Polygon;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface DesignResolver {
    void highlightHexes(Collection<UUID> hexes);

    void resetToDefault();

    Map<HexShape, Polygon> getHexPolygonMap();

    void setManagedHexagon(Hexagon hexagon);
}
