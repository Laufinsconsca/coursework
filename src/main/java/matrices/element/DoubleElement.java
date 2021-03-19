package matrices.element;

public class DoubleElement implements Element<Double> {
    private double element;

    public DoubleElement() {
        this(0);
    }

    public DoubleElement(double element) {
        this.element = element;
    }

    public DoubleElement(Element<Double> element) {
        this(element.get());
    }

    @Override
    public Element<Double> add(Element<Double> element) {
        return new DoubleElement(this.element + element.get());
    }

    @Override
    public Element<Double> add(double num) {
        return new DoubleElement(this.element += num);
    }

    @Override
    public Element<Double> multiply(Element<Double> element) {
        return new DoubleElement(this.element * element.get());
    }

    @Override
    public Element<Double> multiply(double num) {
        return new DoubleElement(this.element *= num);
    }

    @Override
    public Element<Double> divide(Element<Double> element) {
        return new DoubleElement(this.element / element.get());
    }

    @Override
    public Element<Double> reciprocal() {
        return new DoubleElement(1 / this.get());
    }

    @Override
    public boolean equals(Element<Double> element) {
        return this.element == element.get();
    }

    @Override
    public int compareTo(Element<Double> element) {
        return sgn(element.get() - this.element);
    }

    @Override
    public void set(Element<Double> element) {
        this.element = element.get();
    }

    @Override
    public void set(Double element) {
        this.element = element;
    }

    @Override
    public Double get() {
        return element;
    }

    private int sgn(double num) {
        return num > 0 ? 1 : num == 0 ? 0 : -1;
    }

}
