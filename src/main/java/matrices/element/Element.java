package matrices.element;

public interface Element<T> {
    Element<T> add(Element<T> element);

    Element<T> add(double num);

    default Element<T> subtract(Element<T> element) {
        return add(element.negate());
    }

    Element<T> multiply(Element<T> element);

    Element<T> multiply(double num);

    Element<T> divide(Element<T> element);

    default Element<T> negate() {
        return this.multiply(-1);
    }

    Element<T> reciprocal();

    boolean equals(Element<T> element);

    int compareTo(Element<T> element);

    void set(Element<T> element);

    void set(T element);

    T get();
}
