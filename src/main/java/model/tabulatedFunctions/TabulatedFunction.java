package model.tabulatedFunctions;

import model.complex.Complex;
import model.Point;

public interface TabulatedFunction extends Iterable<Point> {

    Complex apply(double x);

    Complex getU(int index);

    int indexOfX(double x);

    double leftBound();

    double rightBound();

    int floorIndexOfX(double x);

    String getName();

    void setName(String name);
}
