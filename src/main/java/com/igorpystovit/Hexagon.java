package com.igorpystovit;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class Hexagon {
    private Set<HexShape> hexagon = new LinkedHashSet<>();

    public Set<HexShape> getHexagon(){
        return Collections.unmodifiableSet(hexagon);
    }

}
