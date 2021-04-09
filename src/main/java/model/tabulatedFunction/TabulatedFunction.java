package model.tabulatedFunction;

import model.Nameable;
import model.Point;
import model.complex.Complex;

public interface TabulatedFunction extends Iterable<Point>, Nameable {
    Complex apply(double x);
}
