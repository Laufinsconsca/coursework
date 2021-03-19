package tabulatedFunctions;

import complex.Complex;
import model.Point;

public interface TabulatedFunction extends Iterable<Point> {

    int getCount();

    Complex apply(double r);

    double getR(int index);

    Complex getU(int index);

    void setU(int index, Complex value);

    void setU(TabulatedFunction function);

    int indexOfR(double r);

    int indexOfU(Complex u);

    double leftBound();

    double rightBound();

    TabulatedFunction copy();

    int floorIndexOfR(double r);

    default boolean isCanBeComposed(TabulatedFunction function) {
        return Math.abs(leftBound() - function.leftBound()) < 1E-12
                && Math.abs(rightBound() - function.rightBound()) < 1E-12
                && getCount() == function.getCount();
    }

    default String getName() {
        return "TF(x)";
    }

    void setName(String name);
}
