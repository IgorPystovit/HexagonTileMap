package com.igorpystovit.converter;

import com.igorpystovit.dto.HexShapeDto;
import com.igorpystovit.entity.HexShape;

public class HexShapeDtoConverter {
    public static HexShapeDto convertToDto(HexShape hexShape) {
        HexShapeDto hexShapeDto = new HexShapeDto();
        hexShapeDto.setUuid(hexShape.getUuid());
        hexShapeDto.setValue(hexShape.getValue());
        hexShapeDto.setRoot(hexShape.isRoot());
        hexShapeDto.setInitPair(hexShape.getInitPair());
        hexShapeDto.setConnections(hexShape.getConnections());
        return hexShapeDto;
    }

    public static HexShape convertFromDto(HexShapeDto hexDto) {
        HexShape hexShape = new HexShape();
        hexShape.setRoot(hexDto.isRoot());
        hexShape.setValue(hexDto.getValue());
        hexShape.setInitPair(hexDto.getInitPair());
        hexShape.setConnections(hexDto.getConnections());
        hexShape.setUuid(hexDto.getUuid());
        return hexShape;
    }
}
