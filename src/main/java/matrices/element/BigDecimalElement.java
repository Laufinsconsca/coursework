package matrices.element;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalElement implements Element<BigDecimal> {

    BigDecimal element;

    public BigDecimalElement(BigDecimal element) {
        this.element = element;
    }

    public BigDecimalElement(double element) {
        this.element = BigDecimal.valueOf(element);
    }

    public BigDecimalElement(Element<BigDecimal> element) {
        this(element.get());
    }

    @Override
    public Element<BigDecimal> add(Element<BigDecimal> element) {
        return new BigDecimalElement(this.element.add(element.get()));
    }

    @Override
    public Element<BigDecimal> add(double num) {
        return new BigDecimalElement(this.element.add(BigDecimal.valueOf(num)));
    }

    @Override
    public Element<BigDecimal> multiply(Element<BigDecimal> element) {
        return new BigDecimalElement(this.element.multiply(element.get()));
    }

    @Override
    public Element<BigDecimal> multiply(double num) {
        return new BigDecimalElement(this.element.multiply(BigDecimal.valueOf(num)));
    }

    @Override
    public Element<BigDecimal> divide(Element<BigDecimal> element) {
        return new BigDecimalElement(this.element.divide(element.get(), RoundingMode.CEILING));
    }

    @Override
    public Element<BigDecimal> reciprocal() {
        return new BigDecimalElement(BigDecimal.ONE.divide((this.element), RoundingMode.CEILING));
    }

    @Override
    public boolean equals(Element<BigDecimal> element) {
        return this.element.equals(element.get());
    }

    @Override
    public int compareTo(Element<BigDecimal> element) {
        return this.element.compareTo(element.get());
    }

    @Override
    public void set(Element<BigDecimal> element) {
        this.element = element.get();
    }

    @Override
    public void set(BigDecimal element) {
        this.element = element;
    }

    @Override
    public BigDecimal get() {
        return element;
    }

}
