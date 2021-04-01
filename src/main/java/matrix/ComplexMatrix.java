package matrix;

import complex.Complex;

public class ComplexMatrix {
    private final int rows;
    private final int columns;
    private final Complex[][] elements;

    public ComplexMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        elements = new Complex[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                elements[i][j] = Complex.ZERO;
            }
        }
    }

    public int getCountRows() {
        return rows;
    }

    public int getCountColumns() {
        return columns;
    }

    public void set(Complex element, int row, int column) throws IllegalArgumentException {
        elements[row - 1][column - 1] = element;
    }

    public void set(double element, int row, int column) throws IllegalArgumentException {
        elements[row - 1][column - 1] = new Complex(element, 0);
    }

    public Complex get(int row, int column) throws ArrayIndexOutOfBoundsException {
        return elements[row - 1][column - 1];
    }
}
