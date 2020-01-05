package com.igorpystovit;

import com.igorpystovit.resolvers.impl.InitPointPositionResolverImpl;
import com.igorpystovit.util.Pair;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HexagonTileMapGenerator {
    private Random random = new Random();
    private InitPointPositionResolverImpl positionResolver = new InitPointPositionResolverImpl();
    private HexPositionValidator positionValidator = new HexPositionValidator();
    private HexagonService hexagonService = new HexagonServiceImpl();

    public Hexagon generate(int mapSize, int width, int height) {
        hexagonService.setManagedHexagon(new Hexagon());
        Set<HexShape> checkedHexes = new HashSet<>();

        HexShape rootHex = new HexShape(getScreenCenterPair().getLeft(),getScreenCenterPair().getRight() - 300);
        rootHex.setRoot(true);
        rootHex.setValue(generateValue());
        hexagonService.addRootHex(rootHex);

        HexShape randomUncheckedHex;

        while (hexagonService.getHexagonSize() < mapSize) {
            randomUncheckedHex = getRandomHex(hexagonService.getManagedHexagon(), checkedHexes);
            if (generateConnections(randomUncheckedHex, mapSize, width, height)) {
                checkedHexes.add(randomUncheckedHex);
            }
        }
        return hexagonService.getManagedHexagon();
    }

    /**
     * @return true if at least some of connections were added
     * false if no new connections were added
     */
    private boolean generateConnections(HexShape hexShape, int mapSize, int width, int height) {
        boolean connectionAdded = false;

        if (hexShape.getConnections().size() < 6){
            List<Integer> availablePositions = hexShape.getAvailablePositions();
            for (Integer position : availablePositions){
                HexShape tempHex;

                tempHex = new HexShape(generateValue());

                tempHex.setInitPair(positionResolver.resolveConnectionInitPoint(position,hexShape));

                if (positionValidator.isScreenPositionValid(tempHex, width, height)) {
                    hexagonService.addHexAtPosition(position,hexShape,tempHex);
                    connectionAdded = true;
                } else {
                    return connectionAdded;
                }

                if (hexagonService.getHexagonSize() >= mapSize){
                    break;
                }
            }

        }

        return connectionAdded;
    }

    private HexShape getRandomHex(Hexagon hexagon, Set<HexShape> checkedHexes) {
        HexShape randomHex;
        do {
            int hexIndex = random.nextInt(hexagon.getHexes().size());
            randomHex = (HexShape) hexagon.getHexes().toArray()[hexIndex];
        } while (checkedHexes.contains(randomHex));
        return randomHex;
    }

    private int generateValue() {
        return random.nextInt(99) + 1;
    }

    private Pair<Double> getScreenCenterPair() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return new Pair<>(dimension.width / 2.0, dimension.getHeight() / 2.0);
    }
}
