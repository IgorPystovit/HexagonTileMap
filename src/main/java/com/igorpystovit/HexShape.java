package com.igorpystovit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@NoArgsConstructor
@Getter
@Setter
public class HexShape {
    //init values for other shapes to coordinate themselves
    //spot size in px
    public static final int SPOT_SIZE = 50;
    //map of coordinates for shape to store its position
    private Map<Integer,Pair> coordinateMap = new LinkedHashMap<>();
    private Map<Integer,HexShape> nearShapes = new LinkedHashMap<>();
    private int value;

    public HexShape(double initX,double initY){
        coordinateMap.put(1,new Pair(initX,initY));
    }

    public HexShape(Pair pair){
        coordinateMap.put(1,pair);
    }

    public Map<Integer,Pair> getCoordinates(){
        if (coordinateMap.get(1) != null){
            double initX = coordinateMap.get(1).getX();
            double initY = coordinateMap.get(1).getY();

            coordinateMap.put(2,new Pair(initX + SPOT_SIZE,initY));
            coordinateMap.put(3,new Pair(initX + SPOT_SIZE + (SPOT_SIZE / 2.0),initY + SPOT_SIZE));
            coordinateMap.put(4,new Pair(initX + SPOT_SIZE,initY + (SPOT_SIZE * 2)));
            coordinateMap.put(5,new Pair(initX,initY + (SPOT_SIZE * 2)));
            coordinateMap.put(6,new Pair(initX - (SPOT_SIZE / 2.0),initY + SPOT_SIZE));
        }
        else{
            System.out.println("Cannot initialize coordinate map");
            throw new NoSuchElementException();
        }
        return coordinateMap;
    }

    public Pair getInitPair(){
        return coordinateMap.get(1);
    }

    public boolean containsInitPair(){
        return getInitPair() != null;
    }

    @Getter
    @Setter
    public static class CoordinateSetPair{
        private List<Double> x;
        private List<Double> y;

        public CoordinateSetPair(List<Double> x, List<Double> y) {
            this.x = x;
            this.y = y;
        }
    }
}
