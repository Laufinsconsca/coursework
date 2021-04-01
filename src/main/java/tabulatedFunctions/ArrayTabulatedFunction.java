package tabulatedFunctions;

import complex.Complex;
import exceptions.InterpolationException;
import exceptions.NaNException;
import javafx.collections.ObservableList;
import model.Point;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Serializable {
    private static final long serialVersionUID = 3990511369369675738L;
    private final double[] xValues;
    private final Complex[] uValues;
    private final int count;
    private final double fixedVariable;
    private String name;

    public ArrayTabulatedFunction(ObservableList<Point> points, double fixedVariable) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("Array less than minimum length");
        }
        if (points.stream().anyMatch(point -> point.getU() != point.getU())) {
            throw new NaNException();
        }
        points.sorted((o1, o2) -> (int) Math.signum(o1.getX() - o2.getX()));
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

    @Override
    protected Complex interpolate(double x, int floorIndex) {
        if (x < xValues[floorIndex] || xValues[floorIndex + 1] < x) {
            throw new InterpolationException();
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], uValues[floorIndex], uValues[floorIndex + 1]);
    }

    @Override
    public Complex getU(int index) throws ArrayIndexOutOfBoundsException {
        return uValues[index];
    }

    @Override
    public int indexOfX(double x) {
        int i;
        for (i = 0; i < count; i++) {
            if (xValues[i] == x) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    @NotNull
    public Iterator<Point> iterator() {
        return new Iterator<>() {
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
}
