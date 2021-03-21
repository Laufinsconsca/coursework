package matrices.element;

import complex.Complex;
import exceptions.IllegalTypeException;

public class ComplexElement implements Element<Complex> {

    private Complex element;

    public ComplexElement(Complex number) {
        element = number;
    }

    public ComplexElement(String text) {
        Double[] element = Complex.parseStringToComplex(text);
        this.element = new Complex(element[0], element[1]);
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

    public Element<Complex> multiply(Complex element) {
        return new ComplexElement(this.element.times(element));
    }

    @Override
    public Element<Complex> multiply(double num) {
        return new ComplexElement(this.element.scaleOn(num));
    }

    @Override
    public Element<Complex> divide(Element<Complex> element) {
        return new ComplexElement(this.element.times(element.get().reciprocal()));
    }

    public Element<Complex> divide(Complex element) {
        return new ComplexElement(this.element.times(element.reciprocal()));
    }

    @Override
    public Element<Complex> reciprocal() {
        return new ComplexElement(this.element.reciprocal());
    }

    @Override
    public boolean equals(Element<Complex> element) {
        return this.element.equals(element);
    }

    @Override
    public int compareTo(Element<Complex> element) {
        throw new IllegalTypeException("The type doesn't possess a operation of compare");
    }

    @Override
    public void set(Element<Complex> element) {
        this.element = element.get();
    }

    @Override
    public void set(Complex element) {
        this.element = element;
    }

    @Override
    public Complex get() {
        return element;
    }
}
