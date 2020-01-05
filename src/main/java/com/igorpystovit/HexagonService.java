package com.igorpystovit;

import java.util.Optional;
import java.util.UUID;

public interface HexagonService {
    void addRootHex(HexShape rootHex);

    void addHex(HexShape parentHex,HexShape childHex);

    void addHexAtPosition(int position,HexShape parentHex,HexShape childHex);

    void removeHex(HexShape hex);

    Optional<HexShape> getByUUID(UUID uuid);

    boolean containsHexByUUID(UUID uuid);

    int getHexagonSize();

    Hexagon getManagedHexagon();

    void setManagedHexagon(Hexagon hexagon);

    static int optimizePosition(int position) {
        if (position > 6) {
            position -= 6;
        } else if (position <= 0) {
            position = 6;
        }
        return position;
    }
}
