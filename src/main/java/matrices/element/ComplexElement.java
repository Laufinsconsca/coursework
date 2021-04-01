package matrices.element;

import complex.Complex;

public class ComplexElement implements Element<Complex> {

    private final Complex element;

    public ComplexElement(Complex number) {
        element = number;
    }

    public ComplexElement(double real, double imag) {
        this(new Complex(real, imag));
    }

    @Override
    public Element<Complex> add(Element<Complex> element) {
        return new ComplexElement(this.element.add(element.get()));
    }

    @Override
    public Element<Complex> add(double num) {
        return new ComplexElement(this.element.add(num));
    }

    @Override
    public Element<Complex> multiply(Element<Complex> element) {
        return new ComplexElement(this.element.times(element.get()));
    }

    @Override
    public Element<Complex> multiply(double num) {
        return new ComplexElement(this.element.scaleOn(num));
    }

    @Override
    public Element<Complex> divide(Element<Complex> element) {
        return new ComplexElement(this.element.times(element.get().reciprocal()));
    }

    @Override
    public Complex get() {
        return element;
    }
}
