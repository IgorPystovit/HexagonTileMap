package com.igorpystovit.converter;

import com.igorpystovit.dto.HexShapeDto;
import com.igorpystovit.dto.HexagonDto;
import com.igorpystovit.dto.ShortestPathDto;
import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;
import com.igorpystovit.util.Pair;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class HexagonDtoConverter {
    public static HexagonDto convertToDto(Hexagon hexagon) {
        HexagonDto hexagonDto = new HexagonDto();
        Set<HexShapeDto> hexShapeDtos = hexagon.getHexes().stream().map(HexShapeDtoConverter::convertToDto).collect(Collectors.toSet());
        hexagonDto.setHexes(hexShapeDtos);
        for (Map.Entry<Pair<HexShape>, Set<UUID>> shortestPathEntry : hexagon.getShortestPaths().entrySet()) {
            Pair<HexShapeDto> beginEndPair = new Pair<>();

            beginEndPair.setLeft(HexShapeDtoConverter.convertToDto(shortestPathEntry.getKey().getLeft()));
            beginEndPair.setRight(HexShapeDtoConverter.convertToDto(shortestPathEntry.getKey().getRight()));

            hexagonDto.setShortestPath(new ShortestPathDto(beginEndPair.getLeft(), beginEndPair.getRight(), shortestPathEntry.getValue()));
        }
        return hexagonDto;
    }

    public static Hexagon convertFromDto(HexagonDto hexagonDto) {
        Hexagon hexagon = new Hexagon();
        Set<HexShape> hexes = hexagonDto.getHexes()
                .stream()
                .map(HexShapeDtoConverter::convertFromDto)
                .collect(Collectors.toSet());
        hexagon.setHexes(hexes);

        ShortestPathDto shortestPath = hexagonDto.getShortestPath();

        if (shortestPath != null){
            HexShape begin = HexShapeDtoConverter.convertFromDto(shortestPath.getBegin());
            HexShape end = HexShapeDtoConverter.convertFromDto(shortestPath.getEnd());
            hexagon.getShortestPaths().put(new Pair<>(begin, end), shortestPath.getPath());
        }
        return hexagon;
    }
}
