package matrices.matrix;

import exceptions.IllegalTypeException;
import exceptions.IncompatibleDimensions;
import matrices.element.Element;

import java.io.*;
import java.util.Arrays;

public interface Tensor<T> extends Serializable {

    Matrix<T> add(Matrix<T> elements) throws IncompatibleDimensions;

    Matrix<T> subtract(Matrix<T> elements) throws IncompatibleDimensions;

    Matrix<T> multiply(Matrix<T> elements) throws IncompatibleDimensions;

    Matrix<T> multiply(double multiplyOnThe);

    int getCountRows();

    int getCountColumns();

    //void set(Element<T> element, int row, int column);

    void set(Element<T> element, int row, int column);

    void set(Object element, int row, int column);

    //void set(Object element, int row, int column, Class clazz);

    void setVectorInRow(Vector elements, int row);

    void setVectorInColumn(Vector elements, int column);

    Element<T> get(int row, int column);

    Class<?> getType();


    default void isLegalType(Class<?> type) {
        if (Arrays.stream(LegalTypes.values()).noneMatch(x -> x.toString().equals(type.getSimpleName()))) {
            throw new IllegalTypeException();
        }
    }

    default Matrix<T> clone() {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ObjectOutputStream ous;
            ous = new ObjectOutputStream(baos);
            ous.writeObject(this);
            ous.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Matrix<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
