package com.igorpystovit.entity;

import com.igorpystovit.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Setter
@Getter
public class Hexagon {
    private Set<HexShape> hexes = new LinkedHashSet<>();
    private Map<Pair<HexShape>, Set<UUID>> shortestPaths = new LinkedHashMap<>();
}
