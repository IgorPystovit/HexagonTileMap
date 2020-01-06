package com.igorpystovit.service.api;

import com.igorpystovit.entity.HexShape;
import com.igorpystovit.entity.Hexagon;

import java.util.List;
import java.util.UUID;

public interface PathSeekerService {
    List<HexShape> seekPath(Hexagon hexagon, UUID beginUuid, UUID endUuid);
}
