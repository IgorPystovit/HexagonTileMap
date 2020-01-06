package com.igorpystovit.service.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.resolver.api.InitPointPositionResolver;
import com.igorpystovit.service.api.HexagonService;
import com.igorpystovit.validator.HexPositionValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Service
public class HexagonServiceImpl implements HexagonService {
    private HexShape rootHex;
    private Hexagon managedHexagon;
    private InitPointPositionResolver positionResolver;

    @Autowired
    public HexagonServiceImpl(InitPointPositionResolver positionResolver) {
        this.positionResolver = positionResolver;
        this.managedHexagon = new Hexagon();
    }

    @Override
    public void addRootHex(HexShape rootHex) {
        if (rootHex.isRoot()) {
            this.rootHex = rootHex;
            rootHex.getConnections().clear();
            managedHexagon.getHexes().add(rootHex);
        }
    }

    @Override
    public void addHex(HexShape containedHex, HexShape newHex) {
        if (rootHex != null) {
            if (managedHexagon.getHexes().contains(containedHex) &&
                    !managedHexagon.getHexes().contains(newHex)) {
                newHex.getConnections().clear();
                addConnection(null, containedHex, newHex);
                managedHexagon.getHexes().add(newHex);
            } else {
                log.error("An error occurred while adding new hex");
            }
        } else {
            log.error("Root hex is not initialized! Cannot perform add operation");
        }
    }

    @Override
    public void addHexAtPosition(int position, HexShape containedHex, HexShape newHex) {
        if (rootHex != null) {
            if (managedHexagon.getHexes().contains(containedHex) &&
                    !managedHexagon.getHexes().contains(newHex)) {
                newHex.getConnections().clear();
                addConnection(position, containedHex, newHex);
                managedHexagon.getHexes().add(newHex);
            } else {
                log.error("An error occurred while adding new hex");
            }
        } else {
            log.error("Root hex is not initialized! Cannot perform add operation");
        }
    }

    private void addConnection(Integer connectionPosition, HexShape parentHex, HexShape childHex) {
        Map<Integer, UUID> parentHexConnections = parentHex.getConnections();

        if ((parentHexConnections.size() < 6) &&
                (!parentHexConnections.containsValue(childHex.getUuid()))) {

            if (connectionPosition == null) {
                connectionPosition = parentHex.getAvailablePositions().get(0);
            }

            formRingOfDependencies(connectionPosition, parentHex)
                    .forEach((position, hex) -> connectAtPosition(position, childHex, hex));

            if (!childHex.containsInitPair()) {
                childHex.setInitPair(positionResolver.resolveConnectionInitPoint(connectionPosition, parentHex));
            }
        }
    }

    private void connectAtPosition(int position, HexShape parentHex, HexShape childHex) {
        int mirrorPosition = HexPositionValidator.validatePosition(position + 3);
        if (!parentHex.isConnectionPresent(position)) {
            parentHex.getConnections().put(position, childHex.getUuid());
            connectAtPosition(mirrorPosition, childHex, parentHex);
        }
    }

    private Map<Integer, HexShape> formRingOfDependencies(int position, HexShape parentHex) {
        Map<Integer, HexShape> ringOfDependencies = new TreeMap<>();

        ringOfDependencies.putIfAbsent(HexPositionValidator.validatePosition(position + 3), parentHex);

        int tempPosition = HexPositionValidator.validatePosition(position - 1);
        Optional<HexShape> tempHexShapeConnection = getByUUID(parentHex.getConnections().get(tempPosition));


        for (int t = 0; t < 2; t++) {

            if (tempHexShapeConnection.isPresent()) {
                //do six iterations because ring consists of no more than six hexes
                for (int i = 0; i < 6; i++) {

                    if (!tempHexShapeConnection.isPresent()) {
                        break;
                    }

                    if (t == 0) {
                        ringOfDependencies.putIfAbsent(HexPositionValidator.validatePosition(tempPosition - 1), tempHexShapeConnection.get());
                        tempPosition = HexPositionValidator.validatePosition(tempPosition + 1);
                    } else {
                        ringOfDependencies.putIfAbsent(HexPositionValidator.validatePosition(tempPosition + 1), tempHexShapeConnection.get());
                        tempPosition = HexPositionValidator.validatePosition(tempPosition - 1);
                    }

                    tempHexShapeConnection = getByUUID(tempHexShapeConnection.get().getConnections().get(tempPosition));
                }
            }
            tempPosition = HexPositionValidator.validatePosition(position + 1);
            tempHexShapeConnection = getByUUID(parentHex.getConnections().get(tempPosition));
        }
        return ringOfDependencies;
    }

    @Override
    public void removeHex(HexShape hexToRemove) {
        if (managedHexagon.getHexes().contains(hexToRemove)) {
            Set<Optional<HexShape>> childHexes = hexToRemove
                    .getConnections()
                    .values()
                    .stream()
                    .map(this::getByUUID)
                    .collect(Collectors.toSet());
            childHexes
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(connection -> removeConnection(hexToRemove, connection));
        } else {
            log.warn("Hex is not contained on hexagon! Cannot perform remove operation");
        }
    }

    private void removeConnection(HexShape parentHex, HexShape childHex) {
        if (parentHex.getConnections().containsValue(childHex.getUuid())) {
            parentHex.getPositionOfConnection(childHex.getUuid()).ifPresent(position -> parentHex.getConnections().remove(position));
            removeConnection(childHex, parentHex);
        }
    }

    @Override
    public int getHexagonSize() {
        return managedHexagon.getHexes().size();
    }

    @Override
    public Optional<HexShape> getByUUID(UUID uuid) {
        return managedHexagon.getHexes().stream().filter(hex -> hex.getUuid().equals(uuid)).findAny();
    }

    @Override
    public boolean containsHexByUUID(UUID uuid) {
        return managedHexagon.getHexes().stream().map(HexShape::getUuid).anyMatch(uuid::equals);
    }
}
