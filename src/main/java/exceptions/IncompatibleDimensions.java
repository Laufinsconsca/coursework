package exceptions;

public class IncompatibleDimensions extends RuntimeException {

    public IncompatibleDimensions() {
        super();
    }

    public IncompatibleDimensions(String str) {
        super(str);
    }
}
