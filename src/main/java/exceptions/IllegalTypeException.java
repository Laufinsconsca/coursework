package exceptions;

public class IllegalTypeException extends RuntimeException {

    public IllegalTypeException() {
        super();
    }

    public IllegalTypeException(String str) {
        super(str);
    }
}
