package com.igorpystovit.service.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.resolver.impl.InitPointPositionResolverImpl;
import com.igorpystovit.service.api.HexagonGeneratorService;
import com.igorpystovit.service.api.HexagonService;
import com.igorpystovit.util.Pair;
import com.igorpystovit.validator.HexPositionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class HexagonGeneratorServiceImpl implements HexagonGeneratorService {
    private Random random = new Random();
    private HexPositionValidator positionValidator;
    private InitPointPositionResolverImpl positionResolver;
    private HexagonService hexagonService;

    @Autowired
    public HexagonGeneratorServiceImpl(InitPointPositionResolverImpl positionResolver, HexagonService hexagonService) {
        this.positionResolver = positionResolver;
        this.hexagonService = hexagonService;
        this.positionValidator = new HexPositionValidator();
    }

    @Override
    public Hexagon generate(int size, int width, int height) {

        hexagonService.setManagedHexagon(new Hexagon());
        Set<HexShape> checkedHexes = new HashSet<>();

        HexShape rootHex = new HexShape(getScreenCenterPair().getLeft(), getScreenCenterPair().getRight() - 300);
        rootHex.setRoot(true);
        rootHex.setValue(generateValue());
        hexagonService.addRootHex(rootHex);

        HexShape randomUncheckedHex;

        int counter = 0;
        while (hexagonService.getHexagonSize() < size) {
            //counter avoiding infinite loop
            counter++;
            randomUncheckedHex = getRandomHex(hexagonService.getManagedHexagon(), checkedHexes);
            if (generateConnections(randomUncheckedHex, size, width, height)) {
                checkedHexes.add(randomUncheckedHex);
            }

            if (counter > size + 50){
                break;
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

        if (hexShape.getConnections().size() < 6) {
            List<Integer> availablePositions = hexShape.getAvailablePositions();
            for (Integer position : availablePositions) {
                HexShape tempHex;

                tempHex = new HexShape(generateValue());

                tempHex.setInitPair(positionResolver.resolveConnectionInitPoint(position, hexShape));

                if (positionValidator.isScreenPositionValid(tempHex, width, height)) {
                    hexagonService.addHexAtPosition(position, hexShape, tempHex);
                    connectionAdded = true;
                } else {
                    return connectionAdded;
                }

                if (hexagonService.getHexagonSize() >= mapSize) {
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
