package complex;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Complex implements Serializable {
    public static final Class<Complex> TYPE = Complex.class;
    public static final Complex ZERO = new Complex(0, 0);
    public final static Complex I = new Complex(0, 1);
    private double re;   // the real part
    private double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    public Complex(String text) {
        Double[] number = Complex.parseStringToComplex(text);
        re = number[0];
        im = number[1];
    }

    // a static version of plus
    public static Complex add(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public static Double[] parseStringToComplex(String text) {
        BigDecimal[] array = ComplexBigDecimal.parseStringToComplex(text);
        return new Double[]{array[0].doubleValue(), array[1].doubleValue()};
    }

    // return a string representation of the invoking firstLab.complex.Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im < 0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(re, im);
    }

    // return angle/phase/argument, normalized to be between -pi and pi
    public double phase() {
        return Math.atan2(im, re);
    }

    // return a new firstLab.complex.Complex object whose value is (this + b)
    public Complex add(Complex b) {
        return new Complex(re + b.re, im + b.im);
    }

    public Complex add(double b) {
        return new Complex(re + b, im);
    }

    // return a new firstLab.complex.Complex object whose value is (this - b)
    public Complex subtract(Complex b) {
        return new Complex(re - b.re, im - b.im);
    }

    public Complex negate() {
        return new Complex(-re, -im);
    }

    // return a new firstLab.complex.Complex object whose value is (this * b)
    public Complex times(Complex b) {
        return new Complex(re * b.re - im * b.im, re * b.im + im * b.re);
    }

    // return a new object whose value is (this * alpha)
    public Complex scaleOn(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new firstLab.complex.Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    // return a new firstLab.complex.Complex object whose value is the reciprocal of this
    public Complex reciprocal() {
        double scale = re * re + im * im;
        return new Complex(re / scale, -im / scale);
    }

    // return the real or imaginary part
    public double re() {
        return re;
    }

    public double im() {
        return im;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public void setIm(double im) {
        this.im = im;
    }

    // return a / b
    public Complex divide(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }
    public Complex divide(double a) {
        return this.scaleOn(1/a);
    }

    // return a new firstLab.complex.Complex object whose value is the firstLab.complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    // return a new firstLab.complex.Complex object whose value is the firstLab.complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    // return a new firstLab.complex.Complex object whose value is the firstLab.complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    // return a new firstLab.complex.Complex object whose value is the firstLab.complex tangent of this
    public Complex tan() {
        return sin().divide(cos());
    }

    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    public int hashCode() {
        return Objects.hash(re, im);
    }
}
