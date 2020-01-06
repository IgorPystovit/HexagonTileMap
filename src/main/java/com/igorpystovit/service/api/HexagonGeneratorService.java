package com.igorpystovit.service.api;

import com.igorpystovit.entity.Hexagon;

public interface HexagonGeneratorService {
    Hexagon generate(int size, int width, int height);
}
