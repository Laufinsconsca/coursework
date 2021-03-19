package complex;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComplexBigDecimal {

    public static final Class<ComplexBigDecimal> TYPE = ComplexBigDecimal.class;

    private BigDecimal real;
    private BigDecimal imag;

    public ComplexBigDecimal() {
        this.real = BigDecimal.ZERO;
        this.imag = BigDecimal.ZERO;
    }

    public ComplexBigDecimal(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }

    public ComplexBigDecimal(String text) {
        BigDecimal[] array = parseStringToComplex(text);
    }

    public static BigDecimal[] parseStringToComplex(String text) {
        String coefficient, a, b;
        String[] pattern, i;
        BigDecimal real = BigDecimal.ZERO;
        BigDecimal imag = BigDecimal.ZERO;
        if (text != null) {
            coefficient = text.replaceAll(" ", "");
            try {
                if (coefficient.isEmpty()) {
                    real = BigDecimal.ZERO;
                    imag = BigDecimal.ZERO;
                } else {
                    if (coefficient.contains("i")) {
                        if (coefficient.substring(1).contains("-")) {
                            if (coefficient.charAt(0) == '-') {
                                pattern = coefficient.substring(1).split("-");
                                i = pattern[1].split("i");
                                a = "-" + pattern[0];
                                if (pattern[1].charAt(0) != 'i') {
                                    b = "-" + i[0];
                                } else {
                                    b = "-1";
                                }
                            } else {
                                pattern = coefficient.split("-");
                                i = pattern[1].split("i");
                                a = pattern[0];
                                if (pattern[1].charAt(0) != 'i') {
                                    b = "-" + i[0];
                                } else {
                                    b = "-1";
                                }
                            }
                        } else if (coefficient.contains("+")) {
                            if (coefficient.charAt(0) == '-') {
                                pattern = coefficient.split("\\+");
                                i = pattern[1].split("i");
                                a = pattern[0];
                                if (pattern[1].charAt(0) != 'i') {
                                    b = i[0];
                                } else {
                                    b = "1";
                                }
                            } else {
                                pattern = coefficient.split("\\+");
                                i = pattern[1].split("i");
                                a = pattern[0];
                                if (pattern[1].charAt(0) != 'i') {
                                    b = i[0];
                                } else {
                                    b = "1";
                                }
                            }
                        } else {
                            i = coefficient.split("i");
                            a = "0";
                            if (coefficient.charAt(0) == '-') {
                                if (coefficient.charAt(1) == 'i') {
                                    b = "-1";
                                } else {
                                    b = i[0];
                                }
                            } else {
                                if (coefficient.charAt(0) == 'i') {
                                    b = "1";
                                } else {
                                    b = i[0];
                                }
                            }
                        }
                    } else {
                        a = coefficient;
                        b = "0";
                    }
                    real = new BigDecimal(a);
                    imag = new BigDecimal(b);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            real = BigDecimal.ZERO;
            imag = BigDecimal.ZERO;
        }
        return new BigDecimal[]{real, imag};
    }

    public BigDecimal getReal() {
        return real;
    }

    public void setReal(BigDecimal a) {
        real = a;
    }

    public BigDecimal getImag() {
        return imag;
    }

    public void setImag(BigDecimal a) {
        imag = a;
    }

    public ComplexBigDecimal multiply(ComplexBigDecimal a) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.multiply(a.real).subtract(imag.multiply(a.imag));
        result.imag = real.multiply(a.imag).add(imag.multiply(a.real));
        return result;
    }

    public ComplexBigDecimal multiply(BigDecimal a) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.multiply(a);
        result.imag = imag.multiply(a);
        return result;
    }

    public ComplexBigDecimal divide(ComplexBigDecimal a, int accuracyCalculation) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        final BigDecimal temp = a.real.multiply(a.real).add(a.imag.multiply(a.imag));
        result.real = real.multiply(a.real).add(imag.multiply(a.imag)).divide(temp, accuracyCalculation, RoundingMode.CEILING);
        result.imag = imag.multiply(a.real).subtract(real.multiply(a.imag)).divide(temp, accuracyCalculation, RoundingMode.CEILING);
        if (result.real.compareTo(BigDecimal.ZERO) == 0) {
            result.real = BigDecimal.ZERO;
        }
        if (result.imag.compareTo(BigDecimal.ZERO) == 0) {
            result.imag = BigDecimal.ZERO;
        }
        return result;
    }

    public ComplexBigDecimal divide(BigDecimal a, RoundingMode roundingMode) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.divide(a, roundingMode);
        result.imag = imag.divide(a, roundingMode);
        return result;
    }

    public ComplexBigDecimal divide(ComplexBigDecimal a, RoundingMode roundingMode) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.multiply(a.real).add(imag.multiply(a.imag)).divide(a.real.multiply(a.real).add(a.imag.multiply(a.imag)), roundingMode);
        result.imag = imag.multiply(a.real).subtract(real.multiply(a.imag)).divide(a.real.multiply(a.real).add(a.imag.multiply(a.imag)), roundingMode);
        return result;
    }

    public ComplexBigDecimal subtract(ComplexBigDecimal a) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.subtract(a.real);
        result.imag = imag.subtract(a.imag);
        return result;
    }

    public ComplexBigDecimal add(ComplexBigDecimal a) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.add(a.real);
        result.imag = imag.add(a.imag);
        return result;
    }

    public ComplexBigDecimal add(BigDecimal a) {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.add(a);
        return result;
    }

    public ComplexBigDecimal negate() {
        ComplexBigDecimal result = new ComplexBigDecimal();
        result.real = real.negate();
        result.imag = imag.negate();
        return result;
    }

    public ComplexBigDecimal reciprocal() {
        BigDecimal scale = real.multiply(real).add(imag.multiply(imag));
        return new ComplexBigDecimal(real.divide(scale, RoundingMode.CEILING), imag.negate().divide(scale, RoundingMode.CEILING));
    }

    /**
     * Равно ли число нулю
     */

    public boolean isEqualToZero() {
        return real.compareTo(BigDecimal.ZERO) == 0 && imag.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Не равно ли число нулю
     */

    public boolean isNotEqualToZero() {
        return !isEqualRealToZero() && !isEqualImagToZero();
    }

    /**
     * Равна ли действительная часть числа нулю
     */

    public boolean isEqualRealToZero() {
        return real.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Равна ли мнимая часть числа нулю
     */

    public boolean isEqualImagToZero() {
        return imag.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Равно ли число данному
     *
     * @param a - данное число типа ComplexBigDecimal
     */

    public boolean isEqualTo(ComplexBigDecimal a) {
        return real.compareTo(a.getReal()) == 0 && imag.compareTo(a.getImag()) == 0;
    }

    /**
     * Сравнение действительной части числа с нулём
     *
     * @return -1, если Re(z) < 0 <br> 0, если Re(z) == 0 <br> 1, если Re(z) > 0
     */

    public int compareRealToZero() {
        return real.compareTo(BigDecimal.ZERO);
    }

    /**
     * Сравнение мнимой части числа с нулём
     *
     * @return -1, если Im(z) < 0 <br> 0, если Im(z) == 0 <br> 1, если Im(z) > 0
     */

    public int compareImagToZero() {
        return imag.compareTo(BigDecimal.ZERO);
    }

    /**
     * Возведение числа z в степень n
     *
     * @param n степень
     * @return <html>z<font size= 4><sup><small>n</small></sup> </html>
     */

    public ComplexBigDecimal pow(int n) {
        ComplexBigDecimal result = new ComplexBigDecimal(this.real, this.imag);
        for (int i = 1; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public ComplexBigDecimal setScale(int scale, int type_of_rounding) {
        real = real.setScale(scale, type_of_rounding);
        imag = imag.setScale(scale, type_of_rounding);
        return this;
    }

    // return a string representation of the invoking firstLab.complex.Complex object
    public String toString() {
        if (imag.equals(BigDecimal.ZERO)) return real.toString() + "";
        if (real.equals(BigDecimal.ZERO)) return imag.toString() + "i";
        if (imag.compareTo(BigDecimal.ZERO) < 0) return real.toString() + " - " + (imag.negate().toString()) + "i";
        return real.toString() + " + " + imag.toString() + "i";
    }
}