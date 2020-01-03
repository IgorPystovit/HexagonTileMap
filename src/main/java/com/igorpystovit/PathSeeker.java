package com.igorpystovit;

import java.util.*;

public class PathSeeker {

    //seek shortest paths using Dijkstra algorithm
    private Map<HexShape, Map<HexShape, Integer>> buildShortestPathMatrix(Set<HexShape> hexagon, HexShape begin, HexShape end) {
        Map<HexShape, Integer> pathMap = initPathMap(hexagon, begin);
        Set<HexShape> resolvedHexes = new HashSet<>();
        Map<HexShape, Map<HexShape, Integer>> matrix = new LinkedHashMap<>();

        matrix.putIfAbsent(begin, new LinkedHashMap<>(pathMap));

        while (resolvedHexes.size() < hexagon.size()) {

            if (resolvedHexes.contains(end)) {
                return matrix;
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

            for (HexShape connectedHex :
                    new ArrayList<>(targetHex.getConnections().values())) {
                if ((currentValueOfTargetVertex + connectedHex.getValue()) < pathMap.get(connectedHex)) {
                    pathMap.put(connectedHex, currentValueOfTargetVertex + connectedHex.getValue());
                }
                matrix.put(targetHex, new LinkedHashMap<>(pathMap));
            }
        }
        return matrix;
    }

    private Set<HexShape> buildPathByMatrix(Map<HexShape, Map<HexShape, Integer>> matrix) {
        List<HexShape> targetHexesList = new ArrayList<>(matrix.keySet());
        Set<HexShape> path = new LinkedHashSet<>();

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

    public Set<HexShape> seekPath(Set<HexShape> hexagon, HexShape begin, HexShape end) {
        return buildPathByMatrix(buildShortestPathMatrix(hexagon, begin, end));
    }

    private Map<HexShape, Integer> initPathMap(Set<HexShape> hexagon, HexShape begin) {
        Map<HexShape, Integer> pathMap = new LinkedHashMap<>();
        pathMap.putIfAbsent(begin, 0);

        for (HexShape tempHex : hexagon) {
            pathMap.putIfAbsent(tempHex, Integer.MAX_VALUE);
        }

        return pathMap;
    }
}
