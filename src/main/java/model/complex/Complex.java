package model.complex;

import java.io.Serializable;

public class Complex implements Serializable {
    public static final Class<Complex> TYPE = Complex.class;
    public static final Complex ZERO = new Complex(0, 0);
    public final static Complex I = new Complex(0, 1);
    private final double re;   // the real part
    private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    // return abs/modulus/magnitude
    public double abs() {
        return Math.hypot(re, im);
    }

    // return a new firstLab.model.complex.Complex object whose value is (this + b)
    public Complex add(Complex b) {
        return new Complex(re + b.re, im + b.im);
    }

    public Complex add(double b) {
        return new Complex(re + b, im);
    }

    // return a new firstLab.model.complex.Complex object whose value is (this - b)
    public Complex subtract(Complex b) {
        return new Complex(re - b.re, im - b.im);
    }

    public Complex negate() {
        return new Complex(-re, -im);
    }

    // return a new firstLab.model.complex.Complex object whose value is (this * b)
    public Complex multiply(Complex b) {
        return new Complex(re * b.re - im * b.im, re * b.im + im * b.re);
    }

    public Complex divide(Complex b) {
        double scale = b.re * b.re + b.im * b.im;
        return new Complex(re * b.re / scale + im * b.im / scale, -re * b.im / scale + im * b.re / scale);
    }

    // return a new object whose value is (this * alpha)
    public Complex scaleOn(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    // return a new firstLab.model.complex.Complex object whose value is the firstLab.model.complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }
}
