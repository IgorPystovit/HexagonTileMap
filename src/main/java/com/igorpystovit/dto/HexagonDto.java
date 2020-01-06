package com.igorpystovit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class HexagonDto {
    private Set<HexShapeDto> hexes = new LinkedHashSet<>();
    private ShortestPathDto shortestPath = new ShortestPathDto();
}
