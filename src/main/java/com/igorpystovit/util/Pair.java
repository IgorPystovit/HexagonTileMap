package com.igorpystovit.util;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Pair<T> {
    private T left;
    private T right;
}
