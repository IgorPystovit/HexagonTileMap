package com.igorpystovit;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;


@Setter
@Getter
public class Hexagon {
    private Set<HexShape> hexes;

    public Hexagon(){
        hexes = new LinkedHashSet<>();
    }
}
