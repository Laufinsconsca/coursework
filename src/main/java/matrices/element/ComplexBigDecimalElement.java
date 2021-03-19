package matrices.element;

import complex.Complex;
import complex.ComplexBigDecimal;
import exceptions.IllegalTypeException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComplexBigDecimalElement implements Element<ComplexBigDecimal> {

    private ComplexBigDecimal element;

    public ComplexBigDecimalElement(Complex number) {
        element = new ComplexBigDecimal(BigDecimal.valueOf(number.re()), BigDecimal.valueOf(number.im()));
    }

    public ComplexBigDecimalElement(ComplexBigDecimalElement element) {
        this.element = element.get();
    }

    public ComplexBigDecimalElement(ComplexBigDecimal element) {
        this.element = element;
    }

    public ComplexBigDecimalElement(String text) {
        element = new ComplexBigDecimal(text);
    }

    public ComplexBigDecimalElement(BigDecimal real, BigDecimal imag) {
        element = new ComplexBigDecimal(real, imag);
    }

    @Override
    public Element<ComplexBigDecimal> add(Element<ComplexBigDecimal> element) {
        return new ComplexBigDecimalElement(this.element.add(element.get()));
    }

    @Override
    public Element<ComplexBigDecimal> add(double num) {
        return new ComplexBigDecimalElement(this.element.add(BigDecimal.valueOf(num)));
    }

    @Override
    public Element<ComplexBigDecimal> multiply(Element<ComplexBigDecimal> element) {
        return new ComplexBigDecimalElement(this.element.multiply(element.get()));
    }

    @Override
    public Element<ComplexBigDecimal> multiply(double num) {
        return new ComplexBigDecimalElement(this.element.multiply(BigDecimal.valueOf(num)));
    }

    @Override
    public Element<ComplexBigDecimal> divide(Element<ComplexBigDecimal> element) {
        return new ComplexBigDecimalElement(this.element.divide(element.get(), RoundingMode.CEILING));
    }

    @Override
    public Element<ComplexBigDecimal> reciprocal() {
        return new ComplexBigDecimalElement(element.reciprocal());
    }

    @Override
    public boolean equals(Element<ComplexBigDecimal> element) {
        return this.element.isEqualTo(element.get());
    }

    @Override
    public int compareTo(Element<ComplexBigDecimal> element) {
        throw new IllegalTypeException("The type doesn't possess a operation of compare");
    }

    @Override
    public void set(Element<ComplexBigDecimal> element) {
        this.element = element.get();
    }

    @Override
    public void set(ComplexBigDecimal element) {
        this.element = element;
    }

    @Override
    public ComplexBigDecimal get() {
        return element;
    }
}
