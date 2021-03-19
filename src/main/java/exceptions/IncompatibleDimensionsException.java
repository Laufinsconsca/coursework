package exceptions;

public class IncompatibleDimensionsException extends RuntimeException {

    public IncompatibleDimensionsException() {
        super();
    }

    public IncompatibleDimensionsException(String str) {
        super(str);
    }
}
