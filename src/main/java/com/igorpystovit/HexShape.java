package com.igorpystovit;

import com.igorpystovit.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class HexShape {
    //init values for other shapes to coordinate themselves
    //spot size in px
    public static final int SPOT_SIZE = 50;
    //map of coordinates for shape to store its position
    private Map<Integer, Pair> coordinateMap = new TreeMap<>();
    private Map<Integer, HexShape> connections = new TreeMap<>();
    private int value;
    private boolean root;

    public HexShape(int value) {
        this.value = value;
    }

    public HexShape(double initX, double initY) {
        setInitPair(new Pair(initX, initY));
        initCoordinateMap();
    }


    public HexShape(double initX, double initY, int value) {
        setInitPair(new Pair(initX, initY));
        initCoordinateMap();
        this.value = value;
    }

    public HexShape(Pair pair) {
        setInitPair(pair);
        initCoordinateMap();
    }

    private void initCoordinateMap() {
        if (coordinateMap.get(1) != null) {
            double initX = coordinateMap.get(1).getX();
            double initY = coordinateMap.get(1).getY();

            coordinateMap.put(2, new Pair(initX + SPOT_SIZE, initY));
            coordinateMap.put(3, new Pair(initX + SPOT_SIZE + (SPOT_SIZE / 2.0), initY + SPOT_SIZE));
            coordinateMap.put(4, new Pair(initX + SPOT_SIZE, initY + (SPOT_SIZE * 2)));
            coordinateMap.put(5, new Pair(initX, initY + (SPOT_SIZE * 2)));
            coordinateMap.put(6, new Pair(initX - (SPOT_SIZE / 2.0), initY + SPOT_SIZE));
        } else {
            System.out.println("Cannot initialize coordinate map");
            throw new NoSuchElementException();
        }
    }

    public Map<Integer, Pair> getCoordinateMap() {
        if (mapIsFullyInitialized()) {
            return coordinateMap;
        } else if (!mapIsFullyInitialized() && containsInitPair()) {
            initCoordinateMap();
            return coordinateMap;
        } else {
            System.out.println("Map is not fully initialized");
            throw new NoSuchElementException();
        }
    }

    private boolean mapIsFullyInitialized() {
        return coordinateMap.size() >= 6;
    }

    public void setInitPair(Pair initPair) {
        coordinateMap.put(1, initPair);
        initCoordinateMap();
    }

    public Pair getInitPair() {
        return coordinateMap.get(1);
    }

    public boolean containsInitPair() {
        return getInitPair() != null;
    }


    //TODO create service with crud methods
    public void removeConnection(HexShape shape) {
        if (this.connections.containsValue(shape)) {
            int position = connections.entrySet()
                    .stream()
                    .filter(connectionEntry -> connectionEntry.getValue().equals(shape))
                    .findAny()
                    .orElseThrow(NoSuchElementException::new)
                    .getKey();
            remove(position);
            new LinkedHashSet<>(shape.connections.keySet()).forEach(shape::remove);
        }
    }

    public void removeByPosition(int position) {
        if (this.connections.containsKey(position)) {
            HexShape shape = this.connections.get(position);
            remove(position);
            new HashSet<>(shape.connections.keySet()).forEach(shape::remove);
        }
    }

    private void remove(int position) {
        if (this.connections.containsKey(position)) {
            HexShape shapeToRemove = this.connections.get(position);
            this.connections.remove(position);
            shapeToRemove.getConnections().remove(optimizePosition(position + 3));
        }
    }

    public void addConnection(HexShape shape) {
        if ((connections.size() < 6) && (!connections.containsValue(shape))) {
            for (int i = 1; i <= 6; i++) {
                if (connections.get(i) == null) {
                    link(i, shape);
                    formRingOfDependencies(i).forEach(shape::link);
                    break;
                }
            }
        }
    }

    private void link(int position, HexShape hexShape) {
        int mirrorPosition = optimizePosition(position + 3);
        if (!this.isDependencyPresent(position)) {
            this.connections.put(position, hexShape);
            hexShape.link(mirrorPosition, this);
        }
    }


    private boolean isDependencyPresent(int position) {
        return connections.get(position) != null;
    }

    private Map<Integer, HexShape> formRingOfDependencies(int position) {
        Map<Integer, HexShape> ringOfDependencies = new TreeMap<>();


        int tempPosition = optimizePosition(position - 1);
        HexShape tempHexShapeDependency = this.connections.get(tempPosition);


        for (int t = 0; t < 2; t++) {

            if (tempHexShapeDependency != null) {
                //do six iterations because ring consists of no more than six hexes
                for (int i = 0; i < 6; i++) {

                    if (tempHexShapeDependency == null) {
                        continue;
                    }

                    if (t == 0) {
                        ringOfDependencies.putIfAbsent(optimizePosition(tempPosition - 1), tempHexShapeDependency);
                        tempPosition = optimizePosition(tempPosition + 1);
                    } else {
                        ringOfDependencies.putIfAbsent(optimizePosition(tempPosition + 1), tempHexShapeDependency);
                        tempPosition = optimizePosition(tempPosition - 1);
                    }
                    tempHexShapeDependency = tempHexShapeDependency.connections.get(tempPosition);
                }
            }
            tempPosition = optimizePosition(position + 1);
            tempHexShapeDependency = this.connections.get(tempPosition);
        }
        return ringOfDependencies;
    }

    public static int optimizePosition(int position) {
        if (position > 6) {
            position -= 6;
        } else if (position <= 0) {
            position = 6;
        }
        return position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Value = " + value + "\n");
        sb.append("Dependencies: \n");
        for (Map.Entry<Integer, HexShape> hexShapeEntry : connections.entrySet()) {
            sb.append(hexShapeEntry.getKey() + " = " + hexShapeEntry.getValue().getValue() + "\n");
        }
        return sb.toString();
    }

}
