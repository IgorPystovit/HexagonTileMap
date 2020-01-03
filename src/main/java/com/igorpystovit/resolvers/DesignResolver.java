package com.igorpystovit.resolvers;

import com.igorpystovit.HexShape;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

//@Slf4j
@Getter
@Setter
public class DesignResolver {
    private Set<HexShape> managedHexagon;
    private Map<HexShape, Polygon> hexPolygonMap;
    private Set<Transition> activeTransitions;

    public DesignResolver(Set<HexShape> managedHexagon) {
        this.managedHexagon = managedHexagon;
        this.activeTransitions = new HashSet<>();
        this.hexPolygonMap = initHexPolygonMap(managedHexagon);
    }

    public void playTransitionOn(HexShape hex) {
        Polygon polygon = hexPolygonMap.get(hex);
        if (polygon != null) {
            FillTransition fillTransition = new FillTransition(Duration.millis(800), polygon);
            fillTransition.setFromValue(Color.valueOf(polygon.getFill().toString()));
            fillTransition.setToValue(Color.WHITE);
            fillTransition.setAutoReverse(true);
            fillTransition.setCycleCount(Animation.INDEFINITE);
            fillTransition.play();
            activeTransitions.add(fillTransition);
        }
    }

    public void highlightHexes(Collection<HexShape> hexes) {
        hexPolygonMap.entrySet()
                .stream()
                .filter(hexPolygonEntry -> hexes.contains(hexPolygonEntry.getKey()))
                .forEach(hexPolygonEntry -> hexPolygonEntry.getValue().setFill(Color.WHITE));
    }

    public void resetToDefault() {
        activeTransitions.forEach(Transition::stop);
        activeTransitions.clear();
        hexPolygonMap.values().forEach(this::setDefaultStyling);
    }

    public Map<HexShape, Polygon> getHexPolygonMap() {
        if (hexPolygonMap.isEmpty()) {
            hexPolygonMap = initHexPolygonMap(managedHexagon);
        }
        return hexPolygonMap;
    }

    public void setManagedHexagon(Set<HexShape> managedHexagon) {
        this.managedHexagon = managedHexagon;
        hexPolygonMap = initHexPolygonMap(managedHexagon);
    }

    private Map<HexShape, Polygon> initHexPolygonMap(Collection<HexShape> hexagon) {
        Map<HexShape, Polygon> hexPolygonMap = new LinkedHashMap<>();

        for (HexShape tempHex : hexagon) {
            Polygon polygon = new Polygon();
            PolygonStyling polygonStyling = getPolygonStyling();

            tempHex
                    .getCoordinateMap()
                    .values()
                    .forEach(pair -> polygon.getPoints().addAll(pair.getX(), pair.getY()));

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
