package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.complex.Complex;

@Getter
@AllArgsConstructor
public class Point {
    private final double x;
    private final double y;
    private final Complex u;
}
