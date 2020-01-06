package com.igorpystovit.dto;


import com.igorpystovit.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@ToString
public class HexShapeDto implements Serializable {
    private UUID uuid;
    private int value;
    private Map<Integer, UUID> connections = new LinkedHashMap<>();
    private boolean root;
    private Pair<Double> initPair;

}
