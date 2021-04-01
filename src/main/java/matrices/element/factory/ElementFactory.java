package matrices.element.factory;

import complex.Complex;
import enums.LegalTypes;
import exceptions.IllegalTypeException;
import matrices.element.ComplexElement;
import matrices.element.Element;

import java.util.Arrays;

public class ElementFactory {

    private final Class<?> type;

    public ElementFactory(Class<?> type) {
        if (Arrays.stream(LegalTypes.values()).anyMatch(legalTypes -> legalTypes.name().equals(type.getSimpleName()))) {
            this.type = type;
        } else {
            throw new IllegalTypeException();
        }
    }

    public Element<?> create(Object arg) {
//        switch (arg.getClass().getSimpleName()) {
//            case "double[]" -> arg = Arrays.stream((double[]) arg).boxed().toArray(Double[]::new);
//            case "int[]" -> arg = Arrays.stream((int[]) arg).boxed().toArray(Integer[]::new);
//            case "long[]" -> arg = Arrays.stream((long[]) arg).boxed().toArray(Long[]::new);
//        }
        switch (type.getSimpleName()) {
            case "Complex" -> {
                switch (arg.getClass().getSimpleName()) {
                    case "Complex" -> {
                        assert arg instanceof Complex;
                        return new ComplexElement((Complex) arg);
                    }
                    case "double", "Double", "int", "Integer", "long", "Long" -> {
                        assert arg instanceof Number;
                        return new ComplexElement(((Number) arg).doubleValue(), 0);
                    }
                    default -> throw new IllegalTypeException();
                }
            }
            default -> throw new IllegalTypeException();
        }
    }

}