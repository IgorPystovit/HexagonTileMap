package com.igorpystovit;

import com.igorpystovit.resolvers.InitPointPositionResolver;
import com.igorpystovit.resolvers.impl.InitPointPositionResolverImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

@Getter
@Setter
@Slf4j
public class HexagonServiceImpl implements HexagonService{
    private Hexagon managedHexagon;
    private HexShape rootHex;
    private InitPointPositionResolver positionResolver = new InitPointPositionResolverImpl();

    public HexagonServiceImpl(){
        managedHexagon = new Hexagon();
    }

    public HexagonServiceImpl(Hexagon managedHexagon) {
        this.managedHexagon = managedHexagon;
    }

    @Override
    public void addRootHex(HexShape rootHex) {
        if (rootHex.isRoot()){
            this.rootHex = rootHex;
            managedHexagon.getHexes().add(rootHex);
            managedHexagon.getHexes().addAll(rootHex.getConnections().values());
        }
    }

    @Override
    public void addHex(HexShape containedHex, HexShape newHex) {
        if (rootHex != null){
            if (managedHexagon.getHexes().contains(containedHex) &&
                    !managedHexagon.getHexes().contains(newHex)){
                newHex.getConnections().clear();
                addConnection(null,containedHex,newHex);
                managedHexagon.getHexes().add(newHex);
            }
            else{
                log.error("An error occurred while adding new hex");
            }
        }
        else{
            log.error("Root hex is not initialized! Cannot perform add operation");
        }
    }

    @Override
    public void addHexAtPosition(int position, HexShape containedHex, HexShape newHex) {
        if (rootHex != null) {
            if (managedHexagon.getHexes().contains(containedHex) &&
                    !managedHexagon.getHexes().contains(newHex)){
                newHex.getConnections().clear();
                addConnection(position,containedHex,newHex);
                managedHexagon.getHexes().add(newHex);
            }
            else{
                log.error("An error occurred while adding new hex");
            }
        }else{
            log.error("Root hex is not initialized! Cannot perform add operation");
        }
    }

    private void addConnection(Integer connectionPosition,HexShape parentHex, HexShape childHex) {
        Map<Integer,HexShape> parentHexConnections = parentHex.getConnections();

        if ((parentHexConnections.size() < 6) &&
                (!parentHexConnections.containsValue(childHex))) {

            if (connectionPosition == null){
                connectionPosition = parentHex.getAvailablePositions().get(0);
            }

            formRingOfDependencies(connectionPosition,parentHex)
                    .forEach((position,hex) -> connectAtPosition(position,childHex,hex));

            if (!childHex.containsInitPair()){
                childHex.setInitPair(positionResolver.resolveConnectionInitPoint(connectionPosition,parentHex));
            }
        }
    }

    private void connectAtPosition(int position, HexShape parentHex,HexShape childHex) {
        int mirrorPosition = HexagonService.optimizePosition(position + 3);
        if (!parentHex.isConnectionPresent(position)) {
            parentHex.getConnections().put(position, childHex);
            connectAtPosition(mirrorPosition, childHex,parentHex);
        }
    }

    private Map<Integer, HexShape> formRingOfDependencies(int position,HexShape parentHex) {
        Map<Integer, HexShape> ringOfDependencies = new TreeMap<>();

        ringOfDependencies.putIfAbsent(HexagonService.optimizePosition(position + 3),parentHex);

        int tempPosition = HexagonService.optimizePosition(position - 1);
        HexShape tempHexShapeConnection = parentHex.getConnections().get(tempPosition);


        for (int t = 0; t < 2; t++) {

            if (tempHexShapeConnection != null) {
                //do six iterations because ring consists of no more than six hexes
                for (int i = 0; i < 6; i++) {

                    if (tempHexShapeConnection == null) {
                        continue;
                    }

                    if (t == 0) {
                        ringOfDependencies.putIfAbsent(HexagonService.optimizePosition(tempPosition - 1), tempHexShapeConnection);
                        tempPosition = HexagonService.optimizePosition(tempPosition + 1);
                    } else {
                        ringOfDependencies.putIfAbsent(HexagonService.optimizePosition(tempPosition + 1), tempHexShapeConnection);
                        tempPosition = HexagonService.optimizePosition(tempPosition - 1);
                    }
                    tempHexShapeConnection = tempHexShapeConnection.getConnections().get(tempPosition);
                }
            }
            tempPosition = HexagonService.optimizePosition(position + 1);
            tempHexShapeConnection = parentHex.getConnections().get(tempPosition);
        }
        return ringOfDependencies;
    }

    @Override
    public void removeHex(HexShape hexToRemove) {
        if (managedHexagon.getHexes().contains(hexToRemove)){
            hexToRemove.getConnections().values().forEach(connection -> removeConnection(hexToRemove,connection));
        }
        else{
            log.warn("Hex is not contained on hexagon! Cannot perform remove operation");
        }
    }

    private void removeConnection(HexShape parentHex,HexShape childHex){
        if (parentHex.getConnections().containsValue(childHex)){
            parentHex.getPositionOfConnection(childHex).ifPresent(position -> parentHex.getConnections().remove(position));
            removeConnection(childHex,parentHex);
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
