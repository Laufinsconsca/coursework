package model.tabulatedFunctions;

import model.complex.Complex;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    private static final long serialVersionUID = 4087048866240456298L;

    protected abstract Complex interpolate(double x, int floorIndex);

    protected Complex interpolate(double x, double leftX, double rightX, Complex leftU, Complex rightU) {
        return rightU.subtract(leftU).scaleOn((x - leftX) / (rightX - leftX)).add(leftU);
    }

    @Override
    public Complex apply(double x) {
        if (x > rightBound() || x < leftBound()) {
            return new Complex(Double.NaN, 0);
        } else if (indexOfX(x) != -1) {
            return getU(indexOfX(x));
        } else {
            return interpolate(x, floorIndexOfX(x));
        }
    }

}
