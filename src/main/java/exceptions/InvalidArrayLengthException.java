package exceptions;

public class InvalidArrayLengthException extends RuntimeException {
    public InvalidArrayLengthException() {
        super();
    }

    public InvalidArrayLengthException(String str) {
        super(str);
    }
}
