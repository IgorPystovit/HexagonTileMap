package com.igorpystovit.service.impl;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.service.api.HexagonService;
import com.igorpystovit.service.api.PathSeekerService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service to seek shortest path between two {@link HexShape}s
 *
 * It builds matrix of the shortest paths regarding begin {@link HexShape} using Dijkstra algorithm
 * and then walks through that matrix in reverse order building path to end {@link HexShape}
 *  */
@Service
public class PathSeekerServiceImpl implements PathSeekerService {

    private HexagonService hexagonService;

    public PathSeekerServiceImpl(HexagonService hexagonService) {
        this.hexagonService = hexagonService;
    }

    @Override
    public List<HexShape> seekPath(Hexagon hexagon, UUID beginUuid, UUID endUuid) {
        return buildPathByMatrix(buildShortestPathMatrix(hexagon, beginUuid, endUuid));
    }

    private List<HexShape> buildPathByMatrix(Map<HexShape, Map<HexShape, Integer>> matrix) {
        List<HexShape> targetHexesList = new ArrayList<>(matrix.keySet());
        List<HexShape> path = new ArrayList<>();

        HexShape currentHex = targetHexesList.get(targetHexesList.size() - 1);
        int currentValue = matrix.get(currentHex).get(currentHex);

        for (int i = targetHexesList.size() - 1; i >= 0; i--) {
            if (matrix.get(targetHexesList.get(i)).get(currentHex) != currentValue) {
                path.add(currentHex);
                currentHex = targetHexesList.get(i + 1);
                currentValue = matrix.get(targetHexesList.get(i + 1)).get(currentHex);
                i++;
            } else if (i == 0) {
                path.add(currentHex);
            }
        }

        path.add(targetHexesList.get(0));

        return path;
    }


    /**
     * Build matrix of the shortest paths regarding begin {@link HexShape} using Dijkstra algorithm
     * until matrix contains end {@link HexShape}
     */
    private Map<HexShape, Map<HexShape, Integer>> buildShortestPathMatrix(Hexagon hexagon, UUID beginUuid, UUID endUuid) {
        hexagonService.setManagedHexagon(hexagon);
        HexShape begin = hexagonService.getByUUID(beginUuid).orElseThrow(NoSuchElementException::new);
        HexShape end = hexagonService.getByUUID(endUuid).orElseThrow(NoSuchElementException::new);
        Map<HexShape, Integer> pathMap = initPathMap(hexagon, begin);
        Set<HexShape> resolvedHexes = new HashSet<>();
        Map<HexShape, Map<HexShape, Integer>> matrix = new LinkedHashMap<>();

        matrix.putIfAbsent(begin, new LinkedHashMap<>(pathMap));

        while (resolvedHexes.size() < hexagon.getHexes().size()) {

            if (resolvedHexes.contains(end)) {
                break;
            }

            int minPath = pathMap
                    .entrySet()
                    .stream()
                    .filter(entry -> !resolvedHexes.contains(entry.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .min().getAsInt();

            HexShape targetHex = pathMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == minPath)
                    .filter(entry -> !resolvedHexes.contains(entry.getKey()))
                    .findAny()
                    .orElseThrow(NoSuchElementException::new)
                    .getKey();

            resolvedHexes.add(targetHex);

            int currentValueOfTargetVertex = pathMap.get(targetHex);

            Set<HexShape> connectedHexes = targetHex.getConnections().values()
                    .stream()
                    .map(hexagonService::getByUUID)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            for (HexShape connectedHex :
                    new ArrayList<>(connectedHexes)) {
                if ((currentValueOfTargetVertex + connectedHex.getValue()) < pathMap.get(connectedHex)) {
                    pathMap.put(connectedHex, currentValueOfTargetVertex + connectedHex.getValue());
                }
                matrix.put(targetHex, new LinkedHashMap<>(pathMap));
            }
        }

        return matrix;
    }

    private Map<HexShape, Integer> initPathMap(Hexagon hexagon, HexShape begin) {
        Map<HexShape, Integer> pathMap = new LinkedHashMap<>();
        pathMap.putIfAbsent(begin, 0);

        for (HexShape tempHex : hexagon.getHexes()) {
            pathMap.putIfAbsent(tempHex, Integer.MAX_VALUE);
        }

        return pathMap;
    }

}
