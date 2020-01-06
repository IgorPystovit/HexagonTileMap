package com.igorpystovit.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShortestPathDto {
    private HexShapeDto begin;
    private HexShapeDto end;
    private Set<UUID> path;
}
