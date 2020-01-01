package com.igorpystovit;

import com.igorpystovit.resolvers.InitPointPositionResolver;
import com.igorpystovit.util.Pair;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class HexagonTileMapGenerator {
    private Random random = new Random();
    private InitPointPositionResolver positionResolver = new InitPointPositionResolver();

    public Set<HexShape> generate(int mapSize,int width,int height){
        Set<HexShape> hexes = new LinkedHashSet<>();
        Set<HexShape> checkedHexes = new HashSet<>();

        HexShape rootHex = new HexShape(getScreenCenterPair());
        rootHex.setRoot(true);
        rootHex.setValue(generateValue());
        hexes.add(rootHex);

        HexShape randomUncheckedHex;

        while (hexes.size() < mapSize){
            randomUncheckedHex = getRandomHex(hexes,checkedHexes);
            if (addConnections(randomUncheckedHex,hexes,mapSize,width,height)){
                checkedHexes.add(randomUncheckedHex);
            }
        }
        return hexes;
    }

    /**
     * @return
     * true if at least some of connections were added
     * false if no new connections were added
     * */
    private boolean addConnections(HexShape hexShape, Set<HexShape> hexes, int mapSize, int width, int height){
        int numberOfConnections = Math.abs(hexShape.getConnections().size() - 6);
        boolean connectionAdded = false;

        if (numberOfConnections > 0){
            HexShape tempHex;

            int connectionCounter = 0;

            while ((connectionCounter < numberOfConnections) && (hexes.size() < mapSize)){
                tempHex = new HexShape(generateValue());
                hexShape.addConnection(tempHex);
                positionResolver.resolveConnectionsInitPoints(hexShape);
                if (positioningCriteriaMet(tempHex,width,height)){
                    connectionCounter++;
                    hexes.add(tempHex);
                    connectionAdded = true;
                }else{
                    hexShape.removeConnection(tempHex);
                    return connectionAdded;
                }
            }
        }
        return connectionAdded;
    }

    private HexShape getRandomHex(Set<HexShape> hexes,Set<HexShape> checkedHexes){
        HexShape randomHex;
        do{
            int hexIndex = random.nextInt(hexes.size());
            randomHex = (HexShape) hexes.toArray()[hexIndex];
        }while (checkedHexes.contains(randomHex));
        return randomHex;
    }

    private int generateValue(){
        return random.nextInt(99) + 1;
    }

    private Pair getScreenCenterPair(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Pair(dimension.width / 2.0, dimension.getHeight() / 2.0);
    }

    private boolean positioningCriteriaMet(HexShape hexShape, int width, int height){
        boolean yAxisCriteriaMet = false;
        boolean xAxisCriteriaMet = false;

        if (hexShape.containsInitPair()){
            yAxisCriteriaMet = hexShape.getCoordinateMap().values()
                    .stream()
                    .mapToDouble(Pair::getY)
                    .noneMatch(y ->(y <= 0) || (y >= height));

            xAxisCriteriaMet = hexShape.getCoordinateMap().values()
                    .stream()
                    .mapToDouble(Pair::getX)
                    .noneMatch(x ->(x <= 0) || (x >= width));
        }

        return yAxisCriteriaMet && xAxisCriteriaMet;
    }

}
