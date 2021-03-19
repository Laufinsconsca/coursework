package tabulatedFunctions;

import complex.Complex;
import model.Point;

public interface TabulatedFunction extends Iterable<Point> {

    int getCount();

    Complex apply(double r);

    double getR(int index);

    double getZ();

    Complex getU(int index);

    void setU(int index, Complex value);

    void setU(TabulatedFunction function);

    int indexOfR(double r);

    int indexOfU(Complex u);

    double leftBound();

    double rightBound();

    int floorIndexOfR(double r);

    default String getName() {
        return "TF(x)";
    }

    void setName(String name);

    TabulatedFunction copy();
}
