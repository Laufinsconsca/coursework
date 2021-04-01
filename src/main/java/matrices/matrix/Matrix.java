package matrices.matrix;

import enums.LegalTypes;
import exceptions.IllegalTypeException;
import matrices.element.Element;
import matrices.element.factory.ElementFactory;

import java.io.Serializable;
import java.util.Arrays;

public class Matrix<T> implements Serializable {
    private final int rows;
    private final int columns;
    private final Element[][] elements;
    private final ElementFactory factory;

    public Matrix(int rows, int columns, Class<?> type) throws IllegalTypeException {
        isLegalType(type);
        this.rows = rows;
        this.columns = columns;
        factory = new ElementFactory(type);
        elements = new Element[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                elements[i][j] = factory.create(0);
            }
        }
    }

    public int getCountRows() {
        return rows;
    }

    public int getCountColumns() {
        return columns;
    }

    public void set(Object element, int row, int column) throws IllegalArgumentException {
        elements[row - 1][column - 1] = element instanceof Element ? (Element<?>) element : factory.create(element);
    }

    public Element<T> get(int row, int column) throws ArrayIndexOutOfBoundsException {
        return elements[row - 1][column - 1];
    }

    private void isLegalType(Class<?> type) {
        String s = type.getSimpleName().substring(0, 1).toUpperCase() + type.getSimpleName().substring(1);
        if (s.equals("Int")) {
            s = "Integer";
        }
        String finalS = s;
        if (Arrays.stream(LegalTypes.values()).noneMatch(x -> x.toString().equals(finalS))) {
            throw new IllegalTypeException();
        }
    }
}
