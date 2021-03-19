package matrices.matrix;

import complex.Complex;
import exceptions.IllegalTypeException;
import exceptions.IncompatibleDimensionsException;
import matrices.element.Element;
import matrices.element.factory.ElementFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;

public class Matrix<T> implements Serializable {
    private final Class<?> type;
    private final int rows;
    private final int columns;
    private final Element[][] elements;

    // ******************************************
    // TODO
    // private Collection<Collection<T>> elements;
    // ******************************************

    private final ElementFactory factory;

    public Matrix(String[][] elements, Class<?> type) throws IllegalTypeException {
        isLegalType(type);
        this.rows = elements.length;
        this.columns = elements[0].length;
        this.type = type;
        factory = new ElementFactory(type);
        this.elements = new Element[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.elements[i][j] = parseStringToElement(elements[i][j]);
            }
        }
    }

    public Matrix(double[][] elements, Class<?> type) throws IllegalTypeException {
        this(new double[][][]{elements, new double[elements.length][elements[0].length]}, type);
    }

    public Matrix(double[][][] elements, Class<?> type) throws IllegalTypeException {
        isLegalType(type);
        this.rows = elements.length;
        this.columns = elements[0].length;
        this.type = type;
        factory = new ElementFactory(type);
        this.elements = new Element[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.elements[i][j] = factory.create(elements[i][j]);
            }
        }
    }

    public Matrix(int rows, int columns, Class<?> type) throws IllegalTypeException {
        isLegalType(type);
        this.rows = rows;
        this.columns = columns;
        this.type = type;
        factory = new ElementFactory(type);
        elements = new Element[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                elements[i][j] = factory.create(0);
            }
        }
    }

    private void calculate(Matrix<T> l, Function<Element<T>[], Element<T>> f) {
        if (l.getCountColumns() != columns || l.getCountRows() != rows)
            throw new IncompatibleDimensionsException();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.set(f.apply(new Element[]{this.elements[i][j], l.get(i + 1, j + 1)}), i + 1, j + 1);
            }
        }
    }

    public void add(Matrix<T> elements) throws IncompatibleDimensionsException {
        calculate(elements, element -> element[0].add(element[1]));
    }

    public void subtract(Matrix<T> elements) throws IncompatibleDimensionsException {
        calculate(elements, element -> element[0].subtract(element[1]));
    }

    public void multiply(Matrix<T> elements) throws IncompatibleDimensionsException {
        for (int i = 0; i < this.getCountRows(); i++) {
            for (int j = 0; j < elements.getCountColumns(); j++) {
                for (int k = 0; k < this.getCountColumns(); k++) {
                    this.set(this.get(i + 1, j + 1).add(this.get(i + 1, k + 1).multiply(elements.get(k + 1, j + 1))), i + 1, j + 1);
                }
            }
        }
    }

    public void multiply(double multiplyOnThe) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.set(this.elements[i][j].multiply(multiplyOnThe), i + 1, j + 1);
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

    // ************************
    // for further optimization

    public void setVectorInRow(Vector elements, int row) {

    }

    public void setVectorInColumn(Vector elements, int column) {

    }

    // ************************

    public Element<T> get(int row, int column) throws ArrayIndexOutOfBoundsException {
        return elements[row - 1][column - 1];
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= getCountRows(); i++) {
            for (int j = 1; j <= getCountColumns(); j++) {
                builder.append(this.get(i, j).get()).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private Element<?> parseStringToElement(String text) {
        Double[] element = Complex.parseStringToComplex(text);
        if (element[1] == 0 && (type.equals(Double.TYPE) || type.equals(BigDecimal.class))) {
            return factory.create(element[0]);
        }
        return factory.create(element);
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

    public boolean hasEqualDimensionsWith(Matrix<?> secondDimensions) {
        return rows == secondDimensions.getCountRows() && columns == secondDimensions.getCountColumns();
    }
}
