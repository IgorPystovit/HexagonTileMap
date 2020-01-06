package com.igorpystovit.resolver.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.resolver.api.DesignResolver;
import com.igorpystovit.service.api.HexagonService;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolver that manages {@link HexShape} as {@link Polygon}
 * It also contains methods to change the state of certain {@link Polygon} by {@link HexShape} UUID
 * */
@Getter
@Setter
@Component
public class DesignResolverImpl implements DesignResolver {
    private Hexagon managedHexagon;
    private Map<HexShape, Polygon> hexPolygonMap;
    private HexagonService hexagonService;

    @Autowired
    public DesignResolverImpl(HexagonService hexagonService) {
        this.hexagonService = hexagonService;
        this.managedHexagon = new Hexagon();
        this.hexPolygonMap = initHexPolygonMap(managedHexagon);
    }

    public void highlightHexes(Collection<UUID> uuids) {
        Set<HexShape> hexes = uuids
                .stream()
                .map(hexagonService::getByUUID)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        hexPolygonMap.entrySet()
                .stream()
                .filter(hexPolygonEntry -> hexes.contains(hexPolygonEntry.getKey()))
                .forEach(hexPolygonEntry -> hexPolygonEntry.getValue().setFill(Color.WHITE));
    }

    public void resetToDefault() {
        hexPolygonMap.values().forEach(this::setDefaultStyling);
    }

    public Map<HexShape, Polygon> getHexPolygonMap() {
        if (hexPolygonMap.isEmpty()) {
            hexPolygonMap = initHexPolygonMap(managedHexagon);
        }
        return hexPolygonMap;
    }

    public void setManagedHexagon(Hexagon managedHexagon) {
        this.managedHexagon = managedHexagon;
        hexagonService.setManagedHexagon(managedHexagon);
        hexPolygonMap = initHexPolygonMap(managedHexagon);
    }

    private Map<HexShape, Polygon> initHexPolygonMap(Hexagon hexagon) {
        Map<HexShape, Polygon> hexPolygonMap = new LinkedHashMap<>();

        for (HexShape tempHex : hexagon.getHexes()) {
            Polygon polygon = new Polygon();
            PolygonStyling polygonStyling = getPolygonStyling();

            tempHex
                    .getCoordinateMap()
                    .values()
                    .forEach(pair -> polygon.getPoints().addAll(pair.getLeft(), pair.getRight()));

            polygon.setStrokeWidth(polygonStyling.getStrokeWidth());
            polygon.setStroke(polygonStyling.getStrokeColor());
            polygon.setFill(polygonStyling.getFill());

            hexPolygonMap.putIfAbsent(tempHex, polygon);
        }
        return hexPolygonMap;
    }


    private void setDefaultStyling(Polygon polygon) {
        PolygonStyling polygonStyling = getPolygonStyling();
        polygon.setFill(polygonStyling.getFill());
        polygon.setStrokeWidth(polygonStyling.getStrokeWidth());
        polygon.setStroke(polygonStyling.getStrokeColor());
    }

    private PolygonStyling getPolygonStyling() {
        return new PolygonStyling(5, Color.BLACK, Color.valueOf("#ab8c1c"));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class PolygonStyling {
        private int strokeWidth;
        private Color strokeColor;
        private Color fill;
    }
}
