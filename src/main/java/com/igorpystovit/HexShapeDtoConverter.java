package com.igorpystovit;

import java.util.Map;

public class HexShapeDtoConverter {
    public static HexShapeDto convertToDto(HexShape hexShape){
        HexShapeDto hexShapeDto = new HexShapeDto();
        hexShapeDto.setUuid(hexShape.getUuid());
        hexShapeDto.setValue(hexShape.getValue());
        hexShapeDto.setRoot(hexShape.isRoot());
        hexShapeDto.setInitPoint(hexShape.getInitPair());
        for(Map.Entry<Integer,HexShape> connectionEntry : hexShape.getConnections().entrySet()){
            hexShapeDto.getConnections().put(connectionEntry.getKey(),connectionEntry.getValue().getUuid());
        }
        return hexShapeDto;
    }
}
