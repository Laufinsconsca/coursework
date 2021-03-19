package matrices.element.factory;

import complex.Complex;
import complex.ComplexBigDecimal;
import exceptions.IllegalTypeException;
import matrices.element.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class ElementFactory {

    private Class type;

    public ElementFactory(Class type) {
        String s = type.getSimpleName();
        if (s.equals("double")) {
            s = "Double";
        }
        switch (s) {
            case "Double", "Complex", "BigDecimal", "ComplexBigDecimal" -> this.type = type;
            default -> throw new IllegalTypeException();
        }
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Element create(Object arg) {
        switch (arg.getClass().getSimpleName()) {
            case "double[]" -> arg = Arrays.stream((double[]) arg).boxed().toArray(Double[]::new);
            case "int[]" -> arg = Arrays.stream((int[]) arg).boxed().toArray(Integer[]::new);
            case "long[]" -> arg = Arrays.stream((long[]) arg).boxed().toArray(Long[]::new);
        }
        switch (type.getSimpleName()) {
            case "double", "Double" -> {
                switch (arg.getClass().getSimpleName()) {
                    case "Double[]", "Integer[]", "Long[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        if (((Number[]) arg)[1].doubleValue() == 0) {
                            return new DoubleElement(((Number[]) arg)[0].doubleValue());
                        } else {
                            throw new IllegalTypeException(); // not quite correct, here should be thrown another exception
                        }
                    }
                    case "double", "Double", "int", "Integer", "long", "Long" -> {
                        return new DoubleElement(((Number) arg).doubleValue());
                    }
                    case "BigDecimal" -> {
                        return new DoubleElement(((BigDecimal) arg).doubleValue());
                    }
                    default -> throw new IllegalTypeException();
                }
            }
            case "Complex" -> {
                switch (arg.getClass().getSimpleName()) {
                    case "Complex" -> {
                        return new ComplexElement((Complex) arg);
                    }
                    case "double", "Double", "int", "Integer", "long", "Long" -> {
                        return new ComplexElement(((Number) arg).doubleValue(), 0);
                    }
                    case "Double[]", "Integer[]", "Long[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        return new ComplexElement(((Number[]) arg)[0].doubleValue(), ((Number[]) arg)[1].doubleValue());
                    }
//                    case "ComplexBigDecimal" -> {
//                        return new ComplexElement((ComplexBigDecimal)arg); //todo
//                    }
                    default -> throw new IllegalTypeException();
                }
            }
            case "BigDecimal" -> {
                switch (arg.getClass().getSimpleName()) {
                    case "BigDecimal" -> {
                        return new BigDecimalElement((BigDecimal) arg);
                    }
                    case "BigDecimal[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        if (((BigDecimal[]) arg)[1].equals(BigDecimal.ZERO)) {
                            return new BigDecimalElement(((BigDecimal[]) arg)[0]);
                        } else {
                            throw new IllegalTypeException(); // not quite correct, here should be thrown another exception
                        }
                    }
                    case "double", "Double", "int", "Integer", "long", "Long" -> {
                        return new BigDecimalElement(((Number) arg).doubleValue());
                    }
                    default -> throw new IllegalTypeException();
                }
            }
            case "ComplexBigDecimal" -> {
                switch (arg.getClass().getSimpleName()) {
                    case "ComplexBigDecimal" -> {
                        return new ComplexBigDecimalElement((ComplexBigDecimal) arg);
                    }
                    case "BigDecimal" -> {
                        return new ComplexBigDecimalElement((BigDecimal) arg, BigDecimal.ZERO);
                    }
                    case "BigDecimal[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        return new ComplexBigDecimalElement(((BigDecimal[]) arg)[0], ((BigDecimal[]) arg)[1]);
                    }
                    case "double", "Double", "int", "Integer", "long", "Long" -> {
                        return new ComplexBigDecimalElement(BigDecimal.valueOf(((Number) arg).doubleValue()), BigDecimal.ZERO);
                    }
                    case "Double[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        return new ComplexBigDecimalElement(BigDecimal.valueOf((((Double[]) arg)[0])), BigDecimal.valueOf((((Double[]) arg)[1])));
                    }
                    case "Integer[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        return new ComplexBigDecimalElement(BigDecimal.valueOf((((Integer[]) arg)[0])), BigDecimal.valueOf((((Integer[]) arg)[1])));
                    }
                    case "Long[]" -> {
                        if (((Object[]) arg).length > 2) throw new IllegalTypeException();
                        return new ComplexBigDecimalElement(BigDecimal.valueOf((((Long[]) arg)[0])), BigDecimal.valueOf((((Long[]) arg)[1])));
                    }
                    case "Complex" -> {
                        return new ComplexBigDecimalElement((Complex) arg);
                    }
                    default -> throw new IllegalTypeException();
                }
            }
            default -> throw new IllegalTypeException();
        }
    }

    // ************************
    // for further optimization

    public Element<?>[] createVector(Object[] args) {
        return null;
    }

    public Element<?>[] createMatrix(Object[][] args) {
        return null;
    }

    // *************************

}