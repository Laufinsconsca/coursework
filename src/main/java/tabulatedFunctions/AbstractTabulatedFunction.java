package tabulatedFunctions;

import complex.Complex;
import exceptions.ArrayIsNotSortedException;
import exceptions.DifferentLengthOfArraysException;
import model.Point;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {

    private static final long serialVersionUID = 4087048866240456298L;

    protected static void checkLengthIsTheSame(double[] rValues, Complex[] uValues) {
        if (rValues.length != uValues.length) {
            throw new DifferentLengthOfArraysException();
        }
    }

    protected static void checkSorted(double[] xValues) {
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] < xValues[i - 1]) {
                throw new ArrayIsNotSortedException();
            }
        }
    }

    protected abstract Complex extrapolateLeft(double x);

    protected abstract Complex extrapolateRight(double x);

    protected abstract Complex interpolate(double x, int floorIndex);

    protected Complex interpolate(double r, double leftR, double rightR, Complex leftU, Complex rightU) {
        return leftU.plus(rightU.minus(leftU)).scale((r - leftR) / (rightR - leftR));
    }

    @Override
    public Complex apply(double r) {
        if (r < leftBound()) {
            return extrapolateLeft(r);
        } else if (r > rightBound()) {
            return extrapolateRight(r);
        } else if (indexOfR(r) != -1) {
            return getU(indexOfR(r));
        } else {
            return interpolate(r, floorIndexOfR(r));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName()).append(" size = ").append(getCount()).append("\n");
        for (Point point : this) {
            builder
                    .append("[")
                    .append(point.getR())
                    .append("; ")
                    .append(point.getU())
                    .append("]\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }


}
