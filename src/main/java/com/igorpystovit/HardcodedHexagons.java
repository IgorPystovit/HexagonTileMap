package com.igorpystovit;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class HardcodedHexagons {
    public static Set<HexShape> getHexagon(){
        Random random = new Random();

        HexShape startHex = new HexShape(400,500,99);
        HexShape hex1 = new HexShape(11);
        HexShape hex2 = new HexShape(12);
        HexShape hex3 = new HexShape(60);
        HexShape hex4 = new HexShape(27);
        HexShape hex5 = new HexShape(9);
        HexShape hex6 = new HexShape(21);
        HexShape hex7 = new HexShape(31);
        HexShape hex8 = new HexShape(1);

        startHex.addConnection(hex1);
        startHex.addConnection(hex2);
        startHex.addConnection(hex3);

        hex3.addConnection(hex4);
        hex3.addConnection(hex5);
        hex3.addConnection(hex6);

        hex6.addConnection(hex7);
        hex6.addConnection(hex8);

        Set<HexShape> hexagon = new LinkedHashSet<>(Arrays.asList(startHex,hex1,hex2,hex3,hex4,hex5,hex6,hex7,hex8));

//        System.out.println(hexagon);
        PathSeeker pathSeeker = new PathSeeker();
        pathSeeker.seekShortestPath(hexagon,hex1,hex8).forEach((h,v) -> System.out.println(h.getValue()+" = "+v));
        pathSeeker.seekPath(hexagon,hex1,hex8).forEach(hexShape -> System.out.print(hexShape.getValue()+" - "));
        return hexagon;
    }
}
