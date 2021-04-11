package model.tabulatedFunction;

import exceptions.InterpolationException;
import exceptions.NaNException;
import model.Point;
import model.complex.Complex;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayTabulatedFunction implements Serializable, TabulatedFunction {
    private static final long serialVersionUID = 3990511369369675738L;
    private final double[] xValues;
    private final Complex[] uValues;
    private final int count;
    private final double fixedVariable;
    private String name;

    public ArrayTabulatedFunction(List<Point> points, double fixedVariable) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("Array less than minimum length");
        }
        if (points.stream().anyMatch(point -> point.getU() != point.getU())) {
            throw new NaNException();
        }
        points = points.stream().sorted((o1, o2) -> (int) Math.signum(o1.getX() - o2.getX())).collect(Collectors.toList());
        //points.sorted((o1, o2) -> (int) Math.signum(o1.getX() - o2.getX()));
        xValues = new double[points.size()];
        uValues = new Complex[points.size()];
        this.fixedVariable = fixedVariable;
        count = points.size();
        int i = 0;
        for (Point point : points) {
            xValues[i] = point.getX();
            uValues[i++] = point.getU();
        }
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

    public int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            throw new IllegalArgumentException("Argument x less than minimal x in tabulated function");
        }
        for (int i = 0; i < count; i++) {
            if (xValues[i] > x) {
                return i - 1;
            }
        }
        return count;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected Complex interpolate(double x, int floorIndex) {
        if (x < xValues[floorIndex] || xValues[floorIndex + 1] < x) {
            throw new InterpolationException();
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], uValues[floorIndex], uValues[floorIndex + 1]);
    }

    public Complex getU(int index) throws ArrayIndexOutOfBoundsException {
        return uValues[index];
    }

    public int indexOfX(double x) {
        int i;
        for (i = 0; i < count; i++) {
            if (xValues[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public double leftBound() {
        return xValues[0];
    }

    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator() {
            int i = 0;

            public boolean hasNext() {
                return i != count;
            }

            public Point next() {
                if (i == count) {
                    throw new NoSuchElementException();
                }
                return new Point(xValues[i], fixedVariable, uValues[i++]);
            }
        };
    }

    protected Complex interpolate(double x, double leftX, double rightX, Complex leftU, Complex rightU) {
        return rightU.subtract(leftU).multiply((x - leftX) / (rightX - leftX)).add(leftU);
    }
}
