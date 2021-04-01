package model;

import complex.Complex;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Point {
    private final double x;
    private final double y;
    private final Complex u;
}
