package tabulatedFunctions;

import complex.Complex;
import exceptions.InconsistentFunctionsException;
import exceptions.InterpolationException;
import exceptions.NaNException;
import javafx.collections.ObservableList;
import model.Point;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Serializable {
    private static final long serialVersionUID = 3990511369369675738L;
    private final double[] rValues;
    private final Complex[] uValues;
    private final int count;
    private double z;
    private String name;

    public ArrayTabulatedFunction(ObservableList<Point> points, double z) {
        if (points.size() < 2) {
            throw new IllegalArgumentException("Array less than minimum length");
        }
        if (points.stream().anyMatch(point -> point.getU() != point.getU())) {
            throw new NaNException();
        }
        points.sorted((o1, o2) -> (int) Math.signum(o1.getR() - o2.getR()));
        rValues = new double[points.size()];
        uValues = new Complex[points.size()];
        this.z = z;
        count = points.size();
        int i = 0;
        for (Point point : points) {
            rValues[i] = point.getR();
            uValues[i++] = point.getU();
        }
    }

    private ArrayTabulatedFunction() {
        rValues = new double[]{};
        uValues = new Complex[]{};
        count = 0;
    }

    public ArrayTabulatedFunction(double[] rValues, double z, Complex[] uValues) {
        checkLengthIsTheSame(rValues, uValues);
        checkSorted(rValues);
        if (rValues.length < 2) {
            throw new IllegalArgumentException("Array less than minimum length");
        }
        for (Complex uValue : uValues) {
            if (uValue != uValue) {
                throw new NaNException();
            }
        }
        count = rValues.length;
        this.z = z;
        this.rValues = Arrays.copyOf(rValues, count);
        this.uValues = Arrays.copyOf(uValues, count);
    }

    public static TabulatedFunction getIdentity() {
        return new ArrayTabulatedFunction();
    }

    @Override
    public int floorIndexOfR(double r) {
        if (r < rValues[0]) {
            throw new IllegalArgumentException("Argument r less than minimal r in tabulated function");
        }
        for (int i = 0; i < count; i++) {
            if (rValues[i] > r) {
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

    public double getZ() {
        return z;
    }

    @Override
    protected Complex extrapolateLeft(double r) {
        return interpolate(r, rValues[0], rValues[1], uValues[0], uValues[1]);
    }

    @Override
    protected Complex extrapolateRight(double r) {
        return interpolate(r, rValues[count - 2], rValues[count - 1], uValues[count - 2], uValues[count - 1]);
    }

    @Override
    protected Complex interpolate(double r, int floorIndex) {
        if (r < rValues[floorIndex] || rValues[floorIndex + 1] < r) {
            throw new InterpolationException();
        }
        return interpolate(r, rValues[floorIndex], rValues[floorIndex + 1], uValues[floorIndex], uValues[floorIndex + 1]);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getR(int index) throws ArrayIndexOutOfBoundsException {
        return rValues[index];
    }

    @Override
    public Complex getU(int index) throws ArrayIndexOutOfBoundsException {
        return uValues[index];
    }

    @Override
    public void setU(int index, Complex value) throws ArrayIndexOutOfBoundsException {
        uValues[index] = value;
    }

    @Override
    public void setU(TabulatedFunction function) {
        if (function.getCount() > count || leftBound() > function.leftBound() || rightBound() < function.rightBound())
            throw new InconsistentFunctionsException();
        int index = indexOfTheBeginningOfTheProjectionOccurrence(function);
        if (index == -1) throw new InconsistentFunctionsException();
        int j = 0;
        Complex shift = index == 0 ? new Complex("0") : uValues[index - 1];
        for (Point point : function) {
            uValues[j + index] = point.getU().add(shift);
            j++;
        }
    }

    private int indexOfTheBeginningOfTheProjectionOccurrence(TabulatedFunction function) {
        int i;
        for (i = 0; i < count; i++) {
            if (rValues[i] == function.leftBound()) {
                break;
            }
            if (i == count - 1) {
                return -1;
            }
        }
        int j = 0;
        for (Point point : function) {
            if (point.getR() != rValues[j + i]) return -1;
            j++;
        }
        return i;
    }

    @Override
    public int indexOfR(double r) {
        int i;
        for (i = 0; i < count; i++) {
            if (rValues[i] == r) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfU(Complex u) {
        int i;
        for (i = 0; i < count; i++) {
            if (uValues[i].equals(u)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return rValues[0];
    }

    @Override
    public double rightBound() {
        return rValues[count - 1];
    }

    @Override
    public TabulatedFunction copy() {
        return new ArrayTabulatedFunction(rValues, z, uValues);
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
                return new Point(rValues[i], z, uValues[i++]);
            }
        };
    }
}
