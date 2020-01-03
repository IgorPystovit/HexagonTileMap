package com.igorpystovit;

import java.util.*;

public class PathSeeker {

    //seek shortest paths using Dijkstra algorithm
    public Map<HexShape, Integer> seekShortestPath(Set<HexShape> hexagon, HexShape begin, HexShape end) {
        Map<HexShape, Integer> pathMap = initPathMap(hexagon, begin);
        Set<HexShape> resolvedHexes = new HashSet<>();

        while (resolvedHexes.size() < hexagon.size()) {
            if (resolvedHexes.contains(end)) {
                return pathMap;
            }


            double minPath = pathMap
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

            }
        }

        return pathMap;
    }

    public Set<HexShape> seekPath(Set<HexShape> hexagon, HexShape begin, HexShape end) {
        return buildPath(hexagon, seekShortestPath(hexagon, begin, end), begin, end);
    }

    private Set<HexShape> buildPath(Set<HexShape> hexagon, Map<HexShape, Integer> pathMap, HexShape begin, HexShape end) {
        //init phase
        Set<HexShape> path = new LinkedHashSet<>();
        path.add(begin);
//        Map<HexShape, Set<HexShape>> checkedHexes = new LinkedHashMap<>();
//        hexagon.forEach(hex -> checkedHexes.put(hex, new HashSet<>()));

//        HexShape tempHex = begin;
//        HexShape prevHex = null;
        int tempLength = 0;
        int shortestPathLength = pathMap.get(end);

        PrevNextPair pair = new PrevNextPair();
        PrevNextPair tempPair = pair;
        pair.setCurrent(begin);
//        pair.setNextPair(new PrevNextPair(pair.getCurrent()));
        while (!(tempLength <= shortestPathLength && pair.getCurrent().equals(end))) {
            Set<HexShape> checkedHexes = pair.getCheckedHexes();

            Optional<HexShape> optionalHex = pair.getCurrent().getConnections().values().stream()
                    .filter(connectedHex -> !checkedHexes.contains(connectedHex)).findAny();

            if (optionalHex.isPresent()) {
                HexShape nextHex = optionalHex.get();
                ;
//                if (nextHex.equals(prevHex)) {
//                    checkedVertices.add(nextHex);
//                    continue;
//                }
                if ((tempLength + nextHex.getValue()) > shortestPathLength) {
                    pair.getCheckedHexes().add(nextHex);
                } else {
                    tempLength += nextHex.getValue();

                    pair.setNextPair(new PrevNextPair(pair,nextHex));
                    pair = pair.getNextPair();
                    pair.getCheckedHexes().add(pair.getPrevPair().getCurrent());
//                    checkedHexes.get(pair.getCurrent()).add(pair.getPrevPair().getCurrent());
//                    prevHex = tempHex;
//                    tempHex = nextHex;
//                    path.add(pair.getCurrent());
                }
            } else {
                tempLength -= pair.getCurrent().getValue();

                pair.getPrevPair().getCheckedHexes().add(pair.getCurrent());
//                checkedHexes.get(pair.getPrevPair().getCurrent()).add(pair.getCurrent());
//                tempHex = begin;
//                prevHex = null;
//                path.remove(pair.getCurrent());
                pair = pair.getPrevPair();
            }
        }


        while (tempPair.getNextPair() != null){
            tempPair = tempPair.getNextPair();
            path.add(tempPair.getCurrent());
        }
//        System.out.println(pair.getCurrent());
        return path;
    }

    private static class PrevNextPair{
        private PrevNextPair prevPair;
        private HexShape current;
        private PrevNextPair nextPair;
        private Set<HexShape> checkedHexes = new HashSet<>();

        public PrevNextPair(){}

        public PrevNextPair(PrevNextPair prevPair,HexShape current) {
            this.prevPair = prevPair;
            this.current = current;
        }

        public PrevNextPair(HexShape prev) {
            this.current = prev;
        }

        public Set<HexShape> getCheckedHexes() {
            return checkedHexes;
        }

        public PrevNextPair getPrevPair() {
            return prevPair;
        }

        public HexShape getCurrent() {
            return current;
        }

        public void setCurrent(HexShape current) {
            this.current = current;
        }

        public PrevNextPair getNextPair() {
            return nextPair;
        }

        public void setNextPair(PrevNextPair nextPair) {
            this.nextPair = nextPair;
        }
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
