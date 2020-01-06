package com.igorpystovit.entity;

import com.igorpystovit.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class HexShape {
    //init values for other shapes to coordinate themselves
    //spot size in px
    public static final int SPOT_SIZE = 50;
    //map of coordinates for shape to store its position
    private UUID uuid = UUID.randomUUID();
    private Map<Integer, Pair<Double>> coordinateMap = new LinkedHashMap<>();
    private Map<Integer, UUID> connections = new LinkedHashMap<>();
    private int value;
    private boolean root;

    public HexShape(int value) {
        this.value = value;
    }

    public HexShape(double initX, double initY) {
        setInitPair(new Pair<>(initX, initY));
        initCoordinateMap();
    }


    public HexShape(double initX, double initY, int value) {
        setInitPair(new Pair<>(initX, initY));
        initCoordinateMap();
        this.value = value;
    }

    public HexShape(Pair<Double> pair) {
        setInitPair(pair);
        initCoordinateMap();
    }

    private void initCoordinateMap() {
        if (coordinateMap.get(1) != null) {
            double initX = coordinateMap.get(1).getLeft();
            double initY = coordinateMap.get(1).getRight();

            coordinateMap.put(2, new Pair<>(initX + SPOT_SIZE, initY));
            coordinateMap.put(3, new Pair<>(initX + SPOT_SIZE + (SPOT_SIZE / 2.0), initY + SPOT_SIZE));
            coordinateMap.put(4, new Pair<>(initX + SPOT_SIZE, initY + (SPOT_SIZE * 2)));
            coordinateMap.put(5, new Pair<>(initX, initY + (SPOT_SIZE * 2)));
            coordinateMap.put(6, new Pair<>(initX - (SPOT_SIZE / 2.0), initY + SPOT_SIZE));
        } else {
            log.warn("Cannot initialize coordinate map");
            ;
            throw new NoSuchElementException();
        }
    }

    public Map<Integer, Pair<Double>> getCoordinateMap() {
        if (mapIsFullyInitialized()) {
            return coordinateMap;
        } else if (!mapIsFullyInitialized() && containsInitPair()) {
            initCoordinateMap();
            return coordinateMap;
        } else {
            log.warn("Map is not fully initialized");
            ;
            throw new NoSuchElementException();
        }
    }

    private boolean mapIsFullyInitialized() {
        return coordinateMap.size() >= 6;
    }

    public void setInitPair(Pair<Double> initPair) {
        coordinateMap.put(1, initPair);
        initCoordinateMap();
    }

    public Pair<Double> getInitPair() {
        return coordinateMap.get(1);
    }

    public boolean containsInitPair() {
        return getInitPair() != null;
    }

    public List<Integer> getAvailablePositions() {
        if (connections.size() < 6) {
            return Stream.of(1, 2, 3, 4, 5, 6)
                    .filter(position -> !connections.containsKey(position))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Optional<Integer> getPositionOfConnection(UUID hexUuid) {

        if (this.connections.containsValue(hexUuid)) {
            return this.connections.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(hexUuid))
                    .map(Map.Entry::getKey)
                    .findAny();
        }
        return Optional.empty();
    }

    public boolean isConnectionPresent(int position) {
        return connections.get(position) != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Value = " + value + "\n");
        sb.append("Dependencies: \n");
        for (Map.Entry<Integer, UUID> hexShapeEntry : connections.entrySet()) {
            sb.append(hexShapeEntry.getKey() + " = " + hexShapeEntry.getValue() + "\n");
        }
        return sb.toString();
    }

}
